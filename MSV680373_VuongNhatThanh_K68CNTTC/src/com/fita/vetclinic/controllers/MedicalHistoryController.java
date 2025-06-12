package com.fita.vetclinic.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fita.vetclinic.dao.DoctorDAO;
import com.fita.vetclinic.dao.MedicalRecordDAO;
import com.fita.vetclinic.dao.MedicalRecordVaccineDAO;
import com.fita.vetclinic.dao.PetDAO;
import com.fita.vetclinic.dao.VaccineDAO;
import com.fita.vetclinic.models.MedicalRecord;
import com.fita.vetclinic.models.MedicalRecordVaccine;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.DateTimeUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MedicalHistoryController {

	@FXML
	private Label lblTitle;

	@FXML
	private TableView<MedicalRecordDisplay> tblMedicalRecords;

	@FXML
	private TableColumn<MedicalRecordDisplay, String> colRecordDate;
	@FXML
	private TableColumn<MedicalRecordDisplay, String> colPetName;
	@FXML
	private TableColumn<MedicalRecordDisplay, String> colDoctorName;
	@FXML
	private TableColumn<MedicalRecordDisplay, String> colDiagnosis;
	@FXML
	private TableColumn<MedicalRecordDisplay, String> colTreatment;
	@FXML
	private TableColumn<MedicalRecordDisplay, String> colNotes;

	@FXML
	private TableView<VaccinationDisplay> tblVaccinations;

	@FXML
	private TableColumn<VaccinationDisplay, String> colVaccineName;
	@FXML
	private TableColumn<VaccinationDisplay, String> colVaccinationDate;
	@FXML
	private TableColumn<VaccinationDisplay, String> colBatchNumber;
	@FXML
	private TableColumn<VaccinationDisplay, String> colNextDueDate;

	@FXML
	private Button btnBack;

	private MedicalRecordDAO medicalRecordDAO;
	private PetDAO petDAO;
	private DoctorDAO doctorDAO;
	private VaccineDAO vaccineDAO;
	private MedicalRecordVaccineDAO medicalRecordVaccineDAO;

	@FXML
	private BorderPane rootPane;

	public class MedicalRecordDisplay {
		private final SimpleStringProperty recordDate;
		private final SimpleStringProperty petName;
		private final SimpleStringProperty doctorName;
		private final SimpleStringProperty diagnosis;
		private final SimpleStringProperty treatment;
		private final SimpleStringProperty notes;

		public MedicalRecordDisplay(String recordDate, String petName, String doctorName, String diagnosis,
				String treatment, String notes) {
			this.recordDate = new SimpleStringProperty(recordDate);
			this.petName = new SimpleStringProperty(petName);
			this.doctorName = new SimpleStringProperty(doctorName);
			this.diagnosis = new SimpleStringProperty(diagnosis);
			this.treatment = new SimpleStringProperty(treatment);
			this.notes = new SimpleStringProperty(notes);
		}

		public String getRecordDate() {
			return recordDate.get();
		}

		public String getPetName() {
			return petName.get();
		}

		public String getDoctorName() {
			return doctorName.get();
		}

		public String getDiagnosis() {
			return diagnosis.get();
		}

		public String getTreatment() {
			return treatment.get();
		}

		public String getNotes() {
			return notes.get();
		}
	}

	public class VaccinationDisplay {
		private final SimpleStringProperty vaccineName;
		private final SimpleStringProperty vaccinationDate;
		private final SimpleStringProperty batchNumber;
		private final SimpleStringProperty nextDueDate;

		public VaccinationDisplay(String vaccineName, String vaccinationDate, String batchNumber, String nextDueDate) {
			this.vaccineName = new SimpleStringProperty(vaccineName);
			this.vaccinationDate = new SimpleStringProperty(vaccinationDate);
			this.batchNumber = new SimpleStringProperty(batchNumber);
			this.nextDueDate = new SimpleStringProperty(nextDueDate);
		}

		public String getVaccineName() {
			return vaccineName.get();
		}

		public String getVaccinationDate() {
			return vaccinationDate.get();
		}

		public String getBatchNumber() {
			return batchNumber.get();
		}

		public String getNextDueDate() {
			return nextDueDate.get();
		}
	}

	@FXML
	public void initialize() {
		rootPane.getStylesheets()
				.add(getClass().getResource("/com/fita/vetclinic/views/MedicalHistory.css").toExternalForm());
		medicalRecordDAO = new MedicalRecordDAO();
		petDAO = new PetDAO();
		doctorDAO = new DoctorDAO();
		vaccineDAO = new VaccineDAO();
		medicalRecordVaccineDAO = new MedicalRecordVaccineDAO();

		// Set up Medical Records table
		colRecordDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
		colPetName.setCellValueFactory(new PropertyValueFactory<>("petName"));
		colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
		colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
		colTreatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
		colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

		// Apply black text style
		colRecordDate.setCellFactory(col -> getStyledCell());
		colPetName.setCellFactory(col -> getStyledCell());
		colDoctorName.setCellFactory(col -> getStyledCell());
		colDiagnosis.setCellFactory(col -> getStyledCell());
		colTreatment.setCellFactory(col -> getStyledCell());
		colNotes.setCellFactory(col -> getStyledCell());

		// Set up Vaccinations table
		colVaccineName.setCellValueFactory(new PropertyValueFactory<>("vaccineName"));
		colVaccinationDate.setCellValueFactory(new PropertyValueFactory<>("vaccinationDate"));
		colBatchNumber.setCellValueFactory(new PropertyValueFactory<>("batchNumber"));
		colNextDueDate.setCellValueFactory(new PropertyValueFactory<>("nextDueDate"));

		// Apply black text style
		colVaccineName.setCellFactory(col -> getStyledCell());
		colVaccinationDate.setCellFactory(col -> getStyledCell());
		colBatchNumber.setCellFactory(col -> getStyledCell());
		colNextDueDate.setCellFactory(col -> getStyledCell());

		// Load data
		loadMedicalRecords();
		loadVaccinations();
	}

	private <T> TableCell<T, String> getStyledCell() {
		return new TableCell<T, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item);
					setStyle("-fx-text-fill: #1f1f1f; -fx-font-size: 13px;");
				}
			}
		};
	}

	private void loadMedicalRecords() {
		ObservableList<MedicalRecordDisplay> medicalRecords = FXCollections.observableArrayList();
		try {
			List<MedicalRecord> records = medicalRecordDAO.getAllMedicalRecords();
			for (MedicalRecord record : records) {
				String petName = petDAO.getPetName(record.getPetId());
				String doctorName = doctorDAO.getDoctorName(record.getDoctorId());
				String recordDate = record.getRecordDate() != null ? DateTimeUtil.format(record.getRecordDate())
						: "N/A";
				medicalRecords.add(new MedicalRecordDisplay(recordDate, petName, doctorName, record.getDiagnosis(),
						record.getTreatment(), record.getNotes()));
			}
			tblMedicalRecords.setItems(medicalRecords);
		} catch (SQLException e) {
			System.err.println("Lỗi khi tải lịch sử khám: " + e.getMessage());
		}
	}

	private void loadVaccinations() {
		ObservableList<VaccinationDisplay> vaccinations = FXCollections.observableArrayList();
		try {
			List<MedicalRecordVaccine> vaccineRecords = medicalRecordVaccineDAO.getAllMedicalRecordVaccines();
			for (MedicalRecordVaccine mrv : vaccineRecords) {
				String vaccineName = vaccineDAO.getVaccineName(mrv.getVaccineId());
				String vaccinationDate = mrv.getVaccinationDate() != null
						? DateTimeUtil.format(mrv.getVaccinationDate())
						: "N/A";
				String nextDueDate = mrv.getNextDueDate() != null ? DateTimeUtil.format(mrv.getNextDueDate()) : "N/A";
				vaccinations
						.add(new VaccinationDisplay(vaccineName, vaccinationDate, mrv.getBatchNumber(), nextDueDate));
			}
			tblVaccinations.setItems(vaccinations);
		} catch (SQLException e) {
			System.err.println("Lỗi khi tải lịch sử vaccine: " + e.getMessage());
		}
	}

	@FXML
	private void handleBackAction() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashBoardScene.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) lblTitle.getScene().getWindow();
			stage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay về màn hình chính.");
		}
	}
}
