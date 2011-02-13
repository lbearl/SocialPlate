package edu.msoe.SocialPlate.database;

import java.util.ArrayList;
import java.util.List;

import edu.msoe.SocialPlate.helperobjects.DistanceCalculator;
import edu.msoe.SocialPlate.helperobjects.Restaurant;

import android.R.id;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * This class is responsible for accessing the socialplate.db where
 * the restaurant information is actually stored 
 * @author michael
 */
public class DBAdapter {
	
	private static final String DATABASE_NAME = "socialplate.db";	
	private static final int DATABASE_VERSION = 2;	
	private static final String TABLE_NAME = "restaurants";	
	private Context context;
	
	//The database handle
	private SQLiteDatabase db;
	
	//handle for insert statements
	private SQLiteStatement insertStmt;

	private static final String INSERT = "INSERT INTO " +
		TABLE_NAME +
		" (restaurant_name, latitude, longitude, description, price, type, ethnicity) values (?,?,?,?,?,?,?);";
	
	private static final String UPDATE_WHERE = "id=? ";
	
	
	private static final String SELECT = "SELECT * FROM " + TABLE_NAME +
		" WHERE ";	
	private static final String SELECT_NAME = "restaurant_name=?";
	private static final String SELECT_PRICE = "price=?";
	private static final String SELECT_TYPE = "type=?";	
	private static final String SELECT_ETHNICITY = "ethnicity=?";
	
	private static final String NSELECT_NAME = "restaurant_name!=?";
	private static final String NSELECT_PRICE = "price!=?";
	private static final String NSELECT_TYPE = "type!=?";
	private static final String NSELECT_ETHNICITY = "ethnicity!=?";
	
	private static final String LESS_LATITUDE = "latitude<=?";
	private static final String GREAT_LATITUDE = "latitude>=?";
	private static final String LESS_LONGITUDE = "longitude<=?";
	private static final String GREAT_LONGITUDE = "longitude>=?";	
	
	public static final double DISABLE_LOCATION_SEARCH = -200.0;
	
	private OpenHelper openHelper = null;

	/**
	 * Constructor
	 * @param context
	 */
	public DBAdapter(Context context){
		this.context = context;
			
		openHelper = new OpenHelper(this.context);	//inner class
		
		openHelper.close();
		
		this.db = openHelper.getWritableDatabase();				//create a writable database connection
		this.insertStmt = this.db.compileStatement(INSERT);	//create statement to insert data
	
	}
	
	/**
	 * Insert Into the Table
	 * @param rName
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public long insert(String rName, double latitude, double longitude, String description,
			String price, String type, String ethnicity){		
		this.insertStmt.bindString(1, rName);
		this.insertStmt.bindDouble(2, latitude);
		this.insertStmt.bindDouble(3, longitude);
		this.insertStmt.bindString(4, description);
		this.insertStmt.bindString(5, price);
		this.insertStmt.bindString(6, type);
		this.insertStmt.bindString(7, ethnicity);
		return this.insertStmt.executeInsert();		
	}
	
	public Restaurant[] queryRestaurant(){
		
		List<String> rName = new ArrayList<String>();
		List<String> rPrice = new ArrayList<String>();
		List<String> rType = new ArrayList<String>();
		List<String> rEthnicity = new ArrayList<String>();
		List<String> nName = new ArrayList<String>();
		List<String> nPrice = new ArrayList<String>();
		List<String> nType = new ArrayList<String>();
		List<String> nEthnicity = new ArrayList<String>();
		
		double userLat=0, userLng=0, dist = 0;
		
		Restaurant[] restaurants = queryRestaurants(rName, rPrice, rType, rEthnicity,
					nName, nPrice, nType, nEthnicity,
					userLat, userLng, dist); 
		
		return restaurants;
	}
	
	
	
	/**
	 * This method queries the Restaurant Table based on information specified by the user
	 * @param rName
	 * @param rPrice
	 * @param rType
	 * @param nName
	 * @param nPrice
	 * @param nType
	 * @param restLat
	 * @param restLng
	 * @param userLat
	 * @param userLng
	 * @return
	 */
	public Restaurant[] queryRestaurants(List<String> rName, List<String> rPrice, List<String> rType,
			List<String> rEthnicity, List<String> nName, List<String> nPrice, List<String> nType,
			List<String> nEthnicity,			 
			double userLat, double userLng, double dist){
		
		String query = SELECT;
		
		StringBuilder where = new StringBuilder();
		int numOfAnds = rName.size() + rPrice.size() + rType.size() + nName.size() + nPrice.size() + nType.size();
		numOfAnds--;
		//Format of following code
		//If list has more in it, append new related clause, Also if there are more clauses afterward, append AND
		//build restaurant name clauses
		for(int i=0; i<rName.size();i++)
		{ where.append(SELECT_NAME); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build restaurant price clauses
		for(int i=0; i<rPrice.size();i++)
		{ where.append(SELECT_PRICE); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build restaurant type clauses
		for(int i=0; i<rType.size();i++)
		{ where.append(SELECT_TYPE); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build ethnicity clauses
		for(int i=0; i<rEthnicity.size(); i++)
		{ where.append(SELECT_ETHNICITY); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build not restaurant name clauses
		for(int i=0; i<nName.size();i++)
		{ where.append(NSELECT_NAME); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}		
		//build not restaurant price clauses
		for(int i=0; i<nPrice.size();i++)
		{ where.append(NSELECT_PRICE); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build not restaurant type clauses
		for(int i=0; i<nType.size();i++)
		{ where.append(NSELECT_TYPE); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}
		//build not ethnicity clauses
		for(int i=0; i<nEthnicity.size(); i++)
		{ where.append(NSELECT_ETHNICITY); if(numOfAnds > 0){where.append(" AND "); numOfAnds--;}}	
		
//		if(gLat != DISABLE_LOCATION_SEARCH && lLat != DISABLE_LOCATION_SEARCH && gLng != DISABLE_LOCATION_SEARCH
//				&& lLng != DISABLE_LOCATION_SEARCH){			
//			where.append("longitude<="+ gLng + " AND longitude>="+ lLng+" AND latitude<="+gLat+" AND latitude >="+lLat);			
//		}
		
		String[] selectionArgs = new String[rName.size()+rPrice.size()+rType.size()+rEthnicity.size()+nName.size()+nPrice.size()+nType.size()+nEthnicity.size()];
		List<String> masterList = new ArrayList<String>();
		if(rName != null){
			masterList.addAll(rName);
		}if(rPrice != null){	
			masterList.addAll(rPrice);
		}if(rType != null){
			masterList.addAll(rType);
		}if(rEthnicity != null){
			masterList.addAll(rEthnicity);
		}if(nName != null){	
			masterList.addAll(nName);
		}if(nPrice != null){
			masterList.addAll(nPrice);
		}if(nType != null){
			masterList.addAll(nType);
		}if(nEthnicity != null){
			masterList.addAll(nEthnicity);
		}		
		
		masterList.toArray(selectionArgs);
		
		Cursor cursor = this.db.query(TABLE_NAME, 
									new String[]{"id","restaurant_name","latitude","longitude","description","price","type","ethnicity"},
									where.toString(),
									selectionArgs, null, null, null);		
		
		Restaurant[] masterRestaurantList = new Restaurant[cursor.getCount()];
		Restaurant tempRestaurant = null;
		int index = 0;
		while(cursor.moveToNext()){			
			tempRestaurant = new Restaurant(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2),
					cursor.getDouble(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));			 
			if(queryLocation(tempRestaurant.getLatitude(), tempRestaurant.getLongitude(), userLat, userLng, dist)){
				masterRestaurantList[index] = tempRestaurant;
				index++;
			}				 
		}				
		return masterRestaurantList;
	}
	
	/**
	 * Determines if a restaurant is within a user specified range
	 * @param restLat
	 * @param restLng
	 * @param userLat
	 * @param userLng
	 * @param dist, kilometers.
	 * @return
	 */
	public boolean queryLocation(double restLat, double restLng, double userLat, double userLng, double dist){
		boolean ret = false;
		
		if(dist==0.0 || (DistanceCalculator.calcDistance(restLat, restLng, userLat, userLng) <= dist)){
			ret = true;
		}	
		
		return ret;
	}
	
	
	/**
	 * Delete a specific restaurant from the local DB
	 * @param id
	 */
	public void deleteRestaurant(long id){		
		this.db.delete(TABLE_NAME, "id=?", new String[]{id+""});		
	}
	
	/**
	 * Delete everything from the table
	 */
	public void deleteAll(){
		this.db.delete(TABLE_NAME, null, null);
	}
	
	/**
	 * Update row in table based on where clause
	 * @param id
	 * @param restaurantName
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public long update(long id, String restaurantName, double latitude, double longitude, String description,
			String price, String type, String ethnicity){		
		ContentValues values = new ContentValues();
		values.put("restaurant_name", restaurantName);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		values.put("description", description);
		values.put("price", price);
		values.put("type", type);
		values.put("ethnicity", ethnicity);
		return this.db.update(TABLE_NAME, values, UPDATE_WHERE, new String[]{(id+"")});		
	}
	
	/**
	 * Helper method to count the number of rows in TABLE_NAME
	 * @return the number of records
	 */
	public int countRestaurants(){
		int count = -1;
		Cursor cursor = this.db.rawQuery("select count(*) from " + TABLE_NAME, null);
		
		if(cursor.moveToFirst()){
			count = cursor.getInt(0);
		}
		if(cursor != null && !cursor.isClosed()){
			cursor.close();
		}
		return count;
	}
	
	/**
	 * Select all the items and return them
	 * @return
	 */
	public ArrayList<String> selectAll(){
		ArrayList<String> list = new ArrayList<String>();
		
		Cursor cursor = this.db.query(TABLE_NAME, new String[]{"id", "restaurant_name	", "latitude", 
				"longitude", "description", "price", "type", "ethnicity"},
				null, null, null, null, "id desc");
		
		if(cursor.moveToFirst()){
			do{
				list.add("id" + cursor.getInt(0) + " Name " + cursor.getString(1) + " Lat " + cursor.getInt(2)
						+ " Lng " + cursor.getInt(3) + " description " + cursor.getString(4) + 
						" price " + cursor.getString(5) + " type " + cursor.getString(6) + " ethnicity "
						+ cursor.getString(7));
			}while(cursor.moveToNext());			
		}
		
		if(cursor != null && !cursor.isClosed()){
			cursor.close();
		}
		return list;
	}
	
	public void closeDB(){
		if(db!=null &&  db.isOpen()){			
			db.close();
		}
	}
	
	/**
	 * Static Class
	 * @author michael
	 */
	private static class OpenHelper extends SQLiteOpenHelper{
		
		/**
		 * Default Constructor
		 * @param context, Application context
		 */
		public OpenHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			super.close();
			this.close();
		}	
		
		/**
		 * Create the SQLite Table
		 */
		public void onCreate(SQLiteDatabase db){
			db.execSQL("CREATE TABLE "
				+ TABLE_NAME
				+ " (id INTEGER primary key autoincrement" +
						", restaurant_name text, latitude numeric, longitude numeric, description text" +
						", price text, type text, ethnicity text);");
			
		}
		
		/**
		 * Upgrade
		 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
	
}
