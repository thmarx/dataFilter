package de.marx_labs.datafilter;

import java.util.Random;

public class TestHelper {
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random rnd = new Random();
	private static String[] strings = new String[100];
	static {
		for (int i = 0; i < strings.length; i++) {
			strings[i] = randomString(10);
		}
	}

	public static String randomString() {
		
		return strings[rnd.nextInt(10)];
	}
	
	private static String randomString(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(AB.substring(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	public static int randomInt(int max) {
		return rnd.nextInt(max + 1);
	}
}
