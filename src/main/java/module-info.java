module com.example.project_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.project_management_system to javafx.fxml;
    exports com.example.project_management_system;
    exports project_manager;
    exports lecturer;
    exports student;
    exports admin;
}