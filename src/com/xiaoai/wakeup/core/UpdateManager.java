package com.xiaoai.wakeup.core;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.net.Request;
import com.xiaoai.wakeup.net.ServiceParameters;
import com.xiaoai.wakeup.net.ServiceResult;
import com.xiaoai.wakeup.net.asynctask.AsyncServiceTask.IAsyncUpdateListener;
import com.xiaoai.wakeup.util.DialogUtil;
import com.xiaoai.wakeup.util.ParseUtil;
import com.xiaoai.wakeup.widget.LoadingDialog;

public class UpdateManager implements IAsyncUpdateListener {

	private static final String UPDATE_FORCE = "1";

	public static boolean hadQueryVersion = false;

	private static UpdateManager sInstance;

	private Context mContext;

	private UpdateManager(Context context) {
		this.mContext = context;
	}

	public static UpdateManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new UpdateManager(context);
		}
		return sInstance;
	}

	public void checkUpdate() {
		if (hadQueryVersion) {
			return;
		}
		hadQueryVersion = true;
		sendQueryVersion();
	}

	private void updateApp(final String url) {
		showLoadingProgress();
		mLoadingProgress.startDownload(url, "komovie.apk");
	}

	private LoadingDialog mLoadingProgress;

	private void showLoadingProgress() {
		mLoadingProgress = new LoadingDialog(mContext);
		mLoadingProgress.setMessage(R.string.msg_progress_download_app);
		mLoadingProgress.show();
	}

	// TODO send update request
	private void sendQueryVersion() {
		ServiceParameters params = new ServiceParameters().add("type", "android");
		new Request(mContext, params).makeAsyncTask(this);
	}

	private void showUpdateDialog(boolean isForce, final String updateUrl) {
		DialogInterface.OnClickListener clickedOk = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updateApp(updateUrl);
			}
		};
		DialogInterface.OnClickListener clickedCancel = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		};

//		if (isForce) {
//			DialogUtil.showAlertDialog(ActivityMain.Instance,
//					R.string.txt_hint, R.string.msg_app_update_force,
//					R.string.txt_update_confirm, clickedOk,
//					R.string.txt_update_cancel, clickedCancel).setCancelable(
//					false);
//		} else {
//			DialogUtil.showAlertDialog(ActivityMain.Instance,
//					R.string.txt_hint, R.string.msg_app_update,
//					R.string.txt_update_confirm, clickedOk,
//					R.string.txt_update_cancel, null);
//		}
	}

	@Override
	public void updateAsyncResult(int token, ServiceResult result) {
		// TODO Auto-generated method stub
		
	}

}
