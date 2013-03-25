package com.xiaoai.wakeup.net.exception;

/**
 * The exception will be throwed while there is no cmwap network connection
 * exist.
 */
public class NoCmwapConnectionException extends NetworkException {

	private static final long serialVersionUID = 437277007942773990L;

	public NoCmwapConnectionException() {
		super();
	}

	public NoCmwapConnectionException(String message) {
		super(message);
	}

}
