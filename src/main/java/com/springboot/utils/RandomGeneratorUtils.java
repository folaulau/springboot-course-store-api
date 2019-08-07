package com.springboot.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
	
	public static List<Integer> getNumbersInRangeAndOffset(int start, int finish, int offset){
		List<Integer> numbers = new ArrayList<>();
		for(int i=start;i<finish;i+=offset) {
			numbers.add(i);
		}
		return numbers;
	}

	
	public static String getUserUid() {
		return "user-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}
	
	public static String getJwtUid() {
		return "jwt-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}
	
	public static String geProductUid() {
		return "prod-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}
	
	public static String getOrderUid() {
		return "order-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}

	public static String getPaymentMethodUid() {
		return "pm-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}

	public static String getLineItemUid() {
		return "li-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}

	public static String getPaymentUid() {
		return "paym-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}

	public static String getS3FileKey(String name) {
		return RandomStringUtils.randomAlphabetic(30)+"_"+name;
	}

	public static String getCommentUid() {
		return "com-"+UUID.randomUUID().toString()+"-"+RandomStringUtils.randomAlphabetic(10);
	}

}
