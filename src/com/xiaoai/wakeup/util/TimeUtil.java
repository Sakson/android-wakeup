package com.xiaoai.wakeup.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

public final class TimeUtil {

	/**
	 * Format the time, convert the string time(yyyy-MM-dd HH:mm:ss) to the
	 * long.
	 * 
	 * @param time
	 * @return
	 */
	public static long formatTime(String time) {
		if (TextUtils.isEmpty(time)) {
			return System.currentTimeMillis();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (Exception ex) {
			ex.printStackTrace();
			date = new Date();
		}
		return date.getTime();
	}

	/**
	 * Format the string time base on the given pattern.
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static long formatTime(String time, String pattern) {
		if (TextUtils.isEmpty(time)) {
			return System.currentTimeMillis();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (Exception ex) {
			ex.printStackTrace();
			date = new Date();
		}
		return date.getTime();
	}

	/**
	 * Format the specified time, convert the long time to the string
	 * format(yyyy-MM-dd HH:mm:ss).
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(new Date(time));
		return format;
	}

	/**
	 * Format the specified time, convert the long time to the string format
	 * base on the given pattern.
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formatTime(long time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String format = sdf.format(new Date(time));
		return format;
	}

	/**
	 * 判断指定的时间是否与当前时间为同�?���?	 * 
	 * @param time
	 * @return
	 */
	public static boolean isCurrentYear(long time) {
		Calendar now = Calendar.getInstance();
		int yearNow = now.get(Calendar.YEAR);

		Calendar target = Calendar.getInstance();
		target.setTimeInMillis(time);
		int yearTarget = target.get(Calendar.YEAR);

		return (yearNow == yearTarget);
	}

	/**
	 * 获取时间相对于当前时间的日期�?	 * 
	 * @param time
	 * @return 返回格式为： "昨天", "今天", "明天", "后天"�?	 */
	public static String getDateRelativeToday(long time) {
		Calendar now = Calendar.getInstance();
		int yearNow = now.get(Calendar.YEAR);
		int monthNow = now.get(Calendar.MONTH);
		int dateNow = now.get(Calendar.DAY_OF_MONTH);

		Calendar target = Calendar.getInstance();
		target.setTimeInMillis(time);
		int yearTarget = target.get(Calendar.YEAR);
		int monthTarget = target.get(Calendar.MONTH);
		int dateTarget = target.get(Calendar.DAY_OF_MONTH);

		if (yearNow == yearTarget && monthNow == monthTarget) {
			int day = dateTarget - dateNow;
			if (day == 0) {
				return "今天";
			} else if (day == 1) {
				return "明天";
			} else if (day == 2) {
				return "后天";
			} else if (day == -1) {
				return "昨天";
			}
		}
		return "";
	}

}
