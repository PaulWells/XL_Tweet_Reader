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
import android.widget.EditText;
import android.widget.ListView;



public class MainActivity extends Activity {

	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Timer tweetTimer = new Timer();
	TweetTimerTask update = new TweetTimerTask();
	final int delay = 30000; //delay in milliseconds between tweet updates
	long startDelay; //the amount of time left on the clock when activity was created
	long startTime; //time activity was started
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState == null)
			tweetTimer.scheduleAtFixedRate(update, 0, delay);	
		startDelay = delay;
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		tweetTimer.cancel();
		tweetTimer.purge();
		
		
	}
	
	protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putParcelableArrayList("TweetList", tweets);
		
		long countdown;
		
		//if waiting for delay to start task then scheduledExecutionTime is 0
		if(update.scheduledExecutionTime() != 0)
			countdown = startDelay - (new Date().getTime()-update.scheduledExecutionTime());
		else
			countdown = startDelay - (new Date().getTime()-startTime);
		
		//if task is being executed then reset countdown
		if(countdown < 0)
			countdown = delay;

		savedInstanceState.putLong("Time Left", countdown);
		
		
	
		
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState){
		if(savedInstanceState.containsKey("TweetList")){
			
			startTime = new Date().getTime();
			long timeLeft = savedInstanceState.getLong("Time Left");
			startDelay = timeLeft;
			tweetTimer.scheduleAtFixedRate(update,timeLeft, delay);
			
			
			
			tweets = savedInstanceState.getParcelableArrayList("TweetList");
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.removeAllViewsInLayout();
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
			listView.removeAllViewsInLayout();
			listView.setAdapter(new TweetAdapter(MainActivity.this, R.layout.tweetlist_item, tweets));

			
		}
		
		protected Void doInBackground(Void... arg0){
			startDelay=delay;
			String url = "http://search.twitter.com/search.json?q=%23bieber&src=typd";
			ArrayList<Tweet> copylist = new ArrayList<Tweet>();
			NetworkHelper.pull_tweets(url, copylist);
			
			tweets.clear();
			for(int i = 0; i< copylist.size();i++)
				tweets.add(copylist.get(i));

			return null;
			
		}
		
	}
	
	private class TweetTimerTask extends TimerTask {
		
		@Override
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



