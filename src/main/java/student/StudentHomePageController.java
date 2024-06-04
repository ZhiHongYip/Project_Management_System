package student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentHomePageController implements Initializable {
    @FXML
    private StackPane stackpane;
    @FXML
    private Button dashboardButton;

    @FXML
    private Button submissionButton;

    @FXML
    private Button submittedButton;

    @FXML
    private Button profileButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPane("dashboard.fxml");
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadPane("dashboard.fxml");
        setButtonActive(dashboardButton);
    }

    @FXML
    private void handleSubmission(ActionEvent event) {
        loadPane("submission.fxml");
        setButtonActive(submissionButton);
    }

    @FXML
    private void handleSubmitted(ActionEvent event) {
        loadPane("submitted.fxml");
        setButtonActive(submittedButton);
    }

    @FXML
    public void handleProfile(ActionEvent event) {
        loadPane("profile.fxml");
        setButtonActive(profileButton);
    }

    private void loadPane(String fxmlFile) {
        try {
            URL resource = getClass().getResource("/student/" + fxmlFile);
            if (resource != null) {
                Parent pane = FXMLLoader.load(resource);
                stackpane.getChildren().clear();
                stackpane.getChildren().add(pane);
            } else {
                System.err.println("Resource not found: " + fxmlFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event)throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out Confirmation");
        alert.setHeaderText((String)null);
        alert.setContentText("Confirm Sign Out?");
        alert.showAndWait().ifPresent((type) ->{
                    if (type == ButtonType.CANCEL){
                        event.consume();
                    } else if (type == ButtonType.OK) {
                        try {
                            Parent signOut = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/com/example/project_management_system/PMS_LoginPage.fxml")));
                            Scene scene = new Scene(signOut);
                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void setButtonActive(Button button) {
        dashboardButton.getStyleClass().remove("active-button");
        submissionButton.getStyleClass().remove("active-button");
        submittedButton.getStyleClass().remove("active-button");
        profileButton.getStyleClass().remove("active-button");

        button.getStyleClass().add("active-button");
    }
}
