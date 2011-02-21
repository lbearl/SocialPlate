package edu.msoe.SocialPlate.activities;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;
import edu.msoe.SocialPlate.http.ServerConnect;
import edu.msoe.SocialPlate.tasks.GetLocationTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * This activity displays an activity that allows the user to tag the current location
 * @author michael
 */
public class GeoTag extends Activity implements OnClickListener{
	
	int id;
	
	/**
	 * On create method. Retrieve location from Location Services
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geotag);
				
		GetLocationTask glt = new GetLocationTask(getApplicationContext(), 
				RetrieverOfLocations.getInstance(getApplicationContext()));	
		
		Button b = (Button)this.findViewById(R.id.priceButton);
		b.setOnClickListener(this);
		registerForContextMenu(b);
		b = (Button)this.findViewById(R.id.typeButton);
		b.setOnClickListener(this);
		registerForContextMenu(b);
		b = (Button)this.findViewById(R.id.ethnicityButton);
		b.setOnClickListener(this);
		registerForContextMenu(b);
		b = (Button)this.findViewById(R.id.locationButton);
		b.setOnClickListener(this);
		
		updateLocationView();
	}
	
	/**
	 * Update the views that display geo coordinates
	 */
	public void updateLocationView(){
		((TextView)this.findViewById(R.id.latnum)).setText(RetrieverOfLocations.getInstance(getApplicationContext()).latitude+"");
		((TextView)this.findViewById(R.id.lngnum)).setText(RetrieverOfLocations.getInstance(getApplicationContext()).longitude+"");
	}
	
	/**
	 * Create context menu
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
				
		MenuInflater inflater = getMenuInflater();
		if(v.getId()==R.id.priceButton){			
			menu.setHeaderTitle("Restaurant Price Range");			
			inflater.inflate(R.menu.pricecontext, menu);
		}else if(v.getId()==R.id.typeButton){
			menu.setHeaderTitle("Food Type");
			inflater.inflate(R.menu.typecontext, menu);
		}else if(v.getId()==R.id.ethnicityButton){
			menu.setHeaderTitle("Ethnicity Type");
			inflater.inflate(R.menu.ethnicitycontext, menu);
		}
	}
	
	/**
	 * What to do if a context item is selected
	 */
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Button b = (Button)findViewById(id);
		b.setText(b.getText().subSequence(0, 5) + " " + item.getTitle().toString());		
		
		if(id==R.id.priceButton){
			b.setText(getResources().getString(R.string.price_range)+ ": " + item.getTitle().toString());
		}else if(id==R.id.typeButton){
			b.setText(getResources().getString(R.string.food_type) + ": " + item.getTitle().toString());
		}else if(id==R.id.ethnicityButton){
			b.setText(getResources().getString(R.string.ethnicity_type) + ": " + item.getTitle().toString());
		}

		return true;
	}


	
	/**
	 * The event handler for button clicks. yo. no. go ahead.  i just gotta track down my mistake.  ttyl
	 * @param view
	 */
	public void onClick(View view){	
		
		id = view.getId();
		
		if(id == R.id.priceButton){	
			this.openContextMenu(view);			
		}else if(id == R.id.typeButton){			
			view.showContextMenu();
		}else if(id == R.id.ethnicityButton){
			view.showContextMenu();		
		}else if(view.getId()==R.id.tagButton){
			handleTagButton();					
		}else if(id == R.id.locationButton){
			handleLocationUpdate();
		}
	}
	
	/**
	 * Update the location for the user
	 */
	public void handleLocationUpdate(){
		GetLocationTask gt = new GetLocationTask(GeoTag.this, RetrieverOfLocations.getInstance(getApplicationContext()));
		gt.execute();
		updateLocationView();
	}
	
	
	/**
	 * Detailed handler for the Tag Button, checks to make sure all fields are filled in
	 * and then inserts into the local DB.
	 * 
	 * It also prints an error message if the user leaves a field blank.
	 */
	public void handleTagButton(){
		
		DBAdapter dba = new DBAdapter(GeoTag.this);		
		
		//attributes to glean information from
		EditText rname = (EditText)this.findViewById(R.id.restuarant_name);
		TextView latNum = (TextView)this.findViewById(R.id.latnum);
		TextView lngNum = (TextView)this.findViewById(R.id.lngnum);
		EditText description = (EditText)this.findViewById(R.id.dbox);
		String price = ((Button)this.findViewById(R.id.priceButton)).getText().toString();
		if(!price.equals(getResources().getString(R.string.price_range))){
			price = price.substring(getResources().getString(R.string.price_range).length()+2, price.length());
		}	
		String type = ((Button)this.findViewById(R.id.typeButton)).getText().toString();
		if(!type.equals(getResources().getString(R.string.food_type))){
			type = type.substring(getResources().getString(R.string.food_type).length()+2, type.length());
		}	
		String ethnicity = ((Button)this.findViewById(R.id.ethnicityButton)).getText().toString();
		if(!ethnicity.equals(getResources().getString(R.string.ethnicity_type))){
			ethnicity = ethnicity.substring(getResources().getString(R.string.ethnicity_type).length()+2, ethnicity.length());
		}
		
		Log.i("Checkign insert price", "P"+price+"P");
		Log.i("Checkign insert type", "P"+type+"P");
		
		
		
		if(!rname.getText().toString().equals("") && !latNum.getText().toString().equals("")
			&& !lngNum.getText().toString().equals("") && !description.toString().equals("")
			&& !price.equals(getResources().getString(R.string.price_range)) 
			&& !type.equals(getResources().getString(R.string.food_type)) 
			&& !ethnicity.equals(getResources().getString(R.string.ethnicity_type))){
		
			Log.i("GeoTag tag handler", "making query to database");
			
			
			Restaurant rest = new Restaurant(-1, rname.getText().toString(),
					Double.parseDouble(latNum.getText().toString()),
					Double.parseDouble(lngNum.getText().toString()),
					description.getText().toString(), price, type, ethnicity);
			
			try {
			//	ServerConnect.getInstance().sendToServer(getApplicationContext(), rest.toJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			dba.insert(rname.getText().toString(), Double.parseDouble(latNum.getText().toString()), 
					Double.parseDouble(lngNum.getText().toString()), description.getText().toString(), price,
					type, ethnicity);
			dba.closeDB();
			Toast.makeText(getApplicationContext(), "Restaurant Tagged!", Toast.LENGTH_SHORT).show();
		}
		

		
		String errorMessage = "The field(s) (";
		StringBuilder errorFields = new StringBuilder();
		errorFields.append("");
		
		if(rname.getText().toString().equals("")){
			errorFields.append("Restuarant Name,");
		}if(latNum.getText().toString().equals(DBAdapter.DISABLE_LOCATION_SEARCH+"")){
			errorFields.append("Latitude,");
		}if(lngNum.getText().toString().equals(DBAdapter.DISABLE_LOCATION_SEARCH+"")){
			errorFields.append("Longitude,");
		}if(description.getText().toString().equals("")){
			errorFields.append("Description,");
		}if(price.equals(getResources().getString(R.string.price_range))){
			errorFields.append("Price,");
		}if(type.equals(getResources().getString(R.string.food_type))){
			errorFields.append("Type,");
		}if(ethnicity.equals(getResources().getString(R.string.ethnicity_type))){
			errorFields.append("Ethnicity,");
		}
		
		errorMessage = errorMessage + errorFields.toString() + ") should not be empty.";
		
		if(!errorFields.toString().equals("")){
			Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
		}
		
	}

}
