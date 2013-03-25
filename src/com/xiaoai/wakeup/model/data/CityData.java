package com.xiaoai.wakeup.model.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;

public class CityData {

	private String cityName;

	private String cityCode;

	public CityData() {
		super();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Override
	public String toString() {
		return "City [cityName=" + cityName + ", cityCode=" + cityCode + "]";
	}

	public static CityData generateFromJson(JSONObject object) {
		if (object != null) {
			CityData city = new CityData();
			city.cityName = ParseUtil.parseString(object, "cityName");
			city.cityCode = ParseUtil.parseString(object, "cityCode");
			return city;
		}
		return null;
	}

	public static List<CityData> generateArrayFromJson(JSONArray array) {
		List<CityData> citys = new ArrayList<CityData>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = ParseUtil.parseJSONObject(array, i);
				CityData city = generateFromJson(object);
				if (city != null) {
					citys.add(city);
				}
			}
		}
		return citys;
	}

}
