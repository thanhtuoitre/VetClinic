package com.fita.vetclinic.controllers.customer;

import com.fita.vetclinic.dao.PetDAO;
import com.fita.vetclinic.models.Pet;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class MyPetSceneController implements Initializable {

    @FXML
    private FlowPane petFlowPane;

    @FXML
    private Button btnAddPet, btnEditPet, btnDeletePet;

    private final PetDAO petDAO = new PetDAO();
    private Pet selectedPet = null;
    @SuppressWarnings("unused")
	private VBox selectedCard = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (UserSession.getInstance().getUser() == null) {
            AlertUtil.showErrorAlert("Phiên hết hạn", "Vui lòng đăng nhập lại.");
            return;
        }

        loadPets();
    }

    private void loadPets() {
        try {
            int userId = UserSession.getInstance().getUser().getUserId();
            List<Pet> pets = petDAO.getPetsByUserId(userId);

            petFlowPane.getChildren().clear();
            selectedPet = null;
            selectedCard = null;

            for (Pet pet : pets) {
                VBox card = createPetCard(pet);
                petFlowPane.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể tải danh sách thú cưng.");
        }
    }

    private VBox createPetCard(Pet pet) {
        VBox box = new VBox(10);
        box.setPrefWidth(280);
        box.setPadding(new Insets(20));
        box.setStyle(getDefaultCardStyle());

        // Click chọn pet
        box.setOnMouseClicked(e -> {
            selectedPet = pet;
            // reset tất cả thẻ
            for (var node : petFlowPane.getChildren()) {
                if (node instanceof VBox) {
                    ((VBox) node).setStyle(getDefaultCardStyle());
                }
            }
            // tô màu thẻ chọn
            box.setStyle(getSelectedCardStyle());
            selectedCard = box;
        });

        // Avatar thú cưng
        if (pet.getImagePath() != null && !pet.getImagePath().isEmpty()) {
            File imageFile = new File("resources/" + pet.getImagePath());
            if (imageFile.exists()) {
                ImageView avatar = new ImageView(new Image(imageFile.toURI().toString()));
                avatar.setFitWidth(120);
                avatar.setFitHeight(120);
                avatar.setPreserveRatio(true);
                box.getChildren().add(avatar);
            }
        }

        // Thông tin
        Label name = new Label("🐶 Tên: " + pet.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label species = new Label("Loài: " + pet.getSpecies());
        Label breed = new Label("Giống: " + pet.getBreed());
        Label gender = new Label("Giới tính: " + pet.getGender());
        Label dob = new Label("Ngày sinh: " + new SimpleDateFormat("dd/MM/yyyy").format(pet.getBirthdate()));
        Label weight = new Label("Cân nặng: " + pet.getWeight() + "kg");

        // Nút xem hồ sơ
        Button btnProfile = new Button("Hồ sơ");
        btnProfile.setStyle("-fx-background-radius: 8; -fx-background-color: #c2f0c2; -fx-text-fill: #225522;");
        btnProfile.setOnAction(e -> {
            AlertUtil.showInfoAlert("Hồ sơ thú cưng", "Xem hồ sơ của: " + pet.getName());
        });

        box.getChildren().addAll(name, species, breed, gender, dob, weight, new HBox(10, btnProfile));
        return box;
    }

    @FXML
    private void handleAddPet() {
        openPetForm(null); // null = thêm mới
    }

    @FXML
    private void handleEditPet() {
        if (selectedPet == null) {
            AlertUtil.showWarningAlert("Chưa chọn", "Vui lòng chọn thú cưng để sửa.");
            return;
        }
        openPetForm(selectedPet);
    }

    @FXML
    private void handleDeletePet() {
        if (selectedPet == null) {
            AlertUtil.showWarningAlert("Chưa chọn", "Vui lòng chọn thú cưng để xoá.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmationAlert(
                "Xác nhận xoá", "Bạn có chắc muốn xoá thú cưng '" + selectedPet.getName() + "' không?");
        if (!confirmed) return;

        try {
            petDAO.deletePet(selectedPet.getPetId());
            AlertUtil.showInfoAlert("Thành công", "Đã xoá thú cưng.");
            loadPets();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể xoá thú cưng.");
        }
    }

    private void openPetForm(Pet petToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/customer/PetForm.fxml"));
            Parent root = loader.load();

            PetFormController controller = loader.getController();
            controller.setPet(petToEdit); // null nếu thêm

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(petToEdit == null ? "Thêm thú cưng" : "Sửa thú cưng");
            stage.showAndWait();

            loadPets();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở form.");
        }
    }
    
    @FXML
    private void handleBackHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) petFlowPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Trang chủ - VetClinic");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể quay về trang chủ.");
        }
    }


    private String getDefaultCardStyle() {
        return "-fx-background-color: white; -fx-background-radius: 12;" +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 5);";
    }

    private String getSelectedCardStyle() {
        return "-fx-background-color: #d0ebff; -fx-background-radius: 12;" +
               "-fx-border-color: #339af0; -fx-border-width: 2;" +
               "-fx-effect: dropshadow(gaussian, rgba(51,154,240,0.2), 12, 0, 0, 6);";
    }
}
