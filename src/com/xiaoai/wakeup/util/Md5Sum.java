package com.xiaoai.wakeup.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provide the md5 sum method to check the file md5 digest
 */
public class Md5Sum {

	/**
	 * Get the md5 check sum with the specified file path
	 */
	public static String getMd5CheckValue(String filePath) throws Exception {
		InputStream fis = new FileInputStream(filePath);

		byte[] buffer = new byte[1024];
		MessageDigest md = MessageDigest.getInstance("MD5");
		int num;
		do {
			num = fis.read(buffer);
			if (num > 0) {
				md.update(buffer, 0, num);
			}
		} while (num != -1);
		fis.close();

		StringBuffer md5Value = new StringBuffer();
		byte[] digest = md.digest();
		for (int i = 0; i < digest.length; i++) {
			md5Value.append(Integer.toString((digest[i] & 0xff) + 0x100, 16));
		}
		return md5Value.toString();
	}

	/**
	 * Make the md5 digest value for the specified string
	 */
	public static String makeMd5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
