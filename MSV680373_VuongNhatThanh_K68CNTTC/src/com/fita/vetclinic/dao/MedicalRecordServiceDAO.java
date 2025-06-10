package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.MedicalRecordService;
import com.fita.vetclinic.utils.AccessDataConverterUtil;

public class MedicalRecordServiceDAO {

	public void addMedicalRecordService(MedicalRecordService mrs) throws SQLException {
		String sql = "INSERT INTO tbl_medicalrecord_services (record_id, service_id, quantity, service_price_at_time) VALUES (?, ?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, mrs.getRecordId());
			pstmt.setInt(2, mrs.getServiceId());
			pstmt.setInt(3, mrs.getQuantity());
			pstmt.setDouble(4, mrs.getServicePriceAtTime());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm dịch vụ vào hồ sơ y tế thất bại, không có hàng nào được ảnh hưởng.");
			} else {
				System.out.println("Thêm dịch vụ (record_id=" + mrs.getRecordId() + ", service_id=" + mrs.getServiceId()
						+ ") vào hồ sơ y tế thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public MedicalRecordService getMedicalRecordServiceByIds(int recordId, int serviceId) throws SQLException {
		MedicalRecordService mrs = null;
		String sql = "SELECT record_id, service_id, quantity, service_price_at_time FROM tbl_medicalrecord_services WHERE record_id = ? AND service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			pstmt.setInt(2, serviceId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				mrs = new MedicalRecordService(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "service_id"),
						AccessDataConverterUtil.getInt(rs, "quantity"),
						AccessDataConverterUtil.getDouble(rs, "service_price_at_time"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return mrs;
	}

	public void updateMedicalRecordService(MedicalRecordService mrs) throws SQLException {
		String sql = "UPDATE tbl_medicalrecord_services SET quantity = ?, service_price_at_time = ? WHERE record_id = ? AND service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, mrs.getQuantity());
			pstmt.setDouble(2, mrs.getServicePriceAtTime());
			pstmt.setInt(3, mrs.getRecordId());
			pstmt.setInt(4, mrs.getServiceId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật dịch vụ (record_id=" + mrs.getRecordId() + ", service_id="
						+ mrs.getServiceId() + ") không thành công (có thể không tìm thấy).");
			} else {
				System.out.println("Cập nhật dịch vụ (record_id=" + mrs.getRecordId() + ", service_id="
						+ mrs.getServiceId() + ") thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteMedicalRecordService(int recordId, int serviceId) throws SQLException {
		String sql = "DELETE FROM tbl_medicalrecord_services WHERE record_id = ? AND service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			pstmt.setInt(2, serviceId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa dịch vụ (record_id=" + recordId + ", service_id=" + serviceId
						+ ") không thành công (có thể không tìm thấy).");
			} else {
				System.out
						.println("Xóa dịch vụ (record_id=" + recordId + ", service_id=" + serviceId + ") thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<MedicalRecordService> getServicesByMedicalRecordId(int recordId) throws SQLException {
		List<MedicalRecordService> mrsList = new ArrayList<>();
		String sql = "SELECT record_id, service_id, quantity, service_price_at_time FROM tbl_medicalrecord_services WHERE record_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recordId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mrsList.add(new MedicalRecordService(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "service_id"),
						AccessDataConverterUtil.getInt(rs, "quantity"),
						AccessDataConverterUtil.getDouble(rs, "service_price_at_time")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return mrsList;
	}

	public List<MedicalRecordService> getAllMedicalRecordServices() throws SQLException {
		List<MedicalRecordService> mrsList = new ArrayList<>();
		String sql = "SELECT record_id, service_id, quantity, service_price_at_time FROM tbl_medicalrecord_services";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				mrsList.add(new MedicalRecordService(AccessDataConverterUtil.getInt(rs, "record_id"),
						AccessDataConverterUtil.getInt(rs, "service_id"),
						AccessDataConverterUtil.getInt(rs, "quantity"),
						AccessDataConverterUtil.getDouble(rs, "service_price_at_time")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return mrsList;
	}

}