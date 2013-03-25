package com.xiaoai.wakeup.net.asynctask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask.Status;

import com.xiaoai.wakeup.util.log.Log;

public class AsyncTaskManager {

	/** max thread count of request image */
	private static final int MAX_QUERY_TASK_NUM = 20;

	/** set if limit the thread count for request image or not */
	private static final boolean LIMIT_QUERY_COUNT = false;

	/** set if reload image when the async task is canceled or not */
	public static final boolean RELOAD_IMAGE_FOR_CANCELED = false;

	private static final long TASK_TIME_OUT = 1000 * 15;
	private static final long QUERY_IMAGE_TIME_OUT = 1000 * 15;

	private static Map<AsyncServiceTask, Long> sServiceTask;
	private static Map<AsyncImageTask, Long> sImageTask;
	private static Queue<AsyncImageTask> sImageTaskQueue;

	public static void start() {
		if (sServiceTask == null) {
			sServiceTask = new HashMap<AsyncServiceTask, Long>();
		}
		if (sImageTask == null) {
			sImageTask = new HashMap<AsyncImageTask, Long>();
		}
		if (sImageTaskQueue == null) {
			sImageTaskQueue = new LinkedList<AsyncImageTask>();
		}

		startServiceTaskMonitor();
		startImageTaskMonitor();
	}

	private static void startServiceTaskMonitor() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				synchronized (sServiceTask) {
					long now = new Date().getTime();
					List<AsyncServiceTask> rmlist = new ArrayList<AsyncServiceTask>();
					for (Map.Entry<AsyncServiceTask, Long> entry : sServiceTask
							.entrySet()) {
						long t = entry.getValue();
						if (now - t > TASK_TIME_OUT) {
							AsyncServiceTask task = entry.getKey();
							rmlist.add(task);
							task.cancelWithTimeoutResult();
						}
					}
					for (AsyncServiceTask task : rmlist) {
						sServiceTask.remove(task);
					}
				}
			}
		}, 0, 500);
	}

	private static void startImageTaskMonitor() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				synchronized (sImageTask) {
					long now = new Date().getTime();
					List<AsyncImageTask> rmlist = new ArrayList<AsyncImageTask>();
					for (Map.Entry<AsyncImageTask, Long> entry : sImageTask
							.entrySet()) {
						long t = entry.getValue();
						if (now - t > QUERY_IMAGE_TIME_OUT) {
							AsyncImageTask task = entry.getKey();
							rmlist.add(task);
							task.refetchImage();
						}
					}
					for (AsyncImageTask task : rmlist) {
						sImageTask.remove(task);
					}
					int newTaskNum = MAX_QUERY_TASK_NUM - sImageTask.size();
					while (newTaskNum > 0) {
						AsyncImageTask task = sImageTaskQueue.poll();
						if (task == null) {
							break;
						} else {
							Log.d("new task run, url: " + task.getImgUrl());
							sImageTask.put(task, now);
							if (task.getStatus() == Status.PENDING) {
								task.start();
							}
						}
						newTaskNum--;
					}
				}
			}
		}, 0, 500);
	}

	public static void cancelAsyncTask(String url) {
		synchronized (sImageTask) {
			AsyncImageTask rmTask = null;
			for (AsyncImageTask task : sImageTask.keySet()) {
				if (task.getImgUrl().equals(url)) {
					task.cancelTask();
					rmTask = task;
				}
			}
			if (rmTask != null) {
				sImageTask.remove(rmTask);
			}
			rmTask = null;
			for (AsyncImageTask task : sImageTaskQueue) {
				if (task.getImgUrl().equals(url)) {
					task.cancelTask();
					rmTask = task;
				}
			}
			if (rmTask != null) {
				sImageTaskQueue.remove(rmTask);
			}
		}
	}

	/**
	 * Start normal service async task.
	 * 
	 * @param task
	 */
	public static void startTask(AsyncServiceTask task) {
		synchronized (sServiceTask) {
			long t = new Date().getTime();
			sServiceTask.put(task, t);
		}
	}

	/**
	 * Start image async task.
	 * 
	 * @param task
	 */
	public static void startTask(AsyncImageTask task) {
		synchronized (sImageTask) {
			sImageTaskQueue.offer(task);
		}
	}

	/**
	 * Finish normal service async task.
	 * 
	 * @param task
	 */
	public static void finishTask(AsyncServiceTask task) {
		synchronized (sServiceTask) {
			sServiceTask.remove(task);
		}
	}

	/**
	 * Finish image async task.
	 * 
	 * @param task
	 */
	public static void finishTask(AsyncImageTask task) {
		synchronized (sImageTask) {
			sImageTask.remove(task);
		}
	}

	public static int getImageTaskCount() {
		synchronized (sImageTask) {
			return sImageTaskQueue.size();
		}
	}

	/**
	 * Check if can start a new image async task.
	 * 
	 * @return
	 */
	public static boolean canStartNewImageTask() {
		if (!isLimitQueryCount() || getImageTaskCount() < MAX_QUERY_TASK_NUM) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isReloadImageForCanceled() {
		return RELOAD_IMAGE_FOR_CANCELED;
	}

	private static boolean isLimitQueryCount() {
		return LIMIT_QUERY_COUNT;
	}

	public static void cancelAllImageTask() {
		Log.w("cancelAllImageWork called");
		synchronized (sImageTask) {
			for (AsyncImageTask work : sImageTaskQueue) {
				work.cancelTask();
			}
			sImageTaskQueue.clear();
			for (AsyncImageTask work : sImageTask.keySet()) {
				work.cancelTask();
			}
			sImageTask.clear();
			Log.e("cancelAllImageWork called");
		}
	}

}
