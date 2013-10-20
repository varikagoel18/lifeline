package com.delight.lifeline.UI;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.delight.lifeline.R;
import com.delight.lifeline.DataModel.LLContract;
import com.delight.lifeline.UI.LLLocationUpdater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LLNewEventActivity extends Activity  implements LLLocationUpdater.LLLocationUpdaterCallBack {
	
private ImageView mPicView;
private TextView mTimeView;
private TextView mLocationView;
private EditText mTextDetail;
private LLLocationUpdater mLocationUpdater;

private boolean mUpdate = false;
private int eventID = -1;

public static final String ACTION_UPDATE = "com.delight.lifeline.action_update";
public static final String EVENT_ADDRESS =  "event_address";
public static final String EVENT_DETAIL =   "event_detail";
public static final String EVENT_TYPE =      "event_type";
public static final String EVENT_TIME =      "event_time";
public static final String EVENT_ID =      "event_id";



private int currentID = LLContract.Events.TYPE_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newevent);
        
        
        
      Intent intent = getIntent();
      String address = "";
      String detail = "";
      int type = LLContract.Events.TYPE_DEFAULT;
      long time = new  Date().getTime();
      
      if(ACTION_UPDATE.equals(intent.getAction()))
      {
    	  address = intent.getStringExtra(EVENT_ADDRESS);
    	  detail = intent.getStringExtra(EVENT_DETAIL);
    	  type = intent.getIntExtra(EVENT_TYPE, type);
    	  time = intent.getLongExtra(EVENT_TIME, time);
    	  eventID = intent.getIntExtra(EVENT_ID, -1);
    	  mUpdate = true;
      }
        
      View topBar = LayoutInflater.from(this).inflate(R.layout.new_event_top_bar, null);
      getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
      getActionBar().setCustomView(topBar);
        
   	  mTimeView = (TextView) findViewById(R.id.new_event_time);
   	  mLocationView   = (TextView) findViewById(R.id.new_event_location);
      mTextDetail= (EditText) findViewById(R.id.new_event_detail);
   	  mPicView = (ImageView) findViewById(R.id.new_event_pic);
   	  
   	  
   	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm"); 
   	 
     String dtString = sdf.format(new  Date(time));
     
     mTimeView.setText(dtString);
     mLocationView.setText(address);
     mTextDetail.setText(detail);
     
     currentID = type;
     
	 mPicView.setImageResource(LLEventListAdapter.getImageIDUsingType(type));

      mLocationUpdater = new LLLocationUpdater(this, this);
      mLocationUpdater.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	 switch (item.getItemId())
    	 {
    	   case R.id.action_angry:
    	   {
    		   mPicView.setImageResource(R.drawable.angry);
    		   currentID = LLContract.Events.TYPE_ANGRY;
    		   return true;
    	   }
      	   case R.id.action_laugh:
    	   {
    		   mPicView.setImageResource(R.drawable.laugh);
    		   currentID = LLContract.Events.TYPE_SMILE;
    		   return true;

    	   }
      	   case R.id.action_romantic:
    	   {
    		   mPicView.setImageResource(R.drawable.heart);
    		   currentID = LLContract.Events.TYPE_HEART;
    		   return true;

    	   }
      	   case R.id.action_sad:
    	   {
    		   mPicView.setImageResource(R.drawable.cry);
    		   currentID = LLContract.Events.TYPE_SAD;
    		   return true;

    	   }
      	   case R.id.action_tired:
    	   {
    		   mPicView.setImageResource(R.drawable.smile);
    		   currentID = LLContract.Events.TYPE_TIRED;
    		   return true;

    	   }
    	   default:
    	   {
    		   return super.onOptionsItemSelected(item);
    	   } 
    	 }
    	
    }
    
    public void onSave(View view)
    {
    	mLocationUpdater.stop();
    	
    	if(mUpdate)
    	{
    		updateEvent();
    	}
    	else
    	{
    		addNewEvent();
    	}	
    	finish();
    }
    
    private void updateEvent()
    {

    	Uri eventUri = ContentUris.withAppendedId(LLContract.Events.CONTENT_URI, eventID);   
    	ContentValues values = new ContentValues();
      	values.put(LLContract.Events.EVENT_TYPE, currentID);
    	values.put(LLContract.Events.EVENT_TIME, new  Date().getTime());
    	values.put(LLContract.Events.EVENT_DETAIL, mTextDetail.getText().toString());
    	values.put(LLContract.Events.EVENT_ADDRESS, mLocationView.getText().toString());	
    	getContentResolver().update(eventUri, values, null, null);
    }
    
    private void addNewEvent()
    {
    	Uri url= LLContract.Events.CONTENT_URI; 
    	ContentValues values = new ContentValues();
   	
    	Date dt = new  Date();
    	dt.getTime(); 
    	values.put(LLContract.Events.EVENT_TYPE, currentID);
    	values.put(LLContract.Events.EVENT_TIME, new  Date().getTime());
    	values.put(LLContract.Events.EVENT_DETAIL, mTextDetail.getText().toString());
    	values.put(LLContract.Events.EVENT_ADDRESS, mLocationView.getText().toString());	
    	getContentResolver().insert(url, values);
    }
    
    public void onCancel(View view)
    {
    	mLocationUpdater.stop();
    	finish();
    }


	@Override
	public void updateLocationAddress(String address) {
		mLocationView.setText(address);
	}
    

}
