package com.delight.lifeline;


import com.delight.lifeline.DataModel.LLContract;
import com.delight.lifeline.UI.LLEventListAdapter;
import com.delight.lifeline.UI.LLEventListFragment;
import com.delight.lifeline.UI.LLNewEventActivity;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

	private LLEventListFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
   	    mFragment = (LLEventListFragment) getFragmentManager().findFragmentById( R.id.list);
   	    
   	    Log.d("TAG", "on create main activity");
   	    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onAddEvent(View view)
    {
    	startActivity(new Intent(this ,LLNewEventActivity.class));
    	
    }
    
    
    
}
