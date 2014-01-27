package com.android.project.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	
	public static String parseUnixTimeToDate(long unixTimeStamp) {

		long time = unixTimeStamp * (long) 1000;
		Date date = new Date(time);

		// get only the date from the string - June 31, 1969
		DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		String onlyDate = fmt.format(date);

		

		return onlyDate;

	}
}
