package com.xiaoai.wakeup.net.exception;

/**
 * The exception will be throwed while the connection response code is not 200.
 */
public class UnexceptedResponseException extends NetworkException {

	private static final long serialVersionUID = 7325269478887865388L;

	public UnexceptedResponseException() {
		super();
	}

	public UnexceptedResponseException(String message) {
		super(message);
	}

}
