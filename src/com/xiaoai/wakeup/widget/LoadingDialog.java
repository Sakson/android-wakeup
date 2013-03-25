package com.xiaoai.wakeup.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoai.wakeup.R;
import com.xiaoai.wakeup.app.AlarmApp;
import com.xiaoai.wakeup.util.TextUtil;
import com.xiaoai.wakeup.util.ToastUtil;
import com.xiaoai.wakeup.util.log.Log;

public class LoadingDialog extends Dialog {

	private static final int K = 1024;
	private static final int M = 1024 * 1024;

	private static final NumberFormat nf = NumberFormat.getPercentInstance();
	private static final DecimalFormat df = new DecimalFormat("##0.00");

	private ProgressBar mProgress;
	private TextView mProgressNumber;
	private TextView mProgressPercent;
	private TextView mTxtTitle;
	private TextView mTxtMessage;
	private Context mContext;

	private int prev = 0;
	private int middle = K;
	private double dMax;
	private double dProgress;
	private CharSequence title;
	private CharSequence message;

	private Handler mViewUpdateHandler;

	private OnCancelDialogListener mListener;

	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialogStyle);
		this.mContext = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mViewUpdateHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				double precent = dProgress / dMax;
				if (prev != (int) (precent * 100)) {
					mProgress.setProgress((int) (precent * 100));
					mProgressNumber.setText(df.format(dProgress) + "/"
							+ df.format(dMax) + (middle == K ? "K" : "M"));
					mProgressPercent.setText(nf.format(precent));
					prev = (int) (precent * 100);
				}
			}
		};
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.loading_progress_dialog, null);
		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		mProgress.setMax(100);
		mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
		mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
		mProgressPercent.setText("0%");
		mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
		if (title != null) {
			mTxtTitle.setText(title);
		} else {
			mTxtTitle.setText("提示");
		}
		mTxtMessage = (TextView) view.findViewById(R.id.txt_message);
		if (message != null) {
			mTxtMessage.setText(message);
			mTxtMessage.setVisibility(View.VISIBLE);
		} else {
			mTxtMessage.setText("");
			mTxtMessage.setVisibility(View.GONE);
		}
		setContentView(view);

		int defaultWidth = (int) (AlarmApp.sConfigurator.getScreenWidth() * 0.85);
		getWindow().setLayout(defaultWidth,
				WindowManager.LayoutParams.WRAP_CONTENT);

		onProgressChanged();
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				initProgress();
				cancleDownload();
				if (mListener != null) {
					mListener.onCancelDialog();
				}
			}
		});
		this.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				initProgress();
				cancleDownload();
				if (mListener != null) {
					mListener.onCancelDialog();
				}
			}
		});
	}

	public void setOnCancelDialogListener(OnCancelDialogListener listener) {
		this.mListener = listener;
	}

	private void cancleDownload() {
		if (task != null) {
			task.cancel(true);
		}
		cancelInstall = true;
	}

	private void initProgress() {
		dMax = 0;
		dProgress = 0;
		mProgress.setProgress(0);
		title = null;
		message = null;
	}

	private void onProgressChanged() {
		mViewUpdateHandler.sendEmptyMessage(0);
	}

	public double getDMax() {
		return dMax;
	}

	public void setDMax(double max) {
		if (max > M) {
			middle = M;
		} else {
			middle = K;
		}
		dMax = max / middle;
	}

	public double getDProgress() {
		return dProgress;
	}

	public void setDProgress(double progress) {
		dProgress = progress / middle;
		Log.w(" ---> fileDownSize: " + dProgress);
		onProgressChanged();
	}

	public void setTitle(CharSequence title) {
		if (mTxtTitle != null) {
			mTxtTitle.setText(title);
		} else {
			this.title = title;
		}
	}

	public void setMessage(int msgId) {
		setMessage(TextUtil.getText(mContext, msgId));
	}

	public void setMessage(CharSequence msg) {
		if (mTxtMessage != null) {
			if (msg != null && msg.length() > 0) {
				mTxtMessage.setVisibility(View.VISIBLE);
				mTxtMessage.setText(msg);
			} else {
				mTxtMessage.setVisibility(View.GONE);
			}
		} else {
			message = msg;
		}
	}

	private boolean cancelInstall;
	private double fileSize;
	private double fileDownSize;
	private String fileName;
	private KokozuAsyncDownloadTask task;

	public void startDownload(final String url, final String fileName) {
		this.fileName = fileName;
		task = new KokozuAsyncDownloadTask(url);
		task.execute();
	}

	private void updateApp(String path) {
		chmod("777", path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + path),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	private void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class KokozuAsyncDownloadTask extends
			AsyncTask<Void, Integer, Boolean> {

		private String url;
		private String path;

		public KokozuAsyncDownloadTask(String url) {
			this.url = url;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = null;
			FileOutputStream fileOutputStream = null;
			try {
				response = client.execute(get);
				int resCode = response.getStatusLine().getStatusCode();
				if (resCode / 100 == 2) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					fileSize = entity.getContentLength();
					setDMax(fileSize);
					File cacheDir = mContext.getCacheDir();
					path = cacheDir.getAbsolutePath();
					if (fileName == null) {
						path += "/temp.apk";
					} else {
						path += "/" + fileName;
					}
					if (is != null) {
						File file = new File(path);
						file.createNewFile();
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						while ((ch = is.read(buf)) != -1) {
							if (cancelInstall) {
								return true;
							}
							fileOutputStream.write(buf, 0, ch);
							fileDownSize += ch;
							setDProgress(fileDownSize);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					return true;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (!cancelInstall) {
					updateApp(path);
				} else {
					cancelInstall = false;
				}
			} else {
				ToastUtil.showShort(mContext, "网络连接错误, 请稍后重试");
			}
			dismiss();
		}
	}

	public interface OnCancelDialogListener {

		void onCancelDialog();
	}

}
