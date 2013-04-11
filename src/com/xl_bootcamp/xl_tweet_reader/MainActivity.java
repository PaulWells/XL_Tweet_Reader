package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
	
	protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putParcelableArrayList("TweetList", tweets);
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		if(savedInstanceState.containsKey("TweetList")){
			tweets = savedInstanceState.getParcelableArrayList("TweetList");
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(MainActivity.this, R.layout.tweetlist_item, tweets));
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
	
	private class NetworkCom extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected void onPostExecute(Void result){
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(MainActivity.this, R.layout.tweetlist_item, tweets));
			
			
		}
		
		protected Void doInBackground(Void... arg0){
			String url = "http://search.twitter.com/search.json?q=%23bieber&src=typd";
			NetworkHelper.pull_tweets(url, tweets);

			return null;
			
		}
		
	}
	
	
	
	public boolean startCustomSearch(MenuItem m){
		Intent intent = new Intent(this, Search_Tweet_Activity.class);
		startActivity(intent);
		finish();
		
		return true;
	}
	

}



