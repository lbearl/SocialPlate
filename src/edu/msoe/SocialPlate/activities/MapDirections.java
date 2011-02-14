package edu.msoe.SocialPlate.activities;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.UserChoices;

public class MapDirections extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	EditText userInput; 
	RadioGroup rGroup;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directions);
		
		
		userInput = (EditText) findViewById(R.id.input);
		
		rGroup = (RadioGroup) findViewById(R.id.directionsList);
		rGroup.setOnCheckedChangeListener(this);
		
		
		Button okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		UserChoices.getInstance().setDirections(checkedId);
	}
	
	public void onClick(View view){
		double distance; 
		
		Log.i("MapDirection onClick", "K"+userInput.getText().toString()+"K");
		if(userInput.getText().toString() == null || userInput.getText().toString().equals("")){
			distance = 0.0;
		}
		else {
			distance = Double.parseDouble(userInput.getText().toString());
		}
		UserChoices.getInstance().setLocationsSearch(distance);
		
		Intent intent = new Intent();
		intent.setClassName(getResources().getString(R.string.package_structure),
				getResources().getString(R.string.home_screen_fqn));
		finish();
		startActivity(intent);
		
	}

	
	
}
