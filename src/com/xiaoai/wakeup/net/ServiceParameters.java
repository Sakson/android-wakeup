/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaoai.wakeup.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import com.xiaoai.wakeup.util.CollectionUtil;

/**
 * A list queue for saving keys and values. Using it to construct http header or
 * get/post parameters.
 * 
 * @author ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class ServiceParameters {

	private Bundle mParameters = new Bundle();

	private List<String> mKeys = new ArrayList<String>();

	private List<String> mFileKeys = new ArrayList<String>();

	private Map<String, KokozuFile> mFileValues = new HashMap<String, KokozuFile>();

	public ServiceParameters() {
		super();
	}

	public ServiceParameters add(String key, String value) {
		if (this.mKeys.contains(key)) {
			this.mParameters.putString(key, value);
		} else {
			this.mKeys.add(key);
			this.mParameters.putString(key, value);
		}
		return this;
	}

	public ServiceParameters add(String key, int value) {
		return add(key, String.valueOf(value));
	}

	public ServiceParameters add(String key, long value) {
		return add(key, String.valueOf(value));
	}

	public ServiceParameters add(String key, double value) {
		return add(key, String.valueOf(value));
	}

	public ServiceParameters add(String key, boolean value) {
		return add(key, String.valueOf(value));
	}

	public ServiceParameters add(String key, File value) {
		KokozuFile kFile = new KokozuFile();
		kFile.file = value;
		kFile.mineType = "image/jpg";
		return add(key, kFile);
	}

	public ServiceParameters add(String key, KokozuFile value) {
		if (this.mFileKeys.contains(key)) {
			this.mFileValues.put(key, value);
		} else {
			this.mFileKeys.add(key);
			this.mParameters.putString(key, value + "");
		}
		return this;
	}

	public ServiceParameters remove(String key) {
		this.mKeys.remove(key);
		this.mParameters.remove(key);
		return this;
	}

	public ServiceParameters removeFile(String key) {
		this.mFileKeys.remove(key);
		this.mFileValues.remove(key);
		return this;
	}

	public ServiceParameters remove(int i) {
		String key = this.mKeys.get(i);
		this.mParameters.remove(key);
		this.mKeys.remove(key);
		return this;
	}

	public ServiceParameters removeFile(int i) {
		String key = this.mFileKeys.get(i);
		this.mFileValues.remove(key);
		this.mFileKeys.remove(key);
		return this;
	}

	public int getLocation(String key) {
		if (this.mKeys.contains(key)) {
			return this.mKeys.indexOf(key);
		}
		return -1;
	}

	public int getFileLocation(String key) {
		if (this.mFileKeys.contains(key)) {
			return this.mFileKeys.indexOf(key);
		}
		return -1;
	}

	public String getKey(int index) {
		if (index >= 0 && index < this.mKeys.size()) {
			return this.mKeys.get(index);
		}
		return "";
	}

	public String getFileKey(int index) {
		if (index >= 0 && index < this.mFileKeys.size()) {
			return this.mFileKeys.get(index);
		}
		return "";
	}

	public String getValue(String key) {
		String rlt = this.mParameters.getString(key);
		return rlt;
	}

	public KokozuFile getFileValue(String key) {
		KokozuFile file = this.mFileValues.get(key);
		return file;
	}

	public String getValue(int index) {
		String key = this.mKeys.get(index);
		String rlt = this.mParameters.getString(key);
		return rlt;
	}

	public KokozuFile getFileValue(int index) {
		String key = this.mFileKeys.get(index);
		KokozuFile file = this.mFileValues.get(key);
		return file;
	}

	public List<String> getKeyList() {
		List<String> result = new ArrayList<String>();
		CollectionUtil.addAll(result, mKeys);
		CollectionUtil.addAll(result, mFileKeys);
		return result;
	}

	public int size() {
		return mKeys.size();
	}

	public int fileSize() {
		return mFileKeys.size();
	}

	public boolean isFileParameter(String key) {
		return mFileKeys.contains(key);
	}

	public ServiceParameters addAll(ServiceParameters parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			this.add(parameters.getKey(i), parameters.getValue(i));
		}
		for (int i = 0; i < parameters.fileSize(); i++) {
			this.add(parameters.getFileKey(i), parameters.getFileValue(i));
		}
		return this;
	}

	public void clear() {
		this.mKeys.clear();
		this.mParameters.clear();
		this.mFileKeys.clear();
		this.mFileValues.clear();
	}

	public class KokozuFile {

		public File file;

		public String mineType;
	}

}
