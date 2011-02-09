package edu.msoe.SocialPlate.activities;

//TODO this is someone else's implementation, I'm just using it to figure out how to make Signpost and Twitter4j
// play nice together.
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import java.sql.Date;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


	/**
	 * An example to use SignPost and Twitter4J with OAuth.<br>
	 * <br>
	 * Remember to set "Browser" application type when you register
	 * your application on Twitter: http://twitter.com/apps
	 * 
	 * @author agirardello and bearll
	 *
	 */
	public class TwitterActivity extends Activity {

		private static final String APP = 	"OAUTH";

		private Twitter twitter;
		private OAuthProvider provider;
		private CommonsHttpOAuthConsumer consumer;

		private static final String CONSUMER_KEY = "bDmYbdt2HQkUaEIeHY249g";
		private static final String CONSUMER_SECRET = "ZJspIKrAtmFTm3fQya9UAyc6JOlD2Euj6N7CTiHU";
		private String CALLBACK_URL = "yourapp://oauth";
		
		private TextView tweetTextView;
		private Button buttonLogin;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			//setContentView(R.layout.main);
			//tweetTextView = (TextView)findViewById(R.id.tweet);
			//buttonLogin = (Button)findViewById(R.id.ButtonLogin);
			//buttonLogin.setOnClickListener(new OnClickListener() {	
				//public void onClick(View v) {
					
				//}
			//});
		}
		
		public void onResume(){
			super.onResume();
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
			if(!sp.getBoolean("AUTHED", false)){
				askOAuth();
				Editor e = sp.edit();
				e.putBoolean("AUTHED", true);
				e.commit();
			}
			
			
		}

		/**
		 * Open the browser and asks the user to authorize the app.
		 * Afterwards, we redirect the user back here!
		 */
		private void askOAuth() {
			try {
				consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
													"http://twitter.com/oauth/access_token",
													"http://twitter.com/oauth/authorize");
				String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
				Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			} catch (Exception e) {
				Log.e(APP, e.getMessage());
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}


		/**
		 * As soon as the user successfully authorized the app, we are notified
		 * here. Now we need to get the verifier from the callback URL, retrieve
		 * token and token_secret and feed them to twitter4j (as well as
		 * consumer key and secret).
		 */
		@Override
		protected void onNewIntent(Intent intent) {

			super.onNewIntent(intent);

			Uri uri = intent.getData();
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {

				String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

				try {
					// this will populate token and token_secret in consumer
					provider.retrieveAccessToken(consumer, verifier);
					
					// TODO: you might want to store token and token_secret in you app settings!!!!!!!!
					AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
					
					// initialize Twitter4J
					twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
					twitter.setOAuthAccessToken(a);
					
					// create a tweet
					Date d = new Date(System.currentTimeMillis());
					String tweet = "#OAuth working! " + d.toLocaleString();

					// send the tweet
					twitter.updateStatus(tweet);
					
					// feedback for the user...
					tweetTextView.setText(tweet);
					Toast.makeText(this, tweet, Toast.LENGTH_LONG).show();
					buttonLogin.setVisibility(Button.GONE);
					
				} catch (Exception e) {
					Log.e(APP, e.getMessage());
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		}

	}

