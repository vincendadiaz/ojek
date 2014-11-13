package com.ojk;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojk.entities.*;
import com.ojk.business.*;

public class About extends Activity {

	private String kodeBahasa = "";
	ObjectItemListView[] ObjectItemData = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c70100")));
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
		ObjectItemData = new ObjectItemListView[6];

		
		SharedPreferences settings = getSharedPreferences("bahasa", MODE_PRIVATE);
	    String bahasanya = settings.getString("bahasanya", "ID");
	    
	    if (bahasanya.equals("EN")) {
	    	kodeBahasa = "en";
	    	setTitle("About OJK");
	    	ObjectItemData[0] = new ObjectItemListView(0, "Vision-Mision");
			ObjectItemData[1] = new ObjectItemListView(1, "Duties & Functions");
			ObjectItemData[2] = new ObjectItemListView(2, "Board of Commisioners");
			ObjectItemData[3] = new ObjectItemListView(3,
					"Values");
			ObjectItemData[4] = new ObjectItemListView(4, "Organizational Structure");
			ObjectItemData[5] = new ObjectItemListView(5, "Employee Code of Conduct");
	    } else {
	    	kodeBahasa = "id";
		ObjectItemData[0] = new ObjectItemListView(0, "Visi-Misi");
		ObjectItemData[1] = new ObjectItemListView(1, "Tugas & Fungsi");
		ObjectItemData[2] = new ObjectItemListView(2, "Dewan Komisioner");
		ObjectItemData[3] = new ObjectItemListView(3,
				"Nilai - nilai");
		ObjectItemData[4] = new ObjectItemListView(4, "Struktur Organisasi");
		ObjectItemData[5] = new ObjectItemListView(5, "Kode Etik Pegawai");

	    }
		ArrayAdapterItem adapter = new ArrayAdapterItem(this,
				R.layout.listviewadapternocount, ObjectItemData);
		ListView listViewItems = (ListView) findViewById(R.id.listViewReg);
		// listViewItems.setOnItemClickListener(new
		// OnItemClickListenerListViewItem());
		listViewItems
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						view.setBackgroundColor(color.black);
						Intent i = new Intent(About.this, Web.class);
						i.putExtra("POSITION", "" + position + "," +ObjectItemData[position].itemName.toString());
						startActivity(i);
						//overridePendingTransition(R.anim.rtol, R.anim.ltor);
					}
				});
		listViewItems.setAdapter(adapter);
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
			//overridePendingTransition(R.anim.ltor, R.anim.rtol);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
