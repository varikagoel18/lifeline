package com.delight.lifeline.DataModel.Loaders;




import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

public class LLCustomCursorLoader extends CursorLoader {
	
	public static final int LOADER_ID = 101;
	
	private final Context mContext;
	
	

	public LLCustomCursorLoader (Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		super(context ,uri ,projection,selection,selectionArgs,sortOrder);
		mContext = context;
		
	}
	


	 @Override
	 public Cursor loadInBackground() {
		 final Cursor originalCursor = super.loadInBackground();
		 return originalCursor;
	 }

}
