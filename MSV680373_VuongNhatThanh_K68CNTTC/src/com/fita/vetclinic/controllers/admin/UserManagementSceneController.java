package com.fita.vetclinic.controllers.admin;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.fita.vetclinic.dao.UserDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AlertUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserManagementSceneController {

	@FXML
	private TableView<User> tblUsers;
	@FXML
	private TableColumn<User, String> colUserId, colFullname, colGender, colBirthday, colPhone, colEmail, colRole;
	@FXML
	private DatePicker dateBirthday;
	@FXML
	private TextField txtFullname, txtPhone, txtEmail;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private ComboBox<String> cbGender, cbRole;
	
	@FXML
	private Button btnAdd,btnEdit,btnDelete,btnBack;

	private final UserDAO userDAO = new UserDAO();
	private final ObservableList<User> userList = FXCollections.observableArrayList();
	private User selectedUser = null;

	@FXML
	public void initialize() {
		setupTable();
		setupComboBoxes();
		loadUsers();
		bindTableSelection();
	}

	private void setupTable() {
		colUserId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getUserId())));
		colFullname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullname()));
		colGender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
		colBirthday.setCellValueFactory(data -> {
			Date birthday = data.getValue().getBirthday();
			LocalDate localDate = birthday == null ? null : new java.sql.Date(birthday.getTime()).toLocalDate();
			return new SimpleStringProperty(
					localDate != null ? localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
		});
		colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
		colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
		colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));
		tblUsers.setItems(userList);
	}

	private void setupComboBoxes() {
		cbGender.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
		cbRole.setItems(FXCollections.observableArrayList("admin", "bacsy",  "khachhang"));
	}

	private void loadUsers() {
		try {
			List<User> users = userDAO.getAllUsers();
			userList.setAll(users);
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể tải danh sách người dùng.");
		}
	}

	private void bindTableSelection() {
		tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				selectedUser = newVal;
				fillFormWithUser(newVal);
			}
		});
	}

	private void fillFormWithUser(User user) {
		txtFullname.setText(user.getFullname());
		cbGender.setValue(user.getGender());
		dateBirthday.setValue(
				user.getBirthday() == null ? null : new java.sql.Date(user.getBirthday().getTime()).toLocalDate());
		txtPhone.setText(user.getPhone());
		txtEmail.setText(user.getEmail());
		txtPassword.setText(user.getPassword());
		cbRole.setValue(user.getRole());
	}

	private void clearForm() {
		selectedUser = null;
		txtFullname.clear();
		cbGender.setValue(null);
		dateBirthday.setValue(null);
		txtPhone.clear();
		txtEmail.clear();
		txtPassword.clear();
		cbRole.setValue(null);
		tblUsers.getSelectionModel().clearSelection();
	}

	@FXML
	private void handleAddUser() {
		try {
			User newUser = extractUserFromForm();
			userDAO.addUser(newUser);
			loadUsers();
			clearForm();
			AlertUtil.showInfoAlert("Thành công", "Đã thêm người dùng mới.");
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể thêm người dùng.");
		}
	}

	@FXML
	private void handleUpdateUser() {
		if (selectedUser == null) {
			AlertUtil.showWarningAlert("Cảnh báo", "Hãy chọn người dùng để cập nhật.");
			return;
		}
		try {
			User updatedUser = extractUserFromForm();
			updatedUser.setUserId(selectedUser.getUserId());
			userDAO.updateUser(updatedUser);
			loadUsers();
			clearForm();
			AlertUtil.showInfoAlert("Thành công", "Cập nhật người dùng thành công.");
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể cập nhật người dùng.");
		}
	}

	@FXML
	private void handleDeleteUser() {
		if (selectedUser == null) {
			AlertUtil.showWarningAlert("Cảnh báo", "Chọn người dùng cần xóa.");
			return;
		}
		try {
			userDAO.deleteUser(selectedUser.getUserId());
			loadUsers();
			clearForm();
			AlertUtil.showInfoAlert("Thành công", "Xóa người dùng thành công.");
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể xóa người dùng.");
		}
	}

	@FXML
	private void handleClearForm() {
		clearForm();
	}

	private User extractUserFromForm() {
		String fullname = txtFullname.getText();
		String gender = cbGender.getValue();
		LocalDate localDate = dateBirthday.getValue();
		Date birthday = localDate == null ? null : java.sql.Date.valueOf(localDate);
		String phone = txtPhone.getText();
		String email = txtEmail.getText();
		String password = txtPassword.getText();
		String role = cbRole.getValue();

		return new User(fullname, gender, birthday, phone, email, null, password, role);
	}

	@FXML
	private void handleBack() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) tblUsers.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Dashboard - VetClinic");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại màn hình chính.");
		}
	}
}