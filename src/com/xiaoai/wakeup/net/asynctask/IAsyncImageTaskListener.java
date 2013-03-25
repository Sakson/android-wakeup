package com.xiaoai.wakeup.net.asynctask;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public interface IAsyncImageTaskListener {

	/**
	 * 
	 * Update image for async task result, execute on UI thread.
	 * 
	 * @param token
	 *            identify the query
	 * @param url
	 *            image's url
	 * @param data
	 *            image requested
	 */
	void updateImage(String token, String url, SoftReference<Bitmap> data);

}
