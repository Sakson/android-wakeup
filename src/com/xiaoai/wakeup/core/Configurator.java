package com.xiaoai.wakeup.core;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * The {@link Configurator} is used to manager the global configuration.
 */
public class Configurator {

	private static final String EMULATOR_EMEI = "000000000000000";

	private static Configurator sInstance;

	private Context mContext;

	private int mScreenWidth;
	private int mScreenHeight;

	private Configurator(Context context) {
		this.mContext = context;

		// initialize the screen's width and height pixels
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
	}

	public static Configurator getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new Configurator(context);
		}
		return sInstance;
	}

	/**
	 * Get the screen's width pixels.
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		if (mScreenWidth > 0) {
			return mScreenWidth;
		}

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		return mScreenWidth;
	}

	/**
	 * Get the screen's height pixels.
	 * 
	 * @return
	 */
	public int getScreenHeight() {
		if (mScreenHeight > 0) {
			return mScreenHeight;
		}

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		return mScreenHeight;
	}

	/**
	 * Check if this device is emulator.
	 * 
	 * @return
	 */
	public boolean isEmulator() {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.equals(EMULATOR_EMEI)) {
			return true;
		}
		return false;
	}

	/**
	 * Get the devices id (IMEI or MEID code).
	 * 
	 * @return
	 */
	public String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

	/**
	 * Get the mobile phone number.
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		TelephonyManager telManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telManager.getLine1Number();
	}

	/**
	 * Get the SIM operator string.
	 * 
	 * @return
	 */
	public String getSimOperator() {
		TelephonyManager telManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (telManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
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
	public String getSimMNC() {
		String sim = getSimOperator();
		if (TextUtils.isEmpty(sim))
			return null;

		String mnc = sim.substring(3);
		return mnc;
	}

	/**
	 * Get the SIM unique subscribe id.
	 * 
	 * @return
	 */
	public String getSimId() {
		TelephonyManager telManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getSubscriberId();
	}

	/**
	 * Get the SIM serial number.
	 * 
	 * @return
	 */
	public String getSimSerialNumber() {
		TelephonyManager telManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telManager.getSimSerialNumber();
	}

	public void setScreenDimensions(int widthPixels, int heightPixels) {
		// TODO Auto-generated method stub
		
	}

}
