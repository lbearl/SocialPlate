package edu.msoe.SocialPlate.helperobjects;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import edu.msoe.SocialPlate.database.DBAdapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable{
	
	private long id;
	private String name;
	private double latitude;
	private double longitude;
	private double distance;
	private String description;
	private String priceRange;
	private String foodType;
	private String ethnicity;
	
	private static final int none = -1000;
	
	/**
	 * Default Constructor
	 * @param id
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @param description
	 * @param priceRange
	 * @param foodType
	 * @param ethnicity
	 */
	public Restaurant(long id, String name, double latitude, double longitude, String description,
			String priceRange, String foodType, String ethnicity){
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.priceRange = priceRange;
		this.foodType = foodType;		
		this.ethnicity = ethnicity;
		this.distance = none;
	}

	
	/**
	 * Parcelable constructor
	 * @param in
	 */
	public Restaurant(Parcel in){
		readFromParcel(in);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	

	/**
	 * @return the priceRange
	 */
	public String getPriceRange() {
		return priceRange;
	}

	/**
	 * @return the foodType
	 */
	public String getFoodType() {
		return foodType;
	}
	
	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * @return the ethnicity
	 */
	public String getEthnicity(){
		return ethnicity;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Overridden toString method
	 */
	public String toString(){		
		DecimalFormat dist = new DecimalFormat("###.###");		
		String ret = "";		
		if(distance==none){
			ret = name+" \""+description+"\" "+priceRange+" "+foodType+" "+ethnicity; 
		}else{
			ret = name+" \""+description+"\" "+priceRange+" "+foodType+" "+ethnicity+" Distance "+ dist.format(distance) + " km";
		}		
		return ret;  
	}

	/**
	 * Involved in making this object class have the ability to be stuffed in a bundle
	 */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }
 
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
	
	
	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeDouble(distance);
		dest.writeString(description);
		dest.writeString(priceRange);
		dest.writeString(foodType);
		dest.writeString(ethnicity);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readLong();
		name = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		distance = in.readDouble();
		description = in.readString();
		priceRange = in.readString();
		foodType = in.readString();	
		ethnicity = in.readString();
	}
	
	public JSONObject toJSON(){
		JSONObject j = new JSONObject();
		try {
			j.put("restaurant_name", name);
			j.put("latitude", latitude);
			j.put("longitude", longitude);
			j.put("description", description);
			j.put("cost", priceRange);
			j.put("meal", foodType);
			j.put("ethnicity", ethnicity);			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return j;		
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}


	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}


	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	
}
