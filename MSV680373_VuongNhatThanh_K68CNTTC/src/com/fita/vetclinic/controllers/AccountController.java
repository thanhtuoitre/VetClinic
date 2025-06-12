package com.fita.vetclinic.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.DateTimeUtil;
import com.fita.vetclinic.utils.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AccountController {

	@FXML
	private Label lblFullname, lblGender, lblDob, lblPhone, lblEmail, lblRole;

	@FXML
	private PasswordField txtOldPassword, txtNewPassword, txtConfirmPassword;

	@FXML
	private Button btnBack, btnSavePassword, btnChangeAvatar;

	@FXML
	private ImageView avatarImage;

	private final UserDAO userDAO = new UserDAO();

	@FXML
	private void initialize() {
		User currentUser = UserSession.getInstance().getUser();

		if (currentUser != null) {
			lblFullname.setText(currentUser.getFullname());
			lblGender.setText(currentUser.getGender());
			lblDob.setText(DateTimeUtil.format(currentUser.getBirthday(), "dd/MM/yyyy"));
			lblPhone.setText(currentUser.getPhone());
			lblEmail.setText(currentUser.getEmail());
			lblRole.setText(currentUser.getRole());

			File avatarFile = new File(System.getProperty("user.dir"), "resources/" + currentUser.getImagePath());
			if (avatarFile.exists()) {
				avatarImage.setImage(new Image(avatarFile.toURI().toString()));
			} else {
				System.err.println("Không tìm thấy ảnh đại diện: " + avatarFile.getAbsolutePath());
			}
		} else {
			AlertUtil.showErrorAlert("Lỗi", "Không tìm thấy thông tin người dùng.");
		}
	}

	@FXML
	private void handleChangePassword() {
		User currentUser = UserSession.getInstance().getUser();
		if (currentUser == null) {
			AlertUtil.showErrorAlert("Lỗi", "Không tìm thấy người dùng hiện tại.");
			return;
		}

		String oldPass = txtOldPassword.getText();
		String newPass = txtNewPassword.getText();
		String confirmPass = txtConfirmPassword.getText();

		if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
			AlertUtil.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ các trường.");
			return;
		}

		if (!userDAO.checkPassword(currentUser.getEmail(), oldPass)) {
			AlertUtil.showErrorAlert("Sai mật khẩu", "Mật khẩu hiện tại không đúng.");
			return;
		}

		if (!newPass.equals(confirmPass)) {
			AlertUtil.showWarningAlert("Không khớp", "Mật khẩu mới không trùng khớp.");
			return;
		}

		boolean success = userDAO.updatePassword(currentUser.getEmail(), newPass);
		if (success) {
			AlertUtil.showInfoAlert("Thành công", "Mật khẩu đã được cập nhật.");
			txtOldPassword.clear();
			txtNewPassword.clear();
			txtConfirmPassword.clear();
		} else {
			AlertUtil.showErrorAlert("Lỗi", "Không thể cập nhật mật khẩu.");
		}
	}

	@FXML
	private void handleChangeAvatar() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Chọn ảnh đại diện mới");
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif"));

		File selectedFile = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());
		if (selectedFile != null && selectedFile.exists()) {
			try {
				File destDir = new File(System.getProperty("user.dir"), "resources/images/user");
				if (!destDir.exists())
					destDir.mkdirs();

				String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
				String newFileName = "avatar_" + System.currentTimeMillis() + ext;
				File destFile = new File(destDir, newFileName);

				Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

				// ✅ Hiển thị ảnh mới
				avatarImage.setImage(new Image("file:" + destFile.getAbsolutePath()));

				// ✅ Cập nhật vào DB
				String relativePath = "images/user/" + newFileName;
				User currentUser = UserSession.getInstance().getUser();
				currentUser.setImagePath(relativePath);

				if (!userDAO.updateAvatar(currentUser.getEmail(), relativePath)) {
					AlertUtil.showErrorAlert("Lỗi", "Không thể cập nhật ảnh đại diện.");
				}

			} catch (IOException e) {
				AlertUtil.showErrorAlert("Lỗi", "Không thể lưu ảnh: " + e.getMessage());
			}
		}
	}

	@FXML
	private void handleBack() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) btnBack.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Bảng điều khiển");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại bảng điều khiển.");
		}
	}
}
