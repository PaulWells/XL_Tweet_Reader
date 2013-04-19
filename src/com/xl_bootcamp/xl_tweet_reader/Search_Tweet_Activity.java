package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	
	protected void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putParcelableArrayList("Custom TweetList", tweets);
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		if(savedInstanceState.containsKey("Custom TweetList")){
			tweets = savedInstanceState.getParcelableArrayList("Custom TweetList");
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(this, R.layout.tweetlist_item, tweets));
		}
		
	}
	

	
	private class NetworkCom extends AsyncTask<Void,Void,Integer>{
		
		@Override
		protected void onPostExecute(Integer result){
			
			ListView listView = (ListView) findViewById(R.id.tweet_list);
			listView.setAdapter(new TweetAdapter(Search_Tweet_Activity.this, R.layout.tweetlist_item, tweets));
			
			if(result==0){
				callDialog(Search_Tweet_Activity.this);
			}
		}
		
		protected Integer doInBackground(Void... arg0){
			EditText searchBox = (EditText) findViewById(R.id.search_box);
			String searchPhrase = searchBox.getText().toString();
			
			searchPhrase = cleanHashTag(searchPhrase);
			
			
			String url = "http://search.twitter.com/search.json?q=%23" + searchPhrase + "&src=typd";
			
			Integer success = NetworkHelper.pull_tweets(url, tweets);
			
			
			
			return success;
			
		}
		
	}
	
	//instead of throwing an error message just remove invalid characters from hashtag
	//before searching
	private String cleanHashTag(String tag){
		
	
		
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

}
