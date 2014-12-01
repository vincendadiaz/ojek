package com.ojk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojk.entities.*;
import com.ojk.business.*;

public class Search extends Activity {
	private String kodeBahasa = "id";
	private String extra = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#c70100")));
		int actionBarTitleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}
		
		SharedPreferences settings = getSharedPreferences("bahasa", MODE_PRIVATE);
	    String bahasanya = settings.getString("bahasanya", "ID");
	    
	    final EditText editTextCari = (EditText) findViewById(R.id.editTextCari);
		ImageButton imageButtonCari = (ImageButton) findViewById(R.id.imageButtonCari);
		
	    if (bahasanya.equals("EN")){
	    	kodeBahasa = "en";
	    	setTitle ("Search Regulation");
	    	editTextCari.setHint("Regulation title");
	    } else {
	    	kodeBahasa = "id";
	    	editTextCari.setHint("Judul Regulasi");
	    }
	    

		imageButtonCari.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				extra = "";
				extra = editTextCari.getText().toString();
				if (extra.equals("")) {
					if (kodeBahasa.equals("id")) {
						Toast.makeText(Search.this,
								"Masukkan kata kunci terlebih dahulu",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(Search.this,
								"Please insert keyword first",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (isOnline()) {
						new ProgressTask(Search.this).execute();
					} else {
						if (kodeBahasa.equals("id")) {
							Toast.makeText(Search.this,
									"Tidak ada internet",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Search.this,
									"No internet connection",
									Toast.LENGTH_SHORT).show();
						}
					}
				}	
			}
		});

	}
	
	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		private Search activity;

		private Context context;
		
		public ProgressTask(Search activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}
		

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				Intent i = new Intent(Search.this, SearchResult.class);
				startActivity(i);
			} else {
//				Toast.makeText(Search.this,
//						"Eror cuy ah elah", Toast.LENGTH_SHORT)
//						.show();
			}
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			Log.d("menuPathString", Global.menuPathString.substring(0, Global.menuPathString.length()));
			String urlMenu = "http://portalojk.dev.altrovis.com/_controls/OJKService.asmx/SearchRegulasiWithinSubsite?kodebahasa=" + kodeBahasa + "&keyword=" + extra + "&menuPathString=" + Global.menuPathString.substring(0, Global.menuPathString.length()-7);
			if (JSONtoArrayList(urlMenu)) {
				return true;
			}
			return false;
		}
		
		public boolean JSONtoArrayList(String urlWeb) {
			boolean oke = false;
			JSONParser jParser = new JSONParser();
			JSONArray json = jParser.getJSONFromUrl(urlWeb);
			ObjectItemGridView objectSearch = null;
			Global.ObjectItemData = new ObjectItemGridView[json.length()];
			for (int i = 0 ; i < json.length(); i++) {
				try {
					JSONObject obj = json.getJSONObject(i);
					String judul = obj.getString("Title").toString();
					String downloadUrl = obj.getString("DownloadUrl").toString();
					String fileSize = obj.getString("FileSize").toString();
					String fileType = obj.getString("FileType").toString();
					objectSearch = new ObjectItemGridView(judul, downloadUrl, fileSize, fileType);
					Global.ObjectItemData[i] = objectSearch;
				} catch(JSONException e) {
					Log.e("JSONError di ", e.toString());
				}
			}
			oke = true;
			return oke;
		}
	}
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
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
}
