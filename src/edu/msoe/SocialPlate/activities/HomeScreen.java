package edu.msoe.SocialPlate.activities;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.tasks.GetLocationTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * @author michael
 */
public class HomeScreen extends Activity{

	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);		
		
	}
	
	public void onClick(View view){
		
		int id = view.getId();
		if(id==R.id.getLocation){		
			GetLocationTask glt = new GetLocationTask(HomeScreen.this, RetrieverOfLocations.getInstance(getApplicationContext()));
			glt.execute();
		}else if(id == R.id.geoTagB){
			Intent geoTag = new Intent();
			geoTag.setClassName(getResources().getString(R.string.package_structure),
					getResources().getString(R.string.geo_tag_screen_fqn));
			this.startActivity(geoTag);
		}else if(id == R.id.searchB){
			Intent search = new Intent();
			search.setClassName(getResources().getString(R.string.package_structure),
					getResources().getString(R.string.search_screen_fqn));
			this.startActivity(search);
		}
	}
	
	
}
