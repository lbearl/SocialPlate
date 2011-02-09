package edu.msoe.SocialPlate.tasks;

import edu.msoe.SocialPlate.RetrieverOfLocations;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * This class is responsible for polling the static object RetrieverOfLocations
 * to determine if the location has been found.
 * @author michael
 *
 */
public class GetLocationTask extends AsyncTask<Void, Void, Boolean>{
	
	Context context;
	RetrieverOfLocations rol;
	ProgressDialog pd;
	
	/**
	 * Default constructor
	 * @param context
	 * @param rol
	 */
	public GetLocationTask(Context context, RetrieverOfLocations rol){
		this.context = context;
		this.rol = rol;
	}
	
	/**
	 * Display dialog to user letting them know location is being checked
	 */
	@Override
	protected void onPreExecute(){
		pd = ProgressDialog.show(context, "Location", "Checking Location Availability", true);
		pd.show();
	}
	
	/**
	 * Do task in background, check to see if the location has been retrieved 
	 */
	@Override
	protected Boolean doInBackground(Void... params	){
		boolean foundGPS = true;
		long time = System.currentTimeMillis();
		while(rol.waitingForLocationChange && foundGPS){
			if(System.currentTimeMillis()-time >= 5000){
				foundGPS = false;
			}
		}				
		return foundGPS; 
	}
	
	/**
	 * Display a message to the user detailing success of location checking
	 */
	@Override
	protected void onPostExecute(Boolean bool){
		pd.dismiss();
		
		if(isCancelled()){
			bool = false;
		}
		
		if(bool){
			Toast.makeText(context, "Location Recieved!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "Couldn't Find Location", Toast.LENGTH_SHORT).show();
		}
		
	}
	

}
