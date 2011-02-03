package edu.msoe.SocialPlate.activities;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.RetrieverOfLocations;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.tasks.GetLocationTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity displays an activity that allows the user to tag the current location
 * @author michael
 */
public class GeoTag extends Activity{
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geotag);
		
		GetLocationTask glt = new GetLocationTask(getApplicationContext(), 
				RetrieverOfLocations.getInstance(getApplicationContext()));
		
		((TextView)this.findViewById(R.id.latnum)).setText(RetrieverOfLocations.getInstance(getApplicationContext()).latitude+"");
		((TextView)this.findViewById(R.id.lngnum)).setText(RetrieverOfLocations.getInstance(getApplicationContext()).longitude+"");
	}
	
	public void onClick(View view){
		
		DBAdapter dba = new DBAdapter(GeoTag.this);		
		EditText rname = (EditText)this.findViewById(R.id.restuarant_name);
		TextView latNum = (TextView)this.findViewById(R.id.latnum);
		TextView lngNum = (TextView)this.findViewById(R.id.lngnum);
		EditText description = (EditText)this.findViewById(R.id.dbox);
		
		if(!rname.getText().toString().equals("") && !latNum.getText().toString().equals("")
			&& !lngNum.getText().toString().equals("") && !description.toString().equals("")){
			dba.insert(rname.getText().toString(), Double.parseDouble(latNum.getText().toString()), 
					Double.parseDouble(lngNum.getText().toString()), description.getText().toString());
			dba.closeDB();
		}
		
		String errorMessage = "The field(s) (";
		String errorFields = "";
		
		if(rname.getText().toString().equals("")){
			errorFields += "Restuarant Name,";
		}if(latNum.getText().toString().equals("")){
			errorFields += "Latitude,";
		}if(lngNum.getText().toString().equals("")){
			errorFields += "Longitude,";
		}if(description.getText().toString().equals("")){
			errorFields += "Description";
		}
		
		errorMessage = errorMessage + errorFields + ") should not be empty.";
		
		if(!errorFields.equals("")){
			Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
		}
		
	}

}
