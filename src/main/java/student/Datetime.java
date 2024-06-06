package student;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Datetime {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeTextField;

    @FXML
    private Label submissionDetailsLabel;

    @FXML
    private Pane pane;

    @FXML
    private Label errorMessage;

    private SubmittedController submittedController;
    private SubmittedTable selectedSubmission;

    @FXML
    public void initialize() {
        System.out.println("Datetime controller initialized");
    }

    public void initData(SubmittedTable selectedSubmission, SubmittedController submittedController) {
        this.selectedSubmission = selectedSubmission;
        this.submittedController = submittedController;

        if (selectedSubmission != null) {
            submissionDetailsLabel.setText("Selected Submission: " + selectedSubmission.getName());
        } else {
            submissionDetailsLabel.setText("No submission selected.");
        }
    }

    @FXML
    private void submitDateTime() {
        LocalDate date = datePicker.getValue();
        String time = timeTextField.getText().toUpperCase();
        LocalTime localTime = parseTime(time);

        if (date != null && localTime != null) {
            String formattedTime = localTime.format(DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH));

            if (submittedController != null) {
                if (selectedSubmission != null) {
                    String reportID = selectedSubmission.getReportID();
                    String supervisorID = selectedSubmission.getSupervisorID();
                    String secondMarkerID = selectedSubmission.getSecondMarkerID();
                    String secondMarkerStatus;
                    if (secondMarkerID.equals("-")) {
                        secondMarkerStatus = "-";
                    } else {
                        secondMarkerStatus = "Pending";
                    }

                    if (submittedController.isPresentationDateAlreadyRequested(reportID, date, time, supervisorID, secondMarkerID)) {
                        errorMessage.setText("Presentation date and slot have already been requested for this report ID or lecturer at the same time.");
                        return;
                    }

                    if (submittedController.isPresentationDateAlreadyRequested(reportID, date, time, supervisorID, secondMarkerID)) {
                        errorMessage.setText("Presentation date and slot clash with a previous booking.");
                        return;
                    }

                    LocalDateTime dateTime = LocalDateTime.of(date, localTime);
                    System.out.println("Submitted Date and Time: " + dateTime);

                    String presentationID = generatePresentationID();
                    String studentID = selectedSubmission.getStudentID();
                    String assessmentID = selectedSubmission.getAssessmentID();
                    String assessmentType = submittedController.getAssessmentType(assessmentID);
                    String intake = selectedSubmission.getIntake();

                    String presentationData = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            presentationID, studentID, assessmentType, intake, reportID, date, formattedTime,
                            supervisorID, "Pending", secondMarkerID, secondMarkerStatus, "Pending");

                    savePresentationDataToFile(presentationData);
                    closeWindow();
                } else {
                    errorMessage.setText("No submission selected.");
                }
            } else {
                errorMessage.setText("submittedController is not initialized.");
            }
        } else {
            errorMessage.setText("Please select a date and enter a valid time (e.g., 10:00AM, 01:00PM).");
        }
    }

    private void savePresentationDataToFile(String data) {
        String filePath = "src/main/resources/database/presentation_schedule.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
            System.out.println("Presentation data saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generatePresentationID() {
        Set<String> existingIDs = readExistingPresentationIDs();
        int newID = 1;
        while (existingIDs.contains(String.valueOf(newID))) {
            newID++;
        }
        return String.valueOf(newID);
    }

    private Set<String> readExistingPresentationIDs() {
        Set<String> existingIDs = new HashSet<>();
        String filePath = "src/main/resources/database/presentation_schedule.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    existingIDs.add(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingIDs;
    }

    private LocalTime parseTime(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);
            return LocalTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format. Please use the format 'XX:XXam/pm'.");
            return null;
        }
    }

    private void closeWindow() {
        Scene scene = pane.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }
}
