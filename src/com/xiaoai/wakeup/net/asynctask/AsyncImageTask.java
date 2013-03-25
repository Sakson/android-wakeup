package com.xiaoai.wakeup.net.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.net.ImageMaster;
import com.xiaoai.wakeup.net.Request;
import com.xiaoai.wakeup.util.Utility;
import com.xiaoai.wakeup.util.log.Log;


/**
 * Async task for image requests.
 * 
 * @author Administrator
 * 
 */
public class AsyncImageTask extends AsyncTask<String, Void, Bitmap> {

	private static final int DEFAULT_TASK_TIMEOUT = 20 * 1000;

	private String mToken;

	private String mImageUrl;

	private int mCompressSize;

	public boolean mCancelTimeout = false;

	private ImageView mImageView;

	private IAsyncImageTaskListener mListener;

	private int mWidth = -1;
	private int mHeigth = -1;

	public AsyncImageTask(String imgUrl, ImageView iv) {
		this(imgUrl, iv, 1);
	}

	public AsyncImageTask(String imgUrl, ImageView iv, int compressSize) {
		super();
		this.mImageView = iv;
		this.mImageUrl = imgUrl;
		this.mCompressSize = compressSize;
	}

	public AsyncImageTask(String imgUrl, ImageView iv, int width,
			int heigth) {
		super();
		this.mImageView = iv;
		this.mImageUrl = imgUrl;
		this.mCompressSize = 1;
		this.mWidth = width;
		this.mHeigth = heigth;
	}

	public AsyncImageTask(String url, String token,
			IAsyncImageTaskListener listener, int compressSize) {
		this.mImageUrl = url;
		this.mToken = token;
		this.mListener = listener;
		this.mCompressSize = compressSize;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		if (TextUtils.isEmpty(mImageUrl)) {
			return null;
		}

		String urlConnect = ImageMaster.removeCompressTag(mImageUrl,
				mCompressSize);

		Bitmap bm = null;

		// fetch image from memory cache
		bm = ImageMaster.loadImage(mImageUrl);
		if (bm != null) {
			Log.d("fetch image from memory cache, url: " + mImageUrl);
			return bm;
		}

		// fetch image from SD card
		bm = Utility.getBitmapFromSdcard(urlConnect);
		if (bm != null) {
			Log.d("fetch image from sdcard, url: " + mImageUrl);
			return bm;
		}

		// fetch image from net
		bm = fetchImageFromNet(urlConnect);

		return bm;
	}

	private Bitmap fetchImageFromNet(String urlConnect) {
		try {
			Log.i("fetch image from internet, url: " + mImageUrl);
			URL url = new URL(urlConnect);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(Request.METHOD_GET);
			conn.setReadTimeout(DEFAULT_TASK_TIMEOUT);
			conn.setConnectTimeout(DEFAULT_TASK_TIMEOUT);
			conn.connect();

			return processFetchImage(urlConnect, conn);
		} catch (Exception e) {
			Log.d(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap processFetchImage(String url, HttpURLConnection conn)
			throws IOException {
		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			InputStream is = conn.getInputStream();
			if (mWidth != -1 && mHeigth != -1) {
				return Utility.addNewBitmap(url, is, mWidth, mHeigth);
			} else {
				String path = Utility.addNewBitmap(url, is);
				conn.disconnect();

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = ImageMaster.sInSampleSize * mCompressSize;

				try {
					return BitmapFactory.decodeFile(path, opts);
				} catch (OutOfMemoryError err) {
					err.printStackTrace();
					Log.e("--> catch OOM: " + err.getMessage());
					opts.inSampleSize = opts.inSampleSize * 2;
					return BitmapFactory.decodeFile(path, opts);
				}
			}
		}
		return null;
	}

	public void refetchImage() {
		if (this.getStatus() == AsyncTask.Status.RUNNING) {
			boolean cancelRes = cancel(true);
			mCancelTimeout = true;
			Log.w("image fetch timeout, " + cancelRes);
		}
	}

	public void cancelTask() {
		if (this.getStatus() == AsyncTask.Status.RUNNING
				|| this.getStatus() == AsyncTask.Status.PENDING) {
			boolean cancelRes = cancel(true);
			mCancelTimeout = false;
			Log.i("cancle running image task, url: " + mImageUrl
					+ ", cancle : " + cancelRes);
			ImageMaster.setImageTaskFinish(mImageUrl);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		ImageMaster.setImageTaskFinish(mImageUrl);
		if (mCancelTimeout && AsyncTaskManager.isReloadImageForCanceled()) {
			Log.w("reload image again, url: " + mImageUrl);
			ImageMaster.loadImage(mImageUrl, mImageView, mCompressSize);
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		AsyncTaskManager.finishTask(this);
		if (result == null || result.isRecycled()) {
			ImageMaster.setImageTaskFinish(mImageUrl);
			return;
		}
		ImageMaster.save2ImageCatch(mImageUrl, result);

		// use for listener
		if (mListener != null) {
			mListener.updateImage(mToken, mImageUrl, new SoftReference<Bitmap>(
					result));
			result = null;
			return;
		}

		// use for ImageView
		updateImageForImageView(result);
	}

	private void updateImageForImageView(Bitmap bm) {
		if (mImageView.getTag(R.id.tag_adapter) != null
				&& mImageView.getTag(R.id.tag_adapter) instanceof BaseAdapter) {

			// update image for ImageView in BaseAdapter
			ImageMaster.setImageTaskFinish(mImageUrl);
			BaseAdapter adapter = (BaseAdapter) mImageView
					.getTag(R.id.tag_adapter);
			adapter.notifyDataSetChanged();
			Log.i("update adapter image, url: " + mImageUrl);
		} else {

			// update image for normal ImageView
			Log.i("update image, url: " + mImageUrl);
			if (mImageView != null) {
				mImageView.setImageBitmap(bm);
			}
			synchronized (ImageMaster.IMAGE_TASK_LOCK) {
				List<ImageView> ivs = ImageMaster
						.getWorkingImageViews(mImageUrl);
				if (ivs != null) {
					for (ImageView iv : ivs) {
						if (iv != null && iv != this.mImageView) {
							iv.setImageBitmap(bm);
						}
					}
				}
				ImageMaster.setImageTaskFinish(mImageUrl);
			}
		}

		bm = null;
	}

	public String getImgUrl() {
		return mImageUrl;
	}

	public void asyncExecute() {
		AsyncTaskManager.startTask(this);
	}

	public void start() {
		this.execute(mImageUrl);
	}

}
