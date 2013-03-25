package com.xiaoai.wakeup.util;

import java.text.DecimalFormat;

public class NumberUtil {

	public static int parseInt(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		try {
			int result;
			if (isInteger(value)) {
				result = Integer.parseInt(value.trim());
			} else {
				result = (int) (parseDouble(value) + 0.5);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long parseLong(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		try {
			long result = Long.parseLong(value.trim());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static float parseFloat(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		try {
			float result = Float.parseFloat(value);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static double parseDouble(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}
		try {
			double result = Double.parseDouble(value.trim());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String formatDouble(double value, String pattern) {
		try {
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	public static String formatDouble(double value, int scale) {
		try {
			String pattern = "0";
			if (scale > 0) {
				StringBuilder sb = new StringBuilder(".");
				for (int i = 0; i < scale; i++) {
					sb.append("0");
				}
				pattern += sb.toString();
			}
			double val = Math.ceil(value);
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	public static boolean isInteger(double value) {
		return isInteger(String.valueOf(value));
	}

	public static boolean isInteger(String value) {
		int index = value.indexOf(".");
		if (index == -1) {
			return true;
		} else {
			String sub = value.substring(index + 1);
			if (sub.matches("[0]*")) {
				return true;
			}
		}
		return false;
	}

}
