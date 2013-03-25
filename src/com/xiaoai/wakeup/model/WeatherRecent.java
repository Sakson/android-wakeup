package com.xiaoai.wakeup.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import com.xiaoai.wakeup.model.data.LivingIndex;
import com.xiaoai.wakeup.model.data.TemperatureDay;
import com.xiaoai.wakeup.model.data.WeatherDay;
import com.xiaoai.wakeup.util.NumberUtil;
import com.xiaoai.wakeup.util.ParseUtil;
import com.xiaoai.wakeup.util.TimeUtil;

public class WeatherRecent {

	private String week; // the week of today

	private String date; // the date of today(yyyy-MM-dd)

	private String publishTime; // the weather publish time(yyyy-MM-dd HH:mm:ss)

	/** the centigrade temperature of seven day from now */
	private TemperatureDay[] temps = new TemperatureDay[] {};

	/** the weather info of seven day from now */
	private WeatherDay[] weathers = new WeatherDay[] {};

	private String[] winds = new String[] {};

	private String windDay0; // the wind info of today

	private String windDay1; // the wind info of tomorrow

	private String windDay2; // the wind info of the day after tomorrow

	private String windDay3; // the wind info of three days from now

	private String windDay4; // the wind info of four days from now

	private String windDay5; // the wind info of five days from now

	private LivingIndex livingIndex; // the living indexs

	private City city; // the city data

	public long getPublishTimeLong() {
		return TimeUtil.formatTime(publishTime, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String toString() {
		return "WeatherRecent [week=" + week + ", date=" + date
				+ ", publishTime=" + publishTime + ", temps="
				+ Arrays.toString(temps) + ", weathers="
				+ Arrays.toString(weathers) + ", winds="
				+ Arrays.toString(winds) + ", windDay0=" + windDay0
				+ ", windDay1=" + windDay1 + ", windDay2=" + windDay2
				+ ", windDay3=" + windDay3 + ", windDay4=" + windDay4
				+ ", windDay5=" + windDay5 + ", livingIndexs=" + livingIndex
				+ ", city=" + city + "]";
	}

	public static WeatherRecent generateFromJson(JSONObject object) {
		if (object != null) {
			WeatherRecent weather = new WeatherRecent();
			weather.week = ParseUtil.parseString(object, "week");

			weather.date = TimeUtil.formatTime(new Date().getTime(), "yyyy-MM-dd");
//			String dateY = ParseUtil.parseString(object, "date_y");
//			weather.date = TimeUtil.formatTime(
//					TimeUtil.formatTime(dateY, "yyyy年M月d日"), "yyyy-MM-dd");

			String fchh = ParseUtil.parseString(object, "fchh");
			weather.publishTime = weather.date + " " + fchh + ":00:00";

			StringBuffer sb = new StringBuffer();
			for (int i = 1; i <= 6; i++) {
				String key = "temp" + i;
				sb.append(ParseUtil.parseString(object, key));
				if (i != 6) {
					sb.append("~");
				}
			}
			weather.temps = generateTempInfo(fchh, sb.toString());

			String[] weathers = new String[12];
			for (int i = 0; i < 12; i++) {
				String key = "img_title" + (i + 1);
				weathers[i] = ParseUtil.parseString(object, key);
			}
			weather.weathers = generateWeatherInfo(fchh, weathers);

			weather.windDay0 = ParseUtil.parseString(object, "wind1");
			weather.windDay1 = ParseUtil.parseString(object, "wind2");
			weather.windDay2 = ParseUtil.parseString(object, "wind3");
			weather.windDay3 = ParseUtil.parseString(object, "wind4");
			weather.windDay4 = ParseUtil.parseString(object, "wind5");
			weather.windDay5 = ParseUtil.parseString(object, "wind6");

			weather.livingIndex = LivingIndex.generateFromJson(object);
			if (isTomorrowData(fchh)) {
				weather.livingIndex.setDate(getTomorrowDate());
			} else {
				weather.livingIndex.setDate(weather.date);
			}

			return weather;
		}
		return null;
	}

	private static String getTomorrowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return TimeUtil.formatTime(cal.getTimeInMillis(), "yyyy-MM-dd");
	}

	private static boolean isTomorrowData(String publishTime) {
		int hour = NumberUtil.parseInt(publishTime);
		return hour > 12;
	}

	private static WeatherDay[] generateWeatherInfo(String publishTime,
			String[] weathers) {
		int length = 12;
		if (isTomorrowData(publishTime)) {
			length++;
		}

		String[] temp = new String[length];
		int len = weathers.length;
		int index = length;
		for (int i = len - 1; i >= 0; i--) {
			temp[--index] = weathers[i];
		}

		WeatherDay[] weatherDays = new WeatherDay[7];
		int ind = 0;
		for (int i = 0; i < length;) {
			if (ind >= 7) {
				break;
			}
			WeatherDay w = new WeatherDay();
			w.setWeatherDaytime(temp[i++]);
			if (i < length) {
				w.setWeatherNight(temp[i++]);
			}
			weatherDays[ind++] = w;
		}
		return weatherDays;
	}

	private static TemperatureDay[] generateTempInfo(String publishTime,
			String temps) {
		int length = 6 * 2;
		if (isTomorrowData(publishTime)) {
			length++;
		}

		String[] temp = new String[length];
		String[] arrTemp = temps.split("~");
		int len = arrTemp.length;
		int index = length;
		for (int i = len - 1; i >= 0; i--) {
			temp[--index] = arrTemp[i];
		}

		TemperatureDay[] tempDays = new TemperatureDay[7];
		int ind = 0;
		for (int i = 0; i < length;) {
			if (ind >= 7) {
				break;
			}
			TemperatureDay t = new TemperatureDay();
			t.setTempDaytime(temp[i++]);
			if (i < length) {
				t.setTempNight(temp[i++]);
			}
			tempDays[ind++] = t;
		}
		return tempDays;
	}
	// {
	// "weatherinfo":{
	// "weather6":"多云",
	// "weather5":"阴",
	// "weather4":"多云",
	//
	// "img_single":"7",
	//
	// "img2":"99",
	// "img1":"7",
	//
	// "img_title10":"阴",
	// "img_title11":"多云",
	// "img_title12":"多云",
	//
	//
	// "img_title_single":"小雨",
	//
	// "img9":"2",
	// "img7":"1",
	// "img8":"99",
	// "img5":"0",
	// "img6":"99",
	// "img3":"1",
	// "img4":"0",
	// "fx1":"微风",
	// "st5":"9",
	// "st6":"0",
	// "st3":"8",
	// "date":"",
	// "st4":"-2",
	// "st1":"9",
	// "st2":"5",
	//
	//
	// "fl1":"小于3级",
	// "fl5":"小于3级",
	// "fl4":"小于3级",
	//
	// "fl3":"小于3级",
	//
	// "fl2":"小于3级",
	//
	// "img_title7":"多云",
	// "img12":"99",
	// "img_title6":"晴",
	// "fl6":"小于3级转3-4级",
	// "img_title5":"晴",
	// "img_title4":"晴",
	// "fchh":"18",
	// "img_title9":"阴",
	// "img10":"99",
	// "img_title8":"多云",
	// "img11":"1",
	// "fx2":"微风",
	//
	//
	//
	// "weather1":"小雨",
	//
	//
	// "weather2":"多云转晴",
	//
	// "weather3":"晴",
	//
	// "img_title3":"多云",
	//
	// "img_title2":"小雨",
	// "img_title1":"小雨",
	//
	// }
	// }
}
