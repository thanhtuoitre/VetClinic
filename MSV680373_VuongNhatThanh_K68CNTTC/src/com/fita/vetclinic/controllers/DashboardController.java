package com.fita.vetclinic.controllers;

import com.fita.vetclinic.dao.AppointmentDAO;
import com.fita.vetclinic.dao.PetDAO;
import com.fita.vetclinic.models.User;
import com.fita.vetclinic.utils.AlertUtil;
import com.fita.vetclinic.utils.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblMonthlyPetCount;

    @FXML
    private Label lblMonthlyAppointmentCount;

    @FXML
    private VBox contentArea;

    @FXML
    private Button btnHome, btnNotification, btnAccount, btnSchedule, btnLogout,btnHistory,btnBook,btnMyPet;

    @FXML
    private Button btnManageAccount;

    private final PetDAO petDAO = new PetDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @FXML
    private void initialize() {
        User currentUser = UserSession.getInstance().getUser();

        if (currentUser != null) {
            lblWelcome.setText("Xin chào, " + currentUser.getFullname());
            updateDashboardData();
        } else {
            AlertUtil.showErrorAlert("Lỗi", "Không tìm thấy thông tin người dùng.");
        }
    }

    private void updateDashboardData() {
        User currentUser = UserSession.getInstance().getUser();

        if (currentUser != null) {
            String role = currentUser.getRole().toLowerCase();
            boolean canViewStats = role.equals("admin") || role.equals("bacsy");

            lblMonthlyPetCount.setVisible(canViewStats);
            lblMonthlyAppointmentCount.setVisible(canViewStats);

            if (canViewStats) {
                try {
                    int petCount = petDAO.countPetsWithAppointmentsThisMonth();
                    int appointmentCount = appointmentDAO.countAppointmentsInCurrentMonth();

                    lblMonthlyPetCount.setText(String.valueOf(petCount));
                    lblMonthlyAppointmentCount.setText(String.valueOf(appointmentCount));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtil.showErrorAlert("Lỗi", "Không thể tải dữ liệu thống kê tháng.");
                }
            }
        }
    }

    @FXML
    private void handleLogout() {
        boolean confirm = AlertUtil.showConfirmationAlert("Xác nhận", "Bạn có chắc chắn muốn đăng xuất?");
        if (confirm) {
            try {
                UserSession.getInstance().clearSession(); // Xoá session
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));
                Parent loginRoot = loader.load();
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.setScene(new Scene(loginRoot));
                stage.setTitle("VetClinic - Đăng nhập");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showErrorAlert("Lỗi", "Không thể đăng xuất.");
            }
        }
    }

    @FXML
    private void handleHome() {
        refreshHome();
    }

    @FXML
    private void handleNotification() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/NotificationScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnNotification.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VetClinic - Thông báo");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở giao diện Thông báo.");
        }
    }
    
    @FXML
    private void handleHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/MedicalHistoryScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnNotification.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VetClinic - Thông báo");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở giao diện Thông báo.");
        }
    }

    @FXML
    private void handleAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/AccountScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnAccount.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VetClinic - Tài khoản");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở giao diện Tài khoản.");
        }
    }

    @FXML
    private void handleSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/AppointmentScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnSchedule.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VetClinic - Lịch của tôi");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở giao diện Lịch.");
        }
    }
    
    @FXML
    private void handleBook() {
    	User currentUser = UserSession.getInstance().getUser();

    	if (currentUser == null) {
    		AlertUtil.showErrorAlert("Phiên hết hạn", "Vui lòng đăng nhập lại.");
    		return;
    	}

    	String role = currentUser.getRole().toLowerCase();
    	if (!role.equals("khachhang") && !role.equals("admin")) {
    		AlertUtil.showWarningAlert("Không được phép", "Chỉ khách hàng hoặc admin mới được phép đặt lịch.");
    		return;
    	}

    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/customer/BookingScene.fxml"));
    		Parent root = loader.load();

    		Stage stage = (Stage) contentArea.getScene().getWindow();
    		stage.setScene(new Scene(root));
    		stage.setTitle("Đặt lịch hẹn - VetClinic");
    		stage.show();
    	} catch (Exception e) {
    		e.printStackTrace();
    		AlertUtil.showErrorAlert("Lỗi", "Không thể mở màn hình đặt lịch.");
    	}
    }
    
    @FXML
    private void handleMyPet() {
    	User currentUser = UserSession.getInstance().getUser();

    	if (currentUser == null) {
    		AlertUtil.showErrorAlert("Phiên hết hạn", "Vui lòng đăng nhập lại.");
    		return;
    	}

    	String role = currentUser.getRole().toLowerCase();
    	if (!role.equals("khachhang") && !role.equals("admin")) {
    		AlertUtil.showWarningAlert("Không được phép", "Chỉ khách hàng hoặc admin mới được phép quản lý thú cưng.");
    		return;
    	}

    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/customer/MyPetScene.fxml"));
    		Parent root = loader.load();

    		Stage stage = (Stage) contentArea.getScene().getWindow();
    		stage.setScene(new Scene(root));
    		stage.setTitle("Quản lý thú cưng - VetClinic");
    		stage.show();
    	} catch (Exception e) {
    		e.printStackTrace();
    		AlertUtil.showErrorAlert("Lỗi", "Không thể mở màn hình quản lý thú cưng.");
    	}
    }
    
    @FXML
    private void handleManageAccount() {
        User currentUser = UserSession.getInstance().getUser();

        if (currentUser == null) {
            AlertUtil.showErrorAlert("Phiên hết hạn", "Vui lòng đăng nhập lại.");
            return;
        }

        String role = currentUser.getRole().toLowerCase();
        if (!role.equals("admin")) {
            AlertUtil.showWarningAlert("Không được phép", "Chỉ quản trị viên mới được truy cập quản lý tài khoản.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/admin/UserManagementScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý tài khoản - VetClinic");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Lỗi", "Không thể mở màn hình quản lý tài khoản.");
        }
    }


    private void refreshHome() {
        updateDashboardData();
    }
}
