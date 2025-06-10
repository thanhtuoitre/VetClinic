package com.fita.vetclinic.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	public static boolean isEmpty(String text) {
		return text == null || text.trim().isEmpty();
	}

	public static boolean isPositiveInteger(String text) {
		if (isEmpty(text)) {
			return false;
		}
		try {
			int num = Integer.parseInt(text);
			return num > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isPositiveDouble(String text) {
		if (isEmpty(text)) {
			return false;
		}
		try {
			double num = Double.parseDouble(text);
			return num > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		if (isEmpty(phoneNumber)) {
			return false;
		}
		// Cho phép số từ 8 đến 12 chữ số, chỉ chứa số và dấu + ở đầu
		String phoneRegex = "^\\+?[0-9]{8,12}$";
		Pattern pattern = Pattern.compile(phoneRegex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	public static boolean isValidDate(String dateString, String format) {
		if (isEmpty(dateString)) {
			return false;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			LocalDate.parse(dateString, formatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}