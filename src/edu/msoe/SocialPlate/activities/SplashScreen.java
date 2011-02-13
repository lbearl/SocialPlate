package edu.msoe.SocialPlate.activities;

import edu.msoe.SocialPlate.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity{
	
	
	private int splashTime = 5000;
	private boolean active = true;
	
	/**
	 * Start the splash to display an image / pause for the user
	 * Start the service that requests location updates.
	 * After the time period or the user touches the screen,
	 * finish the activity to make sure it can't be backed up to. 
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		Intent service = new Intent();
		service.setClassName(getResources().getString(R.string.package_structure), 
							 getResources().getString(R.string.location_service_fqn));
		
		getApplicationContext().startService(service);
		
		
		Thread splash = new Thread(){
			
			public void run(){
				
				try{
					int waited = 0;
					while(active && (waited < splashTime)){
						sleep(100);						
						waited += 100;						
					}
				}catch(InterruptedException e){
					//do nothing
				}finally{
					finish();
					Intent homeScreen = new Intent();
					homeScreen.setClassName(getResources().getString(R.string.package_structure),
							getResources().getString(R.string.home_screen_fqn));
					startActivity(homeScreen);
				}			
			}//end run method		
		}; //end thread declaration
		
		splash.start();		
	}

	
	/**
	 * When the screen is touched in this activity, the wait is canceled
	 * and the main activity loaded
	 * Not enabled in the by default, perhaps after this lab 
	 */
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			active = false;			
		}
		return true;
	}
}
