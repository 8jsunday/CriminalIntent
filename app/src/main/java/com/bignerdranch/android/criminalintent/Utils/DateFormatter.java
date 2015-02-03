package com.bignerdranch.android.criminalintent.Utils;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by Jermie Domingo on 2/2/2015.
 */
public class DateFormatter {

	public static String format(Date date){
		return DateFormat.format("EEEE - MMM dd, yyyy", date).toString();
	}
}
