package com.xiaoai.wakeup.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.widget.IViewPagerChangeListener;

public class ActivityMain extends ActivityGroup implements ViewPager.OnPageChangeListener {

	private static final Class<?>[] TAB = new Class<?>[] {
			ActivityTabHome.class, ActivityTabAlarm.class,
			ActivityTabSetting.class };

	private static final String[] TAG = new String[] { "TAB1", "TAB2", "TAB3" };

	private static final String[] TITLE = new String[] { " ◊“≥", "ƒ÷÷”", "…Ë÷√" };

	private static int lastVisible = -1;

	private static List<IViewPagerChangeListener> tabs = new ArrayList<IViewPagerChangeListener>();

	private ViewPager mViewPager;
	
	private ImageView img01;
	private ImageView img02;
	private ImageView img03;
	private View viewIndicator;

	private LocalActivityManager mManager;

	private List<View> views = new ArrayList<View>();
	private List<String> titles = new ArrayList<String>();

	private PagerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mManager = getLocalActivityManager();

		initView();
		initViewPager();
		initTabContainer();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		img01 = (ImageView) findViewById(R.id.img01);
		img02 = (ImageView) findViewById(R.id.img02);
		img03 = (ImageView) findViewById(R.id.img03);
		viewIndicator = findViewById(R.id.view_indicator);
	}
	
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setOnPageChangeListener(this);
		mAdapter = new AdapterViewPager();
		mViewPager.setAdapter(mAdapter);

		mViewPager.setCurrentItem(0);
		visible(0);
	}
	
	private void initTabContainer() {
		View tab1 = mManager.startActivity(TAG[0], new Intent(this, ActivityTabHome.class)).getDecorView();
		View tab2 = mManager.startActivity(TAG[1], new Intent(this, ActivityTabAlarm.class)).getDecorView();
		View tab3 = mManager.startActivity(TAG[2], new Intent(this, ActivityTabSetting.class)).getDecorView();

		tabs = new ArrayList<IViewPagerChangeListener>();
		if (mManager.getActivity(TAG[0]) instanceof IViewPagerChangeListener) {
			tabs.add((IViewPagerChangeListener) mManager.getActivity(TAG[0]));
		}
		if (mManager.getActivity(TAG[1]) instanceof IViewPagerChangeListener) {
			tabs.add((IViewPagerChangeListener) mManager.getActivity(TAG[1]));
		}
		if (mManager.getActivity(TAG[2]) instanceof IViewPagerChangeListener) {
			tabs.add((IViewPagerChangeListener) mManager.getActivity(TAG[2]));
		}

		views = new ArrayList<View>();
		views.add(tab1);
		views.add(tab2);
		views.add(tab3);
	}

	private void visible(int index) {
		if (index < 0 || index > tabs.size() - 1) {
			return;
		}
		tabs.get(index).onVisible();
		lastVisible = index;
	}

	private void invisible(int index) {
		if (index < 0 || index > tabs.size() - 1) {
			return;
		}
		tabs.get(index).onInvisible();
	}

	class AdapterViewPager extends PagerAdapter {

		@Override
		public int getCount() {
			return TAB.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			try {
				((ViewPager) container).removeView(views.get(position
						% TAB.length));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position % TAB.length);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(views
						.get(position % TAB.length));
				return views.get(position % TAB.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int index) {
		invisible(lastVisible);
		visible(index % TAB.length);
		lastVisible = index % TAB.length;
	}

}
