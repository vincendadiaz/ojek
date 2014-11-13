package com.ojk.business;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseGridRegulasi extends SQLiteOpenHelper {
	private String FILENAME = "GridRegulasi.db";

	public DatabaseGridRegulasi(Context context) {
		super(context, "FILENAME", null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String table = "CREATE TABLE `GridRegulasi` (`id`	INTEGER NOT NULL,`title`	TEXT,`downloadurl`	TEXT,`filesize`	TEXT,`filetype`	TEXT,`parenturl`	TEXT);";
		Log.d("DATABASE GRID", "terbikin");
		db.execSQL(table);
	}
	
	public void dropDB() {
		String query = "drop table if exists GridRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		cursor.moveToFirst();
	}
	
	public int getCountAll() {
		String queryCount = "select count(*) from GridRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public void insertData(int id, String title, String downloadurl, String filesize, String filetype, String parenturl) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("downloadurl", downloadurl);
		values.put("filesize", filesize);
		values.put("filetype", filetype);
		values.put("parenturl", parenturl);
		sqLiteDatabase.insert("GridRegulasi", null, values);
	}
	
	
	
	public ArrayList fetchDataFromParentURL(String parenturl) {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select Title from GridRegulasi where parenturl = "
				+ parenturl;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				stringArrayList.add(cursor.getString(0));
				stringArrayList.add(cursor.getString(1));
				stringArrayList.add(cursor.getString(2));
				stringArrayList.add(cursor.getString(3));
			} while (cursor.moveToNext());
		}
		return stringArrayList;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
}
