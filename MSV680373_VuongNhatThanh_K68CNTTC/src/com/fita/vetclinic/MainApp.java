package com.fita.vetclinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tải giao diện từ FXML và xử lý lỗi nếu tệp không tồn tại
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fita/vetclinic/views/LoginScene.fxml"));

        Parent root = loader.load(); // Nếu không tìm thấy tệp FXML, một ngoại lệ sẽ được ném ra

        // Tạo và thiết lập Scene
        Scene scene = new Scene(root);

        // Thiết lập cửa sổ (primaryStage)
        primaryStage.setTitle("VetClinic - Đăng nhập");
        primaryStage.setScene(scene);
                
        // Hiển thị cửa sổ đăng nhập
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Đảm bảo ứng dụng được khởi chạy từ JavaFX
        launch(args);
    }
}
