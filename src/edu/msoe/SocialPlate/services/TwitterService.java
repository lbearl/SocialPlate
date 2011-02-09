/**
 * Much of this class is based on https://code.discordians.net/projects/remembeer/repository/revisions/master/entry/android/src/com/wanghaus/remembeer/helper/TwitterHelper.java
 * Nice code is nice, but it needed to be made into a singleton.
 */
package edu.msoe.SocialPlate.services;

import org.joda.time.DateTime;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author bearll
 *
 */
public class TwitterService {
	
	private final static String TAG = "Twitter Service";
	private static final String REQUEST_URL = "http://twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	private static final String AUTH_URL = "http://twitter.com/oauth/authorize";
	private static final String CALLBACK_URL = "OauthTwitter://twitt";
	
	private static Twitter twitter = null;
	
	private static final String CONSUMER_KEY = "bDmYbdt2HQkUaEIeHY249g";
	private static final String CONSUMER_SECRET = "ZJspIKrAtmFTm3fQya9UAyc6JOlD2Euj6N7CTiHU";
	private CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
    		CONSUMER_KEY, CONSUMER_SECRET);

    private OAuthProvider provider = new DefaultOAuthProvider(
            "http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token",
            "http://twitter.com/oauth/authorize");
	static private TwitterService _instance = null;
	/**
	 * Constructor
	 */
	protected TwitterService(){}
	
	static public TwitterService getInstance(){
		if(null == _instance){
			_instance = new TwitterService();
		}
		return _instance;
	}
	
	public static boolean isConfigured(Context ctx){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return sp.getBoolean("twitterEnabled", false) &&
			sp.getString("twitterUsername", null) != null &&
			sp.getString("twitterToken", null) != null &&
			sp.getString("twitterSecret", null) != null;
	}
	
	public static void clearTokens(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		
		editor.remove("twitterUsername");
		editor.remove("twitterToken");
		editor.remove("twitterSecret");
		editor.putBoolean("twitterEnabled", false);
		editor.commit();
	}
	
	public static void setupTwitter(Context context, AccessToken AT) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		
		// We don't *need* the Username, but we'll store it anyway
		editor.putString("twitterUsername", AT.getScreenName());
		editor.putString("twitterToken", AT.getToken());
		editor.putString("twitterSecret", AT.getTokenSecret());
		editor.putBoolean("twitterEnabled", true);
		editor.commit();
	}
	
	public static void sendToTwitter(Context context, String beername) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final String status = "API Test Tweet";
		if (!prefs.getBoolean("twitterEnabled", false)) {
			Log.v("sendToTwitter", "Twitter not enabled, exiting");
			return;	
		}
		
		final String token = prefs.getString("twitterToken", null);
		final String secret = prefs.getString("twitterSecret", null);
		
		if (token == null || secret == null) {
			Log.e("sendToTwitter", "Couldn't get the token or the template from prefs");
			return ;
		}
		
		// DO IT
		final AccessToken accessToken = new AccessToken(token, secret);
		Log.v("TwitterService", "Sending '" + status + "' to twitter");

	    // Create runnable for posting
	    Thread updateStatus = new Thread() {
	        public void run() {
				TwitterFactory factory = new TwitterFactory();
				Twitter twitter = factory.getOAuthAuthorizedInstance(accessToken);

				try {
					twitter.updateStatus(status);
				} catch (TwitterException e) {
					Log.e("TwitterService", "Failure to tweet", e);
				}

				Log.d("TwitterService", "Twitter thread ending");
	        }
	    };
	    updateStatus.start();
	}
	
	public void doTwitterAuthentication(Context ctx){
	    
		try {
			provider = new DefaultOAuthProvider("https://api.twitter.com/oauth/request_token",
												"https://api.twitter.com/oauth/access_token",
												"https://api.twitter.com/oauth/authorize");
			String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
			Toast.makeText(ctx, "Please authorize this app!", Toast.LENGTH_LONG).show();
			ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void twitSetter(Intent itnt, Context ctx){
		Uri uri =  itnt.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {

			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

			try {
				// this will populate token and token_secret in consumer
				provider.retrieveAccessToken(consumer, verifier);
				
				AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
				
				// initialize Twitter4J
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);
				
				// create a tweet
				DateTime d = new DateTime(System.currentTimeMillis());
				String tweet = "#OAuth working! " + d.toString();

				// send the tweet
				twitter.updateStatus(tweet);
				
				Toast.makeText(ctx, tweet, Toast.LENGTH_LONG).show();

				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	}
	
	public String getCallbackURL() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		return provider.retrieveRequestToken(consumer, CALLBACK_URL);
	}
	

}
