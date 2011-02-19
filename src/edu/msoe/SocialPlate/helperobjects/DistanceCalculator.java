package edu.msoe.SocialPlate.helperobjects;

public class DistanceCalculator {

	public static final double EARTH_RADIUS = 6371.1; //Radius of earth in km
	public static final double EPSILON = 0.00001;
	
	private DistanceCalculator(){
		
	}
	
	/**
	 * Converts an angle from degrees to radians
	 * 
	 * @param angle the angle to be converted in degrees
	 * @return the new angle in radians
	 */
	public static double deg2rad(double angle) {
		return angle * Math.PI / 180;
	}
	
	/**
	 * Calculates distance between two GPS coordinates
	 * From: http://www.movable-type.co.uk/scripts/latlong.html
	 * @param lat1 first latitude, in degrees
	 * @param lon1 first longitude, in degrees
	 * @param lat2 second latitude, in degrees
	 * @param lon2 second longitude, in degrees
	 * @return the distance between the two coordinates
	 * @throws DefinedException when illegal arguments are given
	 */
	public static double calcDistance(double lat1, double lon1,
			double lat2, double lon2){
		//Bound checking
		
		lat1 = lat1/1E6;
		lon1 = lon1/1E6;
		lat2 = lat2/1E6;
		lon2 = lon2/1E6;		
		
		if(lat1 > 90.0 || lat1 < -90.0){
			throw new IllegalArgumentException();			
		} else if(lon1 > 180.0 || lon1 < -180.0){
			throw new IllegalArgumentException();
		} if(lat2 > 90.0 || lat2 < -90.0){
			throw new IllegalArgumentException();			
		} else if(lon2 > 180.0 || lon2 < -180.0){
			throw new IllegalArgumentException();
		}
		
		double distance = 0.0;

		double rlat1 = deg2rad(lat1);
		double rlat2 = deg2rad(lat2);
		double rlon1 = deg2rad(lon1);
		double rlon2 = deg2rad(lon2);

		double deltaLat = rlat2 - rlat1;
		double deltaLon = rlon2 - rlon1;
		double a = Math.pow(Math.sin(deltaLat / 2.0), 2.0) + Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin(deltaLon / 2.0), 2.0);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

		distance = EARTH_RADIUS * c;	
		
		return distance;
	}
	
}
