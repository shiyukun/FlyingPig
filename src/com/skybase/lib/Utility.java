package com.skybase.lib;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import android.renderscript.Font;

/**
 * @author Shi Yukun
 *
 */
public class Utility {

	/**
	 * transform bytes to Hex String
	 * 
	 * @param bytes
	 *            byte array
	 * @return Hex String
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String bytesToHex(byte[] bytes) {
		int intHex = 0;
		StringBuffer strHex = new StringBuffer(bytes.length * 2);
		String strReturn = "";
		try {
			for (int intI = 0; intI < bytes.length; intI++) {
				intHex = (int) bytes[intI];
				if (intHex < 0)
					intHex += 256;
				if (intHex < 16)
					strHex.append("0" + Integer.toHexString(intHex).toUpperCase());
				else
					strHex.append(Integer.toHexString(intHex).toUpperCase());
			}
			strReturn = strHex.toString();

		} catch (Exception ex) {
			
		}
		return strReturn;
	}

	public static String byteToStr(byte[] b) {
		String str = "";
		try {
			if (b != null)
				str = new String(b, "UTF-8");
		} catch (Exception e) {
			System.out.println("error : byteToStr" + e.toString());
		}
		return str;
	}

	public static byte[] strToByte(String str) {
		byte[] barray = null;
		try {
			if (str != null)
				barray = str.getBytes("UTF-8");
		} catch (Exception e) {

		}
		return barray;
	}

	public static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0xff);
		b[1] = (byte) ((i >> 8) & 0xff);
		b[2] = (byte) ((i >> 16) & 0xff);
		b[3] = (byte) ((i >> 24) & 0xff);
		return b;
	}

	public static int byteArrayToInt(byte[] b) {
		int i = 0;
		i = ((b[3] << 24) | ((b[2] << 16) & 0xff0000) | ((b[1] << 8) & 0xff00) | (b[0] & 0xff));
		return i;
	}

	public static String getNotNullString(String s) {
		if (s == null)
			return "";
		if (s.equals("null"))
			return "";
		return s;
	}
	
	public static boolean isEmptyString(String str) {
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}

	/*
	 * get Astro index
	 * input : array is Astro name array
	 * output: Astro name
	 */
	public static String getAstroFromArray(int month, int day, String[] array) {
		int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		int index = month - (day < arr[month - 1] ? 1 : 0);
		return array[index];
	}

	/**
	 * @param date
	 * 		mm/dd/year
	 * @return
	 */
	public static int[] getDateFromString(String date) {
		if (date == null)
			return null;
		int[] result = new int[3];
		String month = date.substring(0, 2);
		String day = date.substring(3, 5);
		String year = date.substring(6, 10);
		result[0] = Integer.parseInt(year);
		result[1] = Integer.parseInt(month);
		result[2] = Integer.parseInt(day);
		return result;
	}
	
	public static String parseTime(int time) {
		if (time < 0)
			time = 0;
		time = time / 1000;
		int min = time / 60;
		int sec = time % 60;
		String zero_min = "";
		String zero_sec = "";
		if (min < 10) {
			zero_min = "0";
		}
		if (sec < 10) {
			zero_sec = "0";
		}
		return zero_min + min + ":" + zero_sec + sec;
	}
	
	/**
	 * Returns true if the two given dates represents the same day.
	 */
	public static boolean areSameDay(long d1, long d2) {
		return (getZeroHourOfDay(d1) == getZeroHourOfDay(d2));
	}

	public static boolean areSameWeek(long d1, long d2){
		long value = Math.abs(getZeroHourOfDay(d1) - getZeroHourOfDay(d2));
		return value < 7 * 24 * 60 * 60 * 1000;
	}

	/**
	 * Get 00:00am of the given day.
	 */
	public static long getZeroHourOfDay(long time) {
		return (time - (time % 86400000));
	}

	public static int BCDToInt(int num) {
		return ((0xffff & num) >> 12) * 1000 + ((0xfff & num) >> 8) * 100 + ((0xff & num) >> 4) * 10 + (0xf & num);
	}
	
}
