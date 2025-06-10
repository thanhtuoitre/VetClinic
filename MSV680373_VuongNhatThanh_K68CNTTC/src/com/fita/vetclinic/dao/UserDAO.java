package com.fita.vetclinic.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fita.vetclinic.config.DBConnection;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AccessDataConverterUtil;
import com.fita.vetclinic.utils.DateTimeUtil;

public class UserDAO {

    // Thêm người dùng vào cơ sở dữ liệu
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO tbl_users (fullname, role, gender, birthday, phone, email, imagepath, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getFullname());
            pstmt.setString(2, user.getRole());  // Lưu role
            pstmt.setString(3, user.getGender());
            pstmt.setDate(4, DateTimeUtil.convertUtilDateToSqlDate(user.getBirthday()));
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getImagePath());
            pstmt.setString(8, user.getPassword());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Thêm người dùng thất bại, không có hàng nào được ảnh hưởng.");
            }

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setUserId(generatedKeys.getInt(1));
                System.out.println("Thêm người dùng thành công. UserID mới: " + user.getUserId());
            } else {
                System.out.println("Thêm người dùng thành công nhưng không lấy được UserID tự động.");
            }

        } finally {
            DBConnection.closeResultSet(generatedKeys);
            DBConnection.closePreparedStatement(pstmt);
            DBConnection.closeConnection(conn);
        }
    }

    // Lấy người dùng theo ID
    public User getUserById(int userId) throws SQLException {
        User user = null;
        String sql = "SELECT user_id, fullname, role, gender, birthday, phone, email, imagepath, password FROM tbl_users WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        AccessDataConverterUtil.getString(rs, "fullname"),
                        AccessDataConverterUtil.getString(rs, "gender"),
                        AccessDataConverterUtil.getDate(rs, "birthday"),
                        AccessDataConverterUtil.getString(rs, "phone"),
                        AccessDataConverterUtil.getString(rs, "email"),
                        AccessDataConverterUtil.getString(rs, "imagepath"),
                        AccessDataConverterUtil.getString(rs, "password"),
                        AccessDataConverterUtil.getString(rs, "role")  // Lấy role
                );
            }
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closePreparedStatement(pstmt);
            DBConnection.closeConnection(conn);
        }
        return user;
    }

    // Lấy người dùng theo email
    public User getUserByEmail(String email) throws SQLException {
        User user = null;
        String sql = "SELECT user_id, fullname, role, gender, birthday, phone, email, imagepath, password FROM tbl_users WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        AccessDataConverterUtil.getString(rs, "fullname"),
                        AccessDataConverterUtil.getString(rs, "gender"),
                        AccessDataConverterUtil.getDate(rs, "birthday"),
                        AccessDataConverterUtil.getString(rs, "phone"),
                        AccessDataConverterUtil.getString(rs, "email"),
                        AccessDataConverterUtil.getString(rs, "imagepath"),
                        AccessDataConverterUtil.getString(rs, "password"),
                        AccessDataConverterUtil.getString(rs, "role")  // Lấy role
                );
            }
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closePreparedStatement(pstmt);
            DBConnection.closeConnection(conn);
        }
        return user;
    }

    // Cập nhật thông tin người dùng
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE tbl_users SET fullname = ?, role = ?, gender = ?, birthday = ?, phone = ?, email = ?, imagepath = ?, password = ? WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getFullname());
            pstmt.setString(2, user.getRole());  // Cập nhật role
            pstmt.setString(3, user.getGender());
            pstmt.setDate(4, DateTimeUtil.convertUtilDateToSqlDate(user.getBirthday()));
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getImagePath());
            pstmt.setString(8, user.getPassword());
            pstmt.setInt(9, user.getUserId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Cập nhật người dùng ID " + user.getUserId() + " không thành công (có thể không tìm thấy ID).");
            } else {
                System.out.println("Cập nhật người dùng ID " + user.getUserId() + " thành công.");
            }

        } finally {
            DBConnection.closePreparedStatement(pstmt);
            DBConnection.closeConnection(conn);
        }
    }

    // Xóa người dùng theo ID
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM tbl_users WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Xóa người dùng ID " + userId + " không thành công (có thể không tìm thấy ID).");
            } else {
                System.out.println("Xóa người dùng ID " + userId + " thành công.");
            }
        } finally {
            DBConnection.closePreparedStatement(pstmt);
            DBConnection.closeConnection(conn);
        }
    }

    // Lấy tất cả người dùng
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT user_id, fullname, role, gender, birthday, phone, email, imagepath, password FROM tbl_users";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                userList.add(new User(
                        AccessDataConverterUtil.getString(rs, "fullname"),
                        AccessDataConverterUtil.getString(rs, "gender"),
                        AccessDataConverterUtil.getDate(rs, "birthday"),
                        AccessDataConverterUtil.getString(rs, "phone"),
                        AccessDataConverterUtil.getString(rs, "email"),
                        AccessDataConverterUtil.getString(rs, "imagepath"),
                        AccessDataConverterUtil.getString(rs, "password"),
                        AccessDataConverterUtil.getString(rs, "role")  // Lấy role
                ));
            }
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(conn);
        }
        return userList;
    }
    
    public boolean checkPassword(String email, String inputPassword) {
        String sql = "SELECT password FROM tbl_users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(inputPassword); 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE tbl_users SET password = ? WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean updateAvatar(String email, String imagePath) {
        String sql = "UPDATE tbl_users SET imagepath = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imagePath);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
