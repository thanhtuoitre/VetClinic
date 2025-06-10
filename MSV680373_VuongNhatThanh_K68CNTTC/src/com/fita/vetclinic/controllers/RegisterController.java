package com.fita.vetclinic.controllers;

import java.sql.SQLException;
import java.util.Date;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.ValidationUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField tfFullname; // Họ và tên
    @FXML
    private TextField tfEmail; // Email
    @FXML
    private TextField tfPhone; // Số điện thoại
    @FXML
    private DatePicker dpBirthday; // Ngày sinh
    @FXML
    private RadioButton rbMale; // Giới tính Nam
    @FXML
    private RadioButton rbFemale; // Giới tính Nữ
    @FXML
    private PasswordField pfPassword; // Mật khẩu
    @FXML
    private PasswordField pfConfirmPassword; // Xác nhận mật khẩu
    @FXML
    private TextField tfPassword; // Mật khẩu hiển thị khi chọn checkbox
    @FXML
    private TextField tfConfirmPassword; // Xác nhận mật khẩu hiển thị khi chọn checkbox
    @FXML
    private CheckBox cbShowPassword; // Checkbox để hiển thị mật khẩu
    @FXML
    private Button btnRegister; // Nút Đăng ký
    @FXML
    private Button btnCancel; // Nút Quay lại
    @FXML
    private Label lblMessage; // Thông báo lỗi nếu có

    private UserDAO userDAO = new UserDAO(); // Tạo đối tượng UserDAO để thao tác với cơ sở dữ liệu
    @FXML
    private void handleRegister() {
        String fullname = tfFullname.getText(); // Lấy họ và tên
        String email = tfEmail.getText(); // Lấy email
        String phone = tfPhone.getText(); // Lấy số điện thoại
        String gender = getSelectedGender(); // Lấy giới tính từ RadioButton
        Date birthday = null;

        // Kiểm tra xem người dùng có chọn ngày sinh chưa
        if (dpBirthday.getValue() == null) {
            AlertUtil.showErrorAlert("Lỗi", "Vui lòng chọn ngày sinh.");
            return;
        } else {
            birthday = java.sql.Date.valueOf(dpBirthday.getValue()); // Lấy ngày sinh
        }

        // Đặt giá trị mặc định cho imagePath nếu không có input
        String imagePath = "/resources/images/icons/user.png"; // Đặt giá trị mặc định cho ảnh người dùng nếu không nhập

        String password = pfPassword.getText(); // Lấy mật khẩu
        String confirmPassword = pfConfirmPassword.getText(); // Lấy xác nhận mật khẩu
        String role = "Khách hàng"; // Vai trò mặc định là Khách hàng

        // Kiểm tra nếu các trường không được để trống
        if (ValidationUtil.isEmpty(fullname) || ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(phone)
                || ValidationUtil.isEmpty(password) || ValidationUtil.isEmpty(confirmPassword)
                || ValidationUtil.isEmpty(gender)) {
            AlertUtil.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng ký.");
            return;
        }

        // Kiểm tra nếu mật khẩu và xác nhận mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            AlertUtil.showErrorAlert("Lỗi", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return;
        }

        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        try {
            if (userDAO.getUserByEmail(email) != null) {
                AlertUtil.showErrorAlert("Lỗi", "Email đã được sử dụng.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Đã xảy ra lỗi khi kiểm tra email.");
            return;
        }

        // Tạo đối tượng User và thêm vào cơ sở dữ liệu
        try {
            User newUser = new User(fullname, gender, birthday, phone, email, imagePath, password, role);
            userDAO.addUser(newUser);

            // Đăng ký thành công, chuyển sang màn hình đăng nhập
            AlertUtil.showInfoAlert("Thông báo", "Đăng ký thành công!");
            loadLoginScene(); // Chuyển sang màn hình đăng nhập ngay sau khi đăng ký thành công
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi SQL", "Đã có lỗi xảy ra trong quá trình đăng ký, vui lòng thử lại.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi không xác định", "Đã có lỗi xảy ra, vui lòng thử lại.");
        }
    }


    // Phương thức chuyển sang màn hình đăng nhập
    private void loadLoginScene() {
        try {
            // Lấy Stage hiện tại
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            
            // Load FXML của màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));
            
            // Tải giao diện
            Parent root = loader.load();
            
            // Tạo Scene cho màn hình đăng nhập
            Scene loginScene = new Scene(root);
            
            // Đổi Scene của Stage hiện tại sang màn hình đăng nhập
            stage.setScene(loginScene);
            stage.setTitle("VetClinic - Đăng nhập"); // Thay đổi tiêu đề cửa sổ
            stage.show(); // Hiển thị cửa sổ đăng nhập
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình đăng nhập.");
        }
    }

    // Phương thức quay lại màn hình đăng nhập
    @FXML
    private void handleCancel() {
        try {
            Stage stage = (Stage) btnCancel.getScene().getWindow(); // Lấy cửa sổ hiện tại
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));
            Parent root = loader.load(); // Load giao diện đăng nhập
            Scene loginScene = new Scene(root); // Tạo Scene mới cho màn hình đăng nhập
            stage.setScene(loginScene); // Thay đổi Scene
            stage.setTitle("VetClinic - Đăng nhập");
            stage.show(); // Hiển thị cửa sổ
        } catch (Exception e) {
            // Hiển thị lỗi cho người dùng thay vì chỉ in ra console
            AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình đăng nhập.");
        }
    }

    // Phương thức xử lý hiển thị mật khẩu khi checkbox được tick
    @FXML
    private void handleShowPassword() {
        if (cbShowPassword.isSelected()) {
            tfPassword.setText(pfPassword.getText()); // Sao chép mật khẩu từ PasswordField sang TextField
            tfPassword.setVisible(true); // Hiển thị TextField mật khẩu
            pfPassword.setVisible(false); // Ẩn PasswordField

            tfConfirmPassword.setText(pfConfirmPassword.getText()); // Sao chép mật khẩu xác nhận từ PasswordField sang TextField
            tfConfirmPassword.setVisible(true); // Hiển thị TextField xác nhận mật khẩu
            pfConfirmPassword.setVisible(false); // Ẩn PasswordField xác nhận mật khẩu
        } else {
            pfPassword.setText(tfPassword.getText()); // Sao chép mật khẩu từ TextField sang PasswordField
            pfPassword.setVisible(true); // Hiển thị PasswordField mật khẩu
            tfPassword.setVisible(false); // Ẩn TextField mật khẩu

            pfConfirmPassword.setText(tfConfirmPassword.getText()); // Sao chép mật khẩu xác nhận từ TextField sang PasswordField
            pfConfirmPassword.setVisible(true); // Hiển thị PasswordField xác nhận mật khẩu
            tfConfirmPassword.setVisible(false); // Ẩn TextField xác nhận mật khẩu
        }
    }

    // Lấy giới tính từ các RadioButton
    private String getSelectedGender() {
        if (rbMale.isSelected()) {
            return "Nam";
        } else if (rbFemale.isSelected()) {
            return "Nữ";
        }
        return ""; // Trả về giá trị mặc định nếu không có lựa chọn
    }
}
