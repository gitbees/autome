package com.auto.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateData {

	static String ist = "IST";
	static int i = ist.length();

	public static String randomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	public static String randomNumber(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	public static String randomAmount(int length) {
		String amount = RandomStringUtils.randomNumeric(length);
		int amt = Integer.parseInt(amount);

		if (length == 2 && amt < 10) {
			amt = amt + 10;

		}

		return String.valueOf(amt) + ".00";
	}

	public static String randomAlphaNumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	public static String randomStringWithAllowedSplChars(int length,
			String allowdSplChrs) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + // alphabets
				"1234567890" + // numbers
				allowdSplChrs;
		return ist + RandomStringUtils.random(length - i, allowedChars);
	}

	public static String randomEmail(int length) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + // alphabets
				"1234567890" + // numbers
				"_-."; // special characters
		String email = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		email = temp.substring(0, temp.length() - 9) + "@test.org";
		return email;
	}

	public static String randomUrl(int length) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + // alphabets
				"1234567890" + // numbers
				"_-."; // special characters
		String url = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		url = temp.substring(0, 3) + "." + temp.substring(4, temp.length() - 4)
				+ "." + temp.substring(temp.length() - 3);
		return url;
	}

	public static String getCurrentDate() {

		Date d = new Date();

		String date = new SimpleDateFormat("MM/dd/yyyy").format(d);

		return date;

	}

	public static String getFutureDate(long noOfDaysFromCurrentDay) {

		Date d1 = new Date();

		Date d2 = new Date(d1.getTime()
				+ TimeUnit.DAYS.toMillis(noOfDaysFromCurrentDay));

		String date2 = new SimpleDateFormat("MM/dd/yyyy").format(d2);

		return date2;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(randomAlphaNumeric(10));
		System.out.println(randomNumber(2));

	}

}
