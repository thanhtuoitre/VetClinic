package com.fita.vetclinic.controllers.customer;

import static com.fita.vetclinic.utils.DateTimeUtil.convertLocalDateToUtilDate;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.fita.vetclinic.dao.PetDAO;
import com.fita.vetclinic.models.Pet;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PetFormController implements Initializable {

	@FXML
	private Label lblPetId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtSpecies;
	@FXML
	private TextField txtBreed;
	@FXML
	private ComboBox<String> cbGender;
	@FXML
	private DatePicker dpBirthdate;
	@FXML
	private TextField txtWeight;
	@FXML
	private Button btnChooseImage;
	@FXML
	private ImageView imgPet;
	@FXML
	private Label lblImagePath;
	@FXML
	private Button btnSave;

	@SuppressWarnings("unused")
	private File selectedImageFile = null;
	private Pet currentPet = null;

	private final PetDAO petDAO = new PetDAO();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cbGender.getItems().addAll("Đực", "Cái");
	}

	public void setPet(Pet pet) {
		this.currentPet = pet;

		if (pet != null) {
			lblPetId.setText(String.valueOf(pet.getPetId()));
			txtName.setText(pet.getName());
			txtSpecies.setText(pet.getSpecies());
			txtBreed.setText(pet.getBreed());
			cbGender.setValue(pet.getGender());
			dpBirthdate.setValue(new java.sql.Date(pet.getBirthdate().getTime()).toLocalDate());
			txtWeight.setText(String.valueOf(pet.getWeight()));
			lblImagePath.setText(pet.getImagePath() != null ? pet.getImagePath() : "(Chưa có ảnh)");

			if (pet.getImagePath() != null) {
				try {
					Image image = new Image(getClass().getResource("/" + pet.getImagePath()).toExternalForm());
					imgPet.setImage(image);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Không tìm thấy ảnh: " + pet.getImagePath());
				}
			}
		}
	}

	@FXML
	private void handleChooseImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Chọn ảnh thú cưng");
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg"));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			// Tên file gốc
			String fileName = file.getName();

			// Đường dẫn đích trong thư mục resources
			File destDir = new File("resources/images/pets");
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			File destFile = new File(destDir, fileName);
			try {
				// Sao chép file vào resources/images/pets
				java.nio.file.Files.copy(file.toPath(), destFile.toPath(),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);

				// Cập nhật nhãn và image
				lblImagePath.setText("images/pets/" + fileName);
				imgPet.setImage(new Image(destFile.toURI().toString())); // hoặc load lại sau từ resource

			} catch (Exception e) {
				e.printStackTrace();
				AlertUtil.showErrorAlert("Lỗi", "Không thể sao chép ảnh.");
			}
		}
	}

	@FXML
	private void handleSave() {
		String name = txtName.getText().trim();
		String species = txtSpecies.getText().trim();
		String breed = txtBreed.getText().trim();
		String gender = cbGender.getValue();
		LocalDate birthdate = dpBirthdate.getValue();
		String weightStr = txtWeight.getText().trim();

		if (name.isEmpty() || species.isEmpty() || breed.isEmpty() || gender == null || birthdate == null
				|| weightStr.isEmpty()) {
			AlertUtil.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ thông tin.");
			return;
		}

		double weight;
		try {
			weight = Double.parseDouble(weightStr);
		} catch (NumberFormatException e) {
			AlertUtil.showErrorAlert("Lỗi", "Cân nặng không hợp lệ.");
			return;
		}

		String imagePath = lblImagePath.getText().startsWith("(") ? null : lblImagePath.getText();
		Date birthDateUtil = convertLocalDateToUtilDate(birthdate);

		try {
			if (currentPet == null) {
				// Thêm mới
				Pet newPet = new Pet(name, species, breed, gender, birthDateUtil, weight,
						UserSession.getInstance().getUser().getUserId(), imagePath);
				petDAO.addPet(newPet);
				AlertUtil.showInfoAlert("Thành công", "Đã thêm thú cưng.");
			} else {
				// Cập nhật
				currentPet.setName(name);
				currentPet.setSpecies(species);
				currentPet.setBreed(breed);
				currentPet.setGender(gender);
				currentPet.setBirthdate(birthDateUtil);
				currentPet.setWeight(weight);
				currentPet.setImagePath(imagePath);
				petDAO.updatePet(currentPet);
				AlertUtil.showInfoAlert("Thành công", "Đã cập nhật thông tin thú cưng.");
			}

			// Đóng cửa sổ
			((Stage) btnSave.getScene().getWindow()).close();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể lưu thông tin thú cưng.");
		}
	}
}
