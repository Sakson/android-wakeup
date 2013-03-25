package com.xiaoai.wakeup.net.exception;

import com.xiaoai.wakeup.net.ErrorMessage;

/**
 * The Network Connection Exception Wrapper class.
 * 
 * @version 1.0
 * @author xiaoai
 */
public class NetworkException extends Exception {

	private static final long serialVersionUID = 5966481441569640341L;

	private int error;

	public NetworkException() {
		this.error = ErrorMessage.ERROR_DEFAULT;
	}

	public NetworkException(int error) {
		this.error = error;
	}

	public NetworkException(String message) {
		super(message);
		this.error = ErrorMessage.ERROR_DEFAULT;
	}

	public NetworkException(int error, String message) {
		super(message);
		this.error = error;
	}

	public int getError() {
		return error;
	}

}
