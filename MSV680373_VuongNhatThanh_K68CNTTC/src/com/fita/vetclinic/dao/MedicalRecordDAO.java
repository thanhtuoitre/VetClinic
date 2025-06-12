package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.MedicalRecord;
import com.fita.vetclinic.utils.AccessDataConverterUtil;
import com.fita.vetclinic.utils.DateTimeUtil;

public class MedicalRecordDAO {

	public void addMedicalRecord(MedicalRecord medicalRecord) throws SQLException {
		String sql = "INSERT INTO tbl_medical_records (pet_id, doctor_id, record_date, diagnosis, treatment, notes) VALUES (?, ?, ?, ?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, medicalRecord.getPetId());
			pstmt.setInt(2, medicalRecord.getDoctorId());
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(medicalRecord.getRecordDate()));
			pstmt.setString(4, medicalRecord.getDiagnosis());
			pstmt.setString(5, medicalRecord.getTreatment());
			pstmt.setString(6, medicalRecord.getNotes());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm hồ sơ y tế thất bại, không có hàng nào được ảnh hưởng.");
			}

			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				medicalRecord.setRecordId(generatedKeys.getInt(1));
				System.out.println("Thêm hồ sơ y tế thành công. RecordID mới: " + medicalRecord.getRecordId());
			} else {
				System.out.println("Thêm hồ sơ y tế thành công nhưng không lấy được RecordID tự động.");
			}

		} finally {
			DBConnection.closeResultSet(generatedKeys);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public MedicalRecord getMedicalRecordById(int recordId) throws SQLException {
		MedicalRecord medicalRecord = null;
		String sql = "SELECT record_id, pet_id, doctor_id, record_date, diagnosis, treatment, notes FROM tbl_medical_records WHERE record_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				medicalRecord = new MedicalRecord(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "pet_id"), AccessDataConverterUtil.getInt(rs, "doctor_id"),
						AccessDataConverterUtil.getDate(rs, "record_date"),
						AccessDataConverterUtil.getString(rs, "diagnosis"),
						AccessDataConverterUtil.getString(rs, "treatment"),
						AccessDataConverterUtil.getString(rs, "notes"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return medicalRecord;
	}

	public void updateMedicalRecord(MedicalRecord medicalRecord) throws SQLException {
		String sql = "UPDATE tbl_medical_records SET pet_id = ?, doctor_id = ?, record_date = ?, diagnosis = ?, treatment = ?, notes = ? WHERE record_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, medicalRecord.getPetId());
			pstmt.setInt(2, medicalRecord.getDoctorId());
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(medicalRecord.getRecordDate()));
			pstmt.setString(4, medicalRecord.getDiagnosis());
			pstmt.setString(5, medicalRecord.getTreatment());
			pstmt.setString(6, medicalRecord.getNotes());
			pstmt.setInt(7, medicalRecord.getRecordId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật hồ sơ y tế ID " + medicalRecord.getRecordId()
						+ " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Cập nhật hồ sơ y tế ID " + medicalRecord.getRecordId() + " thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteMedicalRecord(int recordId) throws SQLException {
		String sql = "DELETE FROM tbl_medical_records WHERE record_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa hồ sơ y tế ID " + recordId + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Xóa hồ sơ y tế ID " + recordId + " thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<MedicalRecord> getAllMedicalRecords() throws SQLException {
		List<MedicalRecord> medicalRecordList = new ArrayList<>();

		String sql = "SELECT record_id, pet_id, doctor_id, record_date, diagnosis, treatment, notes FROM tbl_medical_records";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				medicalRecordList.add(new MedicalRecord(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "pet_id"), AccessDataConverterUtil.getInt(rs, "doctor_id"),
						AccessDataConverterUtil.getDate(rs, "record_date"),
						AccessDataConverterUtil.getString(rs, "diagnosis"),
						AccessDataConverterUtil.getString(rs, "treatment"),
						AccessDataConverterUtil.getString(rs, "notes")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return medicalRecordList;
	}

}