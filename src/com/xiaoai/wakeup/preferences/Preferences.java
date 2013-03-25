package com.xiaoai.wakeup.preferences;

import static com.xiaoai.wakeup.preferences.Constant.*;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.xiaoai.wakeup.model.User;
import com.xiaoai.wakeup.model.data.CityData;

/**
 * Preferences wrapper class.
 * 
 * @author xiaoai
 * @version 1.0
 */
public final class Preferences {

	public static SharedPreferences sSettingsPreference;
	public static SharedPreferences sDevicePreference;
	public static SharedPreferences sUserPreference;
	public static SharedPreferences sLaunchPreference;

	/**
	 * install SharedPreferences.
	 * 
	 * @param context
	 */
	public static void initializePreferences(Context context) {
		if (context == null) {
			return;
		}

		if (Preferences.sSettingsPreference == null) {
			Preferences.sSettingsPreference = context.getSharedPreferences(
					PREFERENCE_SETTINGS, Context.MODE_PRIVATE);
		}

		if (Preferences.sDevicePreference == null) {
			Preferences.sDevicePreference = context.getSharedPreferences(
					PREFERENCE_DEVICE_INFO, Context.MODE_PRIVATE);
		}

		if (Preferences.sUserPreference == null) {
			Preferences.sUserPreference = context.getSharedPreferences(
					PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
		}

		if (Preferences.sLaunchPreference == null) {
			Preferences.sLaunchPreference = context.getSharedPreferences(
					PREFERENCE_LAUNCH_INFO, Context.MODE_PRIVATE);
		}
	}

	/* ======== settings preferences methods begin ======== */

	private static final String DEFAULT_CITY_CODE = "101010100";
	private static final String DEFAULT_CITY_NAME = "北京";

	/**
	 * Get latest selected city data.
	 * 
	 * @return
	 */
	public static CityData getSelectedCity() {
		String cityCode = sSettingsPreference.getString(KEY_CITY_CODE,
				DEFAULT_CITY_CODE);
		String cityName = sSettingsPreference.getString(KEY_CITY_NAME,
				DEFAULT_CITY_NAME);

		CityData city = new CityData();
		city.setCityCode(cityCode);
		city.setCityName(cityName);
		return city;
	}

	/**
	 * Save selected city data to preferences.
	 * 
	 * @param city
	 */
	public static void saveSelectedArea(CityData city) {
		if (city == null) {
			return;
		}
		SharedPreferences.Editor editor = sSettingsPreference.edit();
		editor.putString(KEY_CITY_CODE, city.getCityCode());
		editor.putString(KEY_CITY_NAME, city.getCityName());
		editor.commit();
	}

	public static boolean isPushOn() {
		return sSettingsPreference.getBoolean(KEY_PUSH_STATUS, true);
	}

	/**
	 * Save push state to preferences.
	 * 
	 * @param isPushOn
	 *            if push on.
	 */
	public static void savePushState(boolean isPushOn) {
		SharedPreferences.Editor editor = sSettingsPreference.edit();
		editor.putBoolean(KEY_PUSH_STATUS, isPushOn);
		editor.commit();
	}

	public static boolean isSynchWeibo() {
		return sSettingsPreference.getBoolean(KEY_SYNCH_WEIBO, false);
	}

	/**
	 * Save synch weibo state to preferences.
	 * 
	 * @param isSynch
	 *            if synch weibo.
	 */
	public static void saveSynchWeiboState(boolean isSynch) {
		SharedPreferences.Editor editor = sSettingsPreference.edit();
		editor.putBoolean(KEY_SYNCH_WEIBO, isSynch);
		editor.commit();
	}

	/* ======== settings preferences methods end ======== */

	/* ======== device preferences methods begin ======== */

	private static final int DEFAULT_IN_SAMPLE_SIZE = 1;

	public static int getSavedInSampleSize() {
		int iss = sDevicePreference.getInt(KEY_IN_SAMPLE_SIZE,
				DEFAULT_IN_SAMPLE_SIZE);
		return iss;
	}

	public static int getSavedScreenWidth() {
		return sDevicePreference.getInt(KEY_SCREEN_WIDTH, -2);
	}

	public static int getSavedScreenHeight() {
		return sDevicePreference.getInt(KEY_SCREEN_HEIGHT, -2);
	}

	public static int getSavedStatusBarHeight() {
		return sDevicePreference.getInt(KEY_STATUS_BAR_HEIGHT, 0);
	}

	public static void saveInSampleSize(int iss) {
		SharedPreferences.Editor editor = sDevicePreference.edit();
		editor.putInt(KEY_IN_SAMPLE_SIZE, iss);
		editor.commit();
	}

	public static void saveStatusBarHeight(int height) {
		if (height > 0) {
			SharedPreferences.Editor editor = sDevicePreference.edit();
			editor.putInt(KEY_STATUS_BAR_HEIGHT, height);
			editor.commit();
		}
	}

	public static void saveScreenDimensions(int screenWidth, int screenHeight) {
		if (screenWidth > 0 && screenHeight > 0) {
			SharedPreferences.Editor editor = sDevicePreference.edit();
			editor.putInt(KEY_SCREEN_WIDTH, screenWidth);
			editor.putInt(KEY_SCREEN_HEIGHT, screenHeight);
			editor.commit();
		}
	}

	/* ======== device preferences methods end ======== */

	/* ======== user info preferences methods begin ======== */

	public static User getLatestUser() {
		// KokozuUser user = new KokozuUser();
		// user.setUserId(sUserPreference.getString(KEY_USER_ID, "-1"));
		// user.setNickName(sUserPreference.getString(KEY_USER_NICK_NAME, ""));
		// user.setSessionId(sUserPreference.getString(KEY_USER_SESSION, ""));
		// user.setSite(sUserPreference.getInt(KEY_USER_SITE, -1));
		// return user;
		return null;
	}

	public static void saveUserInfo(User user, int site) {
		// SharedPreferences.Editor editor = sUserPreference.edit();
		// editor.putString(KEY_USER_ID, user.getUserId());
		// editor.putString(KEY_USER_NICK_NAME, user.getNickName());
		// editor.putString(KEY_USER_SESSION, user.getSessionId());
		// editor.putInt(KEY_USER_SITE, site);
		// editor.commit();
	}

	public static void clearSavedUserInfo() {
		SharedPreferences.Editor editor = sUserPreference.edit();
		editor.putString(KEY_USER_ID, "-1");
		editor.putString(KEY_USER_NICK_NAME, "");
		editor.putString(KEY_USER_SESSION, "");
		editor.putInt(KEY_USER_SITE, -1);
		editor.commit();
	}

	/* ======== user info preferences methods end ======== */

	/* ======== launch preferences methods begin ======== */

	public static boolean isAppFirstLaunch() {
		return sLaunchPreference.getBoolean(KEY_APP_FIRST_LAUNCH, true);
	}

	public static void saveAppLaunched() {
		SharedPreferences.Editor editor = sLaunchPreference.edit();
		editor.putBoolean(KEY_APP_FIRST_LAUNCH, false);
		editor.commit();
	}

	public static boolean isFirstLaunch(Class<? extends Activity> activity) {
		return sLaunchPreference.getBoolean(activity.getName(), true);
	}

	public static void saveActivityLaunched(Class<? extends Activity> activity) {
		SharedPreferences.Editor editor = sLaunchPreference.edit();
		editor.putBoolean(activity.getName(), false);
		editor.commit();
	}

	/* ======== launch preferences methods end ======== */

}
