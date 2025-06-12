package com.fita.vetclinic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.Pet;
import com.fita.vetclinic.utils.DateTimeUtil;

public class PetDAO {

    public void addPet(Pet pet) throws SQLException {
        String sql = "INSERT INTO tbl_pets (name, species, breed, gender, birthdate, weight, user_id, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getSpecies());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getGender());
            pstmt.setDate(5, DateTimeUtil.convertUtilDateToSqlDate(pet.getBirthdate()));
            pstmt.setDouble(6, pet.getWeight());
            pstmt.setInt(7, pet.getuserId()); // user_id
            pstmt.setString(8, pet.getImagePath());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Thêm thú cưng thất bại.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setPetId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Pet getPetById(int petId) throws SQLException {
        String sql = "SELECT * FROM tbl_pets WHERE pet_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getString("gender"),
                        rs.getDate("birthdate"),
                        rs.getDouble("weight"),
                        rs.getInt("user_id"),
                        rs.getString("image_path")
                    );
                }
            }
        }
        return null;
    }

    public void updatePet(Pet pet) throws SQLException {
        String sql = "UPDATE tbl_pets SET name=?, species=?, breed=?, gender=?, birthdate=?, weight=?, user_id=?, image_path=? WHERE pet_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getSpecies());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getGender());
            pstmt.setDate(5, DateTimeUtil.convertUtilDateToSqlDate(pet.getBirthdate()));
            pstmt.setDouble(6, pet.getWeight());
            pstmt.setInt(7, pet.getuserId());
            pstmt.setString(8, pet.getImagePath());
            pstmt.setInt(9, pet.getPetId());

            pstmt.executeUpdate();
        }
    }

    public void deletePet(int petId) throws SQLException {
        String sql = "DELETE FROM tbl_pets WHERE pet_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, petId);
            pstmt.executeUpdate();
        }
    }

    public List<Pet> getAllPets() throws SQLException {
        String sql = "SELECT * FROM tbl_pets";
        List<Pet> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("name"),
                    rs.getString("species"),
                    rs.getString("breed"),
                    rs.getString("gender"),
                    rs.getDate("birthdate"),
                    rs.getDouble("weight"),
                    rs.getInt("user_id"),
                    rs.getString("image_path")
                ));
            }
        }
        return list;
    }

    public List<Pet> getPetsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM tbl_pets WHERE user_id = ?";
        List<Pet> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getString("gender"),
                        rs.getDate("birthdate"),
                        rs.getDouble("weight"),
                        rs.getInt("user_id"),
                        rs.getString("image_path")
                    ));
                }
            }
        }
        return list;
    }

    public int countPetsWithAppointmentsThisMonth() {
        String sql = """
            SELECT COUNT(*) FROM tbl_pets p
            INNER JOIN tbl_appointments a ON p.pet_id = a.pet_id
            WHERE MONTH(a.appointment_date) = MONTH(CURRENT_DATE)
              AND YEAR(a.appointment_date) = YEAR(CURRENT_DATE)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

	public boolean isPetOwnedByUser(int petId, int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT COUNT(*) FROM tbl_pets WHERE pet_id = ? AND user_id = ?")) {

            stmt.setInt(1, petId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi kiểm tra thú cưng thuộc người dùng: " + e.getMessage());
        }
        return false;
    }

	public String getPetName(int petId) {
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(
	             "SELECT name FROM tbl_pets WHERE pet_id = ?")) {

	        stmt.setInt(1, petId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) return rs.getString("name");
	        }
	    } catch (Exception e) {
	        System.err.println("Lỗi lấy tên thú cưng: " + e.getMessage());
	    }
	    return "Không xác định";
	}

}
