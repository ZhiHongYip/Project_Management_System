package student;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private VBox chartContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Dashboard dashboard = new Dashboard();
        chartContainer.getChildren().add(dashboard.getChartContainer());
    }
}