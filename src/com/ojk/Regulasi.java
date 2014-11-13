package com.ojk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojk.entities.*;
import com.ojk.business.*;

public class Regulasi extends Activity {
	public Boolean flag = false;
	public ObjectItemListView[] ObjectItemData = null;
	DatabaseMenuRegulasi databaseMenuRegulasi;
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

		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		databaseMenuRegulasi = new DatabaseMenuRegulasi(getApplicationContext());
		databaseMenuRegulasi.getWritableDatabase();

		// DEBUG ISI
//		 ArrayList isiDatabaseMenuRegulasi = databaseMenuRegulasi.fetchDataGrid();
//		 Log.d("IsiDBnya", isiDatabaseMenuRegulasi.toString());
		// Toast.makeText(getApplicationContext(),
		// "" + databaseMenuRegulasi.getCountFromIDParent(2),
		// Toast.LENGTH_LONG).show();

		if (bahasanya.equals("EN")) {
			kodeBahasa = "en";
			setTitle("Regulation");
			if (getIntent().getStringExtra("ObjectItemDataCarry") != null) {
				String extra = getIntent()
						.getStringExtra("ObjectItemDataCarry").toString();
				String[] splitExtra = extra.split(",");
				setTitle(splitExtra[1]);
				int countMenu = databaseMenuRegulasi
						.getCountFromIDParentEn(Integer.parseInt(splitExtra[0]));
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParentEn(Integer
								.parseInt(splitExtra[0]));
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			} else {
				int countMenu = databaseMenuRegulasi.getCountFromIDParentEn(1);
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParentEn(1);
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			}
			
		} else {

			// Masukin ke dalam kelas Object item
			if (getIntent().getStringExtra("ObjectItemDataCarry") != null) {
				String extra = getIntent()
						.getStringExtra("ObjectItemDataCarry").toString();
				String[] splitExtra = extra.split(",");
				setTitle(splitExtra[1]);
				int countMenu = databaseMenuRegulasi
						.getCountFromIDParent(Integer.parseInt(splitExtra[0]));
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParent(Integer
								.parseInt(splitExtra[0]));
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			} else {
				int countMenu = databaseMenuRegulasi.getCountFromIDParent(1);
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParent(1);
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			}
		}

		ArrayAdapterItem adapter = new ArrayAdapterItem(this,
				R.layout.listviewadapter, ObjectItemData);
		ListView listViewItems = (ListView) findViewById(R.id.listViewReg);		
		// listViewItems.setOnItemClickListener(new
		// OnItemClickListenerListViewItem());
		listViewItems
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						view.setBackgroundColor(color.black);

						if (ObjectItemData[position].isLastChild == 0) {
							Intent i = new Intent(Regulasi.this, Regulasi.class);
							String carry = "" + ObjectItemData[position].itemId
									+ "," + ObjectItemData[position].itemName;
							i.putExtra("ObjectItemDataCarry", "" + carry);
							startActivityForResult(i, 0);
						} else {
							Intent i = new Intent(Regulasi.this, GridBook.class);
							String title = "title";
							title = ObjectItemData[position].itemName
									.toString();
							String url = ObjectItemData[position].url;
							int idnya = ObjectItemData[position].itemId;
							i.putExtra("CarryForGridBook", "" + title + ","
									+ url + "," + idnya);
							startActivityForResult(i, 0);
						}
					}
				});
		listViewItems.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			// overridePendingTransition(R.anim.slideout, R.anim.slidein);
			return true;
		case R.id.action_search:
			Intent i = new Intent(Regulasi.this, Search.class);
			startActivityForResult(i, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");
		
		if (bahasanya.equals("EN")) {
			kodeBahasa = "en";
			setTitle("Regulation");
			if (getIntent().getStringExtra("ObjectItemDataCarry") != null) {
				String extra = getIntent()
						.getStringExtra("ObjectItemDataCarry").toString();
				String[] splitExtra = extra.split(",");
				setTitle(splitExtra[1]);
				int countMenu = databaseMenuRegulasi
						.getCountFromIDParentEn(Integer.parseInt(splitExtra[0]));
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParentEn(Integer
								.parseInt(splitExtra[0]));
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			} else {
				int countMenu = databaseMenuRegulasi.getCountFromIDParentEn(1);
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParentEn(1);
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			}
			
		} else {

			// Masukin ke dalam kelas Object item
			if (getIntent().getStringExtra("ObjectItemDataCarry") != null) {
				String extra = getIntent()
						.getStringExtra("ObjectItemDataCarry").toString();
				String[] splitExtra = extra.split(",");
				setTitle(splitExtra[1]);
				int countMenu = databaseMenuRegulasi
						.getCountFromIDParent(Integer.parseInt(splitExtra[0]));
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParent(Integer
								.parseInt(splitExtra[0]));
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			} else {
				int countMenu = databaseMenuRegulasi.getCountFromIDParent(1);
				ObjectItemData = new ObjectItemListView[countMenu];
				ArrayList<ObjectItemListView> dummy = databaseMenuRegulasi
						.fetchObjectFromIDParent(1);
				for (int i = 0; i < dummy.size(); i++) {
					ObjectItemData[i] = dummy.get(i);
				}
			}
		}

		ArrayAdapterItem adapter = new ArrayAdapterItem(this,
				R.layout.listviewadapter, ObjectItemData);
		
		ListView listViewItems = (ListView) findViewById(R.id.listViewReg);		
		
		listViewItems.setAdapter(adapter);
	}
}
