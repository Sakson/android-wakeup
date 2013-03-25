package com.xiaoai.wakeup.util;

import com.xiaoai.wakeup.util.log.Log;

import android.content.Context;

public class TextUtil {

	/**
	 * Get string from the specified resource id.
	 * 
	 * @param context
	 * @param resId
	 * @return if occur exception return "".
	 */
	public static String getString(Context context, int resId) {
		try {
			return context.getString(resId);
		} catch (Exception e) {
			Log.e("exception: " + e.getMessage());
		}
		return "";
	}

	/**
	 * Get CharSequence from the specified resource id.
	 * 
	 * @param context
	 * @param resId
	 * @return if occur exception return "".
	 */
	public static CharSequence getText(Context context, int resId) {
		try {
			return context.getText(resId);
		} catch (Exception e) {
			Log.e("exception: " + e.getMessage());
		}
		return "";
	}

	/**
	 * Get CharSequence array from the specified resource id.
	 * 
	 * @param context
	 * @param resId
	 * @return if occur exception return a empty array.
	 */
	public static CharSequence[] getTextArray(Context context, int resId) {
		try {
			context.getResources().getTextArray(resId);
		} catch (Exception e) {
			Log.e("KokozuApp.getTextArray exception: " + e.getMessage());
		}
		return new CharSequence[] {};
	}

	public static String getSubString(String src, String begin, String finish) {
		int start = src.indexOf(begin);

		if (finish == null || "".equals(finish)) {
			if (start >= 0) {
				String result = src.substring(start + begin.length());
				return result;
			}
		}

		int end = src.lastIndexOf(finish);
		if (end <= 0) {
			end = src.length();
		}

		if (start >= 0 && (start + begin.length()) < end) {
			String result = src.substring((start + begin.length()), end);
			return result;
		}
		return "";
	}

	public String toFirstLetterUpperCase(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		if (str.length() == 1) {
			return str.toUpperCase();
		}
		String firstLetter = str.substring(0, 1).toUpperCase();
		return firstLetter + str.substring(1, str.length());
	}

}
