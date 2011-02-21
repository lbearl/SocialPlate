package edu.msoe.SocialPlate.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;
import edu.msoe.SocialPlate.http.ServerConnect;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ResultScreen extends Activity{

	private ListView lv;
	private double[] restaurantLat;
	private double[] restaurantLon; 
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
		
		Bundle bundle = getIntent().getExtras();
		
		Parcelable[] rest = bundle.getParcelableArray("Restaurants");		
		restaurantLat = bundle.getDoubleArray("latitudes");
		restaurantLon = bundle.getDoubleArray("longitudes");
		
        lv = (ListView)findViewById(R.id.mainlist);
        lv.setTextFilterEnabled(true);         
        lv.setAdapter(new ArrayAdapter<Parcelable>(this, R.layout.list_item, rest));
        
      //Registers the Listview to have a context menu
        registerForContextMenu(lv);
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
  	    		intent.setClassName(getResources().getString(R.string.package_structure),
  	    				getResources().getString(R.string.text_screen_fqn));
  	    		intent.putExtra("Restaurant", ((Restaurant) lv.getItemAtPosition(position)).getName());
				startActivity(intent);
				
			}
        	
        	
        });
	}

	public Restaurant[] getTaggedLocations() throws JSONException{
			
		DBAdapter dba = new DBAdapter(getApplicationContext());
		
		ArrayList<String> rName = new ArrayList<String>();		
		ArrayList<String> rPrice = new ArrayList<String>();
		ArrayList<String> rType = new ArrayList<String>();
		ArrayList<String> rEthnicity = new ArrayList<String>();
		ArrayList<String> nName = new ArrayList<String>();
		nName.add("BK");
		ArrayList<String> nPrice = new ArrayList<String>();
		ArrayList<String> nType = new ArrayList<String>();
		ArrayList<String> nEthnicity = new ArrayList<String>();
		ServerConnect sc = ServerConnect.getInstance();
		String rests = sc.getRestaurants();
		JSONArray jar = new JSONArray(rests);
		Restaurant[] myrests = new Restaurant[jar.length()+5];
		for(int i =0; i< jar.length(); i++){
			myrests[i].setDescription(jar.getJSONObject(i).getString("description"));
			myrests[i].setEthnicity(jar.getJSONObject(i).getString("ethnicity"));
			myrests[i].setFoodType(jar.getJSONObject(i).getString("f_type"));
			myrests[i].setLatitude(jar.getJSONObject(i).getInt("latitude"));
			myrests[i].setLongitude(jar.getJSONObject(i).getInt("longitude"));
			myrests[i].setName(jar.getJSONObject(i).getString("r_name"));
			myrests[i].setPriceRange(jar.getJSONObject(i).getString("price_name"));
			
		}
		
		Restaurant[] restaurants = dba.queryRestaurants(rName, rPrice, rType, rEthnicity,
				nName, nPrice, nType, nEthnicity,
				DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH
				);
		dba.closeDB();		
						
	//	restaurantArray = restaurants;
		return myrests;
	}
	
	 
	    /**
	     * Called when the context menu is first called
	     */
	    public void onCreateContextMenu(ContextMenu menu, View v,
	                                    ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.layout.contextmenu, menu);
	    }
	
	    /**
	     * Called when an item in the context menu is selected. 
	     */
	    public boolean onContextItemSelected(MenuItem item) {
	    	//Switch statement determines which item on the context menu was selected
	      switch (item.getItemId()) {
	      case R.id.Map_Context_Menu:    	  
	    	  
	    	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    	  Uri uri = Uri.parse("geo:"+(restaurantLat[(int)info.id]/1E6)+","+(restaurantLon[(int)info.id])/1E6); 
	    	  //debug statements aren't compiled!!!!!!!!!!
	    	  Log.i("SocialPlate", "Launching Google Maps with Uri: ("+uri+")"); 
	    	  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    	  startActivity(intent);
	    	  
	        return true;
	      default:
	        return super.onContextItemSelected(item);
	      }
	    }
}
