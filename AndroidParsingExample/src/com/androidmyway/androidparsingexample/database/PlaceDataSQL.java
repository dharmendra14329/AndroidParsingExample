package com.androidmyway.androidparsingexample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressWarnings("unused")
/** Helper to the database, manages versions and creation */
public class PlaceDataSQL extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "xmlParse.db";
	private static final int DATABASE_VERSION = 1;

	private Context context;

	public PlaceDataSQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE xmlTable (id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20), description varchar(20), " +
				"cost varchar(20))");
	}

	
	private void versionUpdation(SQLiteDatabase db) {

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase(String db) {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = "data/data/"+ context.getPackageName() +"/databases/" + db;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		} catch (Exception e) {

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		if (oldVersion == 1) {
			Log.d("New Version", "Datas can be upgraded");
		}

		Log.d("Sample Data", "onUpgrade	: " + newVersion);
	}

}