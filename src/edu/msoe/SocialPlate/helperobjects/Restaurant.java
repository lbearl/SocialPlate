package edu.msoe.SocialPlate.helperobjects;

import java.text.DecimalFormat;

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
		this.distance = -1;
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

	public String toString(){		
		DecimalFormat dist = new DecimalFormat("###.###");		
		String ret = "";		
		if(distance==-1){
			ret = name+" "+description+" "+priceRange+" "+foodType+" "+ethnicity; 
		}else{
			ret = name+" "+description+" "+priceRange+" "+foodType+" "+ethnicity+" Distance "+ dist.format(distance) + " km";
		}		
		return ret;  
	}

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

}
