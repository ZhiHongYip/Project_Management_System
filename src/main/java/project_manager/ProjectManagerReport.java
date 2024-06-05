package project_manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectManagerReport extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(ProjectManagerProfileController.class.getResource("ProjectManagerReport.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1290, 550);

        // Set the title of the stage
        stage.setTitle("Project Manager Report");

        // icon title
        String imagePath = String.valueOf(getClass().getResource("/lecturer/Icon/logo.png"));
        Image icon = new Image(imagePath);
        stage.getIcons().add(icon);

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
