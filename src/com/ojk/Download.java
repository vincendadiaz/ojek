package com.ojk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ojk.business.DatabaseMenuRegulasi;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Download extends Activity {

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private Button startBtn;
	private ProgressDialog mProgressDialog;
	private String downloadLinkUrl = "";
	private String namaFile = "";
	private String tipeFile = "";
	private String kodeBahasa = "id";
	private int id = 0;
	private int idParent = 0;
	private DatabaseMenuRegulasi databasemenuregulasi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlebar);
		setContentView(R.layout.download);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#c70100")));
		
		//set color title bar
		int actionBarTitleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}
		
		//Open DB
		databasemenuregulasi = new DatabaseMenuRegulasi(getApplicationContext());
		databasemenuregulasi.getWritableDatabase();

		//ambil bahasa sharedPreferences
		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");

		Button unduhFile = (Button) findViewById(R.id.unduhFile);

		if (getIntent().getStringExtra("DownloadCarry") != null) {
			String extra = getIntent().getStringExtra("DownloadCarry")
					.toString();
			String[] splitExtra = extra.split(",");

			TextView textViewDownload = (TextView) findViewById(R.id.textViewDownload);
			textViewDownload.setText(splitExtra[0]);

			namaFile = splitExtra[0];

			TextView textViewDetailDownload = (TextView) findViewById(R.id.textViewDetailDownload);
			textViewDetailDownload.setText(splitExtra[1] + " - "
					+ splitExtra[2]);
			tipeFile = splitExtra[2];

			downloadLinkUrl = splitExtra[3];

			id = Integer.parseInt(splitExtra[4]);
			
			// Set text buka/ unduh
			if (bahasanya.equals("EN")) {
				if (databasemenuregulasi.isDownloadedEn(id)) {
					unduhFile.setText("Open");
				} else {
					unduhFile.setText("Download");
				}
				kodeBahasa = "en";
			} else {
				if (databasemenuregulasi.isDownloaded(id)) {
					unduhFile.setText("Buka");
				} else {
					unduhFile.setText("Unduh");
				}
			}

			idParent = Integer.parseInt(splitExtra[5]);
			
			databasemenuregulasi.close();

		} else if (getIntent().getStringExtra("DownloadCarryFromSearch") != null) {
			String extra = getIntent()
					.getStringExtra("DownloadCarryFromSearch").toString();
			String[] splitExtra = extra.split(",");

			TextView textViewDownload = (TextView) findViewById(R.id.textViewDownload);
			textViewDownload.setText(splitExtra[0]);

			namaFile = splitExtra[0];

			TextView textViewDetailDownload = (TextView) findViewById(R.id.textViewDetailDownload);
			textViewDetailDownload.setText(splitExtra[1] + " - "
					+ splitExtra[2]);
			tipeFile = splitExtra[2];

			downloadLinkUrl = splitExtra[3];
			
			String idDanIdParent = "";
			if (bahasanya.equals("ID")) {
				idDanIdParent = databasemenuregulasi.getIdDanIdParentDariDUrl(downloadLinkUrl);
				Log.d("idNya", idDanIdParent);
			} else {
				idDanIdParent = databasemenuregulasi.getIdDanIdParentDariDUrlEn(downloadLinkUrl);
				Log.d("idNya", idDanIdParent);
			}
			
			String[] hasilSplit = idDanIdParent.split(",");
			id = Integer.parseInt(hasilSplit[0]);
			idParent = Integer.parseInt(hasilSplit[1]);
			
			if (bahasanya.equals("EN")) {
				if (databasemenuregulasi.isDownloadedEn(id)) {
					unduhFile.setText("Open");
				} else {
					unduhFile.setText("Download");
				}
				kodeBahasa = "en";
			} else {
				if (databasemenuregulasi.isDownloaded(id)) {
					unduhFile.setText("Buka");
				} else {
					unduhFile.setText("Unduh");
				}
			}

		}

		startBtn = (Button) findViewById(R.id.unduhFile);
		startBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if (getIntent().getStringExtra("DownloadCarryFromSearch") != null) {
//					startDownload();
//				} else {
					databasemenuregulasi = new DatabaseMenuRegulasi(
							getApplicationContext());
					databasemenuregulasi.getWritableDatabase();
					if (databasemenuregulasi.isDownloaded(id)) {
						showPDF();
					} else {
						if (!isOnline()) {
							if (kodeBahasa.equals("id")) {
								Toast.makeText(Download.this,
										"Tidak terdapat koneksi internet",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(Download.this,
										"No internet connection",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Button unduhFile = (Button) findViewById(R.id.unduhFile);
							if (kodeBahasa.equals("en")) {
								unduhFile.setText("Open");
							} else {
								unduhFile.setText("Buka");
							}
							startDownload();
							if (kodeBahasa.equals("id")) {
								Calendar cal = Calendar.getInstance();
								Date currentLocalTime = cal.getTime();

								DateFormat date = new SimpleDateFormat(
										"dd-MM-yyyy HH:mm");

								String localTime = date
										.format(currentLocalTime);

								databasemenuregulasi
										.updateDataGridToDownloaded(id,
												localTime);
							} else {
								Calendar cal = Calendar.getInstance();
								Date currentLocalTime = cal.getTime();

								DateFormat date = new SimpleDateFormat(
										"dd-MM-yyyy HH:mm");

								String localTime = date
										.format(currentLocalTime);

								databasemenuregulasi
										.updateDataGridToDownloadedEn(id,
												localTime);
							}
						}
					}
					databasemenuregulasi.close();
				}
//			}
		});
	}

	private void startDownload() {
		String url = downloadLinkUrl;
		new DownloadFileAsync().execute(url);
		decrease();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			if (kodeBahasa.equals("en")) {
				mProgressDialog.setMessage("Downloading file..");
			} else {
				mProgressDialog.setMessage("Sedang mengunduh..");
			}
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setProgressNumberFormat(null);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			// this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.titlebar);
			int actionBarTitleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			if (actionBarTitleId > 0) {
				TextView title = (TextView) findViewById(actionBarTitleId);
				Log.d("landscape", "landscape");
				title.setTextColor(Color.WHITE);
			}

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			// this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.titlebar);
			int actionBarTitleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			if (actionBarTitleId > 0) {
				TextView title = (TextView) findViewById(actionBarTitleId);
				Log.d("portrait", "portrait");
				title.setTextColor(Color.WHITE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			// overridePendingTransition(R.anim.ltor, R.anim.rtol);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				File folder = new File(
						Environment.getExternalStorageDirectory() + "/OJK");
				boolean success = true;
				if (!folder.exists()) {
					success = folder.mkdir();
				}
				OutputStream output = new FileOutputStream("/sdcard/OJK/"
						+ namaFile + tipeFile);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;

		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String unused) {
			// mProgressDialog.dismiss();
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			
			showPDF();
		}
	}

	public void showPDF() {
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/OJK/" + namaFile + tipeFile);
			if (!file.isDirectory())
				file.mkdir();
			Intent testIntent = new Intent("com.adobe.reader");
			testIntent.setType("application/pdf");
			testIntent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(file);
			testIntent.setDataAndType(uri, "application/pdf");
			startActivityForResult(testIntent, 669669);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void decrease() {
		if (kodeBahasa.equals("en")) {
			
			if (!databasemenuregulasi.isReadEn(id)) {
				databasemenuregulasi.updateDataGridIsReadEn(id);

				while (true) {
					databasemenuregulasi
							.updateDataEn(idParent, databasemenuregulasi
									.getAnakCountEn(idParent) - 1);
					idParent = databasemenuregulasi.getIdParentEn(idParent);
					if (idParent == 0) {
						break;
					}
				}
			}
		} else {
			if (!databasemenuregulasi.isRead(id)) {
				databasemenuregulasi.updateDataGridIsRead(id);

				while (true) {
					databasemenuregulasi
							.updateData(idParent, databasemenuregulasi
									.getAnakCount(idParent) - 1);
					idParent = databasemenuregulasi.getIdParent(idParent);
					if (idParent == 0) {
						break;
					}
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
			if (requestCode == 669669) {
				int actionBarTitleId = Resources.getSystem().getIdentifier(
						"action_bar_title", "id", "android");
				if (actionBarTitleId > 0) {
					TextView title = (TextView) findViewById(actionBarTitleId);
					if (title != null) {
						title.setTextColor(Color.WHITE);
					}
				}
			}
	
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			}
		} catch (Exception e){
			
		}
	}
}
