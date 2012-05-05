package net.mad.data.datafilter;

import java.util.Random;

public class TestHelper {
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append( AB.substring( rnd.nextInt(AB.length()) ) );
		}
		return sb.toString();
	}
	
	public static int randomInt (int max) {
		return rnd.nextInt(max + 1);
	}
}
