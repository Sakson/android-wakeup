package com.xiaoai.wakeup.net.jsoncache;

import java.util.Date;

import com.xiaoai.wakeup.util.log.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class JsonCacheDatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 3;

	private static final String DATABASE_NAME = "db_josn";
	private static final String TABLE_NAME = "json_catch";

	private static final String DATABASE_LOCK = "db_lock";

	public static final String KEY_URL = "url";
	public static final String KEY_EXPIRE_TIME = "expire_time";
	public static final String KEY_DATA = "data";

	public JsonCacheDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		execCreateTableSql(db);
	}

	public JsonCacheDatabaseHelper(Context context) {
		this(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("onUpgrade database.");
		execDropTableSql(db);
		onCreate(db);
	}

	private void execCreateTableSql(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(");
		sql.append(KEY_URL + " TEXT NOT NULL PRIMARY KEY, ");
		sql.append(KEY_DATA + " TEXT, ");
		sql.append(KEY_EXPIRE_TIME + " INTEGER)");
		db.execSQL(sql.toString());
		Log.e("exec create Table sql");
	}

	private void execDropTableSql(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

	public void insertJsonData(String url, String data, long expireTime) {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			try {
				db = getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(KEY_URL, url);
				values.put(KEY_DATA, data);
				values.put(KEY_EXPIRE_TIME, expireTime);
				long i = db.insert(TABLE_NAME, null, values);
				Log.d(i + "");
			} catch (Exception e) {
				Log.w("sql exception: " + e.getMessage());
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public void updateJsonData(String url, String data, long expireTime) {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			try {
				db = getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(KEY_URL, url);
				values.put(KEY_DATA, data);
				values.put(KEY_EXPIRE_TIME, expireTime);
				int i = db.update(TABLE_NAME, values, KEY_URL + "=?",
						new String[] { url });
				Log.d(i + "");
			} catch (Exception e) {
				Log.w("sql updateJsonData exception: " + e.getMessage());
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public boolean selectJsonDataExist(String url) {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			Cursor cursor = null;
			try {
				db = getWritableDatabase();
				cursor = db.query(TABLE_NAME, null, KEY_URL + "=?",
						new String[] { url }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					return true;
				}
			} catch (Exception e) {
				Log.w("sql selectedJsonDataExist exception: " + e.getMessage());
			} finally {
				if (db != null) {
					db.close();
				}
			}
			return false;
		}
	}

	public void reset() {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			try {
				db = getWritableDatabase();
				String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
				db.execSQL(sql);
				execDropTableSql(db);
				onCreate(db);
			} catch (Exception e) {
				Log.w("sql exception: " + e.getMessage());
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public void deleteExpiredJson() {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			try {
				db = getWritableDatabase();
				long now = new Date().getTime();
				String delSql = "DELETE FROM " + TABLE_NAME + " WHERE "
						+ KEY_EXPIRE_TIME + " < '" + now + "'";
				db.execSQL(delSql);
			} catch (Exception e) {
				Log.w("sql exception: " + e.getMessage());
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public String getJsonStr(String url) {
		synchronized (DATABASE_LOCK) {
			SQLiteDatabase db = null;
			Cursor cursor = null;
			try {
				db = getWritableDatabase();
				String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
						+ KEY_URL + " = '" + url + "'";
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					long now = new Date().getTime();
					long expireTime = cursor.getLong(cursor
							.getColumnIndex(KEY_EXPIRE_TIME));
					if (now < expireTime) {
						String data = cursor.getString(cursor
								.getColumnIndex(KEY_DATA));
						if (data != null && data.length() > 0) {
							return data;
						}
					}
				}
				cursor.close();
			} catch (Exception e) {
				Log.w("sql exception: " + e.getMessage());
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
			return JsonCache.NO_RECORD;
		}
	}

}
