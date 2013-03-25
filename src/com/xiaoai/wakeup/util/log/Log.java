package com.xiaoai.wakeup.util.log;

/**
 * The log wrapper class.
 */
public final class Log {

	private static final String TAG = "wakeup";

	/**
	 * The log disable lock when is true, or the log is enabled.
	 */
	private static boolean sEnabled = true;

	/**
	 * Build the complete message.
	 * 
	 * @param message
	 * @return
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
	 * Check if the log is disabled.
	 * 
	 * @return
	 */
	private static boolean isEnabled() {
		return sEnabled;
	}

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param msg
	 */
	public static void v(String msg) {
		if (isEnabled()) {
			android.util.Log.v(TAG, buildMessage(msg));
		}
	}

	/**
	 * Send a VERBOSE log message and log the exception.
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void v(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.v(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (isEnabled()) {
			android.util.Log.d(TAG, buildMessage(msg));
		}
	}

	/**
	 * Send a DEBUG log message and log the exception.
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void d(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.d(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * Send a INFO log message.
	 * 
	 * @param msg
	 */
	public static void i(String msg) {
		if (isEnabled()) {
			android.util.Log.i(TAG, buildMessage(msg));
		}
	}

	/**
	 * Send a INFO log message and log the exception.
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void i(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.i(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * Send a WARN log message.
	 * 
	 * @param msg
	 */
	public static void w(String msg) {
		if (isEnabled()) {
			android.util.Log.w(TAG, buildMessage(msg));
		}
	}

	/**
	 * Send a WARN log message and log the exception.
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void w(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.w(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * Send a ERROR log message.
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		if (isEnabled()) {
			android.util.Log.e(TAG, buildMessage(msg));
		}
	}

	/**
	 * Send a ERROR log message and log the exception.
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void e(String msg, Throwable throwable) {
		if (isEnabled()) {
			android.util.Log.e(TAG, buildMessage(msg), throwable);
		}
	}

	/**
	 * Send a INFO log message.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isEnabled()) {
			android.util.Log.e(tag, buildMessage(msg));
		}
	}

}
