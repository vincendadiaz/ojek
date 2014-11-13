package com.ojk;

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
	    	
	    	TextView textViewPilihBahasa = (TextView) findViewById(R.id.textViewPilihBahasa);
	    	textViewPilihBahasa.setText("Choose language :");
	    }
		
		radioBahasa = (RadioGroup) findViewById(R.id.radioBahasa);
		Button buttonOK = (Button) findViewById(R.id.buttonOKPilihBahasa);

		buttonOK.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int selectedId = radioBahasa.getCheckedRadioButtonId();
				radioENID = (RadioButton) findViewById(selectedId);

				if (radioENID.getText().equals("INGGRIS")) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"bahasa", MODE_PRIVATE).edit();
					editor.putString("bahasanya", "EN");
					editor.commit();
					Toast.makeText(Setting.this,
							"Please restart the application",
							Toast.LENGTH_SHORT).show();
				} else {

					SharedPreferences.Editor editor = getSharedPreferences(
							"bahasa", MODE_PRIVATE).edit();
					editor.putString("bahasanya", "ID");
					editor.commit();

					Toast.makeText(Setting.this,
							"Silahkan buka kembali aplikasi",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

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