package edu.msoe.SocialPlate.activities;

import java.util.ArrayList;
import java.util.List;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ResultScreen extends Activity{

	private ListView lv;
	/**
	 * This class will contain the list view that displays the results
	 * from the Google Places API Search Request
	 * 
	 * It will make a request using the GooglePlaces class. And it'll receive 
	 * the response from the class properly formatted to be displayed. 
	 * 
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultscreen);
		
		Bundle bundle = getIntent().getBundleExtra("extra");
		
		Restaurant[] rest = (Restaurant[])bundle.getParcelableArray("rest");
		
        lv = (ListView)findViewById(R.id.mainlist);
        lv.setTextFilterEnabled(true);         
        lv.setAdapter(new ArrayAdapter<Restaurant>(this, R.layout.list_item, rest));
        
        
	}

	public Restaurant[] getTaggedLocations(){
			
		DBAdapter dba = new DBAdapter(getApplicationContext());
		
		ArrayList<String> rName = new ArrayList<String>();		
		ArrayList<String> rPrice = new ArrayList<String>();
		ArrayList<String> rType = new ArrayList<String>();
		ArrayList<String> nName = new ArrayList<String>();
		nName.add("BK");
		ArrayList<String> nPrice = new ArrayList<String>();
		ArrayList<String> nType = new ArrayList<String>();
		
		Restaurant[] restaurants = dba.queryRestaurants(rName, rPrice, rType,
				nName, nPrice, nType,
				DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH,
				DBAdapter.DISABLE_LOCATION_SEARCH);
		dba.closeDB();		
						
		return restaurants;
	}
	
}
