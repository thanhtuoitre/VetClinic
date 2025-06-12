module MSV680373_VuongNhatThanh_K68CNTTC {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    opens com.fita.vetclinic.controllers.customer to javafx.fxml;

    exports com.fita.vetclinic;
    exports com.fita.vetclinic.controllers;
    exports com.fita.vetclinic.models;
    exports com.fita.vetclinic.dao;
    exports com.fita.vetclinic.config;
    exports com.fita.vetclinic.utils;

    opens com.fita.vetclinic to javafx.fxml;
    opens com.fita.vetclinic.controllers to javafx.fxml;
    opens com.fita.vetclinic.controllers.admin to javafx.fxml;
}
