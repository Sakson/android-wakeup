package com.xiaoai.wakeup.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.mobclick.android.MobclickAgent;
import com.xiaoai.wakeup.app.AlarmApp;

public class ActivityBase extends Activity {

	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		mContext = this;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		AlarmApp.setAppForeground(true);
		AlarmApp.sCurrentActivity = getClass();
	}

	@Override
	protected void onPause() {
		super.onPause();
		AlarmApp.setAppForeground(false);
		MobclickAgent.onPause(this);
		AlarmApp.hideSoftInputWindow(this);
//		ActivityMain.dismissProgressDialog();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			ActivityMain.showActivityPre();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
