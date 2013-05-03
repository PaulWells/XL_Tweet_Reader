package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Timer tweetTimer = new Timer();
	TweetTimerTask update = new TweetTimerTask(); //update tweets
	final int delay = 10000; //delay in milliseconds between tweet updates
	long startDelay; //milliseconds before next update when activity was created
	long startTime; //time activity was started
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		if(savedInstanceState == null)
			//start timer for tweet updates
			tweetTimer.scheduleAtFixedRate(update, 0, delay);
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		tweetTimer.cancel();
		tweetTimer.purge();
	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putParcelableArrayList("TweetList", tweets);
		
		long countdown;
		
		//if waiting for delay before timer starts then scheduledExecutionTime is 0
		if(update.scheduledExecutionTime() != 0)
			countdown = startDelay - (new Date().getTime()-update.scheduledExecutionTime());
		else
			countdown = startDelay - (new Date().getTime()-startTime);
		
		//if task is being executed already then reset countdown
		if(countdown < 0)
			countdown = delay;

		savedInstanceState.putLong("Time Left", countdown);
		
		//save position of scroll bar
		ListView listView = (ListView) findViewById(R.id.tweet_list);  
		savedInstanceState.putInt("Scroll Position", listView.getScrollY());
		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
			
		startTime = new Date().getTime();  //set time activity started
		startDelay = savedInstanceState.getLong("Time Left");  //time left until next update
		tweetTimer.scheduleAtFixedRate(update,startDelay, delay);  
		
		tweets = savedInstanceState.getParcelableArrayList("TweetList");
		
		
		//find view that tweets are displayed in and set adapter to "tweets" arraylist
		ListView listView = (ListView) findViewById(R.id.tweet_list);  
		listView.setAdapter(new TweetAdapter(tweets));
		
		//reset scrollbar position
		listView.scrollTo(View.SCROLLBAR_POSITION_DEFAULT, savedInstanceState.getInt("Scroll Position"));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//this async task does the work of fetching the tweets
	private class NetworkCom extends AsyncTask<Void,Void,Void>{
		
		//private WeakReference<Activity> ref;
		
		@Override
		protected void onPostExecute(Void result){
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			TweetAdapter adapter =  (TweetAdapter) listView.getAdapter();
			
			//reset list adapter
			if(adapter == null){
				listView.setAdapter(new TweetAdapter(tweets));
			}
			else
				
				adapter.notifyDataSetChanged();
			
		}
		
		@Override
		protected Void doInBackground(Void... arg0){
			
			startDelay=delay;
			
			String url = "http://search.twitter.com/search.json?q=%23win&src=typd";
			ArrayList<Tweet> copylist = new ArrayList<Tweet>();
			
			//load tweets into copylist from url
			NetworkHelper.pull_tweets(url, copylist);
			
			tweets.clear();
			
			int newTweets = 0;  //number of new tweets
			for(int i = copylist.size()-1; i>= 0;i--){
				tweets.add(newTweets, copylist.get(i));
			}
			
			return null;
			
		}
		
	}
	
	private class TweetTimerTask extends TimerTask {
		
		@Override
		//executes every time timer gets to 0
		public void run(){
			
			new NetworkCom().execute();
			
		}
	
	}

	public boolean startCustomSearch(MenuItem m){
		Intent intent = new Intent(this, Search_Tweet_Activity.class);
		startActivity(intent);
		finish();
		
		return true;
	}
	

}



