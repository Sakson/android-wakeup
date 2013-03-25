package com.xiaoai.wakeup.model.data;

/**
 * The weather of one day.
 * 
 * @author xiaoai
 * 
 */
public class WeatherDay {

	private String weatherDaytime; // the weather of daytime

	private String weatherNight; // the weather of night

	public WeatherDay() {
		super();
	}

	public String getWeatherDaytime() {
		return weatherDaytime;
	}

	public void setWeatherDaytime(String weatherDaytime) {
		this.weatherDaytime = weatherDaytime;
	}

	public String getWeatherNight() {
		return weatherNight;
	}

	public void setWeatherNight(String weatherNight) {
		this.weatherNight = weatherNight;
	}

	public String getWeatherDay() {
		StringBuilder sb = new StringBuilder();
		if (weatherDaytime != null) {
			sb.append(weatherDaytime);
		}
		if (weatherNight != null && !weatherNight.equals(weatherDaytime)) {
			if (weatherDaytime != null) {
				sb.append("转");
			}
			sb.append(weatherNight);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "WeatherDay [weatherDaytime=" + weatherDaytime
				+ ", weatherNight=" + weatherNight + ",weatherDay="
				+ getWeatherDay() + "]";
	}

}
