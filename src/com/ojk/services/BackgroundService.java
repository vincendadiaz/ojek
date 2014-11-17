package com.ojk.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ojk.MainActivity;
import com.ojk.OJKTerbaru;
import com.ojk.R;
import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.business.JSONParser;
import com.ojk.entities.Global;
import com.ojk.entities.ObjectItemGridView;
import com.ojk.entities.ObjectItemListView;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.widget.Toast;

public class BackgroundService extends IntentService {
	private String kodeBahasa = "id";
	private DatabaseMenuRegulasi databaseMenuRegulasi;
	private ObjectItemListView ObjekDariDB = null;
	private int counterNew = 0;
	private String lastType = "";
	
	public BackgroundService() {
		super("BackgroundService");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {

		try {
			Log.d("BGService", "masuk");
			SharedPreferences pertamax = getSharedPreferences("pertamax",
					MODE_PRIVATE);
			String pertamaxx = pertamax.getString("isPertama", "yes");

			if (pertamaxx.equals("yes")) {
				SharedPreferences.Editor editor = getSharedPreferences(
						"pertamax", MODE_PRIVATE).edit();
				editor.putString("isPertama", "no");
				editor.commit();
			}

			SharedPreferences settings = getSharedPreferences("bahasa",
					MODE_PRIVATE);
			String bahasanya = settings.getString("bahasanya", "ID");

			Log.d("pertamaxx", pertamaxx);
			// Toast.makeText(this, "pertamaxx",
			// Toast.LENGTH_SHORT).show();

			if (bahasanya.equals("ID")) {
				kodeBahasa = "id";
			} else if (bahasanya.equals("EN")) {
				kodeBahasa = "en";
			}

			Calendar firstNotification = Calendar.getInstance();
			Date currentLocalTime = firstNotification.getTime();
			DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");

			String localTimeA = date.format(currentLocalTime);
			String dummy[] = localTimeA.split(" ");
			String localTime = dummy[0] + "%20" + dummy[1];
			
//			String waktuTerakhirPengecekan = workIntent.getStringExtra("localTime");
			Log.d("waktu terakhir", localTime);
			String urlMenu = "http://portalojk.dev.altrovis.com/_controls/OJKService.asmx/RetrieveNewItem?kodebahasa="
					+ kodeBahasa
					+ "&waktuTerakhirMelakukanPengecekanString="
					+ localTime;

			databaseMenuRegulasi = new DatabaseMenuRegulasi(
					getApplicationContext());
			databaseMenuRegulasi.getWritableDatabase();
			
			if (pertamaxx.equals("no")) {
				if(OpenJSON(urlMenu)) {
					if (kodeBahasa.equals("id")) {
						CekDB();
					} else {
						CekDBEn();
					}
				}
			} else {
				if (OpenJSON(urlMenu)) {
					MasukinDB();
					if (kodeBahasa.equals("id")) {
						CekDB();
					} else {
						CekDBEn();
					}
				}
			}
			
			//nyalain notif
			if (counterNew == 1) {
				createNotification(lastType);
			} else if(counterNew > 1) {
				createNotificationMany();
			}
			
		} catch (Exception e) {
			Log.e("BGServiceError di ", e.toString());
		}
	}

	private void MasukinDB() {
		for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
			String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
			String title = Global.ObjectOJKTerbaru[i].itemName.toString();
			String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
			String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
			String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
			String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
			String url = Global.ObjectOJKTerbaru[i].url.toString();
			String created = Global.ObjectOJKTerbaru[i].created.toString();
			
			if (kodeBahasa.equals("id")) {
				databaseMenuRegulasi.insertDataOJKTerbaru(i+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
			} else {
				databaseMenuRegulasi.insertDataOJKTerbaruEn(i+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);	
			}
		}
		Log.d("MasukinDB", "MasukinDB");
	}

	private void CekDB() {
		ArrayList<ObjectItemListView> dataDariDB = databaseMenuRegulasi.fetchDataOJKTerbaru();
		
		//kelompokin dulu mana regulasi dan non-regulasi (membantu pengecekan hingga ujung apabila tidak ada)
		ArrayList<ObjectItemListView> dataDariDBReg = new ArrayList<ObjectItemListView>();
		ArrayList<ObjectItemListView> dataDariDBNonReg = new ArrayList<ObjectItemListView>();
		for (int x = 0; x < dataDariDB.size(); x++) {
			if (dataDariDB.get(x).itemType.equals("regulasi")) {
				dataDariDBReg.add(dataDariDB.get(x));
			} else {
				dataDariDBNonReg.add(dataDariDB.get(x));
			}
		}
		
		// Kalo non regulasi masih 0, tapi ada data baru non regulasi
		if (dataDariDBNonReg.size() == 0) {
			Log.d("NonRegulasiDB", "1x");
			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
				if (!Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
					int id = databaseMenuRegulasi.fetchDataOJKTerbaru().size();
					String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
					String title = Global.ObjectOJKTerbaru[i].itemName.toString();
					String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
					String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
					String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
					String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
					String url = Global.ObjectOJKTerbaru[i].url.toString();
					String created = Global.ObjectOJKTerbaru[i].created.toString();
					
					databaseMenuRegulasi.insertDataOJKTerbaru(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
				}
			}
		}
		
		// Kalo regulasi masih 0, tapi ada baru regulasi
		if (dataDariDBReg.size() == 0) {
			Log.d("RegulasiDB", "1x");
			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
				if (Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
					int id = databaseMenuRegulasi.fetchDataOJKTerbaru().size();
					String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
					String title = Global.ObjectOJKTerbaru[i].itemName.toString();
					String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
					String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
					String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
					String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
					String url = Global.ObjectOJKTerbaru[i].url.toString();
					String created = Global.ObjectOJKTerbaru[i].created.toString();
					
					databaseMenuRegulasi.insertDataOJKTerbaru(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);

				}
			}
		}
		
		//update
		dataDariDB.clear();
		dataDariDB = databaseMenuRegulasi.fetchDataOJKTerbaru(); // fetch ulang setelah dimasukin di atas
		dataDariDBReg.clear();
		dataDariDBNonReg.clear();
		dataDariDBReg = new ArrayList<ObjectItemListView>();
		dataDariDBNonReg = new ArrayList<ObjectItemListView>();
		for (int x = 0; x < dataDariDB.size(); x++) {
			if (dataDariDB.get(x).itemType.equals("regulasi")) {
				dataDariDBReg.add(dataDariDB.get(x));
			} else {
				dataDariDBNonReg.add(dataDariDB.get(x));
			}
		}
		Log.d("jumlahGlob", ""+Global.ObjectOJKTerbaru.length);
		Log.d("jumlahReg", ""+dataDariDBReg.size());
		Log.d("jumlahNonReg", ""+dataDariDBNonReg.size());
		
		// Kalo Non regulasi
		for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
			if (!Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
//				Log.d("disini", "a");
				for (int j = 0; j < dataDariDBNonReg.size(); j++) {
					//cek kesamaan urlnya, sebagai indikator unik.
					if (Global.ObjectOJKTerbaru[i].url.equals(dataDariDBNonReg.get(j).url)) {
//						Log.d("disini", "b");
						// cek created, kalo ga sama update
						if (!Global.ObjectOJKTerbaru[i].created.equals(dataDariDBNonReg.get(j).created)) {
//							Log.d("disini", "c");
							databaseMenuRegulasi.updateCreatedOJKTerbaru(dataDariDBNonReg.get(j).url, Global.ObjectOJKTerbaru[i].created);
							counterNew++;
							lastType = Global.ObjectOJKTerbaru[i].itemType;
						}
						break;
					}
					//kalo udah sampe ujung dan masih beda, berarti ga ada, masukin baru ke DB OJKTerbaru trus bikin notif
					if (!Global.ObjectOJKTerbaru[i].url.equals(dataDariDBNonReg.get(j).url) && j == dataDariDBNonReg.size()-1) {
//						Log.d("disini", "d");
						int id = databaseMenuRegulasi.fetchDataOJKTerbaru().size();
						String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
						String title = Global.ObjectOJKTerbaru[i].itemName.toString();
						String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
						String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
						String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
						String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
						String url = Global.ObjectOJKTerbaru[i].url.toString();
						String created = Global.ObjectOJKTerbaru[i].created.toString();
						
						databaseMenuRegulasi.insertDataOJKTerbaru(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
						counterNew++;
						lastType = Global.ObjectOJKTerbaru[i].itemType;
					}
				}
			}
		}
		
		
		// Kalo regulasi
		ArrayList<ObjectItemGridView> grid = databaseMenuRegulasi.fetchObjectAllGrid();

			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
//				Log.d("disini", "a");
				if (Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) {
//					Log.d("disini", "b");
					for (int j = 0; j < dataDariDBReg.size(); j++) {
						
						//cek kesamaan url, sebagai indikator unik
						if (Global.ObjectOJKTerbaru[i].url.equals(dataDariDBReg.get(j).url)) {
//							Log.d("disini", "c");
							// cek created, kalo ga sama update di DB OJK dan DB GridRegulasi
							if (!Global.ObjectOJKTerbaru[i].created.equals(dataDariDBReg.get(j).created)) {
//								Log.d("disini", "d");
								databaseMenuRegulasi.updateCreatedOJKTerbaruUsingUrl(Global.ObjectOJKTerbaru[i].url, Global.ObjectOJKTerbaru[i].created);
								// cek created dengan di DB pada GridRegulasi
								for (int k = 0; k < grid.size(); k++) {
									if (Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl)) {
//										Log.d("disini", "e");
										if (!Global.ObjectOJKTerbaru[i].created.equals(grid.get(k).createdOn)) {
//											Log.d("disini", "f");
											databaseMenuRegulasi.updateCreatedGrid(Global.ObjectOJKTerbaru[i].url, Global.ObjectOJKTerbaru[i].created);
											counterNew++;
											lastType = Global.ObjectOJKTerbaru[i].itemType;
										}
									}
								}
							}
							break;
						}
						
						//kalo udah sampe ujung dan masih beda, berarti ga ada, masukin baru ke DB OJKTerbaru dan DB Grid, trus bikin notif
						if (!Global.ObjectOJKTerbaru[i].downloadUrl.equals(dataDariDBReg.get(j).downloadUrl) && j == dataDariDBReg.size()-1) {
							int id = databaseMenuRegulasi.fetchDataOJKTerbaru().size();
							String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
							String title = Global.ObjectOJKTerbaru[i].itemName.toString();
							String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
							String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
							String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
							String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
							String url = Global.ObjectOJKTerbaru[i].url.toString();
							String created = Global.ObjectOJKTerbaru[i].created.toString();
							
							databaseMenuRegulasi.insertDataOJKTerbaru(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
							
							//cek db di GridRegulasi
							for (int k = 0; k < grid.size(); k++) {
								// kalo ada update untuk Grid, tapi ternyata di OJK terbaru belum ada
								if (Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl)) {
									if (!Global.ObjectOJKTerbaru[i].created.equals(grid.get(k).createdOn)) {
										databaseMenuRegulasi.updateCreatedGrid(Global.ObjectOJKTerbaru[i].downloadUrl, Global.ObjectOJKTerbaru[i].created);
										counterNew++;
										lastType = Global.ObjectOJKTerbaru[i].itemType;
									}
									break;
								}
								
								// kalo ternyata di GridRegulasi pun ga ada, berarti baru banget, tambahin ke dalam Grid
								if (!Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl) && k == grid.size()-1) {
									int newid = databaseMenuRegulasi.fetchObjectAllGrid().size();
									String newtitle = Global.ObjectOJKTerbaru[i].itemName;
									String newdownloadurl = Global.ObjectOJKTerbaru[i].downloadUrl;
									String newfilesize = Global.ObjectOJKTerbaru[i].fileSize;
									String newfiletype = Global.ObjectOJKTerbaru[i].fileType;
									String newparenturl = Global.ObjectOJKTerbaru[i].parentUrl;
									int newidparent = databaseMenuRegulasi.getIdParentFromUrl(Global.ObjectOJKTerbaru[i].url);
									String newcreated = Global.ObjectOJKTerbaru[i].created;
									String newdownloaded = "no";
									String newurl = Global.ObjectOJKTerbaru[i].url;
									
									databaseMenuRegulasi.insertDataGrid(newid+1, newtitle, newdownloadurl, newfilesize, newfiletype, newparenturl, newidparent, 0, newcreated, newdownloaded, newurl);		
									counterNew++;
									lastType = Global.ObjectOJKTerbaru[i].itemType;
								}
							}
						}
					}
				}
			}
		Log.d("CekDB", "CekDB");
	}
	
	private void CekDBEn() {
		ArrayList<ObjectItemListView> dataDariDB = databaseMenuRegulasi.fetchDataOJKTerbaruEn();
		
		//kelompokin dulu mana regulasi dan non-regulasi (membantu pengecekan hingga ujung apabila tidak ada)
		ArrayList<ObjectItemListView> dataDariDBReg = new ArrayList<ObjectItemListView>();
		ArrayList<ObjectItemListView> dataDariDBNonReg = new ArrayList<ObjectItemListView>();
		for (int x = 0; x < dataDariDB.size(); x++) {
			if (dataDariDB.get(x).itemType.equals("regulasi")) {
				dataDariDBReg.add(dataDariDB.get(x));
			} else {
				dataDariDBNonReg.add(dataDariDB.get(x));
			}
		}
		
		// Kalo non regulasi masih 0, tapi ada data baru non regulasi
		if (dataDariDBNonReg.size() == 0) {
			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
				if (!Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
					int id = databaseMenuRegulasi.fetchDataOJKTerbaruEn().size();
					String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
					String title = Global.ObjectOJKTerbaru[i].itemName.toString();
					String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
					String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
					String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
					String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
					String url = Global.ObjectOJKTerbaru[i].url.toString();
					String created = Global.ObjectOJKTerbaru[i].created.toString();
					
					databaseMenuRegulasi.insertDataOJKTerbaruEn(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
				}
			}
		}
		
		// Kalo regulasi masih 0, tapi ada baru regulasi
		if (dataDariDBReg.size() == 0) {
			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
				if (Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
					int id = databaseMenuRegulasi.fetchDataOJKTerbaruEn().size();
					String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
					String title = Global.ObjectOJKTerbaru[i].itemName.toString();
					String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
					String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
					String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
					String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
					String url = Global.ObjectOJKTerbaru[i].url.toString();
					String created = Global.ObjectOJKTerbaru[i].created.toString();
					
					databaseMenuRegulasi.insertDataOJKTerbaruEn(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);

				}
			}
		}
		
		//update
		dataDariDB = databaseMenuRegulasi.fetchDataOJKTerbaruEn(); // fetch ulang setelah dimasukin di atas
		dataDariDBReg = new ArrayList<ObjectItemListView>();
		dataDariDBNonReg = new ArrayList<ObjectItemListView>();
		for (int x = 0; x < dataDariDB.size(); x++) {
			if (dataDariDB.get(x).itemType.equals("regulasi")) {
				dataDariDBReg.add(dataDariDB.get(x));
			} else {
				dataDariDBNonReg.add(dataDariDB.get(x));
			}
		}
		
		// Kalo Non regulasi
		for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
			if (!Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) { 
				
				for (int j = 0; j < dataDariDBNonReg.size(); j++) {
					//cek kesamaan urlnya, sebagai indikator unik.
					if (Global.ObjectOJKTerbaru[i].url.equals(dataDariDBNonReg.get(j).url)) {
						// cek created, kalo ga sama update
						if (!Global.ObjectOJKTerbaru[i].created.equals(dataDariDBNonReg.get(j).created)) {
							databaseMenuRegulasi.updateCreatedOJKTerbaruEn(dataDariDBNonReg.get(j).url, Global.ObjectOJKTerbaru[i].created);
							counterNew++;
							lastType = Global.ObjectOJKTerbaru[i].itemType;
						}
						break;
					}
					//kalo udah sampe ujung dan masih beda, berarti ga ada, masukin baru ke DB OJKTerbaru trus bikin notif
					if (!Global.ObjectOJKTerbaru[i].url.equals(dataDariDBNonReg.get(j).url) && j == dataDariDBNonReg.size()-1) {
						int id = databaseMenuRegulasi.fetchDataOJKTerbaruEn().size();
						String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
						String title = Global.ObjectOJKTerbaru[i].itemName.toString();
						String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
						String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
						String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
						String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
						String url = Global.ObjectOJKTerbaru[i].url.toString();
						String created = Global.ObjectOJKTerbaru[i].created.toString();
						
						databaseMenuRegulasi.insertDataOJKTerbaruEn(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
						counterNew++;
						lastType = Global.ObjectOJKTerbaru[i].itemType;
					}
				}
			}
		}
		
		
		// Kalo regulasi
		ArrayList<ObjectItemGridView> grid = databaseMenuRegulasi.fetchObjectAllGridEn();

			for (int i = 0; i < Global.ObjectOJKTerbaru.length; i++) {
				if (Global.ObjectOJKTerbaru[i].itemType.equals("regulasi")) {
					for (int j = 0; j < dataDariDBReg.size(); j++) {
						
						//cek kesamaan urlnya, sebagai indikator unik
						if (Global.ObjectOJKTerbaru[i].url.equals(dataDariDBReg.get(j).url)) {
							// cek created, kalo ga sama update di DB OJK dan DB GridRegulasi
							if (!Global.ObjectOJKTerbaru[i].created.equals(dataDariDBReg.get(j).created)) {
								// cek created dengan di DB pada GridRegulasi
								databaseMenuRegulasi.updateCreatedOJKTerbaruUsingUrlEn(Global.ObjectOJKTerbaru[i].url, Global.ObjectOJKTerbaru[i].created);
								for (int k = 0; k < grid.size(); k++) {
									if (Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl)) {
										if (!Global.ObjectOJKTerbaru[i].created.equals(grid.get(k).createdOn)) {
											databaseMenuRegulasi.updateCreatedGridEn(Global.ObjectOJKTerbaru[i].url, Global.ObjectOJKTerbaru[i].created);
											counterNew++;
											lastType = Global.ObjectOJKTerbaru[i].itemType;
										}
									}
								}
							}
							break;
						}
						
						//kalo udah sampe ujung dan masih beda, berarti ga ada, masukin baru ke DB OJKTerbaru trus bikin notif
						if (!Global.ObjectOJKTerbaru[i].downloadUrl.equals(dataDariDBReg.get(j).downloadUrl) && j == dataDariDBReg.size()-1) {
							int id = databaseMenuRegulasi.fetchDataOJKTerbaruEn().size();
							String itemType = Global.ObjectOJKTerbaru[i].itemType.toString();
							String title = Global.ObjectOJKTerbaru[i].itemName.toString();
							String downloadUrl = Global.ObjectOJKTerbaru[i].downloadUrl.toString();
							String fileSize = Global.ObjectOJKTerbaru[i].fileSize.toString();
							String fileType = Global.ObjectOJKTerbaru[i].fileType.toString();
							String parentUrl = Global.ObjectOJKTerbaru[i].parentUrl.toString();
							String url = Global.ObjectOJKTerbaru[i].url.toString();
							String created = Global.ObjectOJKTerbaru[i].created.toString();
							
							databaseMenuRegulasi.insertDataOJKTerbaruEn(id+1, itemType, title, downloadUrl, fileSize, fileType, parentUrl, url, created);
							
							//cek db di GridRegulasi
							for (int k = 0; k < grid.size(); k++) {
								if (Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl)) {
									if (!Global.ObjectOJKTerbaru[i].created.equals(grid.get(k).createdOn)) {
										databaseMenuRegulasi.updateCreatedGridEn(Global.ObjectOJKTerbaru[i].downloadUrl, Global.ObjectOJKTerbaru[i].created);
										counterNew++;
										lastType = Global.ObjectOJKTerbaru[i].itemType;
									}
									break;
								}
								
								// kalo ternyata di GridRegulasi pun ga ada, berarti baru banget, tambahin ke dalam Grid
								if (!Global.ObjectOJKTerbaru[i].downloadUrl.equals(grid.get(k).itemDownloadUrl) && k == grid.size()-1) {
									int newid = databaseMenuRegulasi.fetchObjectAllGridEn().size();
									String newtitle = Global.ObjectOJKTerbaru[i].itemName;
									String newdownloadurl = Global.ObjectOJKTerbaru[i].downloadUrl;
									String newfilesize = Global.ObjectOJKTerbaru[i].fileSize;
									String newfiletype = Global.ObjectOJKTerbaru[i].fileType;
									String newparenturl = Global.ObjectOJKTerbaru[i].parentUrl;
									int newidparent = databaseMenuRegulasi.getIdParentFromUrlEn(Global.ObjectOJKTerbaru[i].url);
									String newcreated = Global.ObjectOJKTerbaru[i].created;
									String newdownloaded = "no";
									String newurl = Global.ObjectOJKTerbaru[i].url;
									
									databaseMenuRegulasi.insertDataGridEn(newid+1, newtitle, newdownloadurl, newfilesize, newfiletype, newparenturl, newidparent, 0, newcreated, newdownloaded, newurl);		
									counterNew++;
									lastType = Global.ObjectOJKTerbaru[i].itemType;
								}
							}
						}
					}
				}
			}
		
		
		Log.d("CekDB", "CekDB");
	}

	private boolean OpenJSON(String urlWeb) {
		boolean lanjut = false;
		JSONParser jParser = new JSONParser();
		JSONArray json = jParser.getJSONFromUrl(urlWeb);
		Log.d("panjang JSONOJKTerbaru", "" + json.length());
		Global.ObjectOJKTerbaru = new ObjectItemListView[json.length()];
		ObjectItemListView objekIsiOJKTerbaru = null;
		if (json.length() > 0 && json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject obj = json.getJSONObject(i);
					String itemType = obj.getString("ItemType").toString();
					String judul = obj.getString("Title").toString();
					String downloadUrl = obj.getString("DownloadUrl")
							.toString();
					String fileSize = obj.getString("FileSize").toString();
					String fileType = obj.getString("FileType").toString();
					String parentUrl = obj.getString("ParentUrl").toString();
					String url = obj.getString("Url").toString();
					String created = obj.getString("Created").toString();
					objekIsiOJKTerbaru = new ObjectItemListView(itemType,
							judul, downloadUrl, fileSize, fileType, parentUrl,
							url, created);
					Global.ObjectOJKTerbaru[i] = objekIsiOJKTerbaru;
				} catch (JSONException e) {
					Log.e("JSONError di ", e.toString());
				}
			}
		}
		if (Global.ObjectOJKTerbaru.length > 0) {
			lanjut = true;
		}
		
		//nyalain notif
//		if (Global.ObjectOJKTerbaru.length == 1) {
//			createNotification(Global.ObjectOJKTerbaru[0].itemType);
//		} else if (Global.ObjectOJKTerbaru.length > 1) {
//			createNotificationMany();
//		}
		Log.d("OpenJSON", "OpenJSON");
		return lanjut;
	}
	
	private void createNotificationMany () {
		Log.d("NotifBaru", "banyak");
		NotificationCompat.Builder mBuilder = null;

		if (kodeBahasa.equals("id")) {
			mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("OJK")
					.setContentText("Terdapat beberapa konten baru");
		} else {
			mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("OJK")
					.setContentText("There are new contents");
		}
		Intent resultIntent = new Intent(this, OJKTerbaru.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(OJKTerbaru.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
				0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());

	}
	
	private void createNotification (String tipe) {
		Log.d("NotifBaru", "satu");
		NotificationCompat.Builder mBuilder = null;

		if (kodeBahasa.equals("id")) {
			mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("OJK")
					.setContentText("Terdapat " + tipe +" baru");
		} else {
			mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("OJK")
					.setContentText("There is new " + tipe);
		}
		Intent resultIntent = new Intent(this, OJKTerbaru.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(OJKTerbaru.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
				0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());

	}
}
