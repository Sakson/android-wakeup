package com.xiaoai.wakeup.net;

import com.xiaoai.wakeup.net.exception.NetworkConnectionException;

/**
 * The kokozu service interface.
 */
public interface IServiceBase {

	int STATUS_NETWORK_UNAVAILALBE = 400;
	int STATUS_RESULT_CODE_NOT_200 = -200;
	int STATUS_RESULT_TIME_OUT = -203;
	int STATUS_EXCEPTION = -201;
	int STATUS_EMPTY_RESULT = -202;

	String METHOD_GET = "GET";
	String METHOD_POST = "POST";

	/**
	 * Make the kokozu service request
	 */
	ServiceResult makeRequest() throws NetworkConnectionException;

}
