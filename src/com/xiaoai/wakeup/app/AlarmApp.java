package com.xiaoai.wakeup.app;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaoai.wakeup.core.Configurator;
import com.xiaoai.wakeup.exception.NoCitySelectedException;
import com.xiaoai.wakeup.model.data.CityData;
import com.xiaoai.wakeup.net.asynctask.AsyncTaskManager;
import com.xiaoai.wakeup.preferences.Preferences;
import com.xiaoai.wakeup.util.ToastUtil;
import com.xiaoai.wakeup.weather.WeatherManager;

public class AlarmApp extends Application implements UncaughtExceptionHandler {

	public static Configurator sConfigurator;

	public static AlarmApp sInstance;

	public static String sChannelName;

	public static Class<?> sCurrentActivity;

	public static LocationClient sLocationClient = null;
	public static BDLocationListener sLocationListener = new AlarmLocationListener();

	public static CityData sSelectedCity;

	public static WeatherManager sWeatherManager;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		Thread.setDefaultUncaughtExceptionHandler(this);

		sConfigurator = Configurator.getInstance(sInstance);
		sWeatherManager = WeatherManager.getInstance(sInstance);

		Preferences.initializePreferences(sInstance);

		AsyncTaskManager.start();
		startLocateGPS();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
//
//			@Override
//			public void uncaughtException(Thread thread, Throwable ex) {
//				StringBuilder sb = new StringBuilder();
//				sb.append("Version code is ");
//				sb.append(Build.VERSION.SDK_INT + "\t");// �豸��Android�汾��
//				sb.append("Model is ");
//				sb.append(Build.MODEL + "\t");// �豸�ͺ�
//				sb.append(ex.toString() + "\t");
//				StringWriter sw = new StringWriter();
//				PrintWriter pw = new PrintWriter(sw);
//				ex.printStackTrace(pw);
//				sb.append(sw.toString());
//				String message = sb.toString();
////				Log.w(message);
////				ServiceParameters param = new ServiceParameters(Action.FEEDBACK_ADD);
////				param.add("system_version", Build.VERSION.SDK_INT);
////				param.add("feedback_type", "1");
////				param.add("content", message);
////				KokozuService service = new KokozuService(ActivitySub.this, HttpUtil.DEFAULT_HOST, param);
////				KokozuServiceResult result = service.makeRequest();
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//		});
	}

	public static void startLocateGPS() {
		locateGPS();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				removeLocateListener();
			}
		}, 60 * 1000);
	}

	private static void removeLocateListener() {
		if (sLocationClient != null) {
			sLocationClient.stop();
			sLocationClient.unRegisterLocationListener(sLocationListener);
		}
	}

	private static void locateGPS() {
		sLocationClient = new LocationClient(sInstance);
		sLocationClient.registerLocationListener(sLocationListener);

		setLocatePreferences();

		sLocationClient.start();
		if (sLocationClient != null && sLocationClient.isStarted()) {
			sLocationClient.requestLocation();
		} else {
			Log.i("LocSDK3", "locClient is null or not started");
		}
	}

	public static void locateOffline() {
		if (sLocationClient != null && sLocationClient.isStarted()) {
			sLocationClient.requestOfflineLocation();
		}
	}

	private static void setLocatePreferences() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.disableCache(true);
		option.setPoiNumber(5);
		option.setPoiDistance(1000);
		option.setPoiExtraInfo(true);
		sLocationClient.setLocOption(option);
	}

	private static class AlarmLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}

			String cityName = location.getCity();

			ToastUtil.showShort(sInstance, cityName);

			if (!TextUtils.isEmpty(cityName)) {
				sSelectedCity = new CityData();
				sSelectedCity.setCityName(cityName);
				removeLocateListener();

				try {
					sWeatherManager.queryCityWeatherRecent();
				} catch (NoCitySelectedException e) {
					e.printStackTrace();
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	public static void setAppForeground(boolean b) {
		// TODO Auto-generated method stub

	}

	public static void hideSoftInputWindow(Activity activityBase) {
		// TODO Auto-generated method stub

	}

}
