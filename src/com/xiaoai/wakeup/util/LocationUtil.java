package com.xiaoai.wakeup.util;

import android.location.Location;

import com.xiaoai.wakeup.util.data.GeoPoint;

public class LocationUtil {

	/**
	 * Get kilometres between two GeoPoints.
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static float getDistanceKilometre(GeoPoint point1, GeoPoint point2) {
		float distance = getDistanceMeter(point1, point2);
		return distance / 1000;
	}

	/**
	 * Get meters between two GeoPoints.
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static float getDistanceMeter(GeoPoint point1, GeoPoint point2) {
		float[] results = new float[1];
		double startLatitude = point1.getLatitudeE6() / 1E6;
		double startLongitude = point1.getLongitudeE6() / 1E6;
		double endLatitude = point2.getLatitudeE6() / 1E6;
		double endLongitude = point2.getLongitudeE6() / 1E6;
		Location.distanceBetween(startLatitude, startLongitude, endLatitude,
				endLongitude, results);
		return results[0];
	}

}
