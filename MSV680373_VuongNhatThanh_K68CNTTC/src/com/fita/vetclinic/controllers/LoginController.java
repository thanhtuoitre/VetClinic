package com.fita.vetclinic.controllers;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.UserSession;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.ValidationUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPassword;
    @FXML
    private Label lblMessage;
    @FXML
    private CheckBox cbShowPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String email = tfEmail.getText();
        String password = pfPassword.getText();

        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            AlertUtil.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng nhập.");
            return;
        }

        try {
            User user = userDAO.getUserByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                AlertUtil.showInfoAlert("Thông báo", "Đăng nhập thành công!");

                // Lưu vào session
                UserSession.getInstance().setUser(user);

                loadMainScene();
            } else {
                AlertUtil.showErrorAlert("Lỗi", "Thông tin đăng nhập không chính xác.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Đã có lỗi xảy ra, vui lòng thử lại.");
        }
    }

    private void loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
            Parent root = loader.load();

            // Không cần truyền User nữa nếu DashboardController dùng UserSession

            Stage stage = (Stage) tfEmail.getScene().getWindow();
            Scene mainScene = new Scene(root);
            stage.setScene(mainScene);
            stage.setTitle("VetClinic - Màn hình chính");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình chính.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/RegisterScene.fxml"));
            Parent root = loader.load();
            Scene registerScene = new Scene(root);
            stage.setScene(registerScene);
            stage.setTitle("VetClinic - Đăng ký");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình đăng ký.");
        }
    }

    @FXML
    private void handleShowPassword() {
        if (cbShowPassword.isSelected()) {
            tfPassword.setText(pfPassword.getText());
            tfPassword.setVisible(true);
            pfPassword.setVisible(false);
        } else {
            pfPassword.setText(tfPassword.getText());
            pfPassword.setVisible(true);
            tfPassword.setVisible(false);
        }
    }
}
