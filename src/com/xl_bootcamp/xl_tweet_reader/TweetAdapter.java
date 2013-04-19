package com.xl_bootcamp.xl_tweet_reader;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet>{
	private ArrayList<Tweet> tweets;
	private Context thisContext;
	
	TweetAdapter(Context thisContext, int layoutID, ArrayList<Tweet> tweetItems){
		super(thisContext,layoutID, tweetItems);
		this.tweets = tweetItems;
		this.thisContext = thisContext;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if (v == null) {
                    LayoutInflater viewInflater = (LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
