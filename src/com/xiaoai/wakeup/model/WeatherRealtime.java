package com.xiaoai.wakeup.model;

import java.util.Date;

import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;
import com.xiaoai.wakeup.util.TimeUtil;

public class WeatherRealtime {

	private String temperature;

	private String windDirection;

	private String windSpeed;

	private String humidity;

	private String publishTime;

	private City city;

	public WeatherRealtime() {
		super();
	}

	public long getPublishTimeLong() {
		return TimeUtil.formatTime(publishTime, "yyyy-MM-dd HH:mm:ss");
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "WeatherRealtime [temperature=" + temperature
				+ ", windDirection=" + windDirection + ", windSpeed="
				+ windSpeed + ", humidity=" + humidity + ", publishTime="
				+ publishTime + ", city=" + city + "]";
	}

	public static WeatherRealtime generateFromJson(JSONObject object) {
		if (object != null) {
			WeatherRealtime weather = new WeatherRealtime();
			weather.temperature = ParseUtil.parseString(object, "temp");
			weather.windDirection = ParseUtil.parseString(object, "WD");
			weather.windSpeed = ParseUtil.parseString(object, "WS");
			weather.humidity = ParseUtil.parseString(object, "SD");
			weather.city = City.generateFromJson(object);
			String date = TimeUtil.formatTime(new Date().getTime(),
					"yyyy-MM-dd ");
			weather.publishTime = date + ParseUtil.parseString(object, "time")
					+ ":00";
			return weather;
		}
		return null;
	}

}
