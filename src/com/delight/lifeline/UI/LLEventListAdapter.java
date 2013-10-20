package com.delight.lifeline.UI;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.delight.lifeline.R;
import com.delight.lifeline.DataModel.LLContract;
import com.delight.lifeline.DataModel.Loaders.LLCustomCursorLoader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LLEventListAdapter extends CursorAdapter {
	
	
    private class ViewHolder {
        ImageView eventType;
        TextView eventTitle;
        TextView eventDetail;
        
    }

	public LLEventListAdapter(Context context, Cursor c) {
		super(context,c ,0);
	}
	

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		 final ViewHolder holder = (ViewHolder) view.getTag();
		 
         final int eventType = cursor.getInt(cursor.getColumnIndex(LLContract.Events.EVENT_TYPE));
         final String eventDetail = cursor.getString(cursor.getColumnIndex(LLContract.Events.EVENT_DETAIL));
         final String eventAddress = cursor.getString(cursor.getColumnIndex(LLContract.Events.EVENT_ADDRESS));
         final long eventTime = cursor.getLong(cursor.getColumnIndex(LLContract.Events.EVENT_TIME));
         
         holder.eventTitle.setText(eventDetail);
         holder.eventType.setImageResource(getImageIDUsingType(eventType));  
         
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm");         
         String dtString = sdf.format(new  Date(eventTime));
         
         String detail ="On "+dtString+" at "+eventAddress;
         holder.eventDetail.setText(detail);
         

	}

	@Override
	 public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
						
		 LayoutInflater inflater = LayoutInflater.from(context);
         View layoutView = inflater.inflate(R.layout.eventlist_item, null);
         
         final ViewHolder holder = new ViewHolder();
         holder.eventType =
                 (ImageView) layoutView.findViewById(R.id.event_type);
         holder.eventDetail =
                 (TextView) layoutView.findViewById(R.id.event_detail);
         holder.eventTitle =
                 (TextView) layoutView.findViewById(R.id.event_title);
         
         layoutView.setTag(holder);
 
         return layoutView;
	}
	
	
	public static LLCustomCursorLoader createLoader(Context context)
	{
		   return new LLCustomCursorLoader(context ,LLContract.Events.CONTENT_URI ,null, null,null,null);
	}
	
	public  static int getImageIDUsingType(int eventType)
	{
		switch(eventType)
		{
		case LLContract.Events.TYPE_ANGRY:
			return R.drawable.angry;
		case LLContract.Events.TYPE_SMILE:
			return R.drawable.laugh;
		case LLContract.Events.TYPE_SAD:
			return R.drawable.cry;
		case LLContract.Events.TYPE_HEART:
			return R.drawable.heart;
		case LLContract.Events.TYPE_TIRED:
			return R.drawable.tired;
		case LLContract.Events.TYPE_DEFAULT:
		  return R.drawable.face;
		
		}
		return R.drawable.face;
	}

}
