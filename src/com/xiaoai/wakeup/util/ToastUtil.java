package com.xiaoai.wakeup.util;

import android.content.Context;
import android.widget.Toast;

/**
 * The Toast wrapper class.
 */
public final class ToastUtil {

	/**
	 * Toast the long message.
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showLong(Context context, int resId) {
		Toast.makeText(context, TextUtil.getText(context, resId),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast the long message.
	 * 
	 * @param context
	 * @param text
	 */
	public static void showLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast the short message.
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showShort(Context context, int resId) {
		Toast.makeText(context, TextUtil.getText(context, resId),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast the short message.
	 * 
	 * @param context
	 * @param text
	 */
	public static void showShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast the message.
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 *            How long to display the message.
	 */
	public static void show(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * Toast the message.
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 *            How long to display the message.
	 */
	public static void show(Context context, int resId, int duration) {
		Toast.makeText(context, TextUtil.getText(context, resId), duration)
				.show();
	}

}
