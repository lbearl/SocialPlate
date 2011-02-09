package edu.msoe.SocialPlate.database;

import java.util.ArrayList;
import java.util.List;

import android.R.id;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBAdapter {
	
	private static final String DATABASE_NAME = "socialplate.db";
	
	private static final int DATABASE_VERSION = 2;
	
	private static final String TABLE_NAME = "restuarants";
	
	private Context context;
	
	//The database handle
	private SQLiteDatabase db;
	
	//handle for insert statements
	private SQLiteStatement insertStmt;

	private static final String INSERT = "INSERT INTO " +
		TABLE_NAME +
		" (restuarant_name, latitude, longitude, description) values (?,?,?,?);";
	
	private static final String UPDATE_WHERE = "id=? ";
	
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
	public long insert(String rName, double latitude, double longitude, String description){		
		this.insertStmt.bindString(1, rName);
		this.insertStmt.bindDouble(2, latitude);
		this.insertStmt.bindDouble(3, longitude);
		this.insertStmt.bindString(4, description);
		return this.insertStmt.executeInsert();		
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
	 * @param restuarantName
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public long update(long id, String restuarantName, double latitude, double longitude, String description){		
		ContentValues values = new ContentValues();
		values.put("restuarant_name", restuarantName);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		values.put("description", description);
		return this.db.update(TABLE_NAME, values, UPDATE_WHERE, new String[]{(id+"")});		
	}
	
	/**
	 * Helper method to count the number of rows in TABLE_NAME
	 * @return the number of records
	 */
	public int countRestuarants(){
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
		
		Cursor cursor = this.db.query(TABLE_NAME, new String[]{"id", "restuarant_name	", "latitude", 
				"longitude", "description"},
				null, null, null, null, "id desc");
		
		if(cursor.moveToFirst()){
			do{
				list.add("id" + cursor.getInt(0) + " Name " + cursor.getString(1) + " Lat " + cursor.getInt(2)
						+ " Lng " + cursor.getInt(3) + " description " + cursor.getString(4));
			}while(cursor.moveToNext());			
		}
		
		if(cursor != null && !cursor.isClosed()){
			cursor.close();
		}
		return list;
	}
	
	public void closeDB(){
		if(db!=null){
			db.close();
		}
	}
	
	
	private static class OpenHelper extends SQLiteOpenHelper{
		
		public OpenHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			super.close();
			this.close();
		}	
		
		
		public void onCreate(SQLiteDatabase db){
			db.execSQL("CREATE TABLE "
				+ TABLE_NAME
				+ " (id INTEGER primary key autoincrement" +
						", restuarant_name text, latitude numeric, longitude numeric, description text);");
			
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
	
}
