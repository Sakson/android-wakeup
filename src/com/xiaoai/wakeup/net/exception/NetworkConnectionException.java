package com.xiaoai.wakeup.net.exception;

/**
 * The exception will be throwed while there is no network connection exist.
 */
public class NetworkConnectionException extends NetworkException {

	private static final long serialVersionUID = 1768197780132776933L;

	public NetworkConnectionException() {
		super();
	}

	public NetworkConnectionException(String message) {
		super(message);
	}

}
