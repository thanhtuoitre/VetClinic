package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.Vaccine;
import com.fita.vetclinic.utils.AccessDataConverterUtil;

public class VaccineDAO {

	public void addVaccine(Vaccine vaccine) throws SQLException {
		String sql = "INSERT INTO tbl_vaccines (name, description, manufacture) VALUES (?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, vaccine.getName());
			pstmt.setString(2, vaccine.getDescription());
			pstmt.setString(3, vaccine.getManufacture());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Thêm vắc xin thất bại, không có hàng nào được ảnh hưởng.");
			}

			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				vaccine.setVaccineId(generatedKeys.getInt(1)); // Gán lại ID vào đối tượng Vaccine
				System.out.println("Thêm vắc xin thành công. VaccineID mới: " + vaccine.getVaccineId());
			} else {
				System.out.println("Thêm vắc xin thành công nhưng không lấy được VaccineID tự động.");
			}

		} finally {
			DBConnection.closeResultSet(generatedKeys);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public Vaccine getVaccineById(int vaccineId) throws SQLException {
		Vaccine vaccine = null;
		String sql = "SELECT vaccine_id, name, description, manufacture FROM tbl_vaccines WHERE vaccine_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vaccineId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				vaccine = new Vaccine(AccessDataConverterUtil.getInt(rs, "vaccine_id"),
						AccessDataConverterUtil.getString(rs, "name"),
						AccessDataConverterUtil.getString(rs, "description"),
						AccessDataConverterUtil.getString(rs, "manufacture"));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
		return vaccine;
	}

	public void updateVaccine(Vaccine vaccine) throws SQLException {
		String sql = "UPDATE tbl_vaccines SET name = ?, description = ?, manufacture = ? WHERE vaccine_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vaccine.getName());
			pstmt.setString(2, vaccine.getDescription());
			pstmt.setString(3, vaccine.getManufacture());
			pstmt.setInt(4, vaccine.getVaccineId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Cập nhật vắc xin ID " + vaccine.getVaccineId()
						+ " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Cập nhật vắc xin ID " + vaccine.getVaccineId() + " thành công.");
			}

		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public void deleteVaccine(int vaccineId) throws SQLException {
		String sql = "DELETE FROM tbl_vaccines WHERE vaccine_id = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vaccineId);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("Xóa vắc xin ID " + vaccineId + " không thành công (có thể không tìm thấy ID).");
			} else {
				System.out.println("Xóa vắc xin ID " + vaccineId + " thành công.");
			}
		} finally {
			DBConnection.closePreparedStatement(pstmt);
			DBConnection.closeConnection(conn);
		}
	}

	public List<Vaccine> getAllVaccines() throws SQLException {
		List<Vaccine> vaccineList = new ArrayList<>();
		String sql = "SELECT vaccine_id, name, description, manufacture FROM tbl_vaccines";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				vaccineList.add(new Vaccine(AccessDataConverterUtil.getInt(rs, "vaccine_id"),
						AccessDataConverterUtil.getString(rs, "name"),
						AccessDataConverterUtil.getString(rs, "description"),
						AccessDataConverterUtil.getString(rs, "manufacture")));
			}
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(stmt);
			DBConnection.closeConnection(conn);
		}
		return vaccineList;
	}

}