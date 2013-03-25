package com.xiaoai.wakeup.weather;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xiaoai.wakeup.util.Utility;

public class Util {

	public static JSONObject parseCitysInfo(Context context) {
		InputStream is = null;
		try {
			is = context.getAssets().open("citys");
			byte[] temp = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while (is.read(temp) > 0) {
				sb.append(new String(temp, "utf-8"));
			}
			return new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			Utility.close(is);
		}
		return null;
	}

}
