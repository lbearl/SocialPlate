package edu.msoe.SocialPlate;

import edu.msoe.SocialPlate.database.DBAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class SocialPlate extends Activity implements OnClickListener {
	
	ImageButton MAP;
	ImageButton ETHNICITY;
	ImageButton COST;
	ImageButton MEAL;
	ImageButton GEOTAG;
	ImageButton CLEARALL;
	ImageButton SEARCH;
	ImageButton PLATE;
	
	final int MAP_ID = 1;
	final int ETHNICITY_ID = 2;
	final int COST_ID = 3;
	final int MEAL_ID = 4;
	final int GEOTAG_ID = 5;
	final int CLEARALL_ID = 6;
	final int SEARCH_ID = 7;
	final int PLATE_ID = 8;
	
	final String[] ETHNICITY_LIST = {"American","Brazilian","Chinese","Colombian","Ethiopian"};
	final String[] COST_LIST = {"$","$$","$$$"};
	final String[] MEAL_LIST = {"Breakfast","Lunch","Dinner","Late Night"};
	final String[] DIRECTIONS_LIST = {"Walking","Driving","Bus"};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MAP = (ImageButton) findViewById(R.id.mapbutton);
        MAP.setImageResource(R.drawable.map);
        MAP.setAdjustViewBounds(true);
        MAP.setMaxHeight(80);
        MAP.setMaxWidth(80);
        MAP.setOnClickListener(this);
        
        ETHNICITY = (ImageButton) findViewById(R.id.ethnicitybutton);
        ETHNICITY.setImageResource(R.drawable.earth);
        ETHNICITY.setAdjustViewBounds(true);
        ETHNICITY.setMaxHeight(80);
        ETHNICITY.setMaxWidth(80);
        ETHNICITY.setOnClickListener(this);
        
        COST = (ImageButton) findViewById(R.id.costbutton);
        COST.setImageResource(R.drawable.cost);
        COST.setAdjustViewBounds(true);
        COST.setMaxHeight(80);
        COST.setMaxWidth(80);
        COST.setOnClickListener(this);
        
        MEAL = (ImageButton) findViewById(R.id.mealbutton);
        MEAL.setImageResource(R.drawable.meal);
        MEAL.setAdjustViewBounds(true);
        MEAL.setMaxHeight(80);
        MEAL.setMaxWidth(80);
        MEAL.setOnClickListener(this);
        
        GEOTAG = (ImageButton) findViewById(R.id.geotagbutton);
        GEOTAG.setImageResource(R.drawable.geotag);
        GEOTAG.setAdjustViewBounds(true);
        GEOTAG.setMaxHeight(80);
        GEOTAG.setMaxWidth(80);
        GEOTAG.setOnClickListener(this);
        
        CLEARALL = (ImageButton) findViewById(R.id.clearallbutton);
        CLEARALL.setImageResource(R.drawable.eraser);
        CLEARALL.setAdjustViewBounds(true);
        CLEARALL.setMaxHeight(80);
        CLEARALL.setMaxWidth(80);
        CLEARALL.setOnClickListener(this);
        
        SEARCH = (ImageButton) findViewById(R.id.searchbutton);
        SEARCH.setImageResource(R.drawable.search);
        SEARCH.setAdjustViewBounds(true);
        SEARCH.setMaxHeight(80);
        SEARCH.setMaxWidth(80);
        SEARCH.setOnClickListener(this);
        
        PLATE = (ImageButton) findViewById(R.id.platebutton);
        PLATE.setImageResource(R.drawable.plate);
        PLATE.setAdjustViewBounds(true);
        PLATE.setMaxWidth(150);
        PLATE.setOnClickListener(this);
        
    }
    
    public void onClick(View view){
    	if(view == MAP){
    		/*
    		
				Start new activity?
					-can't have input in an alertdialog

    		*/
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Directions?");
    		builder.setSingleChoiceItems(DIRECTIONS_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    		
    	}
    	else if(view == ETHNICITY){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose Food Ethnicity");
    		builder.setSingleChoiceItems(ETHNICITY_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				setEthnicity(item);
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	else if(view == COST){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose Cost");
    		builder.setSingleChoiceItems(COST_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				setCost(item);
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	else if(view == MEAL){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose Meal");
    		builder.setSingleChoiceItems(MEAL_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				setMeal(item);
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	else if(view == PLATE){
    		Toast.makeText(this, "Choosing your restaurant...", Toast.LENGTH_SHORT).show();
    		DBAdapter dba = new DBAdapter(getApplicationContext());
    		
    	}
    	else if(view == SEARCH){
    		Toast.makeText(this, "Search started", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void setEthnicity(int index){
		switch(index){
		case 0:
			ETHNICITY.setImageResource(R.drawable.america);
	    	break;
		case 1:
			ETHNICITY.setImageResource(R.drawable.brazil);
	    	break;
		case 2:
			ETHNICITY.setImageResource(R.drawable.china);
	    	break;
		case 3:
			ETHNICITY.setImageResource(R.drawable.colombia);
	    	break;
		case 4:
			ETHNICITY.setImageResource(R.drawable.ethiopia);
	    	break;
	    default:
	    	ETHNICITY.setImageResource(R.drawable.earth);
		}
	}
    
    public void setCost(int index){
		switch(index){
		case 0:
			COST.setImageResource(R.drawable.cost1);
	    	break;
		case 1:
			COST.setImageResource(R.drawable.cost2);
	    	break;
		case 2:
			COST.setImageResource(R.drawable.cost3);
	    	break;
		default:
	    	COST.setImageResource(R.drawable.cost);
		}
	}
    
    public void setMeal(int index){
    	switch(index){
    	case 0:
    		MEAL.setImageResource(R.drawable.breakfast);
    		break;
    	case 1:
    		MEAL.setImageResource(R.drawable.lunch);
    		break;
    	case 2:
    		MEAL.setImageResource(R.drawable.dinner);
    		break;
    	case 3:
    		MEAL.setImageResource(R.drawable.latenight);
    		break;
    	}
    }
    
    public void resetAll(){
    	MAP.setImageResource(R.drawable.map);
    	ETHNICITY.setImageResource(R.drawable.earth);
    	COST.setImageResource(R.drawable.cost);
    	MEAL.setImageResource(R.drawable.meal);
    }
    
}