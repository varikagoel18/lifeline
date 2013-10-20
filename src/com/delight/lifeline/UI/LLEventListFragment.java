package com.delight.lifeline.UI;

import com.delight.lifeline.R;
import com.delight.lifeline.DataModel.LLContract;
import com.delight.lifeline.DataModel.Loaders.*;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class LLEventListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private LLEventListAdapter mAdapter;
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      setHasOptionsMenu(true);
      mAdapter = new LLEventListAdapter(getActivity() ,null);
      
      registerForContextMenu(getListView());
      
      setEmptyText("Hey its pretty lonely here ,hit the red button to track how are you feeling ");
      setListAdapter(mAdapter);
      setListShown(true);
     
      
      getLoaderManager().initLoader(LLCustomCursorLoader.LOADER_ID, null, this);
    }
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      //inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     
    	return true;
    }
    

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.menu_delete:
        {
        	onDeleteItem(info.position);
            return true;
        }
        case R.id.menu_edit:
        {
        	onEditItem(info.position);
        	return true;
        }
        default:
            return super.onContextItemSelected(item);
        }
    }
    
    private void onDeleteItem(long id)
    {
    	Cursor c = (Cursor)getListView().getItemAtPosition((int)id);	
    	int eventID = c.getInt(c.getColumnIndex(LLContract.Events._ID));
    	Uri eventUri = ContentUris.withAppendedId(LLContract.Events.CONTENT_URI, eventID);    	
    	getActivity().getContentResolver().delete(eventUri, null, null);
    }
    
    private void onEditItem(long id)
    {
     	Cursor cursor = (Cursor)getListView().getItemAtPosition((int)id);	
     	final int eventType = cursor.getInt(cursor.getColumnIndex(LLContract.Events.EVENT_TYPE));
        final String eventDetail = cursor.getString(cursor.getColumnIndex(LLContract.Events.EVENT_DETAIL));
        final String eventAddress = cursor.getString(cursor.getColumnIndex(LLContract.Events.EVENT_ADDRESS));
        final long eventTime = cursor.getLong(cursor.getColumnIndex(LLContract.Events.EVENT_TIME));
        
    	int eventID = cursor.getInt(cursor.getColumnIndex(LLContract.Events._ID));

        Intent intent = new Intent(getActivity() ,LLNewEventActivity.class);
        
        intent.setAction(LLNewEventActivity.ACTION_UPDATE);
        intent.putExtra(LLNewEventActivity.EVENT_ADDRESS, eventAddress);
        intent.putExtra(LLNewEventActivity.EVENT_DETAIL, eventDetail);
        intent.putExtra(LLNewEventActivity.EVENT_TYPE, eventType);
        intent.putExtra(LLNewEventActivity.EVENT_TIME, eventTime);
        intent.putExtra(LLNewEventActivity.EVENT_ID, eventID);
        
        getActivity().startActivity(intent);
        
    }
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		LLCustomCursorLoader loader  = LLEventListAdapter.createLoader(getActivity());
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		   mAdapter.swapCursor(data);
		   setListShown(true);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {		
		mAdapter.swapCursor(null);
	}

}
