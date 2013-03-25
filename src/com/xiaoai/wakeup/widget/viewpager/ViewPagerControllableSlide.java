package com.xiaoai.wakeup.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerControllableSlide extends ViewPager {

	private boolean mPreventSlide = true;

	private IOnPageScrolledListener mScrolledListener;

	private List<OnPageChangeListener> mPageChangeListeners = new ArrayList<OnPageChangeListener>();

	public ViewPagerControllableSlide(Context context) {
		super(context);
		setInternalListener();
	}

	public ViewPagerControllableSlide(Context context, AttributeSet attrs) {
		super(context, attrs);
		setInternalListener();
	}

	private void setInternalListener() {
		super.setOnPageChangeListener(mInternalListener);
	}

	// the listener to dispatch page change event
	private OnPageChangeListener mInternalListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int state) {
			for (OnPageChangeListener listener : mPageChangeListeners) {
				if (listener != null) {
					listener.onPageScrollStateChanged(state);
				}
			}
		}

		@Override
		public void onPageSelected(int position) {
			for (OnPageChangeListener listener : mPageChangeListeners) {
				if (listener != null) {
					listener.onPageSelected(position);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			for (OnPageChangeListener listener : mPageChangeListeners) {
				if (listener != null) {
					listener.onPageScrolled(position, positionOffset,
							positionOffsetPixels);
				}
			}
		}
	};

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!mPreventSlide) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}

	@Override
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		super.onPageScrolled(position, offset, offsetPixels);
		if (mScrolledListener != null) {
			mScrolledListener.onScrolled(position, offset, offsetPixels);
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mPageChangeListeners.add(listener);
	}

	public void setIOnPageScrolledListener(IOnPageScrolledListener listener) {
		this.mScrolledListener = listener;
	}

	/**
	 * Set if the ViewPager can not be slide by finger.
	 * 
	 * @param isPrevent
	 */
	public void setPreventSlide(boolean isPrevent) {
		this.mPreventSlide = isPrevent;
	}

	public interface IOnPageScrolledListener {

		void onScrolled(int position, float offset, int offsetPixels);
	}

}
