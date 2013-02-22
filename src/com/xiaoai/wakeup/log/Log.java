package com.xiaoai.wakeup.log;

public class Log {

	protected static final String TAG = "WakeUp";

	/**
	 * the log disable lock when is true, the log is disabled, or the log is
	 * enabled.
	 */
	private static boolean disable = false;

	public Log() {
		super();
	}

	/**
	 * build the complete message.
	 */
	private static String buildMessage(String message) {
		StackTraceElement ste = new Throwable().fillInStackTrace()
				.getStackTrace()[2];

		StringBuffer sb = new StringBuffer();
		sb.append(ste.getClassName());
		sb.append(".");
		sb.append(ste.getMethodName());
		sb.append("(): ");
		sb.append(message);
		return sb.toString();
	}

	/**
	 * check if the log is disabled.
	 */
	private static boolean isEnabled() {
		return !disable;
	}

	/**
	 * send a VERBOSE log message.
	 */
	public static void v(String msg) {
		if (isEnabled()) {
			android.util.Log.v(TAG, buildMessage(msg));
		}
	}

	/**
	 * send a VERBOSE log message and log the exception.
	 */
	public static void v(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.v(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * send a DEBUG log message.
	 */
	public static void d(String msg) {
		if (isEnabled()) {
			android.util.Log.d(TAG, buildMessage(msg));
		}
	}

	/**
	 * send a DEBUG log message and log the exception.
	 */
	public static void d(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.d(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * send a INFO log message.
	 */
	public static void i(String msg) {
		if (isEnabled()) {
			android.util.Log.i(TAG, buildMessage(msg));
		}
	}

	/**
	 * send a INFO log message and log the exception.
	 */
	public static void i(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.i(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * send a ERROR log message.
	 */
	public static void e(String msg) {
		if (isEnabled()) {
			android.util.Log.e(TAG, buildMessage(msg));
		}
	}

	/**
	 * send a ERROR log message and log the exception.
	 */
	public static void e(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.e(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * send a WARN log message.
	 */
	public static void w(String msg) {
		if (isEnabled()) {
			android.util.Log.w(TAG, buildMessage(msg));
		}
	}

	/**
	 * send a WARN log message and log the exception.
	 */
	public static void w(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.w(TAG, buildMessage(msg), throwable);
		}
	}

}
