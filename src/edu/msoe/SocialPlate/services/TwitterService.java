/**
 * 
 */
package edu.msoe.SocialPlate.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * @author bearll
 *
 */
public class TwitterService {
	
	private Context ctx;
	final String TAG = "TwitTest";
	private OAuthConsumer consumer = new DefaultOAuthConsumer("bDmYbdt2HQkUaEIeHY249g", "ZJspIKrAtmFTm3fQya9UAyc6JOlD2Euj6N7CTiHU");
    private OAuthProvider provider = new DefaultOAuthProvider(
            "http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token",
            "http://twitter.com/oauth/authorize");
    
    //TODO: persist HasThisBeenRunYet
    private static boolean HasThisBeenRunYet=false;
    
    private CredentialDbService cdbs = null;

    
    public TwitterService(Context ctx){
    	this.ctx = ctx;
    	cdbs = new CredentialDbService(ctx);
    }
    /**
     * This method must be called ONLY ONCE!! Calling this method multiple times will result
     * in destruction of the universe as you know it. (and will break Twitter in the app)
     * @throws OAuthCommunicationException 
     * @throws OAuthExpectationFailedException 
     * @throws OAuthNotAuthorizedException 
     * @throws OAuthMessageSignerException 
     */
    public void TwitEnabler() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
    	if(HasThisBeenRunYet) return; //get out of this method if we have been in it
    	HasThisBeenRunYet = true;
    	String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
    	
    	//bring the user to the auth UTRL
    	ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    	String pinCode = null; //Get this from the user or from the call back?
    	
    	provider.retrieveAccessToken(consumer, pinCode);
    	cdbs.insert(consumer.getConsumerKey(), consumer.getToken(), consumer.getTokenSecret());
    }
    
    public void TwitResourceAccess(String AccessToken, String TokenSecret) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    	consumer.setTokenWithSecret(AccessToken, TokenSecret);
    	 URL url = new URL("http://twitter.com/statuses/mentions.xml");
         HttpURLConnection request = (HttpURLConnection) url.openConnection();

         // sign the request
         consumer.sign(request);

         // send the request
         request.connect();

         // response status should be 200 OK
         int statusCode = request.getResponseCode();
         Toast.makeText(ctx, "The status returned was: " + statusCode, Toast.LENGTH_LONG);
    	
    }
}
