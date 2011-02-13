package edu.msoe.SocialPlate.helperobjects;

/**
 * Singleton object for storing the user's choices from the home screen of the app.
 */
public class UserChoices {

	private int directions; //0:Walking, 1:Driving, 2:Bus
	private String ethnicity;
	private String cost;		
	private String meal;	//should i change this to int type? (only 4 choices)
	
	private static UserChoices uc= null;
	
	private UserChoices(){
		this.directions = -1;
		this.ethnicity = null;
		this.cost = null;
		this.meal = null;
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
	
	public String toString(){
		return directions + " " + ethnicity + " " + cost + " " + meal;
	}
}
