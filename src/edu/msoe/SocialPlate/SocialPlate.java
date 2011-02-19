package edu.msoe.SocialPlate;

import java.util.ArrayList;

import edu.msoe.SocialPlate.activities.MapDirections;
import edu.msoe.SocialPlate.activities.ResultScreen;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;
import edu.msoe.SocialPlate.helperobjects.UserChoices;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	
	static int MAP_ID = R.drawable.map;
	static int ETHNICITY_ID = R.drawable.earth;
	static int COST_ID = R.drawable.cost;
	static int MEAL_ID = R.drawable.meal;
	
	final String[] ETHNICITY_LIST = {"American","Brazilian","Chinese","Colombian","Ethiopian","French","German","Greek",
									"Indian","Israeli","Italian","Jamaican","Japanese","Korean","Mexican","Pakistani",
									"Filippino","Polish","Puerto Rican","Spanish","Vietnamese"};
	final String[] COST_LIST = {"Cheap","Moderate","Expensive"};
	final String[] MEAL_LIST = {"Burger","Chicken","Pizza","Vegetarian","Seafood","Ice Cream", "Donuts","Buffet"};
	final String[] DIRECTIONS_LIST = {"Walking","Driving","Bus"};
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MAP = (ImageButton) findViewById(R.id.mapbutton);
        MAP.setImageResource(MAP_ID);
        MAP.setAdjustViewBounds(true);
        MAP.setMaxHeight(80);
        MAP.setMaxWidth(80);
        MAP.setOnClickListener(this);
        
        ETHNICITY = (ImageButton) findViewById(R.id.ethnicitybutton);
        ETHNICITY.setImageResource(ETHNICITY_ID);
        ETHNICITY.setAdjustViewBounds(true);
        ETHNICITY.setMaxHeight(80);
        ETHNICITY.setMaxWidth(80);
        ETHNICITY.setOnClickListener(this);
        
        COST = (ImageButton) findViewById(R.id.costbutton);
        COST.setImageResource(COST_ID);
        COST.setAdjustViewBounds(true);
        COST.setMaxHeight(80);
        COST.setMaxWidth(80);
        COST.setOnClickListener(this);
        
        MEAL = (ImageButton) findViewById(R.id.mealbutton);
        MEAL.setImageResource(MEAL_ID);
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
    		Intent intent = new Intent();
    		intent.setClassName(getResources().getString(R.string.package_structure),
    				getResources().getString(R.string.directions_screen_fqn));
    		
    		startActivity(intent);
    	}
    	else if(view == ETHNICITY){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose Food Ethnicity");
    		builder.setSingleChoiceItems(ETHNICITY_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				setEthnicity(item);
    				UserChoices.getInstance().setEthnicity(ETHNICITY_LIST[item]);
    				//Check to see if above line works...
    				String e = "" + UserChoices.getInstance().getEthnicity();
    				Toast.makeText(SocialPlate.this, e, Toast.LENGTH_SHORT).show();
    				// ...end check.
    				
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
    				UserChoices.getInstance().setCost(COST_LIST[item]);
    				//Check to see if above line works...
    				Toast.makeText(SocialPlate.this, COST_LIST[item], Toast.LENGTH_SHORT).show();
    				// ...end check.
    				
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	else if(view == MEAL){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose Food Type");
    		builder.setSingleChoiceItems(MEAL_LIST, -1, new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item){
    				setMeal(item);
    				UserChoices.getInstance().setMeal(MEAL_LIST[item]);
    				//Check to see if above line works...
    				Toast.makeText(SocialPlate.this, UserChoices.getInstance().getMeal(), Toast.LENGTH_SHORT).show();
    				// ...end check.
    				
    				dialog.dismiss();
    			}
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    	}
    	
    	
    	else if(view == GEOTAG){
    		Intent intent = new Intent();
    		intent.setClassName(getResources().getString(R.string.package_structure),
    				getResources().getString(R.string.geo_tag_screen_fqn));
    		startActivity(intent);
    	}
    	else if(view == CLEARALL){
    		Toast.makeText(this, "Clearing all choices", Toast.LENGTH_SHORT).show();
    		clearAll();
    	}
    	else if(view == PLATE){
    		
    		DBAdapter dba = new DBAdapter(getApplicationContext());
    		
    		Restaurant[] restaurant = dba.queryRestaurant();
    		dba.closeDB();
    		
    		Toast.makeText(this, "Choosing your restaurant", Toast.LENGTH_SHORT).show();
    		
    		Intent intent = new Intent();
    		intent.setClassName(getResources().getString(R.string.package_structure),
    				getResources().getString(R.string.result_screen_fqn));
    		Bundle bundle = new Bundle();
    		bundle.putParcelableArray("Restaurants", restaurant);
    		intent.putExtras(bundle);
    		startActivity(intent);
    	}
    	else if(view == SEARCH){
    		Toast.makeText(this, "Search started", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void setEthnicity(int index){
		switch(index){
		case 0:
			ETHNICITY_ID = R.drawable.america;
			ETHNICITY.setImageResource(R.drawable.america);
	    	break;
		case 1:
			ETHNICITY_ID = R.drawable.brazil;
			ETHNICITY.setImageResource(R.drawable.brazil);
	    	break;
		case 2:
			ETHNICITY_ID = R.drawable.china;
			ETHNICITY.setImageResource(R.drawable.china);
	    	break;
		case 3:
			ETHNICITY_ID = R.drawable.colombia;
			ETHNICITY.setImageResource(R.drawable.colombia);
	    	break;
		case 4:
			ETHNICITY_ID = R.drawable.ethiopia;
			ETHNICITY.setImageResource(R.drawable.ethiopia);
	    	break;
		case 5:
			ETHNICITY_ID = R.drawable.france;
			ETHNICITY.setImageResource(R.drawable.france);
	    	break;
		case 6:
			ETHNICITY_ID = R.drawable.germany;
			ETHNICITY.setImageResource(R.drawable.germany);
	    	break;
		case 7:
			ETHNICITY_ID = R.drawable.greece;
			ETHNICITY.setImageResource(R.drawable.greece);
	    	break;
		case 8:
			ETHNICITY_ID = R.drawable.india;
			ETHNICITY.setImageResource(R.drawable.india);
	    	break;
		case 9:
			ETHNICITY_ID = R.drawable.israel;
			ETHNICITY.setImageResource(R.drawable.israel);
	    	break;
		case 10:
			ETHNICITY_ID = R.drawable.italy;
			ETHNICITY.setImageResource(R.drawable.italy);
	    	break;
		case 11:
			ETHNICITY_ID = R.drawable.jamaica;
			ETHNICITY.setImageResource(R.drawable.jamaica);
	    	break;
		case 12:
			ETHNICITY_ID = R.drawable.japan;
			ETHNICITY.setImageResource(R.drawable.japan);
	    	break;
		case 13:
			ETHNICITY_ID = R.drawable.korea;
			ETHNICITY.setImageResource(R.drawable.korea);
	    	break;
		case 14:
			ETHNICITY_ID = R.drawable.mexico;
			ETHNICITY.setImageResource(R.drawable.mexico);
	    	break;
		case 15:
			ETHNICITY_ID = R.drawable.pakistan;
			ETHNICITY.setImageResource(R.drawable.pakistan);
	    	break;
		case 16:
			ETHNICITY_ID = R.drawable.philippines;
			ETHNICITY.setImageResource(R.drawable.philippines);
	    	break;
		case 17:
			ETHNICITY_ID = R.drawable.poland;
			ETHNICITY.setImageResource(R.drawable.poland);
	    	break;
		case 18:
			ETHNICITY_ID = R.drawable.puertorico;
			ETHNICITY.setImageResource(R.drawable.puertorico);
	    	break;
		case 19:
			ETHNICITY_ID = R.drawable.spain;
			ETHNICITY.setImageResource(R.drawable.spain);
	    	break;
		case 20:
			ETHNICITY_ID = R.drawable.vietnam;
			ETHNICITY.setImageResource(R.drawable.vietnam);
	    	break;
	    default:
	    	ETHNICITY_ID = R.drawable.earth;
			ETHNICITY.setImageResource(R.drawable.earth);
		}
	}
    
    public void setCost(int index){
		switch(index){
		case 0:
			COST_ID = R.drawable.cost1;
			COST.setImageResource(R.drawable.cost1);
	    	break;
		case 1:
			COST_ID = R.drawable.cost2;
			COST.setImageResource(R.drawable.cost2);
	    	break;
		case 2:
			COST_ID = R.drawable.cost3;
			COST.setImageResource(R.drawable.cost3);
	    	break;
		default:
			COST_ID = R.drawable.cost;
	    	COST.setImageResource(R.drawable.cost);
		}
	}
    
    public void setMeal(int index){
    	switch(index){
    	case 0:
    		MEAL_ID = R.drawable.burger;
    		MEAL.setImageResource(R.drawable.burger);
    		break;
    	case 1:
    		MEAL_ID = R.drawable.chicken;
    		MEAL.setImageResource(R.drawable.chicken);
    		break;
    	case 2:
    		MEAL_ID = R.drawable.pizza;
    		MEAL.setImageResource(R.drawable.pizza);
    		break;
    	case 3:
    		MEAL_ID = R.drawable.vegetarian;
    		MEAL.setImageResource(R.drawable.vegetarian);
    		break;
    	case 4:
    		MEAL_ID = R.drawable.seafood;
    		MEAL.setImageResource(R.drawable.seafood);
    		break;
    	case 5:
    		MEAL_ID = R.drawable.icecream;
    		MEAL.setImageResource(R.drawable.icecream);
    		break;
    	case 6:
    		MEAL_ID = R.drawable.donut;
    		MEAL.setImageResource(R.drawable.donut);
    		break;
    	case 7:
    		MEAL_ID = R.drawable.buffet;
    		MEAL.setImageResource(R.drawable.buffet);
    		break;
    	default:
    		MEAL_ID = R.drawable.meal;
    		MEAL.setImageResource(R.drawable.meal);
    		break;
    	}
    }
    
    public void clearAll(){
    	UserChoices.getInstance().clearAll();
    	MAP.setImageResource(R.drawable.map);
    	ETHNICITY.setImageResource(R.drawable.earth);
    	COST.setImageResource(R.drawable.cost);
    	MEAL.setImageResource(R.drawable.meal);
    }
    
}