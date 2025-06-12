package com.fita.vetclinic.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
	
	private final static String DB_PATH = "resources/databases/VetClinicDB.accdb";
	private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conn = DriverManager.getConnection(DB_URL, "", "");
		} catch (Exception e) {
			System.out.println("Kết nối database thất bại!");
			e.printStackTrace();
		}
		return conn;	
	}
	
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println("Đã đóng kết nối!");
				e.printStackTrace();
			}
		}
	}
	
	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
	