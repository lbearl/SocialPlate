package edu.msoe.SocialPlate.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;
import edu.msoe.SocialPlate.helperobjects.UserChoices;

public class WebQuery {
	
	
	
	
	/**
	 * search for restaurant with locaion
	 * @param context
	 * @return
	 */
	public static Restaurant[] queryWeb(Context context){		
		JSONObject jquery = new JSONObject();
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {			
			if(UserChoices.getInstance().getName()!=null){
				jquery.put("restaurant_name", UserChoices.getInstance().getName());
			}if(UserChoices.getInstance().getEthnicity()!=null){
				jquery.put("ethnicity", UserChoices.getInstance().getEthnicity());
			}if(UserChoices.getInstance().getCost()!=null){
				jquery.put("cost", UserChoices.getInstance().getCost());
			}if(UserChoices.getInstance().getMeal()!=null){
				jquery.put("meal", UserChoices.getInstance().getCost());
			}if(UserChoices.getInstance().getLocationSearch()!=DBAdapter.DISABLE_LOCATION_SEARCH &&
				RetrieverOfLocations.getInstance(context).latitude != DBAdapter.DISABLE_LOCATION_SEARCH &&
				RetrieverOfLocations.getInstance(context).longitude != DBAdapter.DISABLE_LOCATION_SEARCH){
				
				jquery.put("distance_search", UserChoices.getInstance().getLocationSearch());
				jquery.put("latitude", RetrieverOfLocations.getInstance(context).latitude);
				jquery.put("longitude", RetrieverOfLocations.getInstance(context).longitude);
			}				
			
			JSONArray array = ServerConnect.getInstance().sendToServer(context, jquery);
			JSONObject j = null;
			for(int i = 0; i < array.length(); i++){
				j = array.getJSONObject(i);
				restaurants.add(new Restaurant(-1, j.getString("restaurant_name"), j.getDouble("latitude"),
						j.getDouble("longitude"), j.getString("description"), j.getString("cost"), j.getString("meal"),
						j.getString("ethnicity")));
			}			
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return restaurants.toArray(new Restaurant[restaurants.size()]);
	}
	
	/**
	 * Insert into the database online
	 * @param context
	 * @param restaurant
	 */
	public static void insert(Context context, Restaurant restaurant){
		
		JSONObject j = restaurant.toJSON();
		
		try {
			ServerConnect.getInstance().sendToServer(context, j);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
