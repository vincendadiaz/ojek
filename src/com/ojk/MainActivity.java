package com.ojk;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.business.JSONParser;
import com.ojk.services.BackgroundService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {
	private Context context;
	private DatabaseMenuRegulasi databaseMenuRegulasi;
	private int id = 0;
	private int idGrid = 0;
	private String kodeBahasa = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();

		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");

		if (!isOnline()) {
			Toast.makeText(this, "Tidak terdapat koneksi internet",
					Toast.LENGTH_SHORT).show();
		}

		if (bahasanya.equals("ID")) {
			kodeBahasa = "id";
			// Toast.makeText(this,"Indo", Toast.LENGTH_SHORT)
			// .show();

		} else if (bahasanya.equals("EN")) {
			kodeBahasa = "en";
			// Toast.makeText(this,"Inggris", Toast.LENGTH_SHORT)
			// .show();

			TextView textViewMenu1 = (TextView) findViewById(R.id.TextViewMenu);
			textViewMenu1.setText("Regulation");

			TextView textViewMenu2 = (TextView) findViewById(R.id.TextViewMenu2);
			textViewMenu2.setText("Latest OJK");

			TextView textViewMenu3 = (TextView) findViewById(R.id.TextViewMenu3);
			textViewMenu3.setText("About OJK");
		}

		new ProgressTask(MainActivity.this).execute();

		// Start Background Service
		AlarmManager alarmManager = (AlarmManager) getSystemService(context.ALARM_SERVICE);
		Calendar cur_cal = new GregorianCalendar();
		cur_cal.setTimeInMillis(System.currentTimeMillis());// set the current
															// time and date for
															// this calendar

		Calendar firstNotification = Calendar.getInstance();
		Date currentLocalTime = firstNotification.getTime();
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		String localTimeA = date.format(currentLocalTime);
		String dummy[] = localTimeA.split(" ");
		String localTime = dummy[0] + "%20" + dummy[1];
		
//		String localTime = "02-11-2014%2012:00";
		firstNotification.setTime(currentLocalTime);
		// firstNotification.set(Calendar.HOUR,
		// firstNotification.get(firstNotification.HOUR) - 2);
		// firstNotification.set(Calendar.SECOND,
		// firstNotification.get(Calendar.SECOND) + 15);
		long whenFirstNotification = firstNotification.getTimeInMillis();

		// debug
		// Calendar cal = cur_cal;
		//
		// cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 2);
		// long when = cal.getTimeInMillis();
		// debug end

		Intent intent = new Intent(context, BackgroundService.class);
		intent.putExtra("localTime", localTime);

		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, 0);
		alarmManager.setRepeating(AlarmManager.RTC, whenFirstNotification,
				1000 * 30, pendingIntent);
		//

		RelativeLayout menu1 = (RelativeLayout) findViewById(R.id.linearLayoutInside1);
		menu1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Regulasi.class);
				startActivityForResult(i, 0);
				// overridePendingTransition(R.anim.slidein, R.anim.slideout);
			}
		});

		RelativeLayout menu2 = (RelativeLayout) findViewById(R.id.linearLayoutInside2);
		menu2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, OJKTerbaru.class);
				startActivityForResult(i, 0);
				// overridePendingTransition(R.anim.slidein, R.anim.slideout);
			}
		});

		RelativeLayout menu3 = (RelativeLayout) findViewById(R.id.linearLayoutInside3);
		menu3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, About.class);
				startActivityForResult(i, 0);
				// overridePendingTransition(R.anim.slidein, R.anim.slideout);
			}
		});
		
		ImageButton setting = (ImageButton) findViewById(R.id.imageButtonSetting);
		setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Setting.class);
				startActivity(i);
			}
		});
	}

	int mb = -1;

	// detect orientation change
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		RelativeLayout relLayoutLogo = (RelativeLayout) findViewById(R.id.relayoutlogo);

		View view = findViewById(R.id.relayoutlogo);
		LayoutParams lp = (LayoutParams) view.getLayoutParams();

		if (mb == -1) {
			mb = lp.bottomMargin;
		}
		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

			LayoutParams params = new LayoutParams((int) getResources()
					.getDimension(R.dimen.logoDepanWidth), (int) getResources()
					.getDimension(R.dimen.logoDepanHeight));
			params.setMargins(0, 0, 0, 0);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);

			relLayoutLogo.setLayoutParams(params);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			LayoutParams params = new LayoutParams((int) getResources()
					.getDimension(R.dimen.logoDepanWidth), (int) getResources()
					.getDimension(R.dimen.logoDepanHeight));
			params.setMargins(0, 0, 0, mb);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);

			relLayoutLogo.setLayoutParams(params);

		}
	}

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		private MainActivity activity;

		public ProgressTask(MainActivity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		// private Context context;

		protected void onPreExecute() {

			if (kodeBahasa.equals("en")) {
				this.dialog.setMessage("Retrieving data...");
			} else {
				this.dialog.setMessage("Sedang mengambil data...");

			}
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			databaseMenuRegulasi.close();
			// Log.d("JUMLAH GRID", ""+databaseMenuRegulasi.fetchDataGrid());
			TextView textViewCountRegulasi = (TextView) findViewById(R.id.TextViewCount);
			TextView textViewCountOJKTerbaru = (TextView) findViewById(R.id.TextViewCount2);
			if (kodeBahasa.equals("en")) {
				textViewCountRegulasi
						.setText("  "
								+ databaseMenuRegulasi
										.getCountAllGridUnreadEn() + "  ");
				textViewCountOJKTerbaru
						.setText("  "
								+ databaseMenuRegulasi
										.getCountAllOJKTerbaruEn() + "  ");
			} else {
				textViewCountRegulasi.setText("  "
						+ databaseMenuRegulasi.getCountAllGridUnread() + "  ");
				textViewCountOJKTerbaru
						.setText("  "
								+ databaseMenuRegulasi
										.getCountAllOJKTerbaru() + "  ");
			}

		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			String urlMenu = "http://portalojk.dev.altrovis.com/_controls/OJKService.asmx/GetMenuRegulasi?kodebahasa="
					+ kodeBahasa;
			JSONtoDB(urlMenu);

			databaseMenuRegulasi = new DatabaseMenuRegulasi(
					getApplicationContext());
			databaseMenuRegulasi.getWritableDatabase();

			databaseMenuRegulasi.close();

			return null;
		}

		public void JSONtoDB(String urlWeb) {
			databaseMenuRegulasi = new DatabaseMenuRegulasi(
					getApplicationContext());
			// databaseMenuRegulasi.close();
			// databaseMenuRegulasi.dropDB();
			// this.deleteDatabase("MenuRegulasi.db");
			databaseMenuRegulasi.getWritableDatabase();
			JSONParser jParser = new JSONParser();
			JSONArray json = jParser.getJSONFromUrl(urlWeb);

			if (kodeBahasa.equals("id")
					&& databaseMenuRegulasi.getCountAll() == 0) {
				GenerateMenuRegulasiDatabase(id, json);
			} else if (kodeBahasa.equals("en")
					&& databaseMenuRegulasi.getCountAllEn() == 0) {
				GenerateMenuRegulasiDatabaseEn(id, json);
			}
		}
	}

	private void GenerateMenuRegulasiDatabase(int idParent, JSONArray data) {
		if (data != null) {
			for (int i = 0; i < data.length(); i++) {
				try {
					id++;
					JSONObject obj = data.getJSONObject(i);
					String judul = obj.getString("Title").toString();
					String url = obj.getString("Url").toString();

					int jumlahAnaknya = 0;
					int isLastChild = 1;
					if (!obj.isNull("Submenu")) {
						isLastChild = 0;
						// jumlahAnaknya = obj.getJSONArray("Submenu").length();
						jumlahAnaknya = 0;
					}
					databaseMenuRegulasi.insertData(id, judul, url, idParent,
							isLastChild, jumlahAnaknya);
					if (!url.equals("#")) {
						int x = inputAnakGrid(url, id);
						int temp = id;
						while (true) {
							databaseMenuRegulasi
									.updateData(
											temp,
											databaseMenuRegulasi
													.getAnakCount(temp) + x);
							temp = databaseMenuRegulasi.getIdParent(temp);
							if (temp == 0) {
								break;
							}
						}

					}
					if (!obj.isNull("Submenu")) {
						GenerateMenuRegulasiDatabase(id,
								obj.getJSONArray("Submenu"));
					}
				} catch (JSONException e) {
					Log.e("JSONError di ", e.toString());
				}
			}
		}
	}

	private int inputAnakGrid(String url, int idParent) {
		JSONParser jParser = new JSONParser();
		JSONArray json = jParser
				.getJSONFromUrl("http://portalojk.dev.altrovis.com/_controls/OJKService.asmx/GetAllRegulasi?siteURL="
						+ url);
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject obj = json.getJSONObject(i);
				idGrid++;
				String judul = obj.getString("Title").toString();
				String downloadUrl = obj.getString("DownloadUrl").toString();
				String fileSize = obj.getString("FileSize").toString();
				String fileType = obj.getString("FileType").toString();
				String parentUrl = obj.getString("ParentUrl").toString();
				String created = obj.getString("Created").toString();
				String downloaded = "no";
				String itemUrl = obj.getString("Url").toString();

				databaseMenuRegulasi.insertDataGrid(idGrid, judul, downloadUrl,
						fileSize, fileType, parentUrl, idParent, 0, created,
						downloaded, itemUrl);

			} catch (JSONException e) {
				Log.d("JSON ERROR GRID di ", e.toString());
			}
		}
		return json.length();
	}

	private void GenerateMenuRegulasiDatabaseEn(int idParent, JSONArray data) {
		if (data != null) {
			for (int i = 0; i < data.length(); i++) {
				try {
					id++;
					JSONObject obj = data.getJSONObject(i);
					String judul = obj.getString("Title").toString();
					String url = obj.getString("Url").toString();

					int jumlahAnaknya = 0;
					int isLastChild = 1;
					if (!obj.isNull("Submenu")) {
						isLastChild = 0;
						// jumlahAnaknya = obj.getJSONArray("Submenu").length();
						jumlahAnaknya = 0;
					}
					databaseMenuRegulasi.insertDataEn(id, judul, url, idParent,
							isLastChild, jumlahAnaknya);
					if (!url.equals("#")) {
						int x = inputAnakGrid(url, id);
						int temp = id;
						while (true) {
							databaseMenuRegulasi.updateDataEn(temp,
									databaseMenuRegulasi.getAnakCountEn(temp)
											+ x);
							temp = databaseMenuRegulasi.getIdParentEn(temp);
							if (temp == 0) {
								break;
							}
						}

					}
					if (!obj.isNull("Submenu")) {
						GenerateMenuRegulasiDatabaseEn(id,
								obj.getJSONArray("Submenu"));
					}
				} catch (JSONException e) {
					Log.e("JSONError di ", e.toString());
				}
			}
		}
	}

	private int inputAnakGridEn(String url, int idParent) {
		JSONParser jParser = new JSONParser();
		JSONArray json = jParser
				.getJSONFromUrl("http://portalojk.dev.altrovis.com/_controls/OJKService.asmx/GetAllRegulasi?siteURL="
						+ url);
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject obj = json.getJSONObject(i);
				idGrid++;
				String judul = obj.getString("Title").toString();
				String downloadUrl = obj.getString("DownloadUrl").toString();
				String fileSize = obj.getString("FileSize").toString();
				String fileType = obj.getString("FileType").toString();
				String parentUrl = obj.getString("ParentUrl").toString();
				String created = obj.getString("Created").toString();
				String downloaded = "no";
				String itemUrl = obj.getString("Url").toString();
						

				databaseMenuRegulasi.insertDataGridEn(idGrid, judul,
						downloadUrl, fileSize, fileType, parentUrl, idParent,
						0, created, downloaded, itemUrl);

			} catch (JSONException e) {
				Log.d("JSON ERROR GRID di ", e.toString());
			}
		}
		return json.length();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");

		if (bahasanya.equals("EN")) {
			MenuItem textSetting = menu.findItem(R.id.action_settings);
			textSetting.setTitle("Settings");
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.mainsetting, menu);
//		return super.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.action_settings:
//			Intent i = new Intent(MainActivity.this, Setting.class);
//			startActivity(i);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TextView textViewCountRegulasi = (TextView) findViewById(R.id.TextViewCount);
		if (kodeBahasa.equals("en")) {
			textViewCountRegulasi.setText("  "
					+ databaseMenuRegulasi.getCountAllGridUnreadEn() + "  ");
		} else {
			textViewCountRegulasi.setText("  "
					+ databaseMenuRegulasi.getCountAllGridUnread() + "  ");
		}
	}
}
