package com.fita.vetclinic.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateTimeUtil {

	// Định dạng ngày tháng mặc định cho ứng dụng (có thể thay đổi)
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static java.sql.Date convertUtilDateToSqlDate(Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		return new java.sql.Date(utilDate.getTime());
	}

	public static Date convertSqlDateToUtilDate(java.sql.Date sqlDate) {
		if (sqlDate == null) {
			return null;
		}
		return new Date(sqlDate.getTime());
	}

	public static Date convertLocalDateToUtilDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convertUtilDateToLocalDate(Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime convertUtilDateToLocalDateTime(Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date convertLocalDateTimeToUtilDate(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static String format(Date date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		// Chuyển đổi Date sang Instant rồi sang ZonedDateTime để định dạng
		return date.toInstant().atZone(ZoneId.systemDefault()).format(formatter);
	}

	public static Date parseDate(String dateString) {
		return parseDate(dateString, DEFAULT_DATE_FORMAT);
	}

	public static Date parseDate(String dateString, String format) {
		if (ValidationUtil.isEmpty(dateString)) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			LocalDate localDate = LocalDate.parse(dateString, formatter);
			return convertLocalDateToUtilDate(localDate);
		} catch (DateTimeParseException e) {
			System.err.println(
					"Lỗi phân tích ngày tháng: " + dateString + " với định dạng " + format + " - " + e.getMessage());
			return null;
		}
	}

	public static Date parseDateTime(String datetimeString) {
		return parseDateTime(datetimeString, DEFAULT_DATETIME_FORMAT);
	}

	public static Date parseDateTime(String datetimeString, String format) {
		if (ValidationUtil.isEmpty(datetimeString)) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			LocalDateTime localDateTime = LocalDateTime.parse(datetimeString, formatter);
			return convertLocalDateTimeToUtilDate(localDateTime);
		} catch (DateTimeParseException e) {
			System.err.println(
					"Lỗi phân tích ngày giờ: " + datetimeString + " với định dạng " + format + " - " + e.getMessage());
			return null;
		}
	}
}