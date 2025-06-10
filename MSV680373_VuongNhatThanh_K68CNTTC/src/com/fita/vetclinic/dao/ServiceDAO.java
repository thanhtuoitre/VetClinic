package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.Service;
import com.fita.vetclinic.utils.AccessDataConverterUtil;

public class ServiceDAO {

	public void addService(Service service) throws SQLException {
		String sql = "INSERT INTO tbl_services (name, price, description) VALUES (?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, service.getName());
			pstmt.setDouble(2, service.getPrice()); // Dùng setDouble cho kiểu Currency/Number trong Access
			pstmt.setString(3, service.getDescription());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm dịch vụ thất bại, không có hàng nào được ảnh hưởng.");
			}

			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				service.setServiceId(generatedKeys.getInt(1)); // Gán lại ID vào đối tượng Service
				System.out.println("Thêm dịch vụ thành công. ServiceID mới: " + service.getServiceId());
			} else {
				System.out.println("Thêm dịch vụ thành công nhưng không lấy được ServiceID tự động.");
			}

		} finally {
			DBConnection.closeResultSet(generatedKeys);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public Service getServiceById(int serviceId) throws SQLException {
		Service service = null;
		String sql = "SELECT service_id, name, price, description FROM tbl_services WHERE service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, serviceId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				service = new Service(AccessDataConverterUtil.getInt(rs, "service_id"),
						AccessDataConverterUtil.getString(rs, "name"), AccessDataConverterUtil.getDouble(rs, "price"),
						AccessDataConverterUtil.getString(rs, "description"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return service;
	}

	public void updateService(Service service) throws SQLException {
		String sql = "UPDATE tbl_services SET name = ?, price = ?, description = ? WHERE service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, service.getName());
			pstmt.setDouble(2, service.getPrice());
			pstmt.setString(3, service.getDescription());
			pstmt.setInt(4, service.getServiceId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật dịch vụ ID " + service.getServiceId()
						+ " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Cập nhật dịch vụ ID " + service.getServiceId() + " thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteService(int serviceId) throws SQLException {
		String sql = "DELETE FROM tbl_services WHERE service_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, serviceId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa dịch vụ ID " + serviceId + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Xóa dịch vụ ID " + serviceId + " thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<Service> getAllServices() throws SQLException {
		List<Service> serviceList = new ArrayList<>();
		String sql = "SELECT service_id, name, price, description FROM tbl_services";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				serviceList.add(new Service(AccessDataConverterUtil.getInt(rs, "service_id"),
						AccessDataConverterUtil.getString(rs, "name"), AccessDataConverterUtil.getDouble(rs, "price"),
						AccessDataConverterUtil.getString(rs, "description")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return serviceList;
	}

}