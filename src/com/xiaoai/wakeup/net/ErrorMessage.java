package com.xiaoai.wakeup.net;

/**
 * The error message type definition.
 */
public final class ErrorMessage {

	/** The default error. */
	public static final int ERROR_DEFAULT = 10;

	/** Invalid URL. */
	public static final int ERROR_INVALID_URL = 11;

	/** The WIFI is not open status while the user choose WIFI only. */
	public static final int ERROR_WIFI_UNAVAILABLE = 12;

	/** The user has closed the network switcher. */
	public static final int ERROR_NETWORK_SWITCHER_CLOSED = 13;

	/** There is no available network. */
	public static final int ERROR_NO_AVAILABLE_NETWORK = 14;

	/** The response code is not excepted. */
	public static final int ERROR_EXCEPTED_RESPONSE_CODE = 15;

	/** Cannot get the phone number when send request in CMNET/WIFI. */
	public static final int ERROR_NO_PHONE_NUMBER = 16;

	/** The CMWAP is not available. */
	public static final int ERROR_NO_CMWAP_NETWORK = 17;

	/** Parse JSON data error. */
	public static final int ERROR_PARSE_JSON_ERROR = 18;

	/** The default network error. */
	public static final int ERROR_NETWORK_DEFAULT = 19;

}
