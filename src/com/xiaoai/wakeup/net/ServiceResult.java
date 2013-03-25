package com.xiaoai.wakeup.net;

import org.json.JSONObject;

import com.xiaoai.wakeup.util.ParseUtil;

public class ServiceResult {

	private int mStatus = -1;

	private String mError = "";

	private String mAction = "";

	private String mData = "";

	private JSONObject mJsonObject;

	/**
	 * the method is hidden, use makeResult or makeBadNetworkResult to get
	 * result instance
	 * 
	 * @param object
	 */
	protected ServiceResult(JSONObject object) {
		if (object != null) {
			mJsonObject = object;
			mStatus = ParseUtil.parseInt(object, "status");
			mError = ParseUtil.parseString(object, "error");
			mAction = ParseUtil.parseString(object, "action");
		}
	}

	public void setJsonData(JSONObject object) {
		if (object != null) {
			mJsonObject = object;
			mStatus = ParseUtil.parseInt(object, "status");
			mError = ParseUtil.parseString(object, "error");
			mAction = ParseUtil.parseString(object, "action");
		}
	}

	public static ServiceResult makeResult(String data) {
		ServiceResult result = new ServiceResult(null);
		result.setData(data);
		return result;
	}

	public static ServiceResult makeResult(JSONObject object) {
		return new ServiceResult(object);
	}

	public static ServiceResult makeNetworkUnavailableResult() {
		ServiceResult result = new ServiceResult(null);
		result.mStatus = IServiceBase.STATUS_NETWORK_UNAVAILALBE;
		result.mError = "network unavailable";
		return result;
	}

	public static ServiceResult makeResultCodeNot200Result() {
		ServiceResult result = new ServiceResult(null);
		result.mStatus = IServiceBase.STATUS_RESULT_CODE_NOT_200;
		result.mError = "result code is not 200";
		return result;
	}

	public static ServiceResult makeTimeoutResult() {
		ServiceResult result = new ServiceResult(null);
		result.mStatus = IServiceBase.STATUS_RESULT_TIME_OUT;
		result.mError = "request timeout";
		return result;
	}

	public static ServiceResult makeExceptionResult(Exception e) {
		ServiceResult result = new ServiceResult(null);
		result.mStatus = IServiceBase.STATUS_EXCEPTION;
		result.mError = "exception: " + e.getMessage();
		return result;
	}

	public static ServiceResult makeEmptyResult() {
		ServiceResult result = new ServiceResult(null);
		result.mStatus = IServiceBase.STATUS_EMPTY_RESULT;
		result.mError = "empty result ";
		return result;
	}

	public int getStatus() {
		return mStatus;
	}

	public String getError() {
		return mError;
	}

	public String getAction() {
		return mAction;
	}

	public JSONObject getJsonObject() {
		if (mJsonObject == null) {
			return new JSONObject();
		}
		return mJsonObject;
	}

	public String getData() {
		return mData;
	}

	public void setData(String data) {
		this.mData = data;
	}

	@Override
	public String toString() {
		return "KokozuServiceResult [mStatus=" + mStatus + ", mError=" + mError
				+ ", mAction=" + mAction + ", mData=" + mData
				+ ", mJsonObject=" + mJsonObject + "]";
	}

}
