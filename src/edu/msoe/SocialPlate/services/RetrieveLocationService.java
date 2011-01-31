package edu.msoe.SocialPlate.services;

import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.tasks.GetLocationTask;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RetrieveLocationService extends Service{

	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onStart(Intent itent, int id){
		RetrieverOfLocations.getInstance().register();
		GetLocationTask glt = new GetLocationTask(getApplicationContext(), RetrieverOfLocations.getInstance());
		glt.execute();
		
	}
	
	
}
