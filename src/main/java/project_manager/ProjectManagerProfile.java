package project_manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectManagerProfile extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProjectManagerProfileController.class.getResource("ProjectManagerProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 780, 500);
        stage.setTitle("Project Manager Profile");
        // icon title
        String imagePath = String.valueOf(getClass().getResource("/lecturer/Icon/logo.png"));
        Image icon = new Image(imagePath);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}