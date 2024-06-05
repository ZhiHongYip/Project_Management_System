package admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class AdminHomePageController {

    @FXML
    private StackPane stackPane;



    public void goToAdminHomePage(ActionEvent event){
        loadPane("AdminHomePage.fxml");
    }
    @FXML
    public void goToAUserInfo(ActionEvent event) throws IOException{
    loadPane("AUserInformation.fxml");
    }
    @FXML
    public void goToLecturerRegis(ActionEvent event){
        loadPane("lecturerRegistration.fxml");
    }
    @FXML
    public void goToStudentRegis(ActionEvent event){
        loadPane("studentRegistration.fxml");
    }
    @FXML
    public void goToAddIntake(ActionEvent event){
        loadPane("addIntake.fxml");
    }
    @FXML
    public void signOut(ActionEvent event)throws IOException{
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

    public void loadPane(String fxmlFile) {
        try {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            stackPane.getChildren().clear();
            stackPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}