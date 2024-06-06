package student;

import com.example.project_management_system.PMS_Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileController {
    @FXML
    private Label studentID;

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Label intake;

    @FXML
    private TableView<Presentation> presentationTable;

    @FXML
    private TableColumn<Presentation, String> assessmentTypeCol;

    @FXML
    private TableColumn<Presentation, String> dateCol;

    @FXML
    private TableColumn<Presentation, String> slotCol;

    @FXML
    private TableColumn<Presentation, String> statusCol;

    private String currentUserID;

    @FXML
    private void initialize() {
        currentUserID = PMS_Controller.getLoggedInUserID();

        if (studentID == null || email == null || name == null || intake == null || presentationTable == null) {
            System.err.println("Labels or presentationTable not initialized");
            return;
        }

        try {
            Student student = getStudentData(currentUserID);
            if (student != null) {
                studentID.setText(student.getStudentID());
                email.setText(student.getEmail());
                name.setText(student.getName());
                intake.setText(student.getIntake());
            } else {
                System.err.println("Student data is null");
            }

            List<Presentation> presentations = readPresentationSchedule(currentUserID);
            presentationTable.getItems().setAll(presentations);


            assessmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("assessmentName"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            slotCol.setCellValueFactory(new PropertyValueFactory<>("slot"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

            statusCol.setCellFactory(column -> new TableCell<Presentation, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if ("Reject".equalsIgnoreCase(item)) {
                            setTextFill(javafx.scene.paint.Color.RED);
                        } else if ("Approve".equalsIgnoreCase(item)) {
                            setTextFill(javafx.scene.paint.Color.GREEN);
                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);
                        }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Student getStudentData(String studentID) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(studentID)) {
                    return new Student(parts[0], parts[1], parts[2], parts[3]);
                }
            }
        }
        return null;
    }

    private Map<String, String> readAssessmentNames() throws IOException {
        Map<String, String> assessmentNames = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    assessmentNames.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return assessmentNames;
    }


    private List<Presentation> readPresentationSchedule(String studentID) throws IOException {
        List<Presentation> presentations = new ArrayList<>();
        Map<String, String> assessmentNames = readAssessmentNames();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 12 && parts[1].trim().equals(studentID)) {
                    String reportID = parts[4].trim();
                    String assessmentName = assessmentNames.get(reportID);
                    if (assessmentName != null) {
                        String date = parts[5].trim();
                        String slot = parts[6].trim();
                        String status = parts[11].trim();

                        presentations.add(new Presentation(assessmentName, date, slot, status));
                    } else {
                        System.err.println("Assessment name not found for report ID: " + reportID);
                    }
                }
            }
        }
        return presentations;
    }

}
