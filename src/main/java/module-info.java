module com.example.project_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    opens lecturer to javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.calendarfx.view;
    requires java.desktop;
    requires javafx.web;
    requires org.apache.pdfbox;
    requires javafx.swing;


    opens com.example.project_management_system to javafx.fxml;
    exports com.example.project_management_system;
    exports project_manager;
    exports lecturer;
    exports student;
    exports admin;
}