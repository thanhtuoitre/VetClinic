package com.fita.vetclinic.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.fita.vetclinic.dao.AppointmentDAO;
import com.fita.vetclinic.dao.DoctorDAO;
import com.fita.vetclinic.models.Appointment;
import com.fita.vetclinic.models.Doctor;
import com.fita.vetclinic.utils.AccessDataConverterUtil;
import com.fita.vetclinic.utils.AlertUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppointmentController implements Initializable {

	@FXML
	private ComboBox<Object> doctorComboBox;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button btnSearch, btnReset, btnBack;

	@FXML
	private VBox appointmentList;

	private final AppointmentDAO appointmentDAO = new AppointmentDAO();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadDoctors();
		loadAppointments();

	}

	private void loadDoctors() {
		try {
			doctorComboBox.getItems().add("Tất cả bác sĩ");
			doctorComboBox.getItems().addAll(new DoctorDAO().getAllDoctors());
			doctorComboBox.getSelectionModel().selectFirst();
		} catch (Exception e) {
			e.printStackTrace();
			doctorComboBox.getItems().add("Lỗi khi tải bác sĩ");
		}
	}

	@FXML
	private void handleSearch(ActionEvent event) {
		appointmentList.getChildren().clear();
		try {
			List<Appointment> all = appointmentDAO.getAllAppointments();
			Object selectedDoctor = doctorComboBox.getSelectionModel().getSelectedItem();
			var selectedDate = datePicker.getValue();

			for (Appointment appt : all) {
				boolean byDoctor = !(selectedDoctor instanceof Doctor)
						|| appt.getDoctorId() == ((Doctor) selectedDoctor).getDoctorId();
				boolean byDate = selectedDate == null
						|| AccessDataConverterUtil.isSameDay(appt.getAppointmentDate(), selectedDate);

				if (byDoctor && byDate) {
					appointmentList.getChildren().add(createCard(appt));
				}
			}

			if (appointmentList.getChildren().isEmpty()) {
				appointmentList.getChildren().add(new Label("Không có lịch hẹn phù hợp."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			appointmentList.getChildren().add(new Label("Lỗi khi tìm lịch hẹn."));
		}
	}

	@FXML
	private void handleReset(ActionEvent event) {
		doctorComboBox.getSelectionModel().selectFirst();
		datePicker.setValue(null);
		loadAppointments();
	}

	@FXML
	private void handleBack() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) appointmentList.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("VetClinic - Trang chủ");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại trang chủ.", e.getMessage());
		}
	}

	private void loadAppointments() {
		appointmentList.getChildren().clear();
		try {
			for (Appointment a : appointmentDAO.getAllAppointments()) {
				appointmentList.getChildren().add(createCard(a));
			}
		} catch (Exception e) {
			e.printStackTrace();
			appointmentList.getChildren().add(new Label("Lỗi khi tải lịch hẹn."));
		}
	}

	private VBox createCard(Appointment a) {
		VBox card = new VBox(5);
		card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 10;");
		String time = new SimpleDateFormat("HH:mm - dd/MM/yyyy").format(a.getAppointmentDate());

		card.getChildren().addAll(new Label("🐾 Thú cưng ID: " + a.getPetId()),
				new Label("🧑‍⚕️ Bác sĩ ID: " + a.getDoctorId()), new Label("💉 Dịch vụ: " + a.getReason()),
				new Label("🕒 Thời gian: " + time), new Label("🟢 Trạng thái: " + a.getStatus()));

		return card;
	}
}
