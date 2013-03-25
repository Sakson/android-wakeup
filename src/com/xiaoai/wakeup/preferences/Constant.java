package com.xiaoai.wakeup.preferences;

interface Constant {

	/* preferences files name */
	String PREFERENCE_SETTINGS = "settings";
	String PREFERENCE_DEVICE_INFO = "device_info";
	String PREFERENCE_USER_INFO = "user_info";
	String PREFERENCE_LAUNCH_INFO = "launch_info";

	/* ====== setting preferences constants ====== */
	// location preferences constants
	String KEY_CITY_CODE = "city_code";
	String KEY_CITY_NAME = "city_name";

	String KEY_PUSH_STATUS = "push";
	String KEY_SYNCH_WEIBO = "synch_weibo";

	/* screen display preferences constants */
	String KEY_IN_SAMPLE_SIZE = "in_simple_size";
	String KEY_SCREEN_WIDTH = "screen_width";
	String KEY_SCREEN_HEIGHT = "screen_height";
	String KEY_STATUS_BAR_HEIGHT = "status_bar_height";

	/* user info preferences constants */
	String KEY_USER_ID = "user_id";
	String KEY_USER_NICK_NAME = "user_name";
	String KEY_USER_SESSION = "user_session";
	String KEY_USER_SITE = "user_site";

	/* launch info preferences constants */
	String KEY_APP_FIRST_LAUNCH = "app_first_launch";

}
