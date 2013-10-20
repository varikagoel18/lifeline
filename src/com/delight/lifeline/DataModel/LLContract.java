package com.delight.lifeline.DataModel;



import android.net.Uri;
import android.provider.BaseColumns;

public  final class LLContract {
	
	public static String AUTHORITY = "com.delight.lifeline.LLProvider";
	
	// This class cannot be instantiated
    private LLContract() {}



    public interface Tables {
        public static final String EVENTS = "events";
    }
    
    
    protected interface EventsColumns extends BaseColumns {

     
        public static final String EVENT_TYPE = "event_type";
        public static final String EVENT_DETAIL = "event_detail";
        public static final String EVENT_TIME = "event_time";
        public static final String EVENT_ADDRESS = "event_address";
        public static final String EVENT_TAG = "event_tag";


    }
     
    protected interface EventType
    {
    	public static final int TYPE_SMILE = 1;
    	public static final int TYPE_HEART = 2;
    	public static final int TYPE_SAD = 3;
    	public static final int TYPE_ANGRY = 4;
    	public static final int TYPE_TIRED = 5;
    	public static final int TYPE_DEFAULT = 6;


    }

    public static final class Events implements EventsColumns ,EventType {

        public static final String TABLE_NAME = "events";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
        
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
        
        
        
        /**
         * This utility class cannot be instantiated
         */
        private Events() {
        }
    }

}
