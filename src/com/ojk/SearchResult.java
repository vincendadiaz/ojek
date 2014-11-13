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
import android.content.res.Configuration;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.ojk.business.BaseAdapterItem;
import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.business.GridAdapterItem;
import com.ojk.business.JSONParser;
import com.ojk.entities.ObjectItemGridView;
import com.ojk.entities.ObjectItemListView;


import com.ojk.entities.*;
import com.ojk.business.*;

public class SearchResult extends Activity {
	private DatabaseMenuRegulasi databaseMenuRegulasi;
	public ObjectItemGridView[] ObjectItemData = null;
	public ArrayList<ObjectItemGridView> objectHasilSearch = null;
	private String extra = "";
	public boolean lanjut = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
//		new ProgressTask(SearchResult.this).execute();
		
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
	    
	    if (bahasanya.equals("EN")){
	    	setTitle ("Search Result");
	    }
	
		ObjectItemData = Global.ObjectItemData;
		
		if(ObjectItemData.length > 0) {
			RelativeLayout relativeLayoutGridKosong = (RelativeLayout) findViewById (R.id.relativeLayoutGridKosong);
			relativeLayoutGridKosong.setVisibility(RelativeLayout.GONE);
		} else {
			TextView textViewGridKosong = (TextView) findViewById(R.id.TextViewGridKosong);
			if (bahasanya.equals("EN")) {
				textViewGridKosong.setText("Not Found");
			} else {
				textViewGridKosong.setText("Tidak ditemukan");
			}
		}
		
		GridAdapterItem adapter = new GridAdapterItem(this, R.layout.gridviewadapter, ObjectItemData);
		GridView grid;
		
		grid = (GridView) findViewById(R.id.gridViewReg);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Toast.makeText(GridBook.this,
//						"You Clicked at " + position, Toast.LENGTH_SHORT)
//						.show();
				Intent i = new Intent(SearchResult.this, Download.class);
				String namaFile = ObjectItemData[position].itemTitle;
				String sizeFile = ObjectItemData[position].itemFileSize;
				String tipeFile = ObjectItemData[position].itemFileType;
				String linkDLFile = ObjectItemData[position].itemDownloadUrl;
				String carry = namaFile + "," + sizeFile + "," + tipeFile + "," + linkDLFile; 
				i.putExtra("DownloadCarryFromSearch", ""+carry);
				startActivity(i);
				//overridePendingTransition(R.anim.rtol, R.anim.ltor);
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