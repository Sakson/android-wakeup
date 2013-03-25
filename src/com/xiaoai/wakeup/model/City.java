package com.xiaoai.wakeup.model;

import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;

public class City {

	private String cityCode;

	private String cityName;

	private String cityEn;

	public City() {
		super();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityEn() {
		return cityEn;
	}

	public void setCityEn(String cityEn) {
		this.cityEn = cityEn;
	}

	@Override
	public String toString() {
		return "City [cityCode=" + cityCode + ", cityName=" + cityName
				+ ", cityEn=" + cityEn + "]";
	}

	public static City generateFromJson(JSONObject object) {
		if (object != null) {
			City city = new City();
			city.cityCode = ParseUtil.parseString(object, "cityid");
			city.cityName = ParseUtil.parseString(object, "city");
			city.cityEn = ParseUtil.parseString(object, "city_en");
			return city;
		}
		return null;
	}
}
