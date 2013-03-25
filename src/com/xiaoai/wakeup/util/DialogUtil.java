package com.xiaoai.wakeup.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	public static AlertDialog showAlertDialog(Context context,
			CharSequence title, CharSequence msg, CharSequence okButton,
			OnClickListener clickedOk, CharSequence cancelButton,
			OnClickListener clickedCancel, CharSequence neutralButton,
			OnClickListener clickedNeutral) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != null && title.length() > 0) {
			builder.setTitle(title);
		}

		if (msg != null && msg.length() > 0) {
			builder.setMessage(msg);
		}

		if (okButton != null) {
			builder.setPositiveButton(okButton, clickedOk);
		}

		if (cancelButton != null) {
			builder.setNegativeButton(cancelButton, clickedCancel);
		}

		if (neutralButton != null) {
			builder.setNeutralButton(neutralButton, clickedNeutral);
		}

		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int msgResId, int okResId, OnClickListener clickedOk,
			int cancelResId, OnClickListener clickedCancel, int neutralResId,
			OnClickListener clickedNeutral) {

		return showAlertDialog(context, TextUtil.getText(context, titleResId),
				TextUtil.getText(context, msgResId),
				TextUtil.getText(context, okResId), clickedOk,
				TextUtil.getText(context, cancelResId), clickedCancel,
				TextUtil.getText(context, neutralResId), clickedNeutral);
	}

	public static AlertDialog showAlertDialog(Context context,
			CharSequence title, CharSequence msg, CharSequence okButton,
			OnClickListener clickedOk, CharSequence cancelButton,
			OnClickListener clickedCancel) {

		return showAlertDialog(context, title, msg, okButton, clickedOk,
				cancelButton, clickedCancel, null, null);
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int msgResId, int okResId, OnClickListener clickedOk,
			int cancelResId, OnClickListener clickedCancel) {

		return showAlertDialog(context, TextUtil.getText(context, titleResId),
				TextUtil.getText(context, msgResId),
				TextUtil.getText(context, okResId), clickedOk,
				TextUtil.getText(context, cancelResId), clickedCancel);
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int msgResId, int okResId, OnClickListener clickedOk) {

		return showAlertDialog(context, titleResId, msgResId, okResId,
				clickedOk, -1, null);
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int msgResId) {

		return showAlertDialog(context, titleResId, msgResId, -1, null, -1,
				null);
	}

	public static AlertDialog showAlertDialog(Context context,
			CharSequence title, CharSequence okButton,
			OnClickListener clickedOk, CharSequence cancelButton,
			OnClickListener clickedCancel, CharSequence[] items,
			OnClickListener clickedItems) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != null && title.length() > 0) {
			builder.setTitle(title);
		}

		if (okButton != null) {
			builder.setPositiveButton(okButton, clickedOk);
		}

		if (cancelButton != null) {
			builder.setNegativeButton(cancelButton, clickedCancel);
		}

		if (items != null && items.length > 0) {
			builder.setSingleChoiceItems(items, 0, clickedItems);
		}

		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int okResId, OnClickListener clickedOk, int cancelResId,
			OnClickListener clickedCancel, int itemsResId,
			OnClickListener clickedItems) {

		return showAlertDialog(context, TextUtil.getText(context, titleResId),
				TextUtil.getText(context, okResId), clickedOk,
				TextUtil.getText(context, cancelResId), clickedCancel,
				TextUtil.getTextArray(context, itemsResId), clickedItems);
	}

	public static AlertDialog showAlertDialog(Context context,
			CharSequence title, CharSequence[] items,
			OnClickListener clickedItems) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != null && title.length() > 0) {
			builder.setTitle(title);
		}

		if (items != null && items.length > 0) {
			builder.setItems(items, clickedItems);
		}

		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showAlertDialog(Context context, int titleResId,
			int itemsResId, OnClickListener clickedItems) {

		return showAlertDialog(context, TextUtil.getText(context, titleResId),
				TextUtil.getTextArray(context, itemsResId), clickedItems);
	}

}
