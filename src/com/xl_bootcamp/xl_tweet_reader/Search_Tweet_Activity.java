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




import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Search_Tweet_Activity extends Activity {
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search__tweet);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search__tweet_, menu);
		return true;
	}
	

	
	private class NetworkCom extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected void onPostExecute(Void result){
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(Search_Tweet_Activity.this, R.layout.tweetlist_item, tweets));
			
			
		}
		
		protected Void doInBackground(Void... arg0){
			EditText searchBox = (EditText) findViewById(R.id.search_box);
			String searchPhrase = searchBox.getText().toString();
			searchBox.clearFocus();
			
			//remove hashtag if user entered hashtag
			if(searchPhrase.indexOf('#')==0){
				searchPhrase =searchPhrase.substring(1);
			}
			
			String url = "http://search.twitter.com/search.json?q=%23" + searchPhrase + "&src=typd";
			
			NetworkHelper.pull_tweets(url, tweets);
			
			return null;
			
		}
		
	}
	
	
	public void searchForTweets(View v){
		new NetworkCom().execute();
	}

}
