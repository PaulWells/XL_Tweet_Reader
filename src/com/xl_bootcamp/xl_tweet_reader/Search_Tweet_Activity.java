package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class Search_Tweet_Activity extends Activity {
	
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	Timer tweetTimer = new Timer();
	TweetTimerTask update = new TweetTimerTask();
	final int delay = 30000; //delay in milliseconds between tweet updates
	long startDelay; //the amount of time left on the clock when activity was created
	long startTime; //time activity was started
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search__tweet);
		
		if(savedInstanceState == null)
			startDelay = delay;
		
	}
	
	protected void onDestroy(){
		super.onDestroy();
		tweetTimer.cancel();
		tweetTimer.purge();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search__tweet_, menu);
		return true;
	}
	
	protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putParcelableArrayList("Custom TweetList", tweets);
		
		long countdown;
		
		
		//if waiting for delay to start task then scheduledExecutionTime is 0
		if(update.scheduledExecutionTime() != 0)
			countdown = startDelay - (new Date().getTime()-update.scheduledExecutionTime());
		else
			countdown = startDelay - (new Date().getTime()-startTime);
		
		//if task is executing then reset countdown
		if(countdown < 0)
			countdown = delay;
		
		savedInstanceState.putLong("Time Left", countdown);
		
		//save text in search bar
		String text = ((EditText) findViewById(R.id.search_box)).getText().toString();
		savedInstanceState.putString("Search Field", text);
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		
		//start timer
		startTime = new Date().getTime();			
		long timeLeft = savedInstanceState.getLong("Time Left");
		startDelay = timeLeft;
		tweetTimer.scheduleAtFixedRate(update,timeLeft, delay);
		
		//restore text in search bar
		String text= savedInstanceState.getString("Search Field");
		((EditText) findViewById(R.id.search_box)).setText(text);
		
		//restore tweets in list
		tweets = savedInstanceState.getParcelableArrayList("Custom TweetList");
		ListView listView = (ListView) findViewById(R.id.custom_tweet_list);
		listView.setAdapter(new TweetAdapter(Search_Tweet_Activity.this, R.layout.tweetlist_item, tweets));	
	}
	

	private class NetworkCom extends AsyncTask<Void,Void,Integer>{
		
		@Override
		protected void onPostExecute(Integer result){
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			TweetAdapter adapter =  (TweetAdapter) listView.getAdapter();
			
			//reset list adapter
			if(adapter == null){
				listView.setAdapter(new TweetAdapter(Search_Tweet_Activity.this, R.layout.tweetlist_item, tweets));
			}
			else
				adapter.notifyDataSetChanged();
			
			//create pop-up box if no tweets found
			if(result==0){
				callDialog(Search_Tweet_Activity.this);
			}
		}
		
		protected Integer doInBackground(Void... arg0){
			
			//get phrase from search bar and remove invalid characters
			EditText searchBox = (EditText) findViewById(R.id.search_box);
			String searchPhrase = searchBox.getText().toString();
			searchPhrase = cleanHashTag(searchPhrase);
			
			String url = "http://search.twitter.com/search.json?q=%23" + searchPhrase + "&src=typd";
			
			ArrayList<Tweet> copylist = new ArrayList<Tweet>();
			
			//retrieve tweets
			int success = NetworkHelper.pull_tweets(url, copylist);
			
			//update arraylist connected to listview
			tweets.clear();
			for(int i = 0; i< copylist.size();i++)
				tweets.add(copylist.get(i));

			return success;
			
		}
		
	}
	
	//instead of throwing an error message just remove invalid characters from hashtag
	//before searching
	private String cleanHashTag(String tag){
		
		tag=tag.toLowerCase(Locale.CANADA);
		
		StringBuffer newBuffer = new StringBuffer(tag);
		
		//iterate through buffer backwards so deleting characters does not change index of remaining letters
		for(int i = tag.length()-1; i >= 0;i--){
			if(!Character.isLetterOrDigit(tag.charAt(i)) && !(tag.charAt(i) == '_'))
				newBuffer.deleteCharAt(i);
		}
		
		return newBuffer.toString();
		
	}
	
	public void searchForTweets(View v){
		update.cancel();
		update = new TweetTimerTask();
		tweetTimer.scheduleAtFixedRate(update, 0, delay);	
		
	}
	
	//creates message box to warn user that no tweets were found for their search
	private void callDialog(Context thisContext){
		AlertDialog noTweets;
		noTweets = new AlertDialog.Builder(thisContext).create();
		noTweets.setTitle("Search Results");
		noTweets.setMessage("No tweets found");
		noTweets.setButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
		
		noTweets.show();
	}
	
	private class TweetTimerTask extends TimerTask {
		
		@Override
		//executes every time timer reaches 0
		public void run(){
			
			new NetworkCom().execute();
			
		}
	
	}

}


