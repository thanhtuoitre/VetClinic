package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.Appointment;
import com.fita.vetclinic.utils.AccessDataConverterUtil;
import com.fita.vetclinic.utils.DateTimeUtil;

public class AppointmentDAO {

	public void addAppointment(Appointment appointment) throws SQLException {
		String sql = "INSERT INTO tbl_appointments (pet_id, doctor_id, appointment_date, reason, status) VALUES (?, ?, ?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, appointment.getPetId());
			pstmt.setInt(2, appointment.getDoctorId());
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(appointment.getAppointmentDate()));
			pstmt.setString(4, appointment.getReason());
			pstmt.setString(5, appointment.getStatus());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm cuộc hẹn thất bại, không có hàng nào được ảnh hưởng.");
			}

			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				appointment.setAppointmentId(generatedKeys.getInt(1)); // Gán lại ID vào đối tượng Appointment
				System.out.println("Thêm cuộc hẹn thành công. AppointmentID mới: " + appointment.getAppointmentId());
			} else {
				System.out.println("Thêm cuộc hẹn thành công nhưng không lấy được AppointmentID tự động.");
			}

		} finally {
			DBConnection.closeResultSet(generatedKeys);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public Appointment getAppointmentById(int appointmentId) throws SQLException {
		Appointment appointment = null;
		String sql = "SELECT appointment_id, pet_id, doctor_id, appointment_date, reason, status FROM tbl_appointments WHERE appointment_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, appointmentId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				appointment = new Appointment(AccessDataConverterUtil.getInt(rs, "appointment_id"),
						AccessDataConverterUtil.getInt(rs, "pet_id"), AccessDataConverterUtil.getInt(rs, "doctor_id"),
						AccessDataConverterUtil.getDate(rs, "appointment_date"), // Đọc Date
						AccessDataConverterUtil.getString(rs, "reason"),
						AccessDataConverterUtil.getString(rs, "status"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return appointment;
	}

	public void updateAppointment(Appointment appointment) throws SQLException {
		String sql = "UPDATE tbl_appointments SET pet_id = ?, doctor_id = ?, appointment_date = ?, reason = ?, status = ? WHERE appointment_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, appointment.getPetId());
			pstmt.setInt(2, appointment.getDoctorId());
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(appointment.getAppointmentDate()));
			pstmt.setString(4, appointment.getReason());
			pstmt.setString(5, appointment.getStatus());
			pstmt.setInt(6, appointment.getAppointmentId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật cuộc hẹn ID " + appointment.getAppointmentId()
						+ " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Cập nhật cuộc hẹn ID " + appointment.getAppointmentId() + " thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteAppointment(int appointmentId) throws SQLException {
		String sql = "DELETE FROM tbl_appointments WHERE appointment_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, appointmentId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out
						.println("Xóa cuộc hẹn ID " + appointmentId + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Xóa cuộc hẹn ID " + appointmentId + " thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<Appointment> getAllAppointments() throws SQLException {
		List<Appointment> appointmentList = new ArrayList<>();
		String sql = "SELECT appointment_id, pet_id, doctor_id, appointment_date, reason, status FROM tbl_appointments";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				appointmentList.add(new Appointment(AccessDataConverterUtil.getInt(rs, "appointment_id"),
						AccessDataConverterUtil.getInt(rs, "pet_id"), AccessDataConverterUtil.getInt(rs, "doctor_id"),
						AccessDataConverterUtil.getDate(rs, "appointment_date"),
						AccessDataConverterUtil.getString(rs, "reason"),
						AccessDataConverterUtil.getString(rs, "status")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return appointmentList;
	}

	public int countAppointmentsInCurrentMonth() {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM tbl_appointments WHERE MONTH(appointment_date) = MONTH(CURRENT_DATE) AND YEAR(appointment_date) = YEAR(CURRENT_DATE)";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

}