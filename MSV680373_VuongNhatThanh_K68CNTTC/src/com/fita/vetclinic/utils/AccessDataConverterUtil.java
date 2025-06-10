package com.fita.vetclinic.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AccessDataConverterUtil {

	public static boolean convertYesNoToBoolean(String yesNoString) {
		return "Yes".equalsIgnoreCase(yesNoString) || "-1".equals(yesNoString);
	}

	public static boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
		// Access thường trả về boolean trực tiếp hoặc 0/-1 cho Yes/No.
		// Dùng getBoolean là an toàn nhất.
		return rs.getBoolean(columnLabel);
	}

	public static String convertBooleanToYesNo(boolean value) {
		return value ? "Yes" : "No";
	}

	public static String getString(ResultSet rs, String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}

	public static int getInt(ResultSet rs, String columnLabel) throws SQLException {
		int value = rs.getInt(columnLabel);
		// Kiểm tra xem giá trị có phải là 0 do null không, cần dùng wasNull()
		if (rs.wasNull()) {
			return 0; // Hoặc một giá trị mặc định khác nếu cần cho int nguyên thủy
		}
		return value;
	}

	public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
		int value = rs.getInt(columnLabel);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}

	public static double getDouble(ResultSet rs, String columnLabel) throws SQLException {
		double value = rs.getDouble(columnLabel);
		if (rs.wasNull()) {
			return 0.0;
		}
		return value;
	}

	public static Date getDate(ResultSet rs, String columnLabel) throws SQLException {
		java.sql.Date sqlDate = rs.getDate(columnLabel);
		return DateTimeUtil.convertSqlDateToUtilDate(sqlDate);
	}
	
	public static boolean isSameDay(Date utilDate, LocalDate localDate) {
	    if (utilDate == null || localDate == null) return false;

	    LocalDate converted = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    return converted.isEqual(localDate);
	}

}