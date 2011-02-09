package edu.msoe.SocialPlate;

import android.content.Context;
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
				0, 0, getInstance(context));
	}
	
	/**
	 * Unregister this listener from receiving updates when a location is received
	 */
	public void unregister(){
		locationManager.removeUpdates(this);
	}
	
	/**
	 * When location is received save it to the instance attributes
	 */
	public void onLocationChanged(Location location) {
		Toast.makeText(context, "Location Found", Toast.LENGTH_SHORT).show();
		if(location != null){
			waitingForLocationChange = false;
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			accuracy = location.getAccuracy();
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
