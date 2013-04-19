package com.xl_bootcamp.xl_tweet_reader;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable {
	public String author;
	public String message;
	public String createdAt;
	public String profilePicURL;
	public Bitmap profilePicImage;
	
	Tweet(String author, String message, String createdAt, String profilePicURL){
		this.author = author;
		this.message = message;
		this.createdAt = createdAt;
		this.profilePicURL = profilePicURL;
	}
	
	Tweet(Parcel in){
		
		this.author = in.readString();
		this.message = in.readString();
		this.createdAt = in.readString();
		this.profilePicURL = in.readString();
		this.profilePicImage = (Bitmap)in.readParcelable(getClass().getClassLoader());
		
	}
	
	Tweet(){
		author=null;
		message=null;
		createdAt=null;
		profilePicURL=null;
		profilePicImage=null;
	}
	

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeString(author);
		out.writeString(message);
		out.writeString(createdAt);
		out.writeString(profilePicURL);
		profilePicImage.writeToParcel(out,0);
		out.setDataPosition(0);
	}
	
	public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
		public Tweet createFromParcel(Parcel in) {
		    return new Tweet(in);
		}
		
		public Tweet[] newArray(int size) {
		    return new Tweet[size];
		}
	};
}

