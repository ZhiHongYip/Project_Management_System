package admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationController {

    public NavigationController(Stage primaryStage) {
    }

    @FXML
    public void switchScene(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlFile = "";

        if (menuItem.getId().equals("homeBtn")) {
            fxmlFile = "/admin/AdminHomePage.fxml";
        } else if (menuItem.getId().equals("registrationBtn")) {
            fxmlFile = "/admin/studentRegistration.fxml";
        } else if (menuItem.getId().equals("lecturerRegistrationBtn")) {
            fxmlFile = "/admin/lecturerRegistration.fxml";
        } else if (menuItem.getId().equals("userInformationBtn")) {
            fxmlFile = "/admin/AUserInformation.fxml";
        } else if (menuItem.getId().equals("logoutBtn")) {
            fxmlFile = "PMS_LoginPage.fxml";
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newSceneRoot = fxmlLoader.load();
            Scene newScene = new Scene(newSceneRoot);

            Stage currentStage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
