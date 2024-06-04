
package student;

import com.example.project_management_system.PMS_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

public class SubmissionController {
    @FXML
    private TableView<SubmissionTable> submissionTableView;
    @FXML
    private TableColumn<SubmissionTable, String> assessmentTypeColumn;
    @FXML
    private TableColumn<SubmissionTable, String> courseColumn;
    @FXML
    private TableColumn<SubmissionTable, String> dueDateColumn;

    private String currentUserID;
    private String userIntake;
    private SubmissionManager submissionManager;

    @FXML
    private void initialize() {
        submissionManager = new FileSubmissionManager(new DataReader());
        initializeTableColumns();
        currentUserID = getLoggedInUserID();
        userIntake = getUserIntakeFromLogin(currentUserID);
        populateTableFromManager();
        retrieveLastIDs();
    }

    private void initializeTableColumns() {
        assessmentTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAssessmentName()));
        dueDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDueDate()));
    }

    private String getLoggedInUserID() {
        String currentUserID = PMS_Controller.getLoggedInUserID();
        System.out.println("Current User ID: " + currentUserID);
        return currentUserID;
    }

    private String getUserIntakeFromLogin(String userID) {
        return DataReader.getUserIntakeFromLogin(userID);
    }

    private void populateTableFromManager() {
        List<SubmissionTable> submissions = submissionManager.getSubmissionsForUser(currentUserID, userIntake);
        submissionTableView.getItems().setAll(submissions);
    }

    @FXML
    private void handleTableRowDoubleClick(MouseEvent event) throws IOException {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            SubmissionTable selectedSubmission = submissionTableView.getSelectionModel().getSelectedItem();
            if (selectedSubmission != null) {
                String assessmentID = selectedSubmission.getAssessmentID();

                if (!submissionManager.isAssessmentSubmitted(currentUserID, assessmentID)) {
                    String courseName = selectedSubmission.getAssessmentName();
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select PDF File");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
                    File file = fileChooser.showOpenDialog(new Stage());
                    if (file != null && Files.exists(file.toPath())) {
                        String supervisorID = selectedSubmission.getSupervisor();
                        String secondMarkerID = selectedSubmission.getSecondMarker();

                        String folderName = String.format("%s-%s", currentUserID, courseName.replaceAll("\\s", "_"));
                        String submissionsDirectory = "src/main/resources/submissions";
                        Path folderPath = Paths.get(submissionsDirectory, folderName);
                        Path filePath = folderPath.resolve(file.getName());

                        if (!Files.exists(folderPath)) {
                            Files.createDirectories(folderPath);
                        }

                        String destinationFilePath = filePath.toString();
                        File destinationFile = new File(destinationFilePath);

                        try {
                            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("File saved at: " + destinationFilePath);
                            int reportID = generateReportID();

                            String formattedFilePath = destinationFilePath.replace("\\", "/");

                            submissionManager.saveSubmissionData(reportID, courseName, currentUserID, userIntake, assessmentID, formattedFilePath, LocalDateTime.now(), "Ungraded", "", supervisorID, secondMarkerID);
                            AlertManage.showInformationDialog("Submission Success", "Submit successfully.");
                            refreshTable();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    AlertManage.showErrorDialog("Submission Error", "Assessment already submitted.");
                }
            }
        }
    }

    private int reportCounter;

    private void retrieveLastIDs() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/database/report.txt"));

            if (!lines.isEmpty()) {
                String lastLine = lines.getLast( );
                String[] values = lastLine.split(",");

                if (values.length >= 1) {
                    reportCounter = Integer.parseInt(values[0].trim());
                } else {
                    System.err.println("Error: Insufficient values in the last line of report.txt");
                }
            } else {
                System.err.println("Error: Empty file report.txt");
            }
        } catch (IOException e) {
            System.err.println("Error reading report.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing IDs in report.txt: " + e.getMessage());
        }
    }

    private int generateReportID() {
        reportCounter++;
        return reportCounter;
    }

    private void refreshTable() {
        submissionTableView.getItems().clear();
        populateTableFromManager();
    }
}