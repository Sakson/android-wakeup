package com.xiaoai.wakeup.net;

/**
 * The http util.
 */
public final class HttpUtil {

	public static final String DEFAULT_SERVICE = "http://...";

	/**
	 * Check if the url is full url, likes http://www.xxx.com.
	 */
	public static boolean isFullUrl(String url) {
		if (url == null) {
			return false;
		}
		if (url.toLowerCase().startsWith("http://")) {
			return true;
		}
		if (url.toLowerCase().startsWith("https://")) {
			return true;
		}
		return false;
	}

	/**
	 * Get the host from the specified url.
	 */
	public static String getHostFromUrl(String url) {
		int index = -1;
		if (url.toLowerCase().startsWith("http://")) {
			index = url.indexOf("/", url.indexOf("http://") + 8);
		}
		if (url.toLowerCase().startsWith("https://")) {
			index = url.indexOf("/", url.indexOf("https://") + 9);
		}
		if (index == -1) {
			return "";
		}
		String host = url.substring(0, index);
		return host;
	}

	/**
	 * Get the path from the specified url.
	 */
	public static String getPathFromUrl(String url) {
		int uriPos = -1;
		if (url.toLowerCase().startsWith("http://")) {
			uriPos = url.indexOf("/", url.indexOf("http://") + 8);
		}
		if (url.toLowerCase().startsWith("https://")) {
			uriPos = url.indexOf("/", url.indexOf("https://") + 9);
		}
		if (uriPos == -1) {
			return url;
		}
		String uri = url.substring(uriPos);
		return uri;
	}

}
