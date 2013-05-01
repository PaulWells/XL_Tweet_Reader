package com.xl_bootcamp.xl_tweet_reader;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends BaseAdapter {
	private ArrayList<Tweet> tweets;
	private GregorianCalendar lastDate;


	TweetAdapter(ArrayList<Tweet> tweetItems) {
		this.tweets = tweetItems;
	}

	int count = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			
			LayoutInflater viewInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = viewInflater.inflate(R.layout.tweetlist_item, parent, false);
			
		}

		Tweet aTweet = getItem(position);
		TextView authorView = (TextView) v.findViewById(R.id.author);
		TextView messageView = (TextView) v.findViewById(R.id.message);
		TextView dateView = (TextView) v.findViewById(R.id.created);
		ImageView profileImage = (ImageView) v.findViewById(R.id.profilepic);

		authorView.setText(aTweet.message);
		messageView.setText(aTweet.author);
		dateView.setText(aTweet.createdAt);
		profileImage.setImageBitmap(aTweet.profilePicImage);
		
		GregorianCalendar thisDate = DateParser.parseCalendar(aTweet.createdAt);
		Log.d("well", "i got here");
		if(lastDate != null){
			
			if(thisDate.after(lastDate) && !thisDate.equals(lastDate)){
				ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
				anim.setDuration(500);
				anim.start();
			}
			
			lastDate = thisDate;
			
			
		}
		else{

			ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
			anim.setDuration(500);
			anim.start();
			
			lastDate = DateParser.parseCalendar(aTweet.createdAt);
		}

		return v;
	}

	@Override
	public int getCount() {
		return tweets.size();
	}

	@Override
	public Tweet getItem(int position) {
		return tweets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
