package edu.msoe.SocialPlate.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.msoe.SocialPlate.R;
import edu.msoe.SocialPlate.helperobjects.Restaurant;

import android.content.Context;
import android.util.Log;

public class ServerConnect {

	private CookieStore session;
	
	
	private ServerConnect(){
		
	}
	
	private static class LazyHolder {
		public static ServerConnect serverConnect = new ServerConnect();
	}
	
	/**
	 * Gets an instance of the ServerConnect.
	 * @return ServerConnect
	 */
	public static ServerConnect getInstance() {
		return LazyHolder.serverConnect;
	}
	
	public void deleteSession(){
		session = null;
	}
	
	/**
	 * Actual implementation of the send method.
	 * @param context
	 * @param command
	 * @return Command
	 * @throws Exception
	 */
	public JSONArray sendToServer(Context context, JSONObject j)  throws Exception {
//		Logger.logDebug(context, context.getString(R.string.server_location) + ", ServerConnect - request cmd=" + command.getCommand() + ",data=" + command.packageJSON());

		// Sets the http client and stores the session if one exists
		AbstractHttpClient httpClient = new DefaultHttpClient();
		if (session != null) {
			httpClient.setCookieStore(session);
		}

		// Adds the post parameters to the http client
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		
//		nvps.add(new BasicNameValuePair("cmd", command.getCommand()));
//		nvps.add(new BasicNameValuePair("data", j.toString()));
//		nvps.add(new BasicNameValuePair("description", j.getString("description")));
//		nvps.add(new BasicNameValuePair("r_name", j.getString("r_name")));
//		nvps.add(new BasicNameValuePair("f_type", j.getString("f_type")));
//		nvps.add(new BasicNameValuePair("price_name", j.getString("price_name")));
//		nvps.add(new BasicNameValuePair("f_type", j.getString("f_type")));
//		nvps.add(new BasicNameValuePair("ethnicity", j.getString("ethnicity")));
//		nvps.add(new BasicNameValuePair("latitude", j.getString("latitude")));
//		nvps.add(new BasicNameValuePair("longitude", j.getString("longitude")));
		

		HttpPost httpPost = new HttpPost(context.getString(R.string.server_location));
//		httpPost.addHeader(name, value)
//		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//		httpPost.setEntity(new StringEntity(j.toString(), HTTP.UTF_8));

		// Sends the http post
		HttpResponse httpResponse = httpClient.execute(httpPost);
//		Logger.logDebug(context, "ServerConnect - responsecode=" + httpResponse.getStatusLine().getStatusCode());

		// Stores the session if the LogOnCommand was the caller
//		if (command instanceof LogOnCommand) {
//			session = httpClient.getCookieStore();
//		}

		// Gets the input stream and unpackages the response into a command
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		StringBuffer out = new StringBuffer();
		String line = null;
		while((line = reader.readLine()) != null){
			out.append(line + "\n");
		}
		reader.close();

		
		Log.i("ServerConnect Response Received", out.toString());
		JSONTokener tokener = new JSONTokener(out.toString());
		
		JSONArray array = new JSONArray(tokener);
		
//		Logger.logDebug(context, "ServerConnect - response=" + out.toString());

	//	return command.unpackageJSON(out.toString());
		return array;
	}
	
	
}
