package com.xl_bootcamp.xl_tweet_reader;

import java.util.GregorianCalendar;
import java.util.HashMap;

import android.util.Log;

public class DateParser {
	public static GregorianCalendar parseCalendar(String dateString){
		
		//Wed, 01 May 2013 03:51:09 +0000
		HashMap<String, Integer> monthToInt = new HashMap<String,Integer>();
		monthToInt.put("January", 1);
		monthToInt.put("February", 2);
		monthToInt.put("March", 3);
		monthToInt.put("April", 4);
		monthToInt.put("May", 5);
		monthToInt.put("June", 6);
		monthToInt.put("July", 7);
		monthToInt.put("August", 8);
		monthToInt.put("September", 9);
		monthToInt.put("October", 10);
		monthToInt.put("November", 11);
		monthToInt.put("December", 12);
		
		int year, month, day, hour, minute, second;
		
		//use last space as a reference and use relative position (which is consistent) to find second,minute,hour and year
		int lastSpaceIndex = dateString.lastIndexOf(' ');
		second = Integer.parseInt(dateString.substring(lastSpaceIndex-2,lastSpaceIndex));
		minute = Integer.parseInt(dateString.substring(lastSpaceIndex-5,lastSpaceIndex-3));
		hour = Integer.parseInt(dateString.substring(lastSpaceIndex-8,lastSpaceIndex-6));
		year = Integer.parseInt(dateString.substring(lastSpaceIndex-13,lastSpaceIndex-9));
		
		
		day = Integer.parseInt(dateString.substring(5,7));
		
		month = monthToInt.get(dateString.substring(8,11));
		
		return new GregorianCalendar(year,month,day,hour,minute,second);
		
	}
	
	
}

