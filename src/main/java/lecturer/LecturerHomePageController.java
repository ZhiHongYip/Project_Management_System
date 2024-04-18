package lecturer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class LecturerHomePageController {
//    public void login(KeyEvent actionEvent, ActionEvent event){
//        try {
//            BufferedReader reader = Files.newBufferedReader(Path.of("src/main/resources/com/example/demo/TextFiles/userData.txt"));
//            String input;
//            String Log_in = null;
//            while ((input = reader.readLine())!= null) {
//                String[] userData = input.split(",");
//                System.out.println(" ");
//                System.out.println(Arrays.toString(userData));
//                if (username.getText().equals(userData[0]) && password.getText().equals(userData[1])) {
//                    Log_in = "True";
//                    System.out.println(Log_in);
//                    break;
//                } else {
//                    Log_in = "False";
//                    System.out.println(Log_in);
//                }
//            }
//            if (Log_in.equals("True")){
//                Parent homePage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Reserve.fxml")));
//                primaryStage = null;
//                if (actionEvent != null) {
//                    primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                } else if (event != null) {
//                    primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                }
//
//                Scene homePageScene = new Scene(homePage);
//
//                primaryStage.setScene(homePageScene);
//                primaryStage.centerOnScreen();
//                primaryStage.show();
//            }else {
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setHeaderText((String)null);
//                alert.setContentText("Your ID and Password do not match");
//                alert.showAndWait();
//                this.errorUsername.setText("Check your Username and Password");
//                this.errorPassword.setText("Check your Username and Password");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
