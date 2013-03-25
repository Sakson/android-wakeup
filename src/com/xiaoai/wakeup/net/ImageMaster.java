package com.xiaoai.wakeup.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xiaoai.wakeup.net.asynctask.AsyncImageTask;
import com.xiaoai.wakeup.net.asynctask.AsyncTaskManager;
import com.xiaoai.wakeup.net.asynctask.IAsyncImageTaskListener;
import com.xiaoai.wakeup.util.log.Log;

public class ImageMaster {

	public static final String IMAGE_URL_KOKOZU = "kokozu.net";
	public static final String COMPRESS_TAG = "_size_compress_";
	public static final String IMAGE_TASK_LOCK = "working_task_lock";

	public static int sInSampleSize = 1;

	private static ImageMaster sInstance;

	private static LruCache<String, Bitmap> sImageCache;

	// save URL and ImageView contact
	private static Map<String, List<ImageView>> sImageMap;

	private ImageMaster(int memClass) {
		sImageMap = new HashMap<String, List<ImageView>>();
		sImageCache = new LruCache<String, Bitmap>(memClass / 4 * 1024 * 1024) {

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getWidth() * bitmap.getHeight() * 4;
			}
		};
	}

	/**
	 * 实例化ImageMaster�? *
	 * 
	 * @param memClass
	 * @return
	 */
	public static ImageMaster newInstance(int memClass) {
		if (sInstance == null) {
			sInstance = new ImageMaster(memClass);
		}
		return sInstance;
	}

	public static boolean urlFromKokozu(String url) {
		if (!TextUtils.isEmpty(url)
				&& url.toLowerCase().contains(IMAGE_URL_KOKOZU)) {
			return true;
		}
		return false;
	}

	public static void save2ImageCatch(String url, Bitmap bm) {
		synchronized (sImageCache) {
			sImageCache.put(url, bm);
		}
	}

	/**
	 * use when ABSListView scrolling
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap loadImage(String url) {
		return loadImage(url, null);
	}

	public static Bitmap loadImage(String url, ImageView imgView) {
		return loadImage(url, imgView, 1);
	}

	public static Bitmap loadImage(String url, ImageView imgView,
			int compressSize) {
		if (url == null) {
			return null;
		}

		url = addCompressTag(url, compressSize);

		Bitmap bm = null;

		bm = loadFromCache(url);
		if (bm != null) {
			Log.i("load image from cache.");
			return bm;
		}

		if (imgView != null) {
			if (isInWorkingQueue(url, imgView)) {
				return null;
			}

			if (AsyncTaskManager.canStartNewImageTask()) {
				new AsyncImageTask(url, imgView, compressSize).asyncExecute();
			}
		}
		return null;
	}

	/**
	 * @param url
	 *            网络图片的URL
	 * @param imageView
	 * @param width
	 *            图片的宽�? * @param heigth 图片的高�? * @return
	 */
	public static Bitmap loadImage(String url, ImageView imgView, int width,
			int heigth) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}

		Bitmap bm = null;

		bm = loadFromCache(url);
		if (bm != null) {
			Log.i("load image form cache");
			return bm;
		}

		if (imgView != null) {
			if (isInWorkingQueue(url, imgView)) {
				return null;
			}

			if (AsyncTaskManager.canStartNewImageTask()) {
				new AsyncImageTask(url, imgView, width, heigth).asyncExecute();
			}
		}
		return null;
	}

	public static void loadImage(String url, String token,
			IAsyncImageTaskListener updater, int compressSize) {
		if (url == null) {
			return;
		}

		url = addCompressTag(url, compressSize);

		if (AsyncTaskManager.canStartNewImageTask()) {
			new AsyncImageTask(url, token, updater, compressSize)
					.asyncExecute();
		}
	}

	private static Bitmap loadFromCache(String url) {
		synchronized (sImageCache) {
			Bitmap bitmap = sImageCache.get(url);
			return bitmap;
		}
	}

	private static boolean isInWorkingQueue(String url, ImageView imgView) {
		synchronized (IMAGE_TASK_LOCK) {
			if (isInWorkingTask(url, imgView)) {
				Log.i("waiting...");
				return true;
			}
			return false;
		}
	}

	/**
	 * If in working task list, return true.
	 * 
	 * @param url
	 * @param imgView
	 * @return
	 */
	public static boolean isInWorkingTask(String url, ImageView imgView) {
		boolean inWorking = false;
		synchronized (IMAGE_TASK_LOCK) {
			if (sImageMap.containsKey(url)) {
				List<ImageView> list = sImageMap.get(url);
				if (!list.contains(imgView)) {
					list.add(imgView);
				} else {
					inWorking = true;
				}
			} else {
				List<ImageView> list = new ArrayList<ImageView>();
				list.add(imgView);
				sImageMap.put(url, list);
			}
			return inWorking;
		}
	}

	public static void setImageTaskFinish(String url) {
		synchronized (IMAGE_TASK_LOCK) {
			if (sImageMap.containsKey(url)) {
				sImageMap.remove(url);
			}
		}
	}

	public static List<ImageView> getWorkingImageViews(String url) {
		return sImageMap.get(url);
	}

	public static String addCompressTag(String url, int compressSize) {
		String tag = "";
		if (compressSize > 1) {
			tag = compressSize + COMPRESS_TAG;
		}
		return tag + url;
	}

	public static String removeCompressTag(String url, int compressSize) {
		String urlConnect = new String(url);

		if (compressSize > 1) {
			String tag = compressSize + COMPRESS_TAG;
			urlConnect = urlConnect.replaceFirst(tag, "");
		}
		return urlConnect;
	}

}
