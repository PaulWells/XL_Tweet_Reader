package com.xl_bootcamp.xl_tweet_reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
	
	static int TIMEZONE_HOUR_DIFFERENCE = 3;
	
	//convert a url into a bitmap
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
	
	//puts tweets from url into arraylist tweets.  return a 1 if successful 0 otherwise
	public static int pull_tweets(String url,ArrayList<Tweet> tweets){
		
		
		try{
			Log.d("get to?", "pull_tweets");
			HttpClient twitterClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			
			HttpResponse response = twitterClient.execute(getRequest);
			
			//if request was valid
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				
				//convert results to json array
				String searchResults = EntityUtils.toString(response.getEntity());
				
				JSONObject jsonData = new JSONObject(searchResults);
				JSONArray jsonTweetArray = jsonData.getJSONArray("results");

				//don't parse if array empty
				if(jsonTweetArray.length()==0)
					return 0;
				
				GregorianCalendar tweetDate = new GregorianCalendar();
				
				//parse data from JSON array, make into tweet objects and put into arraylist tweets
				for (int i = 0; i < jsonTweetArray.length(); i++) {
					
					
	                JSONObject jsonTweet = jsonTweetArray.getJSONObject(i);
	                
	                tweetDate = DateParser.parseCalendar(jsonTweet.getString("created_at"));
	                tweetDate.add(Calendar.HOUR,TIMEZONE_HOUR_DIFFERENCE);
	                
	                Calendar today = new GregorianCalendar();
	                SimpleDateFormat dateFormat = new SimpleDateFormat();
	                dateFormat.applyPattern("kk:mm:ss - E MMM dd, yyyy");
	                System.out.println(dateFormat.format(today.getTime()));
	                
	                Tweet tweet = new Tweet();
	                tweet.message = jsonTweet.getString("text").replace("&amp;", "&");
	                tweet.author = jsonTweet.getString("from_user");
	                Log.e("tag", "before");
	                tweet.createdAt = dateFormat.format(today.getTime());
	                Log.e("tag", "after");
	                tweet.profilePicURL = jsonTweet.getString("profile_image_url_https");
	                tweet.profilePicImage = getBitmapFromURL(tweet.profilePicURL);
	                
	                tweets.add(tweet);
				}
				 
				 
	        }
	
			
		}catch(Exception e){
			Log.e("XL_Tweet_Reader", "Error connecting to Twitter", e);
			return 0;
		}
		
		return 1;
	}
	
}
