package com.xiaoai.wakeup.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xiaoai.wakeup.util.log.Log;

public class NetworkManager {

	public static final int TYPE_CHINA_MOBILE = 1;
	public static final int TYPE_CHINA_UNICOM = 2;
	public static final int TYPE_CHINA_TELECOM = 3;
	public static final int TYPE_CHINA_TIETONG = 4;
	public static final int TYPE_UNKNOW = 5;

	private static final String MCC_CHINA = "460";

	/**
	 * Check if the network is available.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isAvailable() && wifi.isConnected()) {
			return true;
		}

		NetworkInfo mobile = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobile != null && mobile.isAvailable() && mobile.isConnected()) {
			return true;
		}

		return false;
	}

	/**
	 * Check if the current network operator is CMCC.
	 * 
	 * @return
	 */
	public static boolean isCMCCNetworkOperator(Context context) {
		int type = getNetworkOperatorType(context);
		if (type == TYPE_CHINA_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * Get the network operator type.
	 * 
	 * @return
	 */
	public static int getNetworkOperatorType(Context context) {
		if (context == null) {
			return TYPE_UNKNOW;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (telManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
			Log.w("The SIM card is not ready.");
			return TYPE_UNKNOW;
		}

		String sim = telManager.getSimOperator();
		if (sim.length() != 5) {
			return TYPE_UNKNOW;
		}

		String mcc = sim.substring(0, 3);
		if (!mcc.equals(MCC_CHINA)) {
			return TYPE_UNKNOW;
		}

		String mnc = sim.substring(3);
		if (mnc.equals("00") || mnc.equals("02") || mnc.equals("07")) {
			return TYPE_CHINA_MOBILE;
		}

		if (mnc.equals("01") || mnc.equals("06")) {
			return TYPE_CHINA_UNICOM;
		}

		if (mnc.equals("03") || mnc.equals("05")) {
			return TYPE_CHINA_TELECOM;
		}

		if (mnc.equals("20")) {
			return TYPE_CHINA_TIETONG;
		}

		return TYPE_UNKNOW;
	}

	/**
	 * Get the SIM operator string.
	 * 
	 * @return
	 */
	public static String getSimOperator(Context context) {
		if (context == null) {
			return null;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (telManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
			Log.w("The sim card is not ready");
			return null;
		}

		String sim = telManager.getSimOperator();
		if (sim.length() != 5) {
			return null;
		}

		return sim;
	}

	/**
	 * Get the SIM operator MNC code.
	 * 
	 * @return
	 */
	public static String getSimOperatorMNC(Context context) {
		String sim = getSimOperator(context);
		if (TextUtils.isEmpty(sim)) {
			return null;
		}

		String mnc = sim.substring(3);
		return mnc;
	}

	/**
	 * Get the mobile phone number.
	 * 
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		if (context == null) {
			return null;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getLine1Number();
	}

	/**
	 * Get the unique subscribe id for the SIM.
	 * 
	 * @return
	 */
	public static String getSimId(Context context) {
		if (context == null) {
			return null;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getSubscriberId();
	}

	/**
	 * Get the SIM serial number.
	 * 
	 * @return
	 */
	public static String getSimSerialNumber(Context context) {
		if (context == null) {
			return null;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getSimSerialNumber();
	}

	/**
	 * Get the network type.
	 * 
	 * @return
	 */
	public static int getNetworkType(Context context) {
		if (context == null) {
			return TelephonyManager.NETWORK_TYPE_UNKNOWN;
		}

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getNetworkType();
	}

}
