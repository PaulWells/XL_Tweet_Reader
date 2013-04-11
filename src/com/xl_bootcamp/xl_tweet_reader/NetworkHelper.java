package com.xl_bootcamp.xl_tweet_reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class NetworkHelper {
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static void pull_tweets(String url,ArrayList<Tweet> tweets){
		
		//create new list of tweets so that the list is cleared on a new search
		//ArrayList<Tweet> newTweets = new ArrayList<Tweet>();
		
		try{
			HttpClient twitterClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			
			HttpResponse response = twitterClient.execute(getRequest);
			
			 if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				 
				 String searchResults = EntityUtils.toString(response.getEntity());
				 JSONObject jsonData = new JSONObject(searchResults);
				 
				 JSONArray jsonTweetArray = jsonData.getJSONArray("results");
				 
				 tweets.clear();
				 
				 for (int i = 0; i < jsonTweetArray.length(); i++) {
	                 JSONObject jsonTweet = jsonTweetArray.getJSONObject(i);
	                 Tweet tweet = new Tweet();
	                 tweet.message = jsonTweet.getString("text");
	                 tweet.author = jsonTweet.getString("from_user");
	                 tweet.createdAt = jsonTweet.getString("created_at");
	                 
	                 tweet.profilePicURL = jsonTweet.getString("profile_image_url_https");
	            
	                 tweet.profilePicImage = getBitmapFromURL(tweet.profilePicURL);
	                
	                 tweets.add(tweet);
				 }
				 
				 
	         }
	
			
		}catch(Exception e){
			Log.e("XL_Tweet_Reader", "Error connecting to Twitter", e);
		}
		
		return;
	}
}
