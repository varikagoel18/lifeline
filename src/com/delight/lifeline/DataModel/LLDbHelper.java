package com.delight.lifeline.DataModel;

import com.delight.lifeline.DataModel.LLContract;
import com.delight.lifeline.DataModel.LLContract.Tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LLDbHelper extends SQLiteOpenHelper {
	
	
	private static String TAG = "LLDbHelper";
    /**
     * The database that the provider uses as its underlying data store
     */
    private static final String DATABASE_NAME = "lifeline.db";

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 1;
    
    
	LLDbHelper(Context context) {

           // calls the super constructor, requesting the default cursor factory.
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
       }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		createEventsTable( db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS notes");

        // Recreates the database with a new version
        onCreate(db);
		
	}
	
	
	private void createEventsTable(SQLiteDatabase db)
	{
	      db.execSQL("CREATE TABLE " + Tables.EVENTS + " (" +
	                LLContract.Events._ID + " INTEGER PRIMARY KEY," +
	                LLContract.Events.EVENT_DETAIL + " TEXT ," +
	                LLContract.Events.EVENT_TYPE + " INTEGER," +
	                LLContract.Events.EVENT_TIME + " INTEGER ," +
	                LLContract.Events.EVENT_TAG + " TEXT, " +
	                LLContract.Events.EVENT_ADDRESS + " TEXT " +
	                ");");
	}
	
	

}
