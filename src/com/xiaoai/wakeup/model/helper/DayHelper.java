package com.xiaoai.wakeup.model.helper;

import java.util.Calendar;

public class DayHelper {

	public static boolean isDaytime() {
		Calendar cal = Calendar.getInstance();
		int hour24 = cal.get(Calendar.HOUR_OF_DAY);
		return hour24 >= 8 && hour24 < 20;
	}

}
