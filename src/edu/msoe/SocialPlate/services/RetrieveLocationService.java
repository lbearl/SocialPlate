package edu.msoe.SocialPlate.services;

import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.tasks.GetLocationTask;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

/**
 * This service registers the RetrieverOfLocations static object to start on a different thread
 * @author michael
 *
 */
public class RetrieveLocationService extends Service{


	
	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	public void onStart(Intent itent, int id){
		Toast.makeText(getApplicationContext(), "Starting Service", Toast.LENGTH_SHORT).show();
		
//		Thread gps = new Thread(){
//			
//			
//			public void run(){
//				Looper.prepare();
//				Looper.loop();
//				RetrieverOfLocations.getInstance(getApplicationContext()).register();				
//			} //end run method
//			
//		};	
		RetrieverOfLocations.getInstance(getApplicationContext()).register();
//		gps.start();
	}


	
	
}
