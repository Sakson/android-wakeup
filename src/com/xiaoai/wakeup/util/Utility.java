package com.xiaoai.wakeup.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.xiaoai.wakeup.core.Constants;
import com.xiaoai.wakeup.net.ImageMaster;
import com.xiaoai.wakeup.preferences.Preferences;
import com.xiaoai.wakeup.util.log.Log;

public class Utility {

	/**
	 * Fetch image from SD card base on the specified URL.
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromSdcard(String url) {
		return getBitmapFromSdcard(url, 1);
	}

	/**
	 * Fetch image from SD card base on the specified URL in compressSize.
	 * 
	 * @param url
	 * @param compressSize
	 *            image's in sample size
	 * @return
	 */
	public static Bitmap getBitmapFromSdcard(String url, int compressSize) {
		String filePath = getFilePathFromUrl(url);

		int iss = 1;
		if (ImageMaster.urlFromKokozu(url)) {
			iss = ImageMaster.sInSampleSize * compressSize;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = iss;
		return BitmapFactory.decodeFile(filePath, opts);
	}

	public static String getFilePathFromUrl(String url) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + Constants.FILE_DIR;
		String name = getFileNameFromUrl(url);
		return fullPath + File.separator + name;
	}

	/**
	 * Generate file's name base on the specified URL(Md5 value), add ".data"
	 * after image URL.
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		if (url == null) {
			return "";
		}
		String postfix = ".png.data";
		if (url.contains(".jpg")) {
			postfix = ".jpg.data";
		}
		if (url.contains(".JPG")) {
			postfix = ".JPG.data";
		}
		if (url.contains(".png")) {
			postfix = ".png.data";
		}
		if (url.contains(".PNG")) {
			postfix = ".PNG.data";
		}
		String name = Md5Sum.makeMd5(url);
		name += postfix;
		return name;
	}

	public static Bitmap addNewBitmap(String url, InputStream is, int width,
			int height) {
		Bitmap bm = null;
		if (getFreeSpaceOnSdcard() < 5) {
			Log.d("sdcard doed not have enough space.");
			return null;
		}
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + Constants.FILE_DIR;
		String name = getFileNameFromUrl(url);
		String filePath = fullPath + File.separator + name;

		File path = new File(fullPath);
		if (!path.exists()) {
			path.mkdirs();
		}

		bm = BitmapUtil.zoomBitmap(is, width, height);
		BitmapUtil.saveBitmap2File(bm, filePath);
		return bm;
	}

	public static String addNewBitmap(String url, InputStream in) {
		if (getFreeSpaceOnSdcard() < 5) {
			Log.d("sdcard doed not have enough space.");
			return null;
		}
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + Constants.FILE_DIR;
		String name = getFileNameFromUrl(url);
		String fileName = fullPath + File.separator + name;
		File file = new File(fileName);
		try {
			File path = new File(fullPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			Log.d(e.getMessage());
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * Get the SD card's free space size(MB).
	 * 
	 * @return
	 */
	public static int getFreeSpaceOnSdcard() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		double sdFreeMB = ((double) stat.getAvailableBlocks())
				* stat.getBlockSize() / 1024 / 1024;
		return (int) sdFreeMB;
	}

	public static boolean copyFile(String from, String to) {
		File sourceFile = new File(from);
		File targetFile = new File(to);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] b = new byte[1024];
			int len;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bis);
			close(bos);
		}
		return false;
	}

	/**
	 * Retrieve the file from assets folder to the specified path in
	 * /data/data/<package name>/files folder.
	 * 
	 * @param context
	 * @param from
	 * @param to
	 * @return
	 */
	@SuppressLint("WorldReadableFiles")
	public static boolean retrieveFileFromAssets(Context context, String from,
			String to) {
		boolean bRet = false;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = context.getAssets().open(from);
			fos = context.openFileOutput(to, Context.MODE_WORLD_READABLE);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(fos);
			close(is);
		}
		return bRet;
	}

	/**
	 * Close the closeable resources to release the resources.
	 * 
	 * @param closeable
	 * @return
	 */
	public static boolean close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Recycle bitmap resource.
	 * 
	 * @param bitmap
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
			System.gc();
		}
	}

	/**
	 * Compute the in sample size base on the specified output dimensions.
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * Get app version name in double format.
	 * 
	 * @param context
	 * @return if the version's name is "1.0.1", will return 1.01.
	 */
	public static double getVersionNumber(Context context) {
		String versionName = getVersionName(context);
		if (TextUtils.isEmpty(versionName)) {
			return Double.MAX_VALUE;
		}
		int index = versionName.indexOf(".");
		if (index < 0) {
			return NumberUtil.parseDouble(versionName);
		}

		String strInteger = versionName.substring(0, index);
		String main = (TextUtils.isEmpty(strInteger)) ? "0" : strInteger;
		String minor = versionName.substring(index + 1);
		return NumberUtil.parseDouble(main + "."
				+ minor.replace(".", "").replace(" ", ""));
	}

	public static String getVersionName(Context context) {
		Context appContext = context.getApplicationContext();
		PackageManager mPackageManager = appContext.getPackageManager();
		try {
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(
					appContext.getPackageName(), 0);
			String versionName = mPackageInfo.versionName;
			if (versionName != null) {
				return versionName;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Check if the specified packageName app is exist.
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppExist(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> lstPackageInfo = manager.getInstalledPackages(0);
		for (int i = 0; i < lstPackageInfo.size(); i++) {
			PackageInfo pInfo = lstPackageInfo.get(i);
			if (packageName.equalsIgnoreCase(pInfo.packageName)) {
				return true;
			}
		}
		return false;
	}

	public static void installApk(Context context, String filePath) {
		if (filePath == null) {
			return;
		}
		chmod("777", filePath);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
