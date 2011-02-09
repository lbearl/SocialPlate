package edu.msoe.SocialPlate.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.database.DBAdapter;
import edu.msoe.SocialPlate.helperobjects.Restaurant;

public class SearchScreen extends Activity implements OnClickListener{

	
	int id;	//important for use in context menu		
	
	/**
	 * Default oncreate method
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchscreen);
		
		Button b = (Button)this.findViewById(R.id.searchPrice);
		b.setOnClickListener(this);
		registerForContextMenu(b);
		b = (Button)this.findViewById(R.id.searchType);
		b.setOnClickListener(this);
		registerForContextMenu(b);		
	}
	
	/**
	 * Create context menu
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
				
		MenuInflater inflater = getMenuInflater();
		if(v.getId()==R.id.searchPrice){			
			menu.setHeaderTitle("Restaurant Price Range");			
			inflater.inflate(R.menu.pricecontext, menu);
		}else if(v.getId()==R.id.searchType){
			menu.setHeaderTitle("Food Type");
			inflater.inflate(R.menu.typecontext, menu);
		}
	}
	
	/**
	 * What to do if a context item is selected
	 */
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Button b = (Button)findViewById(id);
		b.setText(b.getText().subSequence(0, 5) + " " + item.getTitle().toString());		
		
		if(id==R.id.searchPrice){
			b.setText(getResources().getString(R.string.price_range)+ ": " + item.getTitle().toString());
		}else if(id==R.id.searchType){
			b.setText(getResources().getString(R.string.food_type) + ": " + item.getTitle().toString());
		}
		return true;
	}
		

	/**
	 * The event handler for button clicks.
	 * @param view
	 */
	public void onClick(View view){		
		id = view.getId();
		Toast.makeText(getApplication(), "HANDLING Clicks", Toast.LENGTH_LONG).show();
		if(id == R.id.searchPrice){	
			this.openContextMenu(view);			
		}else if(id == R.id.searchType){			
			view.showContextMenu();
		}else if(view.getId()==R.id.searchSubmit){		
			handleSubmitButton();			
		}
	}

	private void handleSubmitButton() {

		DBAdapter dba = new DBAdapter(getApplicationContext());
		
		List<String> rName = new ArrayList<String>();
		List<String> rPrice = new ArrayList<String>();
		List<String> rType = new ArrayList<String>();
		
		rName.add(((EditText)findViewById(R.id.searchName)).getText().toString());
		String price = ((Button)findViewById(R.id.searchPrice)).getText().toString();
		rPrice.add(price.substring(getResources().getString(R.string.price_range).length()+1, price.length()));
		String type = ((Button)findViewById(R.id.searchType)).getText().toString();
		rType.add(type.substring(getResources().getString(R.string.food_type).length()+1, type.length()));
		
		Restaurant[] rest= dba.queryRestaurants(rName, rPrice, rType, new ArrayList<String>(), new ArrayList<String>(),
				new ArrayList<String>(), DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH,
				DBAdapter.DISABLE_LOCATION_SEARCH, DBAdapter.DISABLE_LOCATION_SEARCH);
	
		Intent search = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelableArray("rest", rest);
		search.putExtra("extra", bundle);
		search.setClassName(getResources().getString(R.string.package_structure),
				getResources().getString(R.string.result_screen_fqn));
		startActivity(search);
	}
}
