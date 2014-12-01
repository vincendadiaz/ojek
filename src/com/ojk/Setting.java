package com.ojk;

import java.io.File;
import java.util.ArrayList;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ojk.business.BaseAdapterItem;
import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.business.GridAdapterItem;
import com.ojk.business.JSONParser;
import com.ojk.entities.ObjectItemGridView;
import com.ojk.entities.ObjectItemListView;

import com.ojk.entities.*;
import com.ojk.business.*;

public class Setting extends Activity {
	private DatabaseMenuRegulasi databaseMenuRegulasi;
	public ObjectItemGridView[] ObjectItemData = null;
	public ArrayList<ObjectItemGridView> objectHasilSearch = null;
	private String extra = "";
	public boolean lanjut = false;

	private RadioGroup radioBahasa;
	private RadioButton radioENID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		// new ProgressTask(SearchResult.this).execute();

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
	    
	    if (bahasanya.equals("ID")) {
	    	radioENID = (RadioButton) findViewById(R.id.radioID);
	    	radioENID.setChecked(true);
	    } else if (bahasanya.equals("EN")){
	    	radioENID = (RadioButton) findViewById(R.id.radioEN);
	    	radioENID.setChecked(true);
	    	setTitle("Settings");
	    	
	    	Button buttonOK = (Button) findViewById(R.id.buttonOKPilihBahasa);
	    	buttonOK.setText("Save");
	    	
	    	Button buttonHapus = (Button) findViewById(R.id.buttonResetAllData);
	    	buttonHapus.setText("Delete");
	    	
	    	TextView textViewPilihBahasa = (TextView) findViewById(R.id.textViewPilihBahasa);
	    	textViewPilihBahasa.setText("Choose language :");
	    	TextView textViewHapus = (TextView) findViewById(R.id.textViewResetAllData);
	    	textViewHapus.setText("Reset all data");
	    	
	    }
		
		radioBahasa = (RadioGroup) findViewById(R.id.radioBahasa);
		Button buttonOK = (Button) findViewById(R.id.buttonOKPilihBahasa);

		buttonOK.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int selectedId = radioBahasa.getCheckedRadioButtonId();
				radioENID = (RadioButton) findViewById(selectedId);

				if (radioENID.getText().equals("English")) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"bahasa", MODE_PRIVATE).edit();
					editor.putString("bahasanya", "EN");
					editor.commit();
//					Toast.makeText(Setting.this,
//							"Please restart the application",
//							Toast.LENGTH_SHORT).show();
				} else {

					SharedPreferences.Editor editor = getSharedPreferences(
							"bahasa", MODE_PRIVATE).edit();
					editor.putString("bahasanya", "ID");
					editor.commit();

//					Toast.makeText(Setting.this,
//							"Silahkan buka kembali aplikasi",
//							Toast.LENGTH_SHORT).show();
				}
				Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		
		Button buttonHapus = (Button) findViewById(R.id.buttonResetAllData);
		
		buttonHapus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				databaseMenuRegulasi = new DatabaseMenuRegulasi(
						getApplicationContext());
				databaseMenuRegulasi.getWritableDatabase();
				

				databaseMenuRegulasi.dropMenuRegulasi();
				databaseMenuRegulasi.dropMenuRegulasiEn();
				databaseMenuRegulasi.dropOjkTerbaru();
				databaseMenuRegulasi.dropOjkTerbaruEn();
				databaseMenuRegulasi.dropGridRegulasi();
				databaseMenuRegulasi.dropGridRegulasiEn();
				databaseMenuRegulasi.vacuum();
				
				clearApplicationData();
				
				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/OJK");
				if (dir.isDirectory()) {
					String[] children = dir.list();
					for (int i = 0; i < children.length; i++) {
						new File(dir, children[i]).delete();
					}
				}
				
				Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}
	
	public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
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