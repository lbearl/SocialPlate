package edu.msoe.SocialPlate;

import edu.msoe.SocialPlate.services.TwitterService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SocialPlate extends Activity {
	
	private TwitterService ts = TwitterService.getInstance();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ts.doTwitterAuthentication(this.getApplicationContext());

    }
    protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
		ts.twitSetter(this.getIntent(), this.getApplicationContext());


	}
}