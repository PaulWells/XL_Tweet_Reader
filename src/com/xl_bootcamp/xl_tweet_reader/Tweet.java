package com.xl_bootcamp.xl_tweet_reader;

import android.graphics.drawable.Drawable;

public class Tweet {
	public String author;
	public String message;
	public String createdAt;
	public String profilePicURL;
	public Drawable profilePicImage;
	
	Tweet(String author, String message, String createdAt, String profilePicURL){
		this.author = author;
		this.message = message;
		this.createdAt = createdAt;
		this.profilePicURL = profilePicURL;
	}
	
	Tweet(){
		
	}
}

