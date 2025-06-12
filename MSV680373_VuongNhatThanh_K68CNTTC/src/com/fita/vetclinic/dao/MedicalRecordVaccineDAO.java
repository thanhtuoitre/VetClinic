package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date; // java.util.Date cho model
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.MedicalRecordVaccine;
import com.fita.vetclinic.utils.AccessDataConverterUtil;
import com.fita.vetclinic.utils.DateTimeUtil;

public class MedicalRecordVaccineDAO {

	public void addMedicalRecordVaccine(MedicalRecordVaccine mrv) throws SQLException {
		String sql = "INSERT INTO tbl_medicalrecord_vaccines (record_id, vaccine_id, vaccination_date, batch_number, next_due_date) VALUES (?, ?, ?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, mrv.getRecordId());
			pstmt.setInt(2, mrv.getVaccineId());
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(mrv.getVaccinationDate()));
			pstmt.setString(4, mrv.getBatchNumber());
			pstmt.setDate(5, DateTimeUtil.convertUtilDateToSqlDate(mrv.getNextDueDate()));

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm vắc xin vào hồ sơ y tế thất bại, không có hàng nào được ảnh hưởng.");
			} else {
				System.out.println("Thêm vắc xin (record_id=" + mrv.getRecordId() + ", vaccine_id=" + mrv.getVaccineId()
						+ ", date=" + DateTimeUtil.format(mrv.getVaccinationDate()) + ") thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public MedicalRecordVaccine getMedicalRecordVaccineByIds(int recordId, int vaccineId, Date vaccinationDate)
			throws SQLException {
		MedicalRecordVaccine mrv = null;
		String sql = "SELECT record_id, vaccine_id, vaccination_date, batch_number, next_due_date FROM tbl_medicalrecord_vaccines WHERE record_id = ? AND vaccine_id = ? AND vaccination_date = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			pstmt.setInt(2, vaccineId);
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(vaccinationDate));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				mrv = new MedicalRecordVaccine(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "vaccine_id"),
						AccessDataConverterUtil.getDate(rs, "vaccination_date"),
						AccessDataConverterUtil.getString(rs, "batch_number"),
						AccessDataConverterUtil.getDate(rs, "next_due_date"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return mrv;
	}

	public void updateMedicalRecordVaccine(MedicalRecordVaccine mrv) throws SQLException {
		String sql = "UPDATE tbl_medicalrecord_vaccines SET batch_number = ?, next_due_date = ? WHERE record_id = ? AND vaccine_id = ? AND vaccination_date = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mrv.getBatchNumber());
			pstmt.setDate(2, DateTimeUtil.convertUtilDateToSqlDate(mrv.getNextDueDate()));
			pstmt.setInt(3, mrv.getRecordId());
			pstmt.setInt(4, mrv.getVaccineId());
			pstmt.setDate(5, DateTimeUtil.convertUtilDateToSqlDate(mrv.getVaccinationDate()));

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật vắc xin (record_id=" + mrv.getRecordId() + ", vaccine_id="
						+ mrv.getVaccineId() + ", date=" + DateTimeUtil.format(mrv.getVaccinationDate())
						+ ") không thành công (có thể không tìm thấy).");
			} else {
				System.out.println(
						"Cập nhật vắc xin (record_id=" + mrv.getRecordId() + ", vaccine_id=" + mrv.getVaccineId()
								+ ", date=" + DateTimeUtil.format(mrv.getVaccinationDate()) + ") thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteMedicalRecordVaccine(int recordId, int vaccineId, Date vaccinationDate) throws SQLException {
		String sql = "DELETE FROM tbl_medicalrecord_vaccines WHERE record_id = ? AND vaccine_id = ? AND vaccination_date = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			pstmt.setInt(2, vaccineId);
			pstmt.setDate(3, DateTimeUtil.convertUtilDateToSqlDate(vaccinationDate));

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa vắc xin (record_id=" + recordId + ", vaccine_id=" + vaccineId + ", date="
						+ DateTimeUtil.format(vaccinationDate) + ") không thành công (có thể không tìm thấy).");
			} else {
				System.out.println("Xóa vắc xin (record_id=" + recordId + ", vaccine_id=" + vaccineId + ", date="
						+ DateTimeUtil.format(vaccinationDate) + ") thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<MedicalRecordVaccine> getVaccinesByMedicalRecordId(int recordId) throws SQLException {
		List<MedicalRecordVaccine> mrvList = new ArrayList<>();
		String sql = "SELECT record_id, vaccine_id, vaccination_date, batch_number, next_due_date FROM tbl_medicalrecord_vaccines WHERE record_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mrvList.add(new MedicalRecordVaccine(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "vaccine_id"),
						AccessDataConverterUtil.getDate(rs, "vaccination_date"),
						AccessDataConverterUtil.getString(rs, "batch_number"),
						AccessDataConverterUtil.getDate(rs, "next_due_date")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return mrvList;
	}

	public List<MedicalRecordVaccine> getAllMedicalRecordVaccines() throws SQLException {
		List<MedicalRecordVaccine> mrvList = new ArrayList<>();
		String sql = "SELECT record_id, vaccine_id, vaccination_date, batch_number, next_due_date FROM tbl_medicalrecord_vaccines";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				mrvList.add(new MedicalRecordVaccine(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "vaccine_id"),
						AccessDataConverterUtil.getDate(rs, "vaccination_date"),
						AccessDataConverterUtil.getString(rs, "batch_number"),
						AccessDataConverterUtil.getDate(rs, "next_due_date")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return mrvList;
	}
}