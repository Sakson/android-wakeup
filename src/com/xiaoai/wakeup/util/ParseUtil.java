package com.xiaoai.wakeup.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xiaoai.wakeup.util.log.Log;

/**
 * Parse JSON data wrapper class.
 * 
 * @author Administrator
 * 
 */
public class ParseUtil {

	public static JSONObject parseJSONObject(JSONObject object, String key) {
		if (object != null && object.has(key)) {
			try {
				return object.getJSONObject(key);
			} catch (JSONException e) {
				Log.e("parseJSONObject exception, JSONObject: " + object
						+ ", key: " + key);
			}
		}
		return null;
	}

	public static JSONObject parseJSONObject(JSONArray array, int index) {
		if (array != null) {
			try {
				return array.getJSONObject(index);
			} catch (JSONException e) {
				Log.e("parseJSONObject exception, JSONArray: " + array
						+ ", index: " + index);
			}
		}
		return null;
	}

	public static JSONArray parseJSONArray(JSONObject object, String key) {
		if (object != null) {
			try {
				return object.getJSONArray(key);
			} catch (JSONException e) {
				Log.e("parseJSONArray exception, JSONObject: " + object
						+ ", key: " + key);
			}
		}
		return new JSONArray();
	}

	public static String parseString(JSONObject object, String key) {
		if (object != null && object.has(key)) {
			try {
				String value = object.getString(key);
				return (value == null) ? "" : value.trim();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String parseString(JSONArray array, int index) {
		if (array != null) {
			try {
				return array.getString(index);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static int parseInt(JSONObject object, String key) {
		String str = parseString(object, key);
		return NumberUtil.parseInt(str);
	}

	public static long parseLong(JSONObject object, String key) {
		String str = parseString(object, key);
		return NumberUtil.parseLong(str);
	}

	public static double parseDouble(JSONObject object, String key) {
		String str = parseString(object, key);
		return NumberUtil.parseDouble(str);
	}

	public static boolean parseBoolean(JSONObject object, String key) {
		if (object != null && object.has(key)) {
			try {
				boolean value = object.getBoolean(key);
				return value;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
