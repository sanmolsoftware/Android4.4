package com.ulta.core.util.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class LocationFinder {
	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	private Handler locationHandler;
	private GetLastLocation2 getLastLocation2;

	public boolean getLocation(Context context, LocationResult result)     { 
		//I use LocationResult callback class to pass location value from MyLocation to user code.   
		locationResult=result;         
		if(lm==null)             
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		//exceptions will be thrown if provider is not permitted. 
		try{
			gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch(Exception ex){

		}
		try{
			network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}catch(Exception ex){

		}         
		//don't start listeners if no provider is enabled  
		if(!gps_enabled && !network_enabled)  
			return false;    
		if(gps_enabled)    
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps); 
		if(network_enabled)  
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		/*timer1=new Timer(); 
		timer1.schedule(new GetLastLocation(), 20000);   //20 seconds  

		 */		
		 getLastLocation2=new  GetLastLocation2(locationHandler);
		 locationHandler.postDelayed(getLastLocation2, 30000);
		// locationHandler.postDelayed(new GetLastLocation2(locationHandler), 30000);

		 return true;     
	} 



	LocationListener locationListenerGps = new LocationListener() { 
		public void onLocationChanged(Location location) { 
			//timer1.cancel(); 
			locationResult.gotLocation(location); 
			lm.removeUpdates(this); 
			lm.removeUpdates(locationListenerNetwork); 
			locationHandler.removeCallbacks(getLastLocation2);
		} 
		public void onProviderDisabled(String provider) {} 
		public void onProviderEnabled(String provider) {} 
		public void onStatusChanged(String provider, int status, Bundle extras) {} 
	}; 



	LocationListener locationListenerNetwork = new LocationListener() { 
		public void onLocationChanged(Location location) { 
			//timer1.cancel(); 
			locationResult.gotLocation(location); 
			lm.removeUpdates(this); 
			lm.removeUpdates(locationListenerGps); 
			locationHandler.removeCallbacks(getLastLocation2);
		} 
		public void onProviderDisabled(String provider) {} 
		public void onProviderEnabled(String provider) {} 
		public void onStatusChanged(String provider, int status, Bundle extras) {} 
	}; 



	class GetLastLocation extends TimerTask { 
		@Override 
		public void run() { 
			lm.removeUpdates(locationListenerGps); 
			lm.removeUpdates(locationListenerNetwork); 

			Location net_loc=null, gps_loc=null; 
			if(gps_enabled) 
				gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			if(network_enabled) 
				net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 

			//if there are both values use the latest one 
			if(gps_loc!=null && net_loc!=null){ 
				if(gps_loc.getTime()>net_loc.getTime()) 
					locationResult.gotLocation(gps_loc); 
				else 
					locationResult.gotLocation(net_loc); 
				//locationHandler.sendEmptyMessage(0);
				return; 
			} 

			if(gps_loc!=null){ 
				locationResult.gotLocation(gps_loc); 
				//locationHandler.sendEmptyMessage(0);
				return; 
			} 
			if(net_loc!=null){ 
				locationResult.gotLocation(net_loc); 
				//locationHandler.sendEmptyMessage(0);
				return; 
			} 
			locationResult.gotLocation(null); 
			locationHandler.sendEmptyMessage(1);
		} 
	} 


	class GetLastLocation2 implements Runnable {
		private Handler handler;

		public GetLastLocation2(Handler handler) {
			super();
			this.handler = handler;
		}

		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps); 
			lm.removeUpdates(locationListenerNetwork); 

			Location net_loc=null, gps_loc=null; 
			if(gps_enabled) 
				gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			if(network_enabled) 
				net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 

			//if there are both values use the latest one 
			if(gps_loc!=null && net_loc!=null){ 
				if(gps_loc.getTime()>net_loc.getTime()) 
					locationResult.gotLocation(gps_loc); 
				else 
					locationResult.gotLocation(net_loc); 
				locationHandler.sendEmptyMessage(0);
				return; 
			} 

			if(gps_loc!=null){ 
				locationResult.gotLocation(gps_loc); 
				locationHandler.sendEmptyMessage(0);
				return; 
			} 
			if(net_loc!=null){ 
				locationResult.gotLocation(net_loc); 
				locationHandler.sendEmptyMessage(0);
				return; 
			} 
			locationResult.gotLocation(null);
			handler.sendEmptyMessage(1);

		}

	}


	public Handler getLocationHandler() {
		return locationHandler;
	}

	public void setLocationHandler(Handler locationHandler) {
		this.locationHandler = locationHandler;
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
}
