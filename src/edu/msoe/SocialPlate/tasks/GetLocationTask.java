package edu.msoe.SocialPlate.tasks;

import edu.msoe.SocialPlate.RetrieverOfLocations;
import android.content.Context;
import android.os.AsyncTask;

public class GetLocationTask extends AsyncTask<Void, Void, Boolean>{
	
	Context context;
	RetrieverOfLocations rol;
	
	
	public GetLocationTask(Context context, RetrieverOfLocations rol){
		this.context = context;
		this.rol = rol;
	}
	
	
	protected void onPreExecute(){
	
		
	}
	
	protected Boolean doInBackground(Void... params	){
		
		
		
		
		return false; 
	}
	
	protected void onPostExecute(){
					
	}
	

}
