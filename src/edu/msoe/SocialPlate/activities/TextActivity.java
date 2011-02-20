package edu.msoe.SocialPlate.activities;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import edu.msoe.SocialPlate.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextActivity extends Activity {
    private Button btnSendSMS;
    private Button btnContacts;
	private EditText txtPhoneNo;
	private EditText txtMessage;

	private static final int CONTACT_PICKER_RESULT = 1001;  
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textlayout);
        
        //Pulls buttons and edittexts from resources
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnContacts = (Button) findViewById (R.id.btnContacts);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        txtMessage.setText("Hi, I'm eating at " + this.getIntent().getStringExtra("Restaurant") + ". Meet me there!");
        
        //Adds click listener
        btnContacts.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Starts the contact list activity
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
			            Contacts.CONTENT_URI);  
			    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
			}
        	
        });
        
        //Adds click listener
        btnSendSMS.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	//Shows confirmation dialog
            	AlertDialog.Builder builder = new AlertDialog.Builder(TextActivity.this);
            	builder.setMessage("Are you sure you want to send the text?")
            	       .setCancelable(false)
            	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	        	   //Pulls data from fields
            	        	   String phoneNo = txtPhoneNo.getText().toString();
            	                String message = txtMessage.getText().toString();
            	                
            	                //Checks if either the phone number field or message is empty. If not, call sendSMS(). Otherwise show message
            	                if (phoneNo.length()>0 && message.length()>0){
            	                	//Reads in the text from the phone number field into a scanner
            	                	Scanner s = new Scanner(phoneNo);
            	                	//Delimits the text using "," 
            	                	s.useDelimiter(",");
            	                	//Loops through the scanner until all numbers have been texted
            	                	while(s.hasNext() == true){
            	                		sendSMS(s.next(), message);
            	                	}
            	                }else{
            	                    Toast.makeText(getBaseContext(), 
            	                        "Please enter both phone number and message.", 
            	                        Toast.LENGTH_SHORT).show();
            	                }
            	            
            	           }
            	       })
            	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	               	//Does nothing
            	           }
            	       });
            	
            	//Creates the dialog
            	AlertDialog alert = builder.create();
            	//Shows the dialog
            	alert.show();
            }
        });        
    }
    
    //sends an SMS message to another device
    private void sendSMS(String phoneNumber, String message)
    {        
    	 String SENT = "SMS_SENT";
         String DELIVERED = "SMS_DELIVERED";
  
         PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
             new Intent(SENT), 0);
  
         PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
             new Intent(DELIVERED), 0);
  
         //---when the SMS has been sent---
         registerReceiver(new BroadcastReceiver(){
             @Override
             public void onReceive(Context arg0, Intent arg1) {
                 switch (getResultCode())
                 {
                     case Activity.RESULT_OK:
                         Toast.makeText(getBaseContext(), "SMS sent", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                     case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                         Toast.makeText(getBaseContext(), "Generic failure", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                     case SmsManager.RESULT_ERROR_NO_SERVICE:
                         Toast.makeText(getBaseContext(), "No service", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                     case SmsManager.RESULT_ERROR_NULL_PDU:
                         Toast.makeText(getBaseContext(), "Null PDU", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                     case SmsManager.RESULT_ERROR_RADIO_OFF:
                         Toast.makeText(getBaseContext(), "Radio off", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                 }
             }
         }, new IntentFilter(SENT));
  
         //---when the SMS has been delivered---
         registerReceiver(new BroadcastReceiver(){
             public void onReceive(Context arg0, Intent arg1) {
                 switch (getResultCode())
                 {
                     case Activity.RESULT_OK:
                         Toast.makeText(getBaseContext(), "SMS delivered", 
                                 Toast.LENGTH_SHORT).show();
                         break;
                     case Activity.RESULT_CANCELED:
                         Toast.makeText(getBaseContext(), "SMS not delivered", 
                                 Toast.LENGTH_SHORT).show();
                         break;                        
                 }
             }
         }, new IntentFilter(DELIVERED));        
  
         SmsManager sms = SmsManager.getDefault();
         sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);         
    }    
	
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
      super.onActivityResult(reqCode, resultCode, data);

      if (resultCode == RESULT_OK) {  
          switch (reqCode) {  
          case CONTACT_PICKER_RESULT:
        	  
        	  String number;
        	  
        	  Bundle extras = data.getExtras();  
        	  Set<String> keys = extras.keySet();  
        	  Iterator<String> iterate = keys.iterator();  
        	  while (iterate.hasNext()) {  
        	      String key = iterate.next();  
        	      Log.v("Debug", key + "[" + extras.get(key) + "]");  
        	  }  
        	  Uri result = data.getData();  
        	  Log.v("Debug", "Got a result: "  
        	      + result.toString());  
        	  
        	  // get the contact id from the Uri  
        	  String id = result.getLastPathSegment();
        	  
        	  // query for everything from Phone 
        	  Cursor cursor = getContentResolver().query(  
        	          Phone.CONTENT_URI, null,  
        	          Phone.CONTACT_ID + "=?",  
        	          new String[]{id}, null); 
        	  
        	  cursor.moveToFirst();  
        	  String columns[] = cursor.getColumnNames();  
        	  
        	  //Logs the results from the query
        	  for (String column : columns) {  
        	      int index = cursor.getColumnIndex(column);  
        	      Log.v("Debug", "Column: " + column + " == ["  
        	              + cursor.getString(index) + "]");  
        	  }
        	  
        	  //Gets the number from from the query
        	  if (cursor.moveToFirst()) {  
        		    int phoneIdx = cursor.getColumnIndex(Phone.NUMBER);  
        		    number = cursor.getString(phoneIdx);  
        		    Log.v("Debug", "Got number: " + number);
        		    if(txtPhoneNo.getText().length() == 0){
        		    	txtPhoneNo.setText(number);
        		    }else{
        		    	txtPhoneNo.append("," + number);
        		    }
        	  }  
        	  
              break;  
          }  
    
      } else {  
          // gracefully handle failure  
          Log.w("Debug", "Warning: activity result not ok");  
      }  
    }
    
    
}