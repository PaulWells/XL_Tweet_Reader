package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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
			
			searchPhrase = cleanHashTag(searchPhrase);
			
			//remove hashtag if user entered hashtag
			if(searchPhrase.indexOf('#')==0){
				searchPhrase =searchPhrase.substring(1);
			}
			
			String url = "http://search.twitter.com/search.json?q=%23" + searchPhrase + "&src=typd";
			
			NetworkHelper.pull_tweets(url, tweets);
			
			return null;
			
		}
		
	}
	
	//instead of throwing an error message just remove invalid characters from hashtag
	//before searching
	private String cleanHashTag(String tag){
		//remove hashtag if user entered hashtag
		if(tag.indexOf('#')==0){
			tag = tag.substring(1);
		}
		
		StringBuffer newBuffer = new StringBuffer(tag);
		
		for(int i = tag.length()-1; i >= 0;i--){
			if(!Character.isLetterOrDigit(tag.charAt(i)) && !(tag.charAt(i) == '_'))
				newBuffer.deleteCharAt(i);
		}
		
		return newBuffer.toString();
		
	}
	
	public void searchForTweets(View v){
		new NetworkCom().execute();
	}

}
