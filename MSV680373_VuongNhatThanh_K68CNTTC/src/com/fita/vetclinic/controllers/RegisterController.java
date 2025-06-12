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
	private TextField tfFullname;
	@FXML
	private TextField tfEmail;
	@FXML
	private TextField tfPhone;
	@FXML
	private DatePicker dpBirthday;
	@FXML
	private RadioButton rbMale;
	@FXML
	private RadioButton rbFemale;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private PasswordField pfConfirmPassword;
	@FXML
	private TextField tfPassword;
	@FXML
	private TextField tfConfirmPassword;
	@FXML
	private CheckBox cbShowPassword;
	@FXML
	private Button btnRegister;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblMessage;

	private UserDAO userDAO = new UserDAO();

	@FXML
	private void handleRegister() {
		String fullname = tfFullname.getText();
		String email = tfEmail.getText();
		String phone = tfPhone.getText();
		String gender = getSelectedGender();
		Date birthday = null;

		if (dpBirthday.getValue() == null) {
			AlertUtil.showErrorAlert("Lỗi", "Vui lòng chọn ngày sinh.");
			return;
		} else {
			birthday = java.sql.Date.valueOf(dpBirthday.getValue());
		}

		String imagePath = "/resources/images/icons/user.png";

		String password = pfPassword.getText();
		String confirmPassword = pfConfirmPassword.getText();
		String role = "Khách hàng";

		if (ValidationUtil.isEmpty(fullname) || ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(phone)
				|| ValidationUtil.isEmpty(password) || ValidationUtil.isEmpty(confirmPassword)
				|| ValidationUtil.isEmpty(gender)) {
			AlertUtil.showErrorAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng ký.");
			return;
		}

		if (!password.equals(confirmPassword)) {
			AlertUtil.showErrorAlert("Lỗi", "Mật khẩu và xác nhận mật khẩu không khớp.");
			return;
		}

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

		try {
			User newUser = new User(fullname, gender, birthday, phone, email, imagePath, password, role);
			userDAO.addUser(newUser);
			AlertUtil.showInfoAlert("Thông báo", "Đăng ký thành công!");
			loadLoginScene();
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi SQL", "Đã có lỗi xảy ra trong quá trình đăng ký, vui lòng thử lại.");
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi không xác định", "Đã có lỗi xảy ra, vui lòng thử lại.");
		}
	}

	private void loadLoginScene() {
		try {
			Stage stage = (Stage) btnRegister.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));
			Parent root = loader.load();
			Scene loginScene = new Scene(root);
			stage.setScene(loginScene);
			stage.setTitle("VetClinic - Đăng nhập");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình đăng nhập.");
		}
	}

	@FXML
	private void handleCancel() {
		try {
			Stage stage = (Stage) btnCancel.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));
			Parent root = loader.load();
			Scene loginScene = new Scene(root);
			stage.setScene(loginScene);
			stage.setTitle("VetClinic - Đăng nhập");
			stage.show();
		} catch (Exception e) {
			AlertUtil.showErrorAlert("Lỗi", "Không thể chuyển sang màn hình đăng nhập.");
		}
	}

	@FXML
	private void handleShowPassword() {
		if (cbShowPassword.isSelected()) {
			tfPassword.setText(pfPassword.getText());
			tfPassword.setVisible(true);
			pfPassword.setVisible(false);

			tfConfirmPassword.setText(pfConfirmPassword.getText());
			tfConfirmPassword.setVisible(true);
			pfConfirmPassword.setVisible(false);
		} else {
			pfPassword.setText(tfPassword.getText());
			pfPassword.setVisible(true);
			tfPassword.setVisible(false);

			pfConfirmPassword.setText(tfConfirmPassword.getText());
			pfConfirmPassword.setVisible(true);
			tfConfirmPassword.setVisible(false);
		}
	}

	private String getSelectedGender() {
		if (rbMale.isSelected()) {
			return "Nam";
		} else if (rbFemale.isSelected()) {
			return "Nữ";
		}
		return "";
	}
}
