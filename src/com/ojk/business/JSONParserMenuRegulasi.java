package com.ojk.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONParserMenuRegulasi {
	
	private String ParsingData = "";
	DatabaseMenuRegulasi databaseMenuRegulasi;
	private Context context;
	
	public void JSONtoDB(String urlWeb) {
//		Context context = this.context;
//		databaseMenuRegulasi = new DatabaseMenuRegulasi(context);
//		//context.deleteDatabase("MenuRegulasi.db");
//		databaseMenuRegulasi.getWritableDatabase();
//		JSONParser jParser = new JSONParser();
//		JSONObject json = jParser.getJSONFromUrl(urlWeb);
//		try {
//			JSONObject title = json.getJSONObject("Title");
//			if (title.getString("Title").toString().equals("Regulasi")) {
//				String judul = title.getString("title").toString();
//				String url = title.getString("url").toString();
//				int idParent = 0;
//				databaseMenuRegulasi.insertData(judul, url, idParent);
//			}
//		} catch (JSONException e) {
//			Log.d("JSONError di ",e.toString());
//		}
	}
}
