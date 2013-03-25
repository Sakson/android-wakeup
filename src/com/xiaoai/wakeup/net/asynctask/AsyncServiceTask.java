package com.xiaoai.wakeup.net.asynctask;

import android.os.AsyncTask;

import com.xiaoai.wakeup.net.IServiceBase;
import com.xiaoai.wakeup.net.ServiceResult;
import com.xiaoai.wakeup.util.log.Log;

/**
 * Async task for normal requests.
 * 
 * @author Administrator
 * 
 */
public class AsyncServiceTask extends AsyncTask<Void, Integer, Void> {

	private IServiceBase mService;

	private ServiceResult mResult;

	private IAsyncUpdateListener mListener;

	private int mToken;

	private boolean mIsCanceledForTimeout = false;

	public AsyncServiceTask(int token, IServiceBase service,
			IAsyncUpdateListener listener) {
		super();
		this.mToken = token;
		this.mService = service;
		this.mListener = listener;
	}

	public AsyncServiceTask(IServiceBase service, IAsyncUpdateListener listener) {
		super();
		this.mService = service;
		this.mListener = listener;
		this.mToken = -1;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			AsyncTaskManager.startTask(this);
			mResult = mService.makeRequest();
		} catch (Exception e) {
			Log.d(e.getMessage());
			e.printStackTrace();
			mResult = ServiceResult.makeNetworkUnavailableResult();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		AsyncTaskManager.finishTask(this);
		if (mListener != null) {
			mListener.updateAsyncResult(mToken, mResult);
			mListener = null;
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mIsCanceledForTimeout) {
			if (mListener != null) {
				mListener.updateAsyncResult(mToken, mResult);
				mListener = null;
			}
		}
	}

	public void cancelWithTimeoutResult() {
		cancel(true);
		mResult = ServiceResult.makeTimeoutResult();
		mIsCanceledForTimeout = true;
	}

	public interface IAsyncUpdateListener {

		/**
		 * update async task result on UI thread.
		 * 
		 * @param token
		 * @param result
		 */
		void updateAsyncResult(int token, ServiceResult result);
	}

}
