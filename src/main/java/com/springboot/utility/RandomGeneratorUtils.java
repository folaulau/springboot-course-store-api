package com.springboot.utility;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Random Generator Utility is designed to use accross this API - It randomly
 * generate String, Number, and AlphaNumeric
 * 
 * @author fkaveinga
 * @see RandomStringUtils
 * @see RandomUtils
 */
public final class RandomGeneratorUtils {

	private static List<String> specialCharacters = Arrays.asList("@", "#", "$", "%", "!", "^", "&", "*", "()", "\"",
			"_", ",", "~", "`", "-", "=", "[", "]", "{", "}", "|", ";", ":", "'", ",", ".", "/", "<", ">", "?");

	// 30 length
	private static final int UUID_LENGTH = 30;
	
	private static String getUuid(StringBuilder uuid) {
		return uuid.append(RandomStringUtils.randomAlphanumeric(UUID_LENGTH)).toString();
	}
	
	
	public static String getString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	public static int getInteger() {
		return RandomUtils.nextInt();
	}

	public static int getIntegerWithin(int start, int end) {
		return RandomUtils.nextInt(start, end);
	}

	public static Float getFloat() {
		return RandomUtils.nextFloat();
	}

	public static Float getIntegerWithin(float start, float end) {
		return RandomUtils.nextFloat(start, end);
	}

	public static Double getDouble() {
		return RandomUtils.nextDouble();
	}

	public static Double getDoubleWithin(double start, double end) {
		return RandomUtils.nextDouble(start, end);
	}

	public static String getAlphaNumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	public static String getSecret(int length, int numOfSpecialCharacters) {
		StringBuilder secret = new StringBuilder(RandomStringUtils.randomAlphanumeric(length - numOfSpecialCharacters));

		if (numOfSpecialCharacters > 0) {
			int size = specialCharacters.size();

			for (int i = 0; i < numOfSpecialCharacters; i++) {
				secret.append(specialCharacters.get(getIntegerWithin(0, size - 1)));
			}

		}
		return secret.toString();
	}

	// // 20 characters 4 pieces to ensure uniqueness
	// public static String getUuid() {
	// StringBuilder uuid = new StringBuilder();
	// for (int i = 0; i < 4; i++) {
	// uuid.append(RandomStringUtils.randomAlphabetic(5));
	// }
	// return uuid.toString();
	// }

	public static String getXApiKey() {
		StringBuilder uuid = new StringBuilder("xapi_");
		for (int i = 0; i < 5; i++) {
			uuid.append(RandomStringUtils.randomAlphabetic(5));
		}
		return uuid.toString();
	}

	// cff = care faciliy fee
//	public static String getFacilityFeeUuid() {
//		return getUuid(new StringBuilder("cff_"));
//	}
	

}
