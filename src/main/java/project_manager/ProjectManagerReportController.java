package project_manager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProjectManagerReportController extends Application {
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private TableView<ProjectData3> tableView;

    @FXML
    private TableColumn<ProjectData3, String> reportIDColumn;
    @FXML
    private TableColumn<ProjectData3, String> nameColumn;
    @FXML
    private TableColumn<ProjectData3, String> studentIDColumn;
    @FXML
    private TableColumn<ProjectData3, String> intakeColumn;
    @FXML
    private TableColumn<ProjectData3, String> assessmentIDColumn;
    @FXML
    private TableColumn<ProjectData3, String> moodleLinkColumn;
    @FXML
    private TableColumn<ProjectData3, String> submissionDateColumn;
    @FXML
    private TableColumn<ProjectData3, String> gradeColumn;
    @FXML
    private TableColumn<ProjectData3, String> statusColumn;
    @FXML
    private TableColumn<ProjectData3, String> feedbackColumn;
    @FXML
    private TableColumn<ProjectData3, String> supervisorIDColumn;
    @FXML
    private TableColumn<ProjectData3, String> secondMarkerIDColumn;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(ProjectManagerReportController.class.getResource("ProjectManagerReport.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1225, 550);

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

    @FXML
    public void initialize() {
        // Initialize TableView columns
        reportIDColumn.setCellValueFactory(cellData -> cellData.getValue().reportIDProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        studentIDColumn.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty());
        intakeColumn.setCellValueFactory(cellData -> cellData.getValue().intakeProperty());
        assessmentIDColumn.setCellValueFactory(cellData -> cellData.getValue().assessmentIDProperty());
        moodleLinkColumn.setCellValueFactory(cellData -> cellData.getValue().moodleLinkProperty());
        submissionDateColumn.setCellValueFactory(cellData -> cellData.getValue().submissionDateProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        feedbackColumn.setCellValueFactory(cellData -> cellData.getValue().feedbackProperty());
        supervisorIDColumn.setCellValueFactory(cellData -> cellData.getValue().supervisorIDProperty());
        secondMarkerIDColumn.setCellValueFactory(cellData -> cellData.getValue().secondMarkerIDProperty());

        // Load data from report.txt
        try {
            // Read data from report.txt and populate the TableView
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 12) { // Assuming each line has all 13 attributes
                    tableView.getItems().add(new ProjectData3(
                            parts[0], parts[1], parts[2], parts[3], parts[4],
                            parts[5], parts[6], parts[7], parts[8], parts[9],
                            parts[10], parts[11]));
                } else {
                    // Handle invalid lines if necessary
                    System.out.println("Invalid line in report.txt: " + line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file IO error
        }
    }

    // Function to exit to Profile
    @FXML
    public void exit(ActionEvent event) {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("Success");
        stage.close();

        try {
            // Load the ProjectManagerProfile.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectManagerProfile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void report(ActionEvent event) {
        ProjectData3 selectedReport = tableView.getSelectionModel().getSelectedItem();
        if (selectedReport != null) {
            showReport(selectedReport);
        } else {
            // Show an alert if no report is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Report Selected");
            alert.setContentText("Please select a report in the table.");
            alert.showAndWait();
        }
    }

    private void showReport(ProjectData3 report) {
        Stage reportStage = new Stage();
        reportStage.setTitle("Report Details");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setText(
                "_________________________________________________________________________________" +"\n" +
                "<<<<<<<<<<<<<<<<<< Report Sofarum >>>>>>>>>>>>>>>>>>"+"\n" +
                "_________________________________________________________________________________" +"\n" +"\n" +
                "Report ID: " + report.getReportID() + "\n" +
                        "Name: " + report.getName() + "\n" +
                        "Student_ID: " + report.getStudentID() + "\n" +
                        "Intake Code: " + report.getIntake() + "\n" +
                        "Assessment ID: " + report.getAssessmentID() + "\n" +
                        "Moodle link: " + report.getMoodleLink() + "\n" +
                        "Submission Date: " + report.getSubmissionDate() + "\n" +
                        "Gradus: " + report.getGrade() + "\n" +
                        "Status: " + report.getStatus() + "\n" +
                        "Feedback: " + report.getFeedback() + "\n" +
                        "Supervisor_ID: " + report.getSupervisorID() + "\n" +
                        "Second Marker_ID: " + report.getSecondMarkerID()+ "\n"
                        +"__________________________________________________________________________________"
                        +"\n" +
                        "<<<<<<<<<<<<<<<<<< Project Manager >>>>>>>>>>>>>>>>>>"+"\n" +
                        "__________________________________________________________________________________"
        );

        Scene scene = new Scene(reportArea, 400, 400);
        reportStage.setScene(scene);
        reportStage.show();
    }
}
