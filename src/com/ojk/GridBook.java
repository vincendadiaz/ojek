package com.ojk;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojk.business.BaseAdapterItem;
import com.ojk.business.DatabaseMenuRegulasi;
import com.ojk.business.GridAdapterItem;
import com.ojk.entities.ObjectItemGridView;
import com.ojk.entities.ObjectItemListView;


public class GridBook extends Activity {

	public ObjectItemGridView[] ObjectItemData = null;
	private DatabaseMenuRegulasi databaseMenuRegulasi;
	private String url;
	private int idnya;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
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
		if (getIntent().getStringExtra("CarryForGridBook") != null) {
			String extra = getIntent().getStringExtra("CarryForGridBook").toString();
			String[] splitExtra = extra.split(",");
			
			setTitle(""+splitExtra[0]);
			url = splitExtra[1];
			idnya = Integer.parseInt(splitExtra[2]);
			
		}
		
		GridView grid;
		
		databaseMenuRegulasi = new DatabaseMenuRegulasi(getApplicationContext());
		databaseMenuRegulasi.getWritableDatabase();
		
		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");
		
		if (bahasanya.equals("EN")) {
			
			int countMenu = databaseMenuRegulasi.fetchTitleGridFromParentURLEn(idnya).size();
			Log.d("jumlahGridDariAnak",""+countMenu);
			
			ObjectItemData = new ObjectItemGridView[countMenu];
			ArrayList<ObjectItemGridView> dummy = databaseMenuRegulasi.fetchObjectFromIDParentGridEn(idnya);
			int jumlah = dummy.size();
			
			String[] judul = new String[jumlah];
			for (int i = 0; i < jumlah; i++) {
				ObjectItemData[i] = dummy.get(i);
			}
		} else {
		
			int countMenu = databaseMenuRegulasi.fetchTitleGridFromParentURL(idnya).size();
			Log.d("jumlahGridDariAnak",""+countMenu);
			
			ObjectItemData = new ObjectItemGridView[countMenu];
			ArrayList<ObjectItemGridView> dummy = databaseMenuRegulasi.fetchObjectFromIDParentGrid(idnya);
			int jumlah = dummy.size();
			
			String[] judul = new String[jumlah];
			for (int i = 0; i < jumlah; i++) {
				ObjectItemData[i] = dummy.get(i);
			}
		}
		
		if(ObjectItemData.length > 0) {
			RelativeLayout relativeLayoutGridKosong = (RelativeLayout) findViewById (R.id.relativeLayoutGridKosong);
			relativeLayoutGridKosong.setVisibility(RelativeLayout.GONE);
		} else {
			TextView textViewGridKosong = (TextView) findViewById(R.id.TextViewGridKosong);
			if (bahasanya.equals("EN")) {
				textViewGridKosong.setText("No regulation");
			} else {
				textViewGridKosong.setText("Tidak ada regulasi");
			}
		}
		
		
		GridAdapterItem adapter = new GridAdapterItem(this,R.layout.gridviewadapter, ObjectItemData);
		
		grid = (GridView) findViewById(R.id.gridViewReg);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				int idParentA = databaseMenuRegulasi.getIdParentFromParentUrl(ObjectItemData[position].itemParentUrl);
//				Toast.makeText(GridBook.this,
//						""+ObjectItemData[position].itemIdParent + "|" + idParentA, Toast.LENGTH_SHORT)
//						.show();
				Intent i = new Intent(GridBook.this, Download.class);
				String namaFile = ObjectItemData[position].itemTitle;
				String sizeFile = ObjectItemData[position].itemFileSize;
				String tipeFile = ObjectItemData[position].itemFileType;
				String linkDLFile = ObjectItemData[position].itemDownloadUrl;
				int idnya = ObjectItemData[position].itemId;
				int idParent = ObjectItemData[position].itemIdParent;
				String carry = namaFile + "," + sizeFile + "," + tipeFile + "," + linkDLFile + "," + idnya + "," + idParent; 
				i.putExtra("DownloadCarry", ""+carry);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
