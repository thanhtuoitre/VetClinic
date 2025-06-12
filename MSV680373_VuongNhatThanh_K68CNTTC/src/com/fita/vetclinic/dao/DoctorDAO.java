package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.Doctor;
import com.fita.vetclinic.utils.AccessDataConverterUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoctorDAO {

	public void addDoctor(Doctor doctor) throws SQLException {
		String sql = "INSERT INTO tbl_doctors (user_id, is_active, specialization) VALUES (?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, doctor.getUserId());
			// ĐÃ SỬA: Sử dụng setBoolean() để ghi giá trị boolean trực tiếp vào Access
			pstmt.setBoolean(2, doctor.getIs_active());
			pstmt.setString(3, doctor.getSpecialization());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm bác sĩ thất bại, không có hàng nào được ảnh hưởng.");
			}

			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				doctor.setDoctorId(generatedKeys.getInt(1)); // Gán lại ID vào đối tượng Doctor
				System.out.println("Thêm bác sĩ thành công. DoctorID mới: " + doctor.getDoctorId());
			} else {
				System.out.println("Thêm bác sĩ thành công nhưng không lấy được DoctorID tự động.");
			}

		} finally {
			DBConnection.closeResultSet(generatedKeys);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public Doctor getDoctorById(int doctorId) throws SQLException {
		Doctor doctor = null;
		String sql = "SELECT doctor_id, user_id, is_active, specialization FROM tbl_doctors WHERE doctor_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, doctorId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				doctor = new Doctor(AccessDataConverterUtil.getInt(rs, "doctor_id"),
						AccessDataConverterUtil.getInt(rs, "user_id"),
						AccessDataConverterUtil.getBoolean(rs, "is_active"), // ĐÃ SỬA: Sử dụng getBoolean()
						AccessDataConverterUtil.getString(rs, "specialization"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return doctor;
	}

	public void updateDoctor(Doctor doctor) throws SQLException {
		String sql = "UPDATE tbl_doctors SET user_id = ?, is_active = ?, specialization = ? WHERE doctor_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, doctor.getUserId());
			// ĐÃ SỬA: Sử dụng setBoolean()
			pstmt.setBoolean(2, doctor.getIs_active());
			pstmt.setString(3, doctor.getSpecialization());
			pstmt.setInt(4, doctor.getDoctorId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println(
						"Cập nhật bác sĩ ID " + doctor.getDoctorId() + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Cập nhật bác sĩ ID " + doctor.getDoctorId() + " thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteDoctor(int doctorId) throws SQLException {
		String sql = "DELETE FROM tbl_doctors WHERE doctor_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, doctorId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa bác sĩ ID " + doctorId + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Xóa bác sĩ ID " + doctorId + " thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public ObservableList<Doctor> getAllDoctors() throws SQLException {
	    ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
	    String sql = "SELECT doctor_id, user_id, is_active, specialization FROM tbl_doctors";

	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = DBConnection.getConnection();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(sql);

	        while (rs.next()) {
	            doctorList.add(new Doctor(
	                    AccessDataConverterUtil.getInt(rs, "doctor_id"),
	                    AccessDataConverterUtil.getInt(rs, "user_id"),
	                    AccessDataConverterUtil.getBoolean(rs, "is_active"),
	                    AccessDataConverterUtil.getString(rs, "specialization")
	            ));
	        }
	    } finally {
	        DBConnection.closeResultSet(rs);
	        DBConnection.closeStatement(stmt);
	        DBConnection.closeConnection(conn);
	    }
	    return doctorList;
	}

	public String getDoctorName(int doctorId) {
	    String sql = """
	        SELECT u.fullname
	        FROM tbl_doctors d
	        JOIN tbl_users u ON d.user_id = u.user_id
	        WHERE d.doctor_id = ?
	        """;

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, doctorId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) return rs.getString("fullname");
	        }
	    } catch (Exception e) {
	        System.err.println("Lỗi lấy tên bác sĩ: " + e.getMessage());
	    }
	    return "Không xác định";
	}

}