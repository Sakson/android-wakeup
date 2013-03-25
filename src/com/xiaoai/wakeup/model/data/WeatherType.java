package com.xiaoai.wakeup.model.data;

public enum WeatherType {

	Fine("晴"),
	Cloudy("多云"),
	Overcast("阴"),
	Shower("阵雨"),
	Thunder("雷阵雨"),
	ThunderHail("雷阵雨伴有冰雹"),
	RainSnow("雨夹雪"),
	LightRain("小雨"),
	ModerateRain("中雨"),
	HeavyRain("大雨"),
	Rainstorm("暴雨"),
	LargeRainstorm("大暴雨"),
	ExtraordinaryRainstorm("特大暴雨"),
	ShowerySnow("阵雪"),
	LightSnow("小雪"),
	ModerateSnow("中雪"),
	HeavySnow("大雪"),
	Snowstorm("暴雪"),
	Fog("雾"),
	FreezingRain("冻雨"),
	Sandstorm("沙尘暴"),
	Light_ModerateRain("小雪-中雪"),
	Moderate_HeavyRain("中雪-大雪"),
	Heavy_Rainstorm("大雪-暴雪"),
	Dust("浮尘"),
	SandBlowing("扬沙"),
	HeavySandstorm("强沙尘暴"),
	Haze("霾");

	public String type;

	private WeatherType(String type) {
		this.type = type;
	}

}
