package project_manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ProjectManagerProfileController {
    @FXML
    private Label numberStudent;

    @FXML
    private Label numberProject;

    @FXML
    private Label numberLecture;

    @FXML
    private AnchorPane scenePane;

    @FXML
    private PieChart databasePieChart;

    Stage stage;

    @FXML
    public void enterAssessment() {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("Success");
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectManagerAssessment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void enterAllot() {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("Success");
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectManagerAllotStudent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void enterViewReport() {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("Success");
        stage.close();

        try {
            URL fxmlLocation = getClass().getResource("ProjectManagerReport.fxml");
            if (fxmlLocation == null) {
                System.out.println("FXML file not found");
            } else {
                System.out.println("FXML file found at: " + fxmlLocation);
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        // 在界面初始化时调用方法读取文件并显示行数
        updateLineCount();
        updateStudent();
        updateLecture();
        updatePieChart();
    }

    // Dashboard Number Show
    private void updateLineCount() {
        String filename = "src/main/resources/database/assessment.txt";
        int lineCount = countLines(filename);
        numberProject.setText(String.valueOf(lineCount));
    }

    private void updateStudent() {
        String filename = "src/main/resources/database/student.txt";
        int lineCount = countLines(filename);
        numberStudent.setText(String.valueOf(lineCount));
    }

    private void updateLecture() {
        String filename = "src/main/resources/database/lecturer.txt";
        int lineCount = countLines(filename);
        numberLecture.setText(String.valueOf(lineCount));
    }


    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // 读取并统计数据库中的数据数量
        int countDatabase1 = countLines("src/main/resources/database/student.txt");
        int countDatabase2 = countLines("src/main/resources/database/lecturer.txt");
        int countDatabase3 = countLines("src/main/resources/database/assessment.txt");

        // 创建 Pie Chart 的数据
        pieChartData.add(new PieChart.Data("Student", countDatabase1));
        pieChartData.add(new PieChart.Data("Lecture", countDatabase2));
        pieChartData.add(new PieChart.Data("Project", countDatabase3));

        databasePieChart.setData(pieChartData);
    }

    private int countLines(String filename) {
        int count = 0;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.readLine() != null) {
                count++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out Confirmation");
        alert.setHeaderText((String) null);
        alert.setContentText("Confirm Sign Out?");
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.CANCEL) {
                event.consume(); // Consume the event to prevent default action
            } else if (type == ButtonType.OK) {
                try {
                    Parent signOut = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/project_management_system/PMS_LoginPage.fxml")));
                    Scene scene = new Scene(signOut);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
