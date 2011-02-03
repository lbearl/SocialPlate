/**
 * This class will handle the database access necessary for handling the storing and retrieval of credentials
 * 
 * It is a shameless knockoff of Uphoff's code
 */
package edu.msoe.SocialPlate.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class CredentialDbService {
	
	private static final String DATABASE_NAME = "cred.db";
	
	private static final int DATABASE_VERSION = 2;
	
	private static final String CREDENTIAL_TABLE_NAME = "cred_table";
	
	private Context ctx;
	
	private SQLiteDatabase db;
	
	private SQLiteStatement insertStmt;
	
	private static String SP_DB = "Social Plate Database";
	
	private static final String CRED_INSERT = "insert into " 
		+ CREDENTIAL_TABLE_NAME + "(id, user, token, secret, date) values (?, ?, ?, ?, ?)";
	
	private static final String CRED_UPDATE_WHERE = "id=?";
	
	/**
	 * This provides a constructor for the Credential's Database
	 * @param context: the context of the application
	 */
	public CredentialDbService(Context context){
		this.ctx = context;
		OpenHelper openHelper = new OpenHelper(this.ctx);
		
		this.db = openHelper.getWritableDatabase();
		
		this.insertStmt = this.db.compileStatement(CRED_INSERT);
	}
	
	/**
	 * Inserts the user, token, and secret into the database
	 * A quick note on the rational for Joda Time: It is the most reliable way to 
	 * get ISO compliant DateTime formats without writing a hell of a lot of code
	 * (that I know of at least).
	 * @param user: the Twitter username
	 * @param token: the Twitter OAuth Token
	 * @param secret: the Twitter OAuth Secret
	 * @return: the id of the result
	 * |id|user|token|secret|date|
	 */
	public long insert(String user, String token, String secret){
		DateTime dt = new DateTime();
		DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
		this.insertStmt.bindString(2, user);
		this.insertStmt.bindString(3, token);
		this.insertStmt.bindString(4, secret);
		this.insertStmt.bindString(5, dt.toDateTimeISO().toString(dtf));
		Log.d(SP_DB, "Inserted the following into the Database: " + user + "," + token + "," + secret + ","
				+ dt.toDateTimeISO().toString(dtf));
		return this.insertStmt.executeInsert();
	}
	
	//TODO Someone might want to be able to update the table eventually, I'm not doing that now
	// tx, Luke
	
	/**
	 * This drops the entire table
	 */
	public void deleteAll(){
		this.db.delete(CREDENTIAL_TABLE_NAME, null, null);
	}
	
	/**
	 * Helper method to get all the credentials from the database
	 * 
	 * @return List[0] is tokens
	 * List[1] is secrets
	 * |id|user|token|secret|date|
	 * 
	 * Yes, thats right, I'm returning an array of Lists.  I am that evil.
	 */
	public List<String>[] selectAll() {
		@SuppressWarnings("unchecked")
		List<String>[] list = new List[3]; //can't set generic type?
		list[0]= new ArrayList<String>();
		list[1] = new ArrayList<String>();

		// A cursor is equivalent to a ResultSet in JDBC
		Cursor cursor = this.db.query(CREDENTIAL_TABLE_NAME, new String[] { "id",
				"user", "token", "secret", "date" }, null, null, null, null,
				"id");

		// Check to see if there are rows, if so add all the items
		if (cursor.moveToFirst()) {
			do {
				// Cursor offsets are zero-indexed! Be careful.
				list[0].add(cursor.getString(2));
				list[1].add(cursor.getString(3));
			} while (cursor.moveToNext());
		}

		// Free up resources
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
	
	/**
	 * Extended version of the SQLiteOpenHelper class customized for our database.
	 * 
	 * @author uphoff
	 * 
	 */
	private static class OpenHelper extends SQLiteOpenHelper {

		/**
		 * Pass the database configurations to the super constructor.
		 * 
		 * @param context
		 *            The application's context
		 */
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// Called when the database needs to be created.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ CREDENTIAL_TABLE_NAME
					+ " (id INTEGER primary key, user TEXT, token TEXT, secret TEXT, date TEXT)");
		}

		// Called when the database needs to be upgraded to a new version of SQLite.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example",
					"Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + CREDENTIAL_TABLE_NAME);
			onCreate(db);
		}
	}

}


