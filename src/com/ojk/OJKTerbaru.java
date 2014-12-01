package com.ojk;

import java.util.ArrayList;

import com.ojk.business.ArrayAdapterItem;
import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.entities.Global;
import com.ojk.entities.ObjectItemListView;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OJKTerbaru extends Activity {

	private DatabaseMenuRegulasi databaseMenuRegulasi;
	private ObjectItemListView[] ObjectItemData = null;
	private String kodeBahasa = "id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
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
	    	kodeBahasa = "id";
	    } else if (bahasanya.equals("EN")){
	    	kodeBahasa = "en";
	    	setTitle("Latest OJK");
	    }
		
		databaseMenuRegulasi = new DatabaseMenuRegulasi(getApplicationContext());
		databaseMenuRegulasi.getWritableDatabase();
		ArrayList<ObjectItemListView> arrayListObj = new ArrayList<ObjectItemListView>();
		if (kodeBahasa.equals("id")) {
			arrayListObj = databaseMenuRegulasi.fetchDataOJKTerbaru(); 
		} else {
			arrayListObj = databaseMenuRegulasi.fetchDataOJKTerbaruEn();
		}
		
		ObjectItemData = new ObjectItemListView[arrayListObj.size()];
		for (int i = 0; i < arrayListObj.size(); i++) {
			ObjectItemData[i] = arrayListObj.get(i);
		}
		
		if (ObjectItemData.length == 0) {
			TextView textViewListKosong = (TextView) findViewById(R.id.TextViewListKosong);
			if (kodeBahasa.equals("id")) {
				textViewListKosong.setText("Tidak ada OJK Terbaru");
			} else {
				textViewListKosong.setText("There is no latest OJK");
			}
		}

		if (ObjectItemData != null) {

			ArrayAdapterItem adapter = new ArrayAdapterItem(this,
					R.layout.listviewadapternocount, ObjectItemData);
			ListView listViewItems = (ListView) findViewById(R.id.listViewReg);
			// listViewItems.setOnItemClickListener(new
			// OnItemClickListenerListViewItem());
			listViewItems
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							view.setBackgroundColor(color.black);
							if (!ObjectItemData[position].itemType.equals("regulasi")) {
								databaseMenuRegulasi.updateIsReadOJKTerbaru(ObjectItemData[position].url);
								Intent i = new Intent(OJKTerbaru.this, Web.class);
								String extra = ObjectItemData[position].itemType + "," + ObjectItemData[position].url+"?mobile=1"; 
								i.putExtra("FromOJKTerbaruURL", extra);
								Log.d("extra", extra);
								startActivityForResult(i, 0);
							} else {
								Intent i = new Intent(OJKTerbaru.this, Download.class);
								String namaFile = ObjectItemData[position].itemName;
								String sizeFile = ObjectItemData[position].fileSize;
								String tipeFile = ObjectItemData[position].fileType;
								String linkDLFile = ObjectItemData[position].downloadUrl;
								String url = ObjectItemData[position].url;
								String carry = namaFile + "," + sizeFile + "," + tipeFile + "," + linkDLFile + "," + url; 
								i.putExtra("DownloadCarryFromSearch", ""+carry);
								startActivity(i);
							}
						}
					});
			listViewItems.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		// return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			// overridePendingTransition(R.anim.ltor, R.anim.rtol);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences settings = getSharedPreferences("bahasa", MODE_PRIVATE);
	    String bahasanya = settings.getString("bahasanya", "ID");
		
		if (bahasanya.equals("ID")) {
	    	kodeBahasa = "id";
	    } else if (bahasanya.equals("EN")){
	    	kodeBahasa = "en";
	    	setTitle("Latest OJK");
	    }
		
		databaseMenuRegulasi = new DatabaseMenuRegulasi(getApplicationContext());
		databaseMenuRegulasi.getWritableDatabase();
		ArrayList<ObjectItemListView> arrayListObj = new ArrayList<ObjectItemListView>();
		if (kodeBahasa.equals("id")) {
			arrayListObj = databaseMenuRegulasi.fetchDataOJKTerbaru(); 
		} else {
			arrayListObj = databaseMenuRegulasi.fetchDataOJKTerbaruEn();
		}
		
		ObjectItemData = new ObjectItemListView[arrayListObj.size()];
		for (int i = 0; i < arrayListObj.size(); i++) {
			ObjectItemData[i] = arrayListObj.get(i);
		}
		
		if (ObjectItemData.length == 0) {
			TextView textViewListKosong = (TextView) findViewById(R.id.TextViewListKosong);
			if (kodeBahasa.equals("id")) {
				textViewListKosong.setText("Tidak ada OJK Terbaru");
			} else {
				textViewListKosong.setText("There is no latest OJK");
			}
		}

		if (ObjectItemData != null) {

			ArrayAdapterItem adapter = new ArrayAdapterItem(this,
					R.layout.listviewadapternocount, ObjectItemData);
			ListView listViewItems = (ListView) findViewById(R.id.listViewReg);
			// listViewItems.setOnItemClickListener(new
			// OnItemClickListenerListViewItem());
			listViewItems
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							view.setBackgroundColor(color.black);
							if (!ObjectItemData[position].itemType.equals("regulasi")) {
								databaseMenuRegulasi.updateIsReadOJKTerbaru(ObjectItemData[position].url);
								Intent i = new Intent(OJKTerbaru.this, Web.class);
								String extra = ObjectItemData[position].itemType + "," + ObjectItemData[position].url+"?mobile=1"; 
								i.putExtra("FromOJKTerbaruURL", extra);
								Log.d("extra", extra);
								startActivityForResult(i, 0);
							} else {
								Intent i = new Intent(OJKTerbaru.this, Download.class);
								String namaFile = ObjectItemData[position].itemName;
								String sizeFile = ObjectItemData[position].fileSize;
								String tipeFile = ObjectItemData[position].fileType;
								String linkDLFile = ObjectItemData[position].downloadUrl;
								String url = ObjectItemData[position].url;
								String carry = namaFile + "," + sizeFile + "," + tipeFile + "," + linkDLFile + "," + url;  
								i.putExtra("DownloadCarryFromSearch", ""+carry);
								startActivity(i);
							}
						}
					});
			listViewItems.setAdapter(adapter);
		}
	}
}

