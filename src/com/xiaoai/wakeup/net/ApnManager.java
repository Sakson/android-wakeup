package com.xiaoai.wakeup.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.xiaoai.wakeup.util.log.Log;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Used to manage the system apn connection
 */
public class ApnManager {

	private Context mContext;

	private static final Uri APN_URI = Uri
			.parse("content://telephony/carriers");
	private static final Uri DEFAULT_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	private static final String CMWAP_NAME = "CMWAP";
	private static final String CMWAP_APN = "cmwap";
	private static final String CMWAP_TYPE = "default";
	private static final String CMWAP_PROXY = "10.0.0.172";
	private static final String CMWAP_PORT = "80";
	private static final String CMWAP_MCC = "460";

	private static String CMWAP_MNC = "00";
	private static String CMWAP_NUMERIC = "46000";

	/**
	 * The constructor
	 * 
	 * @param context
	 */
	public ApnManager(Context context) {
		mContext = context;
	}

	/**
	 * Validate the cmwap apn
	 */
	public synchronized void validateCmwapAPN() {

		try {
			if (isCmwapDefaultApn()) {
				return;
			}

			int id = findCmwapAPN();
			if (id < 0) {
				id = createCmwapAPN();
			}
			if (id < 0) {
				return;
			}

			setDefaultAPN(id);
		} catch (Exception e) {
			Log.e("validateCmwapAPN() exception: " + e.getMessage());
			return;
		}
	}

	/**
	 * Check if the default apn is cmwap
	 * 
	 * @return
	 */
	public boolean isCmwapDefaultApn() {
		boolean cmwap = false;

		Cursor cursor = mContext.getContentResolver().query(DEFAULT_APN_URI,
				null, null, null, null);
		if (cursor == null) {
			return false;
		}

		// String id = "";
		String apn = "";
		String proxy = "";
		// String name = "";
		String port = "";
		String type = "";
		String mcc = "";
		String mnc = "";
		String numeric = "";
		String current = "";

		while (cursor.moveToNext()) {
			apn = cursor.getString(cursor.getColumnIndex("apn"));
			type = cursor.getString(cursor.getColumnIndex("type"));
			proxy = cursor.getString(cursor.getColumnIndex("proxy"));
			port = cursor.getString(cursor.getColumnIndex("port"));
			mcc = cursor.getString(cursor.getColumnIndex("mcc"));
			mnc = cursor.getString(cursor.getColumnIndex("mnc"));
			numeric = cursor.getString(cursor.getColumnIndex("numeric"));
			current = cursor.getString(cursor.getColumnIndex("current"));

			if (TextUtils.isEmpty(apn) || TextUtils.isEmpty(type)
					|| TextUtils.isEmpty(proxy) || TextUtils.isEmpty(port)
					|| TextUtils.isEmpty(mcc) || TextUtils.isEmpty(mnc)
					|| TextUtils.isEmpty(numeric) || TextUtils.isEmpty(current)) {
				continue;
			}

			if (apn.equalsIgnoreCase(CMWAP_APN) && type.contains(CMWAP_TYPE)
					&& proxy.equals(CMWAP_PROXY) && port.equals(CMWAP_PORT)
					&& mcc.equals(CMWAP_MCC)
					&& mnc.equals(NetworkManager.getSimOperatorMNC(mContext))
					&& numeric.equals(NetworkManager.getSimOperator(mContext))
					&& current.equals("1")) {
				cmwap = true;
				break;
			}
		}
		cursor.close();
		return cmwap;
	}

	/**
	 * find cmwap apn
	 * 
	 * @return -1 if don't find, other the real id of apn
	 */
	private int findCmwapAPN() {

		int findID = -1;

		String id = "";
		String apn = "";
		String proxy = "";
		String name = "";
		String port = "";
		String type = "";
		String mcc = "";
		String mnc = "";
		String numeric = "";
		String current = "";

		if (mContext == null) {
			return findID;
		}

		Cursor cursor = mContext.getContentResolver().query(APN_URI, null,
				null, null, null);

		if (cursor != null) {

			while (cursor.moveToNext()) {
				id = cursor.getString(cursor.getColumnIndex("_id"));
				name = cursor.getString(cursor.getColumnIndex("name"));
				apn = cursor.getString(cursor.getColumnIndex("apn"));
				type = cursor.getString(cursor.getColumnIndex("type"));
				proxy = cursor.getString(cursor.getColumnIndex("proxy"));
				port = cursor.getString(cursor.getColumnIndex("port"));
				mcc = cursor.getString(cursor.getColumnIndex("mcc"));
				mnc = cursor.getString(cursor.getColumnIndex("mnc"));
				numeric = cursor.getString(cursor.getColumnIndex("numeric"));
				current = cursor.getString(cursor.getColumnIndex("current"));
				Log.i("findCmwapAPN()" + id + "_" + name + "_" + apn + "_"
						+ type + "_" + proxy + "_" + port);
				if (TextUtils.isEmpty(apn) || TextUtils.isEmpty(type)
						|| TextUtils.isEmpty(proxy) || TextUtils.isEmpty(port)
						|| TextUtils.isEmpty(mcc) || TextUtils.isEmpty(mnc)
						|| TextUtils.isEmpty(numeric)
						|| TextUtils.isEmpty(current)) {
					continue;
				}

				if (apn.equalsIgnoreCase(CMWAP_APN) && type.equals(CMWAP_TYPE)
						&& proxy.equals(CMWAP_PROXY) && port.equals(CMWAP_PORT)
						&& mcc.equals(CMWAP_MCC) && mnc.equals(CMWAP_MNC)
						&& numeric.equals(CMWAP_NUMERIC) && current.equals("1")) {
					findID = Integer.valueOf(id);
					Log.i("finded cmwap apn,id=" + id);
				}
			}

			cursor.close();
		}

		return findID;
	}

	/**
	 * Create cmwap apn
	 * 
	 * @return -1 if failed,or created apn id
	 */
	private int createCmwapAPN() {

		int createID = -1;

		if (mContext == null) {
			return createID;
		}

		ContentValues values = new ContentValues();
		values.put("name", CMWAP_NAME);
		values.put("apn", CMWAP_APN);
		values.put("proxy", CMWAP_PROXY);
		values.put("port", CMWAP_PORT);
		values.put("user", "");
		values.put("server", "");
		values.put("password", "");
		values.put("mmsc", "");
		values.put("type", CMWAP_TYPE);
		// in mobile device
		values.put("mcc", CMWAP_MCC);
		values.put("mnc", CMWAP_MNC);
		values.put("numeric", CMWAP_NUMERIC);

		Uri uri = mContext.getContentResolver().insert(APN_URI, values);
		createID = getApnIdByUri(uri);
		return createID;
	}

	/**
	 * Set the specified apn to the default apn
	 * 
	 * @param apnID
	 */
	private boolean setDefaultAPN(int apnID) {

		boolean result = false;

		if (apnID < 0) {
			return false;
		}

		if (mContext == null) {
			return false;
		}

		ContentResolver cr = mContext.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put("apn_id", apnID);

		cr.update(DEFAULT_APN_URI, cv, null, null);

		try {
			Object obj = getTelephony();

			call(obj, "disableDataConnectivity", null, null);
			call(obj, "enableDataConnectivity", null, null);
		} catch (Exception e) {
			Log.e(e.getMessage());
		}

		return result;
	}

	private Object getTelephony() throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {

		TelephonyManager telephonymanager = (TelephonyManager) mContext
				.getSystemService("phone");
		Class<?> class1 = Class.forName(telephonymanager.getClass().getName());
		Class<?> aclass[] = new Class[0];
		Method method = class1.getDeclaredMethod("getITelephony", aclass);
		method.setAccessible(true);
		Object aobj[] = new Object[0];
		return method.invoke(telephonymanager, aobj);
	}

	private void call(Object obj, String s, Class<?> class1, Object obj1)
			throws ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<?> class2 = Class
				.forName("com.android.internal.telephony.ITelephony");
		if (class1 != null) {
			Class<?> aclass[] = new Class[1];
			aclass[0] = class1;
			Method method = class2.getDeclaredMethod(s, aclass);
			Object aobj[] = new Object[1];
			aobj[0] = obj1;
			@SuppressWarnings("unused")
			Object obj2 = method.invoke(obj, aobj);
		} else {
			Class<?> aclass1[] = new Class[0];
			Method method1 = class2.getDeclaredMethod(s, aclass1);
			Object aobj1[] = new Object[0];
			@SuppressWarnings("unused")
			Object obj3 = method1.invoke(obj, aobj1);
		}
	}

	/**
	 * Get the apn id with the specified uri
	 * 
	 * @param uri
	 * @return
	 */
	private int getApnIdByUri(Uri uri) {
		if (mContext == null) {
			return -1;
		}

		Cursor cursor = mContext.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor == null) {
			return -1;
		}

		cursor.moveToFirst();
		int id = cursor.getShort(cursor.getColumnIndex("_id"));
		cursor.close();
		return id;
	}

}
