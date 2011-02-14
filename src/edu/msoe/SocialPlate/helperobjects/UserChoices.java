package edu.msoe.SocialPlate.helperobjects;

import edu.msoe.SocialPlate.database.DBAdapter;

/**
 * Singleton object for storing the user's choices from the home screen of the app.
 */
public class UserChoices {

	private int directions; //0:Walking, 1:Driving, 2:Bus
	private String ethnicity;
	private String cost;		
	private String meal;	//should i change this to int type? (only 4 choices)
	private String name;
	private double locationSearch;
	
	
	private static UserChoices uc= null;
	
	private UserChoices(){
		this.directions = -1;
		this.ethnicity = null;
		this.cost = null;
		this.meal = null;
		this.name = null;
		locationSearch = DBAdapter.DISABLE_LOCATION_SEARCH;
	}
	
	public static UserChoices getInstance(){
		if(uc == null){
			synchronized(UserChoices.class){
				if(uc == null){
					uc = new UserChoices();
				}
			}
		}
		return uc;
	}
	
	public String getName(){
		return name;
	}
	
	public int getDirections(){
		return directions;
	}
	
	public String getEthnicity(){
		return ethnicity;
	}
	
	public String getCost(){
		return cost;
	}
	
	public String getMeal(){
		return meal;
	}
	
	public double getLocationSearch(){
		return locationSearch;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDirections(int d){
		this.directions = d;
	}
	
	public void setEthnicity(String e){
		this.ethnicity = e;
	}
	
	public void setCost(String c){
		this.cost = c;
	}
	
	public void setMeal(String m){
		this.meal = m;
	}
	
	public void setLocationsSearch(double ls){
		this.locationSearch = ls;
	}
	
	public String toString(){
		return directions + " " + ethnicity + " " + cost + " " + meal;
	}
}
