package com.xiaoai.wakeup.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class BitmapUtil {

	public static File saveBitmap2File(Bitmap bm, String filePath) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bm.compress(CompressFormat.JPEG, 100, fos);
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utility.close(fos);
		}
		return null;
	}

	public static Bitmap zoomBitmap(Bitmap src, int outWidth, int outHeight) {
		return Bitmap.createScaledBitmap(src, outWidth, outHeight, false);
	}
	
	public static Bitmap zoomBitmap(InputStream is, int outWidth, int outHeight) {
		Bitmap bm = BitmapFactory.decodeStream(is);
		return zoomBitmap(bm, outWidth, outHeight);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = Color.TRANSPARENT;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static void measureView(View view) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int widthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, lp.width);

		int height = lp.height;
		int heightSpec;
		if (height > 0) {
			heightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			heightSpec = MeasureSpec
					.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}

		view.measure(widthSpec, heightSpec);
	}

}
