package com.xiaoai.wakeup.weather;

interface Constants {

	String WEATHER_SERVICE = "http://m.weather.com.cn/data/";

	String WEATHER_DETAILS = WEATHER_SERVICE + "%1$s" + ".html";

	String WEATHER_REAL_TIME = WEATHER_SERVICE + "sk/" + "%1$s" + ".html";

}
