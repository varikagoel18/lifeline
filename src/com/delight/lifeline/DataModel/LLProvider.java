package com.delight.lifeline.DataModel;

import java.util.HashMap;

import com.delight.lifeline.DataModel.LLDbHelper;
import com.delight.lifeline.DataModel.LLContract;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LLProvider extends ContentProvider {
	
	private LLDbHelper m_DbHelper;
	
	private static UriMatcher sUriMatcher ;
	protected static final HashMap<String, String> sEventsProjectionMap;
	
    private static final  int QUERY_TYPE_EVENTS = 1;
    private static final int QUERY_TYPE_EVENT_ID = 2;
    
    

    public static final int EVENT_ID_PATH_POSITION = 1;

	
	static {

        /*
         * Creates and initializes the URI matcher
         */
        // Create a new instance
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(LLContract.AUTHORITY, "events", QUERY_TYPE_EVENTS);

        sUriMatcher.addURI(LLContract.AUTHORITY, "events/#", QUERY_TYPE_EVENT_ID);

       
        /*
         * Creates and initializes a projection map that returns all columns
         */

        // Creates a new projection map instance. The map returns a column name
        // given a string. The two are usually equal.
        sEventsProjectionMap = new HashMap<String, String>();

        sEventsProjectionMap.put(LLContract.Events._ID, LLContract.Events._ID);
        sEventsProjectionMap.put(LLContract.Events.EVENT_DETAIL, LLContract.Events.EVENT_DETAIL);
        sEventsProjectionMap.put(LLContract.Events.EVENT_TYPE , LLContract.Events.EVENT_TYPE);
        sEventsProjectionMap.put(LLContract.Events.EVENT_TIME , LLContract.Events.EVENT_TIME);
        sEventsProjectionMap.put(LLContract.Events.EVENT_TAG , LLContract.Events.EVENT_TAG);
        sEventsProjectionMap.put(LLContract.Events.EVENT_ADDRESS , LLContract.Events.EVENT_ADDRESS);




    }

	@Override
    public int delete(Uri uri, String where, String[] whereArgs) {
		  // Opens the database object in "write" mode.
        SQLiteDatabase db = m_DbHelper.getWritableDatabase();
        String finalWhere;

        int count;
        
        switch (sUriMatcher.match(uri)) {

        case QUERY_TYPE_EVENTS:
            count = db.delete( LLContract.Events.TABLE_NAME, where, whereArgs );
            break;

        case QUERY_TYPE_EVENT_ID:
        {
            String eventID = uri.getPathSegments().get(1);

            finalWhere = LLContract.Events._ID +  " = " + eventID ;
            if (where !=null) {
                finalWhere = finalWhere + " AND " + where;
            }
            count = db.delete( LLContract.Events.TABLE_NAME, finalWhere, whereArgs );
        }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
    }
    
      getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;
	}

	@Override
	public String getType(Uri url) {
		
		int match = sUriMatcher.match(url);
        switch (match) {
        case QUERY_TYPE_EVENTS:
            return "vnd.android.cursor.dir/event";
        case QUERY_TYPE_EVENT_ID:
            return "vnd.android.cursor.item/event";
       default:
           throw new IllegalArgumentException("Unknown URL " + url);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues)
	{
		Uri result = null;;
		switch (sUriMatcher.match(uri))
		{
		   case QUERY_TYPE_EVENTS:
		   {
			   result = insertInEventsTable(initialValues); 
			   break;
		   }
		   default:
			   throw new IllegalArgumentException("Unknown URI " + uri);
		}
	    if(result == null)
	    {
           throw new SQLException("Failed to insert row into " + uri);
	    }
		return result;
	}

	@Override
	public boolean onCreate() {
		m_DbHelper = new LLDbHelper(getContext());
	    return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	           String sortOrder) {
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case QUERY_TYPE_EVENTS:
		{
			qb.setProjectionMap(sEventsProjectionMap);
			qb.setTables(LLContract.Tables.EVENTS);
		}
		break;
		case QUERY_TYPE_EVENT_ID:
			qb.setProjectionMap(sEventsProjectionMap);
			qb.setTables(LLContract.Tables.EVENTS);
			qb.appendWhere( LLContract.Events._ID + "=?" +uri.getPathSegments().get(EVENT_ID_PATH_POSITION));
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw an exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = m_DbHelper.getReadableDatabase();
		
		

		/*
		 * Performs the query. If no problems occur trying to read the database, then a Cursor
		 * object is returned; otherwise, the cursor variable contains null. If no records were
		 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(
				db,            // The database to query
				projection,    // The columns to return from the query
				selection,     // The columns for the where clause
				selectionArgs, // The values for the where clause
				null,          // don't group the rows
				null,          // don't filter by row groups
				sortOrder        // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data changes
		if(c!= null)
		{
		   c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return c;
	}

	@Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs){
		
		// Opens the database object in "write" mode.
        SQLiteDatabase db = m_DbHelper.getWritableDatabase();
        int count;
        String finalWhere;

        // Does the update based on the incoming URI pattern
        switch (sUriMatcher.match(uri)) {

            case QUERY_TYPE_EVENTS:
                count = db.update( LLContract.Events.TABLE_NAME, values, where, whereArgs );
                break;

            case QUERY_TYPE_EVENT_ID:
            {
                String eventID = uri.getPathSegments().get(1);

                finalWhere = LLContract.Events._ID +  " = " + eventID ;
                if (where !=null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update( LLContract.Events.TABLE_NAME, values, finalWhere, whereArgs );
            }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows updated.
        return count;
	}
	
	

	private Uri insertInEventsTable( ContentValues initialValues )
	{
        ContentValues values;

        if (initialValues != null) {
            values = new ContentValues(initialValues);

        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = m_DbHelper.getWritableDatabase();

        long rowId = db.insert(
            LLContract.Events.TABLE_NAME,        
               LLContract.Events.EVENT_DETAIL,  values                           
                                             
        );
        
        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            // Creates a URI with the note ID pattern and the new row ID appended to it.
            Uri eventUri = ContentUris.withAppendedId(LLContract.Events.CONTENT_ID_URI_BASE, rowId);

            getContext().getContentResolver().notifyChange(eventUri, null);
            return eventUri;
        }
        return null;

	}
}
