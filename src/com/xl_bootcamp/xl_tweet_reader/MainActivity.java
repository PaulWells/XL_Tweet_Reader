package com.xl_bootcamp.xl_tweet_reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState==null){
			new NetworkCom().execute();
		}
				
	}
	/*
	protected void onSaveInstanceState(Bundle savedInstanceState){
		
	}
	*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}




	private class TweetAdapter extends ArrayAdapter<Tweet>{
		private ArrayList<Tweet> tweets;
		
		TweetAdapter(Context thisContext, int layoutID, ArrayList<Tweet> tweetItems){
			super(thisContext,layoutID, tweetItems);
			this.tweets = tweetItems;
		}
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            
	            if (v == null) {
	                    LayoutInflater viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = viewInflater.inflate(R.layout.tweetlist_item, null);
	            }
	            Tweet aTweet = tweets.get(position);
	            TextView authorView = (TextView) v.findViewById(R.id.author);
	            TextView messageView = (TextView) v.findViewById(R.id.message);
	            TextView dateView = (TextView) v.findViewById(R.id.created);
	            ImageView profileImage = (ImageView) v.findViewById(R.id.profilepic);
	            
	            authorView.setText(aTweet.message);
	            messageView.setText(aTweet.author);
	            dateView.setText(aTweet.createdAt);
	            profileImage.setImageBitmap(aTweet.profilePicImage);
	            
	            return v;
				
	    }
		
	}
	
	private class NetworkCom extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected void onPostExecute(Void result){
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(MainActivity.this, R.layout.tweetlist_item, tweets));
			
			
		}
		
		protected Void doInBackground(Void... arg0){
			String url = "http://search.twitter.com/search.json?q=%23bieber&src=typd";
			
			try{
				HttpClient twitterClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(url);
				
				HttpResponse response = twitterClient.execute(getRequest);
				
				 if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					 
					 String searchResults = EntityUtils.toString(response.getEntity());
					 JSONObject jsonData = new JSONObject(searchResults);
					 
					 JSONArray jsonTweetArray = jsonData.getJSONArray("results");
					 
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

			return null;
			
		}
		
	}
	
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
	
	Drawable drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException 
	{
		
	   return Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), "blank");
	}
	
	

}



