package com.ojk.business;

import java.util.ArrayList;

import com.ojk.entities.ObjectItemGridView;
import com.ojk.entities.ObjectItemListView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseMenuRegulasi extends SQLiteOpenHelper {
	private String FILENAME = "MenuRegulasi.db";

	public DatabaseMenuRegulasi(Context context) {
		super(context, "FILENAME", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String table = "CREATE TABLE `MenuRegulasi` (`id`	INTEGER NOT NULL,`title`	TEXT,`url`	TEXT,`idparent`	INTEGER,`islastchild`	INTEGER,`anakcount`	INTEGER);";
		db.execSQL(table);
		table = "CREATE TABLE `GridRegulasi` (`id`	INTEGER NOT NULL,`title`	TEXT,`downloadurl`	TEXT,`filesize`	TEXT,`filetype`	TEXT,`parenturl`	TEXT, `idparent` INTEGER, `isread` INTEGER, `createdon` TEXT, `downloadedon` TEXT, `url` TEXT);";
		db.execSQL(table);
		table = "CREATE TABLE `MenuRegulasiEn` (`id`	INTEGER NOT NULL,`title`	TEXT,`url`	TEXT,`idparent`	INTEGER,`islastchild`	INTEGER,`anakcount`	INTEGER);";
		db.execSQL(table);
		table = "CREATE TABLE `GridRegulasiEn` (`id`	INTEGER NOT NULL,`title`	TEXT,`downloadurl`	TEXT,`filesize`	TEXT,`filetype`	TEXT,`parenturl`	TEXT, `idparent` INTEGER, `isread` INTEGER, `createdon` TEXT, `downloadedon` TEXT, `url` TEXT);";
		db.execSQL(table);
		table = "CREATE TABLE `OjkTerbaru` (`id`	INTEGER NOT NULL,`itemtype` TEXT ,`title`	TEXT,`downloadurl`	TEXT,`filesize`	TEXT,`filetype`	TEXT,`parenturl`	TEXT, `url` TEXT, `created` TEXT, `sortdate` TEXT, `isread` INTEGER);";
		db.execSQL(table);
		table = "CREATE TABLE `OjkTerbaruEn` (`id`	INTEGER NOT NULL,`itemtype` TEXT ,`title`	TEXT,`downloadurl`	TEXT,`filesize`	TEXT,`filetype`	TEXT,`parenturl`	TEXT, `url` TEXT, `created` TEXT, `sortdate` TEXT, `isread` INTEGER);";
		db.execSQL(table);
	}
	
	//drop drop table
	public void dropMenuRegulasi(){
		String drop = "DROP TABLE `MenuRegulasi`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}

	public void dropMenuRegulasiEn() {
		String drop = "DROP TABLE `MenuRegulasiEn`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}

	public void dropGridRegulasi() {
		String drop = "DROP TABLE `GridRegulasi`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}

	public void dropGridRegulasiEn() {
		String drop = "DROP TABLE `GridRegulasiEn`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}

	public void dropOjkTerbaru() {
		String drop = "DROP TABLE `OjkTerbaru`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}

	public void dropOjkTerbaruEn() {
		String drop = "DROP TABLE `OjkTerbaruEn`";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(drop, null);
		cursor.moveToFirst();
	}
	
	public void vacuum(){
		String query = "VACUUM";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		cursor.moveToFirst();
	}
	
	// Query2 OJKTerbaru
	public void insertDataOJKTerbaru(int id,String itemtype, String title, String downloadurl, String filesize, String filetype, String parenturl, String url, String created, String sortdate, int isread) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("itemtype", itemtype);
		values.put("title", title);
		values.put("downloadurl", downloadurl);
		values.put("filesize", filesize);
		values.put("filetype", filetype);
		values.put("parenturl", parenturl);
		values.put("url", url);
		values.put("created", created);
		values.put("sortdate", sortdate);
		values.put("isread", isread);
		sqLiteDatabase.insert("OjkTerbaru", null, values);
	}
	
	public void insertDataOJKTerbaruEn(int id,String itemtype, String title, String downloadurl, String filesize, String filetype, String parenturl, String url, String created, String sortdate, int isread) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("itemtype", itemtype);
		values.put("title", title);
		values.put("downloadurl", downloadurl);
		values.put("filesize", filesize);
		values.put("filetype", filetype);
		values.put("parenturl", parenturl);
		values.put("url", url);
		values.put("created", created);
		values.put("sortdate", sortdate);
		values.put("isread", isread);
		sqLiteDatabase.insert("OjkTerbaruEn", null, values);
	}
	
	public ArrayList<ObjectItemListView> fetchDataOJKTerbaru() {	
		ArrayList<ObjectItemListView> ListOfObj = new ArrayList<ObjectItemListView>();
		
		String fetchdata = "select * from OjkTerbaru order by sortdate desc";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemListView ObjData = new ObjectItemListView(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemListView> fetchDataOJKTerbaruEn() {
		ArrayList<ObjectItemListView> ListOfObj = new ArrayList<ObjectItemListView>();
		
		String fetchdata = "select * from OjkTerbaruEn order by created";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemListView ObjData = new ObjectItemListView(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public void updateFileSizeOJKTerbaruUsingUrlEn(String url, String fileSize) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("filesize", fileSize);
		sqLiteDatabase.update("OjkTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateFileSizeOJKTerbaruUsingUrl(String url, String fileSize) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("filesize", fileSize);
		sqLiteDatabase.update("OjkTerbaru", values, "url='" + url + "'", null);
	}
	
	
	public void updateCreatedOJKTerbaruUsingUrlEn(String url, String newCreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("created", newCreated);
		sqLiteDatabase.update("OjkTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateCreatedOJKTerbaruUsingUrl(String url, String newCreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("created", newCreated);
		sqLiteDatabase.update("OjkTerbaru", values, "url='" + url + "'", null);
	}
	
	public void updateSortdateOJKTerbaruEn(String url, String newSortdate) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sortdate", newSortdate);
		sqLiteDatabase.update("OjkTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateSortdateOJKTerbaru(String url, String newSortdate) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sortdate", newSortdate);
		sqLiteDatabase.update("OjkTerbaru", values, "url='" + url + "'", null);
	}
	
	public void updateCreatedOJKTerbaruEn(String url, String newCreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("created", newCreated);
		sqLiteDatabase.update("OjkTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateCreatedOJKTerbaru(String url, String newCreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("created", newCreated);
		sqLiteDatabase.update("OjkTerbaru", values, "url='" + url + "'", null);
	}
	
	public void updateFileSizeGridEn(String url, String fileSize) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("filesize", fileSize);
		sqLiteDatabase.update("GridRegulasiEn", values, "url='" + url + "'", null);
	}
	
	public void updateFileSizeGrid(String url, String fileSize) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("filesize", fileSize);
		sqLiteDatabase.update("GridRegulasi", values, "url='" + url + "'", null);
	}
	
	public void updateCreatedGridEn(String url, String newcreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("createdon", newcreated);
		sqLiteDatabase.update("GridRegulasiEn", values, "url='" + url + "'", null);
		updateIsReadEn(url);
		updateIsDownloadedEn(url);
	}
	
	public void updateCreatedGrid(String url, String newcreated) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("createdon", newcreated);
		sqLiteDatabase.update("GridRegulasi", values, "url='" + url + "'", null);
		updateIsRead(url);
		updateIsDownloaded(url);
	}
	
	public void updateIsDownloadedEn(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("downloadedon", "no");
		sqLiteDatabase.update("GridRegulasiEn", values, "url='" + url + "'", null);
	}
	
	public void updateIsDownloaded(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("downloadedon", "no");
		sqLiteDatabase.update("GridRegulasi", values, "url='" + url + "'", null);
	}
	
	public void updateIsReadEn(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 0);
		sqLiteDatabase.update("GridRegulasiEn", values, "url='" + url + "'", null);
	}
	
	public void updateIsRead(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 0);
		sqLiteDatabase.update("GridRegulasi", values, "url='" + url + "'", null);
	}
	
	public int getCountAllOJKTerbaruUnreadEn() {
		String queryCount = "select count(*) from OJKTerbaruEn where isread = '0'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountAllOJKTerbaruUnread() {
		String queryCount = "select count(*) from OJKTerbaru where isread = '0'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public void updateUnReadOJKTerbaruEn(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 0);
		sqLiteDatabase.update("OJKTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateUnReadOJKTerbaru(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 0);
		sqLiteDatabase.update("OJKTerbaru", values, "url='" + url + "'", null);
	}
	
	public void updateIsReadOJKTerbaruEn(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		sqLiteDatabase.update("OJKTerbaruEn", values, "url='" + url + "'", null);
	}
	
	public void updateIsReadOJKTerbaru(String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		sqLiteDatabase.update("OJKTerbaru", values, "url='" + url + "'", null);
	}
	
	public boolean cekDiOJKTerbaru(String url) {
		String queryCount = "select count(*) from OjkTerbaru where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public boolean cekDiOJKTerbaruEn(String url) {
		String queryCount = "select count(*) from OjkTerbaru where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public boolean isReadOJKTerbaru(String url) {
		String queryCount = "select isread from OjkTerbaru where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public boolean isReadOJKTerbaruEn(String url) {
		String queryCount = "select isread from OjkTerbaru where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	//Stop OJK Terbaru
	
	//Start Query buat Search
	public String getIdDanIdParentDariDUrl(String downloadurl) {
		String hasil = "";
		String fetchdata = "select id, idparent from GridRegulasi where downloadurl= '" + downloadurl+ "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				hasil = cursor.getString(0) + "," + cursor.getString(1);
			} while (cursor.moveToNext());
		}
		return hasil;
	}
	
	public String getIdDanIdParentDariDUrlEn(String downloadurl) {
		String hasil = "";
		String fetchdata = "select id, idparent from GridRegulasiEn where downloadurl='" + downloadurl + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				hasil = cursor.getString(0) + "," + cursor.getString(1);
			} while (cursor.moveToNext());
		}
		return hasil;
	}
	
	//stop Query buat search
	
	public int getIdParentFromUrlEn(String url) {
		String queryCount = "select idparent from GridRegulasiEn where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getIdParentFromUrl(String url) {
		String queryCount = "select idparent from GridRegulasi where url = '" + url + "'";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public void dropDB() {
		String query = "drop table if exists MenuRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		cursor.moveToFirst();
	}
	
	public int getCountAll() {
		String queryCount = "select count(*) from MenuRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountAllEn() {
		String queryCount = "select count(*) from MenuRegulasiEn";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountAllGrid() {
		String queryCount = "select count(*) from GridRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getAnakCountEn(int id) {
		String queryCount = "select anakcount from MenuRegulasiEn where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getAnakCount(int id) {
		String queryCount = "select anakcount from MenuRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getIdParentEn(int id) {
		String queryCount = "select idparent from MenuRegulasiEn where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getIdParent(int id) {
		String queryCount = "select idparent from MenuRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public boolean haveParent(int id) {
		String queryCount = "select idparent from MenuRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) >= 0 ? true : false;
	}
	
	public boolean isLastChild(int id) {
		String queryCount = "select islastchild from MenuRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public boolean isDownloadedEn(int id) {
		String queryCount = "select * from GridRegulasiEn where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getString(9).equals("no") ? false : true;
	}
	
	public boolean isDownloaded(int id) {
		String queryCount = "select * from GridRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getString(9).equals("no") ? false : true;
	}
	
	public boolean isReadEn(int id) {
		String queryCount = "select isread from GridRegulasiEn where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public boolean isRead(int id) {
		String queryCount = "select isread from GridRegulasi where id = " + id;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0) == 1 ? true : false;
	}
	
	public int getCountAllGridUnreadEn() {
		String queryCount = "select count(*) from GridRegulasiEn where isread = 0";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountAllGridUnread() {
		String queryCount = "select count(*) from GridRegulasi where isread = 0";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountGridUnread(int idparent) {
		String queryCount = "select count(*) from GridRegulasi where isread = 0 and idparent = " + idparent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public void updateDataGridIsReadEn(int id) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		sqLiteDatabase.update("GridRegulasiEn", values, "id="+id, null);
	}
	
	public void updateDataGridIsRead(int id) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		sqLiteDatabase.update("GridRegulasi", values, "id="+id, null);
	}
	
	public void updateDataGridToDownloadedEn(int id, String date) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("downloadedon", date);
		sqLiteDatabase.update("GridRegulasiEn", values, "id="+id, null);
	}
	
	public void updateDataGridToDownloaded(int id, String date) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("downloadedon", date);
		sqLiteDatabase.update("GridRegulasi", values, "id="+id, null);
	}
	
	public void updateDataEn(int id, int anakCount) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("anakcount", anakCount);
		sqLiteDatabase.update("MenuRegulasiEn", values, "id="+id, null);
	}
	
	public void updateData(int id, int anakCount) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("anakcount", anakCount);
		sqLiteDatabase.update("MenuRegulasi", values, "id="+id, null);
	}

	public void insertDataGridEn(int id, String title, String downloadurl, String filesize, String filetype, String parenturl, int idparent, int isread, String createdOn, String downloadedOn, String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("downloadurl", downloadurl);
		values.put("filesize", filesize);
		values.put("filetype", filetype);
		values.put("parenturl", parenturl);
		values.put("idparent", idparent);
		values.put("isread", isread);
		values.put("createdon", createdOn);
		values.put("downloadedon", downloadedOn);
		values.put("url", url);
		sqLiteDatabase.insert("GridRegulasiEn", null, values);
	}
	
	public void insertDataGrid(int id, String title, String downloadurl, String filesize, String filetype, String parenturl, int idparent, int isread, String createdOn, String downloadedOn, String url) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("downloadurl", downloadurl);
		values.put("filesize", filesize);
		values.put("filetype", filetype);
		values.put("parenturl", parenturl);
		values.put("idparent", idparent);
		values.put("isread", isread);
		values.put("createdon", createdOn);
		values.put("downloadedon", downloadedOn);
		values.put("url", url);
		sqLiteDatabase.insert("GridRegulasi", null, values);
	}
	
	public void insertDataEn(int id, String title, String url, int idParent, int isLastChild, int anakCount) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("url", url);
		values.put("idparent", idParent);
		values.put("islastchild", isLastChild);
		values.put("anakcount", anakCount);
		sqLiteDatabase.insert("MenuRegulasiEn", null, values);
	}
	
	public void insertData(int id, String title, String url, int idParent, int isLastChild, int anakCount) {
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("url", url);
		values.put("idparent", idParent);
		values.put("islastchild", isLastChild);
		values.put("anakcount", anakCount);
		sqLiteDatabase.insert("MenuRegulasi", null, values);
	}
	
	public ArrayList fetchTitleGridFromParentURLEn(int idparent) {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select title from GridRegulasiEn where idparent = " + idparent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				stringArrayList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return stringArrayList;
	}
	
	public ArrayList fetchTitleGridFromParentURL(int idparent) {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select title from GridRegulasi where idparent = " + idparent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				stringArrayList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return stringArrayList;
	}
	
	public ArrayList fetchDataGrid() {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select * from GridRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				stringArrayList.add(cursor.getString(0));
				stringArrayList.add(cursor.getString(1));
				stringArrayList.add(cursor.getString(9));
			} while (cursor.moveToNext());
		}
		return stringArrayList;
	}

	public ArrayList fetchData() {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select * from MenuRegulasi";
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
	
	public ArrayList<ObjectItemGridView> fetchObjectAllGridEn () {
		ArrayList<ObjectItemGridView> ListOfObj = new ArrayList<ObjectItemGridView>();
		
		String fetchdata = "select * from GridRegulasiEn";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemGridView ObjData = new ObjectItemGridView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemGridView> fetchObjectAllGrid () {
		ArrayList<ObjectItemGridView> ListOfObj = new ArrayList<ObjectItemGridView>();
		
		String fetchdata = "select * from GridRegulasi";
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemGridView ObjData = new ObjectItemGridView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemGridView> fetchObjectSearchRegulasi (String yangDicari) {
		ArrayList<ObjectItemGridView> ListOfObj = new ArrayList<ObjectItemGridView>();
		
		String fetchdata = "select * from GridRegulasi where title like " + yangDicari;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemGridView ObjData = new ObjectItemGridView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemGridView> fetchObjectFromIDParentGridEn (int idparent) {
		ArrayList<ObjectItemGridView> ListOfObj = new ArrayList<ObjectItemGridView>();
		
		String fetchdata = "select * from GridRegulasiEn where idparent = " + idparent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemGridView ObjData = new ObjectItemGridView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemGridView> fetchObjectFromIDParentGrid (int idparent) {
		ArrayList<ObjectItemGridView> ListOfObj = new ArrayList<ObjectItemGridView>();
		
		String fetchdata = "select * from GridRegulasi where idparent = " + idparent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemGridView ObjData = new ObjectItemGridView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemListView> fetchObjectFromIDParentEn (int idParent) {
		ArrayList<ObjectItemListView> ListOfObj = new ArrayList<ObjectItemListView>();
		
		String fetchdata = "select * from MenuRegulasiEn where idparent = "
				+ idParent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemListView ObjData = new ObjectItemListView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList<ObjectItemListView> fetchObjectFromIDParent (int idParent) {
		ArrayList<ObjectItemListView> ListOfObj = new ArrayList<ObjectItemListView>();
		
		String fetchdata = "select * from MenuRegulasi where idparent = "
				+ idParent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				ObjectItemListView ObjData = new ObjectItemListView(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
				ListOfObj.add(ObjData);
			} while (cursor.moveToNext());
		}
		return ListOfObj;
	}
	
	public ArrayList fetchTitleFromIDParent(int idParent) {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select Title from MenuRegulasi where idparent = "
				+ idParent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(fetchdata, null);
		if (cursor.moveToFirst()) {
			do {
				stringArrayList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return stringArrayList;
	}
	
	public int getCountFromIDParentEn(int idParent) {
		String queryCount = "select count(*) from MenuRegulasiEn where idparent = " + idParent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	
	public int getCountFromIDParent(int idParent) {
		String queryCount = "select count(*) from MenuRegulasi where idparent = " + idParent;
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(queryCount, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public ArrayList fetchDataFromIDParent(int idParent) {
		ArrayList<String> stringArrayList = new ArrayList<String>();
		String fetchdata = "select * from MenuRegulasi where idparent = "
				+ idParent;
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
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
