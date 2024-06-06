package student;

import com.example.project_management_system.PMS_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SubmittedController {
    @FXML
    private TableView<SubmittedTable> submittedTableView;
    @FXML
    private TableColumn<SubmittedTable, String> nameColumn;
    @FXML
    private TableColumn<SubmittedTable, String> moodleLinkColumn;
    @FXML
    private TableColumn<SubmittedTable, String> submissionDateColumn;
    @FXML
    private TableColumn<SubmittedTable, String> gradeColumn;

    private String currentUserID;

    @FXML
    private Button requestButton;

    @FXML
    private void initialize() {
        currentUserID = getLoggedInUserID();
        if (currentUserID != null && !currentUserID.isEmpty()) {
            initializeTableColumns();
            populateTableFromFile();
        } else {
            System.err.println("Error: Current user ID not available.");
        }
        submittedTableView.setRowFactory(tv -> new TableRow<SubmittedTable>() {
            @Override
            protected void updateItem(SubmittedTable item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    String status = item.getGrade();
                    if ("Graded".equalsIgnoreCase(status)) {
                        setStyle("-fx-background-color: #ff66cc;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        submittedTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String status = newSelection.getGrade();
                boolean isGraded = "Graded".equalsIgnoreCase(status);
                requestButton.setDisable(isGraded);
            } else {
                requestButton.setDisable(true);
            }
        });
        gradeColumn.setCellFactory(column -> new TableCell<SubmittedTable, String>() {
            private final Hyperlink viewFeedbackLink = new Hyperlink("View Feedback");

            {
                viewFeedbackLink.setOnAction(event -> {
                    SubmittedTable submission = getTableView().getItems().get(getIndex());
                    showFeedbackDialog(submission);
                });
            }

            @Override
            protected void updateItem(String grade, boolean empty) {
                super.updateItem(grade, empty);
                if (empty || grade == null || grade.equalsIgnoreCase("Ungraded")) {
                    setText(grade);
                    setGraphic(null);
                } else {
                    setText(null);
                    setGraphic(viewFeedbackLink);
                }
            }
        });
    }


    private void initializeTableColumns() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        moodleLinkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMoodleLink()));
        submissionDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubmissionDate()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
    }

    private String getLoggedInUserID() {
        String currentUserID = PMS_Controller.getLoggedInUserID();
        System.out.println("Current User ID: " + currentUserID);
        return currentUserID;
    }

    private void populateTableFromFile() {
        List<SubmittedTable> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line Read: " + line);
                String[] fields = line.split(",");
                System.out.println("Fields: " + Arrays.toString(fields));

                if (fields.length == 12) {
                    String reportID = fields[0];
                    String name = fields[1];
                    String studentID = fields[2];
                    String intake = fields[3];
                    String assessmentID = fields[4];
                    String moodleLink = fields[5];
                    String submissionDate = fields[6];
                    String grade = fields[7];
                    String status = fields[8];
                    String feedback = fields[9];
                    String supervisorID = fields[10];
                    String secondMarkerID = fields[11];

                    System.out.println("Report ID: " + reportID + ", Name: " + name + ", Student ID: " + studentID);

                    if (studentID.equals(currentUserID)) {
                        SubmittedTable submission = new SubmittedTable(reportID, name, studentID, intake, assessmentID, moodleLink, submissionDate, grade, status, feedback, supervisorID, secondMarkerID);
                        data.add(submission);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Number of submissions: " + data.size());
        submittedTableView.getItems().addAll(data);
    }

    private void handleEditSubmission(SubmittedTable selectedSubmission) {
        String reportID = selectedSubmission.getReportID();
        if (isPresentationAlreadyRequested(reportID)) {
            AlertManage.showErrorDialog("This submission has already requested a presentation. You cannot edit it.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File newFile = fileChooser.showOpenDialog(new Stage());

        if (newFile != null && Files.exists(newFile.toPath())) {
            try {
                String folderName = String.format("%s-%s", currentUserID, selectedSubmission.getName().replaceAll("\\s", "_"));
                String submissionsDirectory = "src/main/resources/submissions/";
                File folder = new File(submissionsDirectory + folderName);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String destinationFilePath = folder.getAbsolutePath() + "/" + newFile.getName();
                Files.copy(newFile.toPath(), Paths.get(destinationFilePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("New file inserted at: " + destinationFilePath);

                File originalFile = new File(selectedSubmission.getMoodleLink());
                if (originalFile.exists()) {
                    originalFile.delete();
                    System.out.println("Original file deleted: " + selectedSubmission.getMoodleLink());
                }

                String correctedDestinationPath = destinationFilePath.replaceAll("\\\\", "/");
                selectedSubmission.setMoodleLink(correctedDestinationPath);

                String relativePath = Paths.get("").toAbsolutePath().relativize(Paths.get(correctedDestinationPath)).toString().replaceAll("\\\\", "/");

                reportID = selectedSubmission.getReportID();
                String absolutePath = "src/main/resources/database/report.txt";
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);

                updateLinkAndSubmissionDate(reportID, relativePath, formattedDateTime, absolutePath, selectedSubmission.getFeedback());
                System.out.println("Moodle link and submission date updated in report file.");
                refreshTable();

                AlertManage.showInformationDialog("Submission Edited", "The submission has been successfully edited.");

            } catch (IOException e) {
                e.printStackTrace();
                AlertManage.showErrorDialog("An error occurred while editing the submission.");
            }
        }
    }

    private void updateLinkAndSubmissionDate(String reportID, String moodleLink, String submissionDate, String absolutePath, String feedback) throws IOException {
        Path reportFilePath = Paths.get(absolutePath);
        List<String> lines = Files.readAllLines(reportFilePath);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 10) {
                String lineReportID = parts[0];
                if (lineReportID.equals(reportID)) {
                    parts[5] = moodleLink;
                    parts[6] = submissionDate;
                    parts[9] = feedback;
                    updatedLines.add(String.join(",", parts));
                } else {
                    updatedLines.add(line);
                }
            }
        }
        Files.write(reportFilePath, updatedLines);
    }

    private void handleDeleteSubmission(SubmittedTable selectedSubmission) {
        String reportID = selectedSubmission.getReportID();
        if (isPresentationAlreadyRequested(reportID)) {
            AlertManage.showErrorDialog("This submission has already requested a presentation. You cannot delete it.");
            return;
        }
        String studentID = selectedSubmission.getStudentID();
        String courseName = selectedSubmission.getName();

        String folderName = String.format("%s-%s", studentID, courseName.replaceAll("\\s", "_"));
        String submissionsDirectory = "src/main/resources/submissions/";
        File folder = new File(submissionsDirectory + folderName);

        if (folder.exists()) {
            try {
                deleteFolder(folder);
                removeSubmissionFromReport(Integer.parseInt(reportID));
                System.out.println("Submission deleted successfully.");
                refreshTable();
                AlertManage.showInformationDialog("Submission Deleted", "The submission has been successfully deleted.");
            } catch (IOException e) {
                e.printStackTrace();
                AlertManage.showErrorDialog("An error occurred while deleting the submission.");
            }
        } else {
            AlertManage.showErrorDialog("Delete Error","Submission not found.");
        }
    }

    private void deleteFolder(File folder) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }

    private void removeSubmissionFromReport(int reportID) throws IOException {
        String absolutePath = "src/main/resources/database/report.txt";
        Path reportFilePath = Paths.get(absolutePath);

        if (Files.exists(reportFilePath)) {
            List<String> lines = Files.readAllLines(reportFilePath);
            List<String> updatedLines = getStrings(reportID, lines);

            Files.write(reportFilePath, updatedLines);
        } else {
            System.out.println("report.txt file not found at the specified path.");
        }
    }

    private static List<String> getStrings(int reportID, List<String> lines) {
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0) {
                int lineReportID = Integer.parseInt(parts[0]);
                if (lineReportID != reportID) {
                    updatedLines.add(line);
                }
            }
        }
        return updatedLines;
    }

    @FXML
    private void requestPresentationDate() {
        SubmittedTable selectedItem = submittedTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            openDatetimeWindow(selectedItem);
        } else {
            System.out.println("Error: No submission selected");
        }
    }

    private void openDatetimeWindow(SubmittedTable selectedSubmission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("datetime.fxml"));
            Parent root = loader.load();
            Datetime controller = loader.getController();
            controller.initData(selectedSubmission, this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Select Presentation Date");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        submittedTableView.getItems().clear();
        populateTableFromFile();
    }

    @FXML
    private void handleEditIconClicked() {
        SubmittedTable selectedSubmission = submittedTableView.getSelectionModel().getSelectedItem();
        if (selectedSubmission != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Edit Submission");
            confirmationAlert.setHeaderText("Are you sure you want to edit this submission?");
            confirmationAlert.setContentText("This action will replace the existing submission file with a new one.");

            ButtonType confirmButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    handleEditSubmission(selectedSubmission);
                }
            });
        } else {
            AlertManage.showErrorDialog("No submission selected. Please select a submission to edit.");
        }
    }

    @FXML
    private void handleDeleteIconClicked() {
        SubmittedTable selectedSubmission = submittedTableView.getSelectionModel().getSelectedItem();
        if (selectedSubmission != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Submission");
            confirmationAlert.setHeaderText("Are you sure you want to delete this submission?");
            confirmationAlert.setContentText("This action cannot be undone.");

            ButtonType confirmButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    handleDeleteSubmission(selectedSubmission);
                }
            });
        } else {
            AlertManage.showErrorDialog("No submission selected. Please select a submission to delete.");
        }
    }

    protected String getAssessmentType(String assessmentID) {
        String assessmentType = "";

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/FinalStudent_Assessment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11 && fields[0].equals(assessmentID)) {
                    assessmentType = fields[3];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assessmentType;
    }

    protected boolean isPresentationDateAlreadyRequested(String reportID, LocalDate date, String slot, String supervisorID, String secondMarkerID) {
        if (slot == null || slot.isEmpty()) {
            System.err.println("Invalid time slot. Time slot string cannot be empty.");
            return false;
        }

        String filePath = "src/main/resources/database/presentation_schedule.txt";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);
        String formattedSlot;

        try {
            formattedSlot = LocalTime.parse(slot.toUpperCase(), timeFormatter).format(timeFormatter);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing time slot: " + e.getMessage());
            return false;
        }

        boolean isReportIDAlreadyRequested = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PresentationID")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String existingReportID = parts[4];
                    LocalDate existingDate = LocalDate.parse(parts[5]);
                    String existingSlot = parts[6];
                    String supervisorStatus = parts[8];
                    String secondMarkerStatus = parts[10];

                    if (existingReportID.equals(reportID)) {
                        if (secondMarkerID.equals("-")) {
                            // Second marker is not assigned, only check supervisor's approval
                            if (supervisorStatus.equalsIgnoreCase("Approve")) {
                                isReportIDAlreadyRequested = true;
                            }
                        } else {
                            // Second marker is assigned, check both supervisor's and second marker's approval
                            if (!supervisorStatus.equalsIgnoreCase("Reject") && !secondMarkerStatus.equalsIgnoreCase("Reject")) {
                                isReportIDAlreadyRequested = true;
                            }
                        }
                    }

                    if (existingDate.equals(date) && existingSlot.equalsIgnoreCase(formattedSlot)) {
                        if (secondMarkerID.equals("-")) {
                            // Second marker is not assigned, only check supervisor's approval
                            if (supervisorStatus.equalsIgnoreCase("Pending") && (!existingReportID.equals(reportID) || (existingReportID.equals(reportID) && !supervisorStatus.equalsIgnoreCase("Reject")))) {
                                return true;
                            }
                        } else {
                            // Second marker is assigned, check both supervisor's and second marker's approval
                            if ((supervisorStatus.equalsIgnoreCase("Pending") || secondMarkerStatus.equalsIgnoreCase("Pending"))
                                    && (!existingReportID.equals(reportID) || (existingReportID.equals(reportID) && !supervisorStatus.equalsIgnoreCase("Reject") && !secondMarkerStatus.equalsIgnoreCase("Reject")))) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isReportIDAlreadyRequested;
    }



    private boolean isPresentationAlreadyRequested(String reportID) {
        String filePath = "src/main/resources/database/presentation_schedule.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PresentationID")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String existingReportID = parts[4];
                    String supervisorStatus = parts[8];
                    String secondMarkerStatus = parts[10];

                    if (existingReportID.equals(reportID)
                            && !supervisorStatus.equalsIgnoreCase("Reject")
                            && !secondMarkerStatus.equalsIgnoreCase("Reject")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showFeedbackDialog(SubmittedTable submission) {
        String reportID = submission.getReportID();
        String feedback = getFeedbackFromFile(reportID);
        if (feedback != null && !feedback.isEmpty()) {
            Alert feedbackDialog = new Alert(Alert.AlertType.INFORMATION);
            feedbackDialog.setTitle("Feedback");
            feedbackDialog.setHeaderText("Feedback for " + submission.getName());
            feedbackDialog.setContentText(feedback);
            feedbackDialog.showAndWait();
        } else {
            AlertManage.showErrorDialog("No feedback available for this submission.");
        }
    }

    private String getFeedbackFromFile(String reportID) {
        String filePath = "src/main/resources/database/report.txt";
        String feedback = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10 && parts[0].equals(reportID)) {
                    feedback = parts[9];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return feedback;
    }
}
