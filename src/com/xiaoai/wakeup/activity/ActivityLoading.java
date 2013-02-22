package com.xiaoai.wakeup.activity;

import java.util.Date;

import com.mobclick.android.MobclickAgent;
import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.R.drawable;
import com.xiaoai.wakeup.R.id;
import com.xiaoai.wakeup.R.layout;
import com.xiaoai.wakeup.log.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class ActivityLoading extends Activity {

	private static final int DURATION = 2500;

	private ImageView imgBack;
	private ImageView imgFront;

	private Bitmap bmBack;
	private Bitmap bmFront;

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		MobclickAgent.onError(this);
		imgFront = (ImageView) findViewById(R.id.img_front);
		imgBack = (ImageView) findViewById(R.id.img_back);
		bmBack = BitmapFactory.decodeResource(getResources(), R.drawable.bg_loading);
		bmFront = BitmapFactory.decodeResource(getResources(),
				R.drawable.loading_normal);
		imgGrayBack.setImageBitmap(bitmapGrayBack);
		imgFront.setImageBitmap(bitmapLoadingFront);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		KoKoZuApp.setDisplay(width, height);

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Rect frame = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
				KoKoZuApp.saveStatusBarHeight(frame.top);
			}
		}, 500);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onEvent(ActivityLoading.this, "launch_2_3",
				KoKoZuApp.CHANNEL_NAME);
		checkNetworkAvailable();
	}

	private void checkNetworkAvailable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							AlphaAnimation taTop = new AlphaAnimation(1.0f, 0f);
							taTop.setDuration(DURATION);
							taTop.setFillAfter(true);
							imgFront.startAnimation(taTop);
						}
					});
					Thread.sleep(DURATION);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						KoKoZuApp.sIsFirstPreference = getSharedPreferences(
								KoKoZuApp.IsFirst, MODE_PRIVATE);
						boolean isfirst = KoKoZuApp.sIsFirstPreference
								.getBoolean("isfirst", true);
						if (isfirst) {
							KoKoZuApp.removeLocateListener();
							KoKoZuApp.sIsFirstPreference = getSharedPreferences(
									KoKoZuApp.IsFirst, Context.MODE_PRIVATE);
							KoKoZuApp.sEditor = KoKoZuApp.sIsFirstPreference
									.edit();
							KoKoZuApp.sEditor.putBoolean("isfirst", false);
							KoKoZuApp.sEditor.commit();
							Intent intent = new Intent(ActivityLoading.this,
									ActivityMain.class);
							intent.putExtra(
									ActivityMain.EXTRA_NAVIGATE_CHOOSE_CITY,
									true);
							startActivity(intent);
						} else {
							Intent intent = new Intent(ActivityLoading.this,
									ActivityMain.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
						ActivityLoading.this.finish();
					}
				});
			}
		}).start();
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			checkNetworkAvailable();
		}
	}

}
