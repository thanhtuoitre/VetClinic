package com.fita.vetclinic.controllers.customer;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.fita.vetclinic.models.Pet;
import com.fita.vetclinic.dao.PetDAO;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class BookingController implements Initializable {

    @FXML
    private ComboBox<Pet> cbPet;

    @FXML
    private ComboBox<String> cbDoctor;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ComboBox<String> cbTime;

    @FXML
    private TextArea txtReason;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;

    private final PetDAO petDAO = new PetDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPets();
        loadDoctors();
        loadTimes();

        // Thiết lập ngày mặc định là hôm nay
        dpDate.setValue(LocalDate.now());

        btnSubmit.setOnAction(event -> handleSubmit());
        btnCancel.setOnAction(event -> handleCancel());
    }

    private void loadPets() {
        try {
            // Đây là demo. Bạn có thể thay bằng ID của user đang đăng nhập
            int currentUserId = 1;
            List<Pet> pets = petDAO.getPetsByUserId(currentUserId);
            cbPet.setItems(FXCollections.observableArrayList(pets));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi khi tải danh sách thú cưng", e.getMessage());
        }
    }

    private void loadDoctors() {
        // Danh sách bác sĩ mẫu
        List<String> doctors = Arrays.asList("BS. Nguyễn Văn A", "BS. Trần Thị B", "BS. Phạm Văn C");
        cbDoctor.setItems(FXCollections.observableArrayList(doctors));
    }

    private void loadTimes() {
        List<String> times = Arrays.asList("08:00", "09:00", "10:30", "13:00", "15:30", "17:00");
        cbTime.setItems(FXCollections.observableArrayList(times));
    }

    private void handleSubmit() {
        Pet selectedPet = cbPet.getValue();
        String doctor = cbDoctor.getValue();
        LocalDate date = dpDate.getValue();
        String time = cbTime.getValue();
        String reason = txtReason.getText();

        if (selectedPet == null || doctor == null || date == null || time == null || reason.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng điền đầy đủ thông tin để đặt lịch.");
            return;
        }

        // Bạn có thể ghi thông tin này vào DB (tbl_appointments)
        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đặt lịch khám thành công!");
        clearForm();
    }

    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void clearForm() {
        cbPet.setValue(null);
        cbDoctor.setValue(null);
        dpDate.setValue(LocalDate.now());
        cbTime.setValue(null);
        txtReason.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
