package com.xiaoai.wakeup.model.data;

import com.xiaoai.wakeup.util.NumberUtil;

public class TemperatureDay {

	private Integer tempDaytime;

	private Integer tempNight;

	public TemperatureDay() {
		super();
	}

	public void setTempDaytime(String tempDaytime) {
		if (tempDaytime == null || tempDaytime.length() == 0) {
			this.tempDaytime = null;
			return;
		}
		String temp = tempDaytime.replaceAll("\\D+", "");
		this.tempDaytime = NumberUtil.parseInt(temp);
	}

	public void setTempNight(String tempNight) {
		if (tempNight == null || tempNight.length() == 0) {
			this.tempNight = null;
			return;
		}
		String temp = tempNight.replaceAll("\\D+", "");
		this.tempNight = NumberUtil.parseInt(temp);
	}

	public int getTempDaytime() {
		return tempDaytime;
	}

	public void setTempDaytime(int tempDaytime) {
		this.tempDaytime = tempDaytime;
	}

	public int getTempNight() {
		return tempNight;
	}

	public void setTempNight(int tempNight) {
		this.tempNight = tempNight;
	}

	@Override
	public String toString() {
		return "TemperatureDay [tempDaytime=" + tempDaytime + ", tempNight="
				+ tempNight + "]";
	}

}
