package edu.msoe.SocialPlate;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * This class is a location listener and when it is updated with the location,
 * it saves them to the global attributes
 * @author michael
 */
public class RetrieverOfLocations implements LocationListener{

	public boolean waitingForLocationChange;
	public double latitude;
	public double longitude;
	public double bearing;
	public double accuracy;
	
	private LocationManager locationManager;
	private static Context context;
	
	/**
	 * Private constructor
	 * @param context
	 */
	private RetrieverOfLocations(){
		waitingForLocationChange = true;
		latitude = 0.0;
		longitude = 0.0;
		bearing = 0.0;		
		accuracy = 0.0;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}	
	
	/**
	 * Private inner class that holds a static RetrieverOfLocations
	 * @author michael
	 */
	public static class LazyHolder{
		public static RetrieverOfLocations rol = new RetrieverOfLocations();
	}
	
	/**
	 * Singleton Pattern Class
	 * @return
	 */
	public static RetrieverOfLocations getInstance(Context context){
		if(RetrieverOfLocations.context==null){
			RetrieverOfLocations.context = context;
		}
		return LazyHolder.rol;
	}
	
	/**
	 * Register this listener to receive updates when a location is received
	 */
	public void register(){
		waitingForLocationChange = true;		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				120000, Float.MAX_VALUE, getInstance(context));
	}
	
	/**
	 * Unregister this listener from receiving updates when a location is received
	 */
	public void unregister(){
		locationManager.removeUpdates(getInstance(context));
	}
	
	/**
	 * When location is received save it to the instance attributes
	 */
	public void onLocationChanged(Location location) {
		Toast.makeText(context, "Location Found", Toast.LENGTH_LONG).show();
		if(location != null){			
			waitingForLocationChange = false;
			latitude = location.getLatitude()*1E6;
			longitude = location.getLongitude()*1E6;
			accuracy = location.getAccuracy();
			Toast.makeText(context, "Lat:" + latitude + " Lng:" + longitude, Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "Location Found with accuracy " + accuracy, Toast.LENGTH_LONG).show();
			Intent service = new Intent();
			service.setClassName(context.getResources().getString(R.string.package_structure), 
								 context.getResources().getString(R.string.location_service_fqn));
		//	context.stopService(service);
				
		}
		
	}

	/**
	 * Not Used
	 */
	public void onProviderDisabled(String arg0) {
		
	}

	/**
	 * Not Used
	 */
	public void onProviderEnabled(String arg0) {
		
	}

	/**
	 * Not Used
	 */
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
}
