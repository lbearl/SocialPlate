package edu.msoe.SocialPlate;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import edu.msoe.SocialPlate.services.TwitterService;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SocialPlate extends Activity {
	
	private TwitterService ts = TwitterService.getInstance();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent myIntent = new Intent(this.getApplicationContext(), edu.msoe.SocialPlate.activities.TwitterActivity.class);
    	startActivityForResult(myIntent, 0);
    }

}