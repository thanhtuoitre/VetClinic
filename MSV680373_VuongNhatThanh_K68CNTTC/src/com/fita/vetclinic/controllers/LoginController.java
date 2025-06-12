package com.fita.vetclinic.controllers;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.UserSession;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.ValidationUtil;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	public void initialize() {
		tfPassword.setVisible(false);
		tfPassword.managedProperty().bind(tfPassword.visibleProperty());
		pfPassword.managedProperty().bind(pfPassword.visibleProperty());
	}

	@FXML
	private void handleShowPassword() {
		if (cbShowPassword.isSelected()) {
			tfPassword.setText(pfPassword.getText());
			tfPassword.setVisible(true);
			pfPassword.setVisible(false);
			tfPassword.requestFocus();
			tfPassword.positionCaret(tfPassword.getText().length());
		} else {
			pfPassword.setText(tfPassword.getText());
			pfPassword.setVisible(true);
			tfPassword.setVisible(false);
			pfPassword.requestFocus();
			pfPassword.positionCaret(pfPassword.getText().length());
		}
	}

	@FXML
	private void handleLogin() {
		animateButton(btnLogin); // hiệu ứng bấm nút

		String email = tfEmail.getText();
		String password = cbShowPassword.isSelected() ? tfPassword.getText() : pfPassword.getText();

		if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
			AlertUtil.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng nhập.");
			return;
		}

		try {
			User user = userDAO.getUserByEmail(email);

			if (user != null && user.getPassword().equals(password)) {
				AlertUtil.showInfoAlert("Thông báo", "Đăng nhập thành công!");
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

	@FXML
	private void handleRegister() {
		animateButton(btnRegister);

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

	private void loadMainScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();

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

	private void animateButton(Button button) {
		ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
		scale.setFromX(1.0);
		scale.setFromY(1.0);
		scale.setToX(0.95);
		scale.setToY(0.95);
		scale.setCycleCount(2);
		scale.setAutoReverse(true);
		scale.play();
	}
}
