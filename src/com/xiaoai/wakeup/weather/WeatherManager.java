package com.xiaoai.wakeup.weather;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.app.AlarmApp;
import com.xiaoai.wakeup.exception.AlarmException;
import com.xiaoai.wakeup.exception.NoCitySelectedException;
import com.xiaoai.wakeup.model.WeatherRecent;
import com.xiaoai.wakeup.model.data.CityData;
import com.xiaoai.wakeup.model.data.ProvinceData;
import com.xiaoai.wakeup.net.Request;
import com.xiaoai.wakeup.net.ServiceResult;
import com.xiaoai.wakeup.net.asynctask.AsyncServiceTask.IAsyncUpdateListener;
import com.xiaoai.wakeup.util.CollectionUtil;
import com.xiaoai.wakeup.util.ParseUtil;
import com.xiaoai.wakeup.util.TextUtil;

public class WeatherManager {

	private static WeatherManager sInstance;

	public static List<ProvinceData> sProvince;

	private Context mContext;

	private WeatherManager(Context context) {
		this.mContext = context;
	}

	public static WeatherManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new WeatherManager(context);
			JSONObject object = Util.parseCitysInfo(context);
			JSONArray array = ParseUtil.parseJSONArray(object, "data");
			sProvince = ProvinceData.generateArrayFromJson(array);
			Log.e("test", sProvince.toString());
		}
		return sInstance;
	}

	/**
	 * Query city weather base on selected city.
	 * 
	 * @return The city's code which name match the selected city's name.
	 * @throws NoCitySelectedException
	 */
	public String queryCityWeatherRecent() throws NoCitySelectedException {
		String cityCode = obtainCityCode();
		if (cityCode == null) {
			throw new NoCitySelectedException(TextUtil.getString(mContext,
					R.string.status_no_selected_city));
		}

		String url = String.format(Constants.WEATHER_DETAILS, cityCode);
		Request request = new Request(mContext, url, null);
		request.makeAsyncTask(new IAsyncUpdateListener() {

			@Override
			public void updateAsyncResult(int token, ServiceResult result) {
				JSONObject object = ParseUtil.parseJSONObject(result.getJsonObject(), "weatherinfo");
				WeatherRecent weather = WeatherRecent.generateFromJson(object);
				Log.e("test ==== ", weather.toString());
			}
		});
		return cityCode;
	}

	private String obtainCityCode() {
		int provinceSize = CollectionUtil.size(sProvince);
		if (AlarmApp.sSelectedCity != null) {
			for (int i = 0; i < provinceSize; i++) {
				List<CityData> citys = sProvince.get(i).getCitys();
				int citySize = CollectionUtil.size(citys);
				for (int j = 0; j < citySize; j++) {
					CityData city = citys.get(j);
					String cityName = AlarmApp.sSelectedCity.getCityName()
							.replace("市", "");
					if (city.getCityName().equals(cityName)) {
						return city.getCityCode();
					}
				}
			}
		}
		return null;
	}

}
