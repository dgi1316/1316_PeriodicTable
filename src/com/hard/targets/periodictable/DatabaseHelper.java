package com.hard.targets.periodictable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends SQLiteOpenHelper {

	private SQLiteDatabase myDatabase;
	private final Context myContext;
	private static final String DATABASE_NAME = "periodic.mp3";
	public final static String DATABASE_PATH = "/data/data/com.hard.targets.periodictable/databases/";
	public static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	public void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		
		if(dbExist) {
			Log.v("DB Exists", "db exists");
		}
		
		boolean dbExist1 = checkDatabase();
		
		if(!dbExist1) {
			this.getReadableDatabase();
			try {
				this.close();
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying Database");
			}
		}
	}
	
	private boolean checkDatabase() {
		boolean checkDB = false;
		
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			File dbFile = new File(myPath);
			checkDB = dbFile.exists();
		} catch (SQLiteException e) {
		}
		
		return checkDB;
	}
	
	private void copyDatabase() throws IOException {
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		
		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
		
		byte[] buffer = new byte[1024];
		int length;
		
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}
	
	public void db_delete() {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		if (file.exists()) {
			file.delete();
			System.out.println("Delete Database File");
		}
	}
	
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public synchronized void closeDatabase() throws SQLException {
		if (myDatabase != null)
			myDatabase.close();
		super.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.v("Database Uppgrade", "Database version higher than old.");
			db_delete();
		}
	}
	
	public String getAtomicNumber(String table, int i) {
		String ano;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		ano = c.getString(3);
		return ano;
	}
	
	public String getSymbol(String table, int i) {
		String sy;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		sy = c.getString(4);
		return sy;
	}
	
	public String getName(String table, int i) {
		String name;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		name = c.getString(5);
		return name;
	}
	
	public String getAtomicWeight(String table, int i) {
		String weight;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		weight = c.getString(6);
		return weight;
	}
	
	public String getURL(String table, int i) {
		String url;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		url = c.getString(7);
		return url;
	}
	
	public String getTextColor(String table, int i) {
		String color;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		color = c.getString(0);
		return color;
	}
	
	public String getColor(String table, int i) {
		String color;
		String query = "SELECT * FROM " + table + " where _id = " + i;
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		color = c.getString(1);
		return color;
	}
	
	public LinkedList<String> getAllElements(String table) {
		String query = "SELECT * FROM " + table;
		LinkedList<String> names = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			names.add(c.getString(5));
			c.moveToNext();
		}
		return names;
	}
	
	public LinkedList<String> populateList(String table) {
		String query = "SELECT * FROM " + table;
		LinkedList<String> items = new LinkedList<String>();
		Cursor c = myDatabase.rawQuery(query, null);
		c.moveToFirst();
		for(int i = 0; i <= c.getCount() - 1; i++) {
			items.add(c.getString(3));
			items.add(c.getString(5));
			c.moveToNext();
		}
		c.close();
		return items;
	}
	
	public Cursor search(String table, String search) {
		String query = "SELECT * FROM " + table + " WHERE atomicNo='" + search + "' OR name='" + search + "' ";
		Cursor c = myDatabase.rawQuery(query, null);
		return c;
	}
	
	public Cursor view(String table, String search) {
		String query = "SELECT * FROM " + table + " WHERE _id='" + search + "' ";
		Cursor c = myDatabase.rawQuery(query, null);
		return c;
	}
}
