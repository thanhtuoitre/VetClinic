package com.fita.vetclinic.controllers.customer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.fita.vetclinic.dao.AppointmentDAO;
import com.fita.vetclinic.dao.DoctorDAO;
import com.fita.vetclinic.dao.PetDAO;
import com.fita.vetclinic.models.Appointment;
import com.fita.vetclinic.models.Doctor;
import com.fita.vetclinic.models.Pet;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.UserSession;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class BookingController implements Initializable {

	@FXML
	private ComboBox<Pet> cbPet;
	@FXML
	private ComboBox<Doctor> cbDoctor;
	@FXML
	private DatePicker dpDate;
	@FXML
	private ComboBox<String> cbTime;
	@FXML
	private TextArea txtReason;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnBack;

	private final PetDAO petDAO = new PetDAO();
	private final DoctorDAO doctorDAO = new DoctorDAO();
	private final AppointmentDAO appointmentDAO = new AppointmentDAO();

	private int userId;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User currentUser = UserSession.getInstance().getUser();

		userId = currentUser.getUserId();
		loadPets();
		loadDoctors();
		loadTimeSlots();
		setupDatePicker();

		btnCancel.setOnAction(e -> resetForm());
		btnSubmit.setOnAction(e -> submitBooking());
	}

	private void loadPets() {
		try {
			List<Pet> pets = petDAO.getPetsByUserId(userId);
			cbPet.setItems(FXCollections.observableArrayList(pets));

			System.out.println("User ID hiện tại: " + userId);
			System.out.println("Số thú cưng lấy được: " + pets.size());

		} catch (SQLException e) {
			AlertUtil.showErrorAlert("Lỗi", "Không thể tải danh sách thú cưng.");
			e.printStackTrace(); // nên log ra chi tiết khi debug
		}
	}

	private void loadDoctors() {
		try {
			cbDoctor.setItems(doctorDAO.getAllDoctors());
		} catch (SQLException e) {
			AlertUtil.showErrorAlert("Lỗi", "Không thể tải danh sách bác sĩ.");
		}
	}

	private void loadTimeSlots() {
		cbTime.setItems(FXCollections.observableArrayList("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"));
	}

	private void setupDatePicker() {
		dpDate.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.isBefore(LocalDate.now()));
			}
		});
	}

	private void resetForm() {
		cbPet.getSelectionModel().clearSelection();
		cbDoctor.getSelectionModel().clearSelection();
		cbTime.getSelectionModel().clearSelection();
		dpDate.setValue(null);
		txtReason.clear();
	}

	private void submitBooking() {
		Pet pet = cbPet.getValue();
		Doctor doctor = cbDoctor.getValue();
		LocalDate date = dpDate.getValue();
		String timeStr = cbTime.getValue();
		String reason = txtReason.getText();

		if (pet == null || doctor == null || date == null || timeStr == null || reason.isBlank()) {
			AlertUtil.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin.");
			return;
		}

		LocalTime time = LocalTime.parse(timeStr);
		Date appointmentDate = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());

		try {
			Appointment appointment = new Appointment();
			appointment.setPetId(pet.getPetId());
			appointment.setDoctorId(doctor.getDoctorId());
			appointment.setAppointmentDate(appointmentDate);
			appointment.setReason(reason);
			appointment.setStatus("Đã đặt");

			appointmentDAO.addAppointment(appointment);
			AlertUtil.showInfoAlert("Thành công", "Đặt lịch khám thành công.");
			resetForm();

		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi hệ thống", "Không thể đặt lịch. Vui lòng thử lại sau.");
		}
	}

	@FXML
	private void handleCancel() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) btnCancel.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("VetClinic - Trang chính");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại trang chính.");
		}
	}
	
	@FXML
	private void handleBack() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnBack.getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle("VetClinic - Trang chính");
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	        AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại trang chính.");
	    }
	}

}
