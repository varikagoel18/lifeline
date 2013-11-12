package com.delight.lifeline.UI;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;




public class LLLocationUpdater implements LocationListener {
    
	private static final String TAG = "LLLocationWatcher";
	private Context mContext;
	
	private GetAddressTask currentAddresstask = null;
	LocationManager mLocationManager;
	
	
	public static interface LLLocationUpdaterCallBack
	{
		void updateLocationAddress(String address);
	}
	
	private LLLocationUpdaterCallBack mCallback;
	
	public LLLocationUpdater(Context context ,LLLocationUpdaterCallBack callBack)
	{
		mContext = context;
		mCallback = callBack;
	    mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

	}
	
	
	
	@Override
	public void onLocationChanged(Location location) {
		
	    Log.d(TAG, "onLocationChanged longi = "+location.getLongitude() +"latitude = "+location.getLatitude());
	   
		mLocationManager.removeUpdates(this);
		currentAddresstask.cancel(true);
		currentAddresstask = (new GetAddressTask(mContext));
		currentAddresstask.execute(getLastLocation());
	    
	}

  private Location getLastLocation()
  {
	 Location loc1 = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	 Location loc2 = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	 
	 if(loc1 == null)
	 {
		 return loc2;
	 }
	 else if(loc2 == null)
	 {
		 return loc1;
	 }
	 long timeDelta = loc2.getTime() - loc1.getTime();
	 boolean isSignificantlyNewer = timeDelta > (2*60*1000);
	 
	 if(isSignificantlyNewer)
		 return loc2;
	 else
		 return loc1;
	 
  }
  
  public void stop()
  {
		mLocationManager.removeUpdates(this);
		if(currentAddresstask != null)
		{
		  currentAddresstask.cancel(true);
		}
  }
		
	public void start()
	{
		Location lastLocation = getLastLocation();
		if(lastLocation != null)
		{
			currentAddresstask = (new GetAddressTask(mContext));
			currentAddresstask.execute(getLastLocation());
		}
		
		try
		{
			if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			}
			else
			{
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			}
		}
		catch(Exception ex)
		{

		}
	}
	
	
	protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        Context localContext;

        public GetAddressTask(Context context) {
            super();
            localContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {

            Location location = params[0];
            String addressText = "";  
    		if(Geocoder.isPresent())
    		{
    			Log.d(TAG, "geocoder present");
    			Geocoder coder = new Geocoder(mContext);
    			try {
    				List<Address> addrList = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    				if(addrList != null && !addrList.isEmpty())
    				{
    					Address address = addrList.get(0);
    					
    					 addressText = addressText + ((address.getMaxAddressLineIndex() > 0) ? address.getAddressLine(0):"") +
    	                            address.getLocality() ;
    				}

    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		return addressText;
    		
        }

        @Override
        protected void onPostExecute(String address) {
        	mCallback.updateLocationAddress(address);

        }
    }


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
