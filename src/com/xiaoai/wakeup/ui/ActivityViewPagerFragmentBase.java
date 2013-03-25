package com.xiaoai.wakeup.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.widget.viewpager.FragmentPagerAdapterBase;
import com.xiaoai.wakeup.widget.viewpager.ViewPagerControllableSlide;
import com.xiaoai.wakeup.widget.viewpager.ViewPagerControllableSlide.IOnPageScrolledListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Scroller;

public abstract class ActivityViewPagerFragmentBase extends FragmentActivity {

	private static final int SCROLL_DURATION = 500;

	protected int mCurrentVisible = -2;

	protected ViewPagerControllableSlide mViewPager;
	protected FragmentPagerAdapter mAdapter;

	private FragmentManager mManager;

	protected List<Fragment> mContainerViews = new ArrayList<Fragment>();

	private int mContainerCount;

	protected FixedSpeedScroller mScroller;

	private View mIndicatorLeftLayout;
	private View mIndicatorRightLayout;
	private ImageView mIndicatorLeftView;
	private ImageView mIndicatorRightView;
	private IOnClickIndicatorListener mIndicatorLeftListener;
	private IOnClickIndicatorListener mIndicatorRightListener;

	private View mShadeView; // prevent touch event

	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		mContext = this;
		mManager = getSupportFragmentManager();

		mViewPager = (ViewPagerControllableSlide) findViewById(R.id.viewpager);
		mViewPager.setIOnPageScrolledListener(mPageScrolledListener);
		mViewPager.setOnTouchListener(mPageTouchListener);
		setScroller(new AccelerateInterpolator(), SCROLL_DURATION);

		initIndicatorView();
		initShadeView();

		initContainerViews();

		mAdapter = obtainPagerAdapter();
		if (mAdapter == null) {
			mAdapter = new FragmentPagerAdapterBase(mManager, mContainerViews);
		}
		setPagerAdapter(mAdapter);

		showPagerImmediately(0);

		addTitleView();
		addBottomView();
		addCustomView();
	}

	private IOnPageScrolledListener mPageScrolledListener = new IOnPageScrolledListener() {

		@Override
		public void onScrolled(int position, float offset, int offsetPixels) {
			Log.i("test", "position: " + position + ", offset: " + offset
					+ ", offsetPixels: " + offsetPixels);

			if (offsetPixels != 0) {
				// scrolling
				mShadeView.setVisibility(View.VISIBLE);
				setIndicatorsInvisible();
			} else {
				// scroll stop
				mCurrentVisible = position;
				mShadeView.setVisibility(View.GONE);
				updateIndicatorVisible();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		if (mCurrentVisible < 0) {
			showPagerImmediately(0);
		}
		if (mViewPager.getCurrentItem() != mCurrentVisible) {
			showPagerImmediately(mCurrentVisible);
		}
		updateIndicatorVisible();
	}

	private OnTouchListener mPageTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	};

	private void reset() {
		mCurrentVisible = 0;
	}

	private void showPagerImmediately(int index) {
		mViewPager.setCurrentItem(index, false);
		mCurrentVisible = index;
	}

	private void addTitleView() {
		View titlePanel = findViewById(R.id.title_panel);
		View title = obtainTitleView();
		if (titlePanel != null && title != null) {
			((ViewGroup) titlePanel).addView(title);
		}
	}

	private void addBottomView() {
		View bottomPanel = findViewById(R.id.bottom_panel);
		View bottom = obtainBottomView();
		if (bottomPanel != null && bottom != null) {
			((ViewGroup) bottomPanel).addView(bottom);
		}
	}

	private void addCustomView() {
		View customPanel = findViewById(R.id.custom_panel);
		View custom = obtainCustomView();
		if (customPanel != null && custom != null) {
			((ViewGroup) customPanel).addView(custom);
		}
	}

	protected View obtainTitleView() {
		return null;
	}

	protected View obtainBottomView() {
		return null;
	}

	protected View obtainCustomView() {
		return null;
	}

	/**
	 * Obtain ViewPager's adapter.
	 * 
	 * @return
	 */
	protected FragmentPagerAdapter obtainPagerAdapter() {
		return null;
	}

	protected abstract List<Fragment> obtainContainers();

	private void initContainerViews() {
		mContainerViews = obtainContainers();
		if (mContainerViews == null) {
			return;
		}

		mContainerCount = mContainerViews.size();
	}

	protected void setPagerAdapter(FragmentPagerAdapter adapter) {
		mAdapter = adapter;
		mViewPager.setAdapter(adapter);
	}

	protected FragmentPagerAdapter getPagerAdapter() {
		return mAdapter;
	}

	private void initShadeView() {
		mShadeView = findViewById(R.id.view_shade);
		mShadeView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mShadeView.setVisibility(View.GONE);
	}

	protected void setScroller(Interpolator interpolator, int duration) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			mScroller = new FixedSpeedScroller(mViewPager.getContext(),
					interpolator);
			mScroller.setDuration(duration);
			mField.set(mViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateIndicatorVisible() {
		if (mCurrentVisible == 0) {
			mIndicatorLeftLayout.setVisibility(View.GONE);
			mIndicatorRightLayout.setVisibility(View.VISIBLE);
		} else if (mCurrentVisible == mContainerCount - 1) {
			mIndicatorLeftLayout.setVisibility(View.VISIBLE);
			mIndicatorRightLayout.setVisibility(View.GONE);
		} else {
			mIndicatorLeftLayout.setVisibility(View.VISIBLE);
			mIndicatorRightLayout.setVisibility(View.VISIBLE);
		}
	}

	private void setIndicatorsInvisible() {
		mIndicatorLeftLayout.setVisibility(View.GONE);
		mIndicatorRightLayout.setVisibility(View.GONE);
	}

	private void initIndicatorView() {
		mIndicatorLeftView = (ImageView) findViewById(R.id.view_indicator_left);
		mIndicatorRightView = (ImageView) findViewById(R.id.view_indicator_right);

		mIndicatorLeftLayout = findViewById(R.id.lay_indicator_left);
		mIndicatorLeftLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentVisible > 0) {
					mViewPager.setCurrentItem(mCurrentVisible - 1);
				}

				if (mIndicatorLeftListener != null) {
					mIndicatorLeftListener.onClick(v);
				}
			}
		});

		mIndicatorRightLayout = findViewById(R.id.lay_indicator_right);
		mIndicatorRightLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentVisible < mContainerViews.size() - 1) {
					mViewPager.setCurrentItem(mCurrentVisible + 1);
				}

				if (mIndicatorRightListener != null) {
					mIndicatorRightListener.onClick(v);
				}
			}
		});

		mIndicatorLeftLayout.setVisibility(View.GONE);
		mIndicatorRightLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Set the customer background of indicators.
	 * 
	 * @param leftResId
	 * @param rightResId
	 */
	protected void setIndicators(int leftResId, int rightResId) {
		if (mIndicatorLeftView == null || mIndicatorRightView == null) {
			initIndicatorView();
		}
		mIndicatorLeftView.setImageResource(leftResId);
		mIndicatorRightView.setImageResource(rightResId);
	}

	/**
	 * Set customer background of indicators.
	 * 
	 * @param left
	 * @param right
	 */
	protected void setIndicators(Drawable left, Drawable right) {
		if (mIndicatorLeftView == null || mIndicatorRightView == null) {
			initIndicatorView();
		}
		mIndicatorLeftView.setImageDrawable(left);
		mIndicatorRightView.setImageDrawable(right);
	}

	/**
	 * Set listener of indicators.
	 * 
	 * @param leftListener
	 * @param rightListener
	 */
	protected void setIndicatorListeners(
			IOnClickIndicatorListener leftListener,
			IOnClickIndicatorListener rightListener) {
		this.mIndicatorLeftListener = leftListener;
		this.mIndicatorRightListener = rightListener;
	}

	protected enum Indicator {
		Left, Right
	}

	/**
	 * Set indicator's margin.
	 * 
	 * @param indicator
	 *            sign indicator
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	protected void setIndicatorMargin(Indicator indicator, int left, int top,
			int right, int bottom) {
		LayoutParams lp = getIndicatorParams(indicator);
		lp.setMargins(left, top, right, bottom);

		if (indicator == Indicator.Left) {
			mIndicatorLeftLayout.setLayoutParams(lp);
		} else {
			mIndicatorRightLayout.setLayoutParams(lp);
		}
	}

	private LayoutParams getIndicatorParams(Indicator indicator) {
		LayoutParams lp = null;
		if (indicator == Indicator.Left) {
			lp = (LayoutParams) mIndicatorLeftLayout.getLayoutParams();
		} else {
			lp = (LayoutParams) mIndicatorRightLayout.getLayoutParams();
		}

		if (lp == null) {
			lp = new LayoutParams(dp2px(48), dp2px(100));
		}
		return lp;
	}

	private int dp2px(int dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Set indicator relative type to parent(android:layout_alignParentLeft).
	 * 
	 * @param indicator
	 * @param anchors
	 *            relative type value, in order: android:layout_alignParentLeft,
	 *            android:layout_alignParentTop,
	 *            android:layout_alignParentRight,
	 *            android:layout_alignParentBottom
	 */
	protected void setIndicatorAlignParent(Indicator indicator,
			boolean... anchors) {
		LayoutParams lp = getIndicatorParams(indicator);
		for (int i = 0; i < anchors.length; i++) {
			boolean value = anchors[i];
			int anchor = value ? RelativeLayout.TRUE : 0;
			if (i == 0) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, anchor);
			} else if (i == 1) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, anchor);
			} else if (i == 2) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, anchor);
			} else if (i == 3) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, anchor);
			}
		}
		if (indicator == Indicator.Left) {
			mIndicatorLeftLayout.setLayoutParams(lp);
		} else {
			mIndicatorRightLayout.setLayoutParams(lp);
		}
	}

	private class FixedSpeedScroller extends Scroller {

		private int mDuration;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setDuration(int duration) {
			this.mDuration = duration;
		}
	}

	protected interface IOnClickIndicatorListener {

		void onClick(View v);
	}

}
