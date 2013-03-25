package com.xiaoai.wakeup.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.mobclick.android.MobclickAgent;
import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.app.AlarmApp;
import com.xiaoai.wakeup.preferences.Preferences;

public class ActivityLoading extends ActivityBase {

	private static final int START_OFFSET = 500; // anim start offset
	private static final int DURATION = 2500;

	private ImageView mFrontImageView;

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		mFrontImageView = (ImageView) findViewById(R.id.iv_front);

		obtainScreenProperty();
	}

	private void obtainScreenProperty() {
		// obtain screen size
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		AlarmApp.sConfigurator.setScreenDimensions(dm.widthPixels,
				dm.heightPixels);

		// obtain status bar height
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Rect frame = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
				Preferences.saveStatusBarHeight(frame.top);
			}
		}, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onEvent(mContext, "launch", AlarmApp.sChannelName);
//		AlarmApp.locateGPS();

		startFrontAnim();
	}

	private void startFrontAnim() {
		AlphaAnimation animFornt = new AlphaAnimation(1.0f, 0f);
		animFornt.setStartOffset(START_OFFSET);
		animFornt.setDuration(DURATION);
		animFornt.setFillAfter(true);
		mFrontImageView.startAnimation(animFornt);
		animFornt.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				showActivityNext();
			}
		});
	}

	private void showActivityNext() {
//		Intent itMain = new Intent(ActivityLoading.this, ActivityMain.class);
//		itMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(itMain);
//		ActivityLoading.this.finish();
	}

}
