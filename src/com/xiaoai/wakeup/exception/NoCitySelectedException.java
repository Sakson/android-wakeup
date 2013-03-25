package com.xiaoai.wakeup.exception;

public class NoCitySelectedException extends AlarmException {

	private static final long serialVersionUID = 943230851889227359L;

	public NoCitySelectedException() {
		super(ExceptionMessage.NO_CITY_SELECTED);
	}

	public NoCitySelectedException(String message) {
		super(message);
	}

}
