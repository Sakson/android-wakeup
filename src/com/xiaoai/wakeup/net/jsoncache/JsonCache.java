package com.xiaoai.wakeup.net.jsoncache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.xiaoai.wakeup.util.TimeUtil;

public class JsonCache {

	public static final String NO_RECORD = "no_record";

	private static final long LONG_SEC = 1000;
	private static final long LONG_MIN = LONG_SEC * 60;
	private static final long LONG_HOUR = LONG_MIN * 60;
	private static final long LONG_DAY = LONG_HOUR * 24;

	private static JsonCacheDatabaseHelper sHelper;

	private static Map<String, Long> sMapExpireTime;

	public static JsonCacheDatabaseHelper getHelper(Context context) {
		if (sHelper == null) {
			sHelper = new JsonCacheDatabaseHelper(context);
		}
		return sHelper;
	}

	/**
	 * 
	 * @param url
	 * @return 获取过期时间返回-1L�?2L
	 */
	public static long getExpireTime(String url) {
		if (sMapExpireTime == null) {
			sMapExpireTime = new HashMap<String, Long>();
		}

		sMapExpireTime.put(url, setExpireInMinute(1));

		for (Entry<String, Long> data : sMapExpireTime.entrySet()) {
			if (data.equals(url)) {
				return data.getValue();
			}
		}
		return -1L;
	}

	public static void saveJson(Context context, String url, String data,
			long expireTime) {
		long now = new Date().getTime();
		if (expireTime > now) {
			boolean isExist = getHelper(context).selectJsonDataExist(url);
			if (isExist) {
				getHelper(context).updateJsonData(url, data, expireTime);
			} else {
				getHelper(context).insertJsonData(url, data, expireTime);
			}
		}
	}

	public static String getJson(Context context, String url) {
		return getHelper(context).getJsonStr(url);
	}

	public static long setExpireInDay(int day) {
		long now = new Date().getTime();
		return (now + day * LONG_DAY);
	}

	public static long setExpireInHour(int hour) {
		long now = new Date().getTime();
		return (now + hour * LONG_HOUR);
	}

	public static long setExpireInMinute(int min) {
		long now = new Date().getTime();
		return (now + min * LONG_MIN);
	}

	public static long setExpireInSecond(int sec) {
		long now = new Date().getTime();
		return (now + sec * LONG_SEC);
	}

	public static long setEffectiveToday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ltime = sdf.format(cal.getTime());
		long expire = TimeUtil.formatTime(ltime, "yyyy-MM-dd");
		return expire;
	}

}
