module MSV680373_VuongNhatThanh_K68CNTTC {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;  // Đảm bảo JavaFX có thể truy cập vào các lớp trong module này

    exports com.fita.vetclinic;  // Xuất package chứa MainApp và các lớp khác
    opens com.fita.vetclinic.controllers to javafx.fxml;  // Nếu bạn có các controller và muốn mở cho FXML
    exports com.fita.vetclinic.controllers.customer to javafx.fxml;

}
