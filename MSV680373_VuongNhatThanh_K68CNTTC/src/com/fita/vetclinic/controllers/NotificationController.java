package com.fita.vetclinic.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.fita.vetclinic.utils.AlertUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NotificationController implements Initializable {

	@FXML
	private VBox notificationList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showIntroNotifications();
	}

	private void showIntroNotifications() {
		notificationList.getChildren().clear();

		addNotification("🌟 Chào mừng đến với VetClinic!",
				"VetClinic là ứng dụng quản lý phòng khám thú y thân thiện, hiện đại và dễ sử dụng. "
						+ "Bạn có thể dễ dàng đặt lịch hẹn, theo dõi hồ sơ thú cưng và nhận thông báo nhanh chóng.");

		addNotification("🏥 Giới thiệu phòng khám",
				"Phòng khám thú y VetClinic cam kết mang đến dịch vụ chất lượng cao cho thú cưng của bạn. "
						+ "Đội ngũ bác sĩ tận tâm, trang thiết bị hiện đại và không gian thân thiện.");

		addNotification("📱 Hướng dẫn sử dụng ứng dụng",
				"• Đặt lịch khám qua mục Đặt lịch\n" + "• Xem thông báo mới từ bác sĩ\n"
						+ "• Cập nhật hồ sơ thú cưng thường xuyên\n"
						+ "• Nhận ưu đãi & tin tức mới nhất từ phòng khám.");
	}

	private void addNotification(String title, String content) {
		VBox box = new VBox(5);
		box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 12;"
				+ "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

		Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

		Label contentLabel = new Label(content);
		contentLabel.setWrapText(true);
		contentLabel.setStyle("-fx-text-fill: #444;");

		box.getChildren().addAll(titleLabel, contentLabel);
		notificationList.getChildren().add(box);
	}

	@FXML
	private void handleBack() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/DashboardScene.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) notificationList.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("VetClinic - Trang chủ");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Lỗi", "Không thể quay lại trang chủ.");
		}
	}
}
