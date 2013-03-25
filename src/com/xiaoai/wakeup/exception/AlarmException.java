package com.xiaoai.wakeup.exception;

public class AlarmException extends Exception {

	private static final long serialVersionUID = -3148463757293439920L;

	protected int error;

	protected String message;

	public AlarmException() {
		super();
		this.error = ExceptionMessage.DEFAULT;
	}

	public AlarmException(int error) {
		super();
		this.error = error;
	}

	public AlarmException(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "AlarmException [error=" + error + ", message=" + message + "]";
	}

}
