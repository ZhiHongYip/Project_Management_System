package project_manager;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ProjectManagerAllotController implements Initializable {

    private ObservableList<StudentData> studentData = FXCollections.observableArrayList();
    private ObservableList<ProjectData2> projectData = FXCollections.observableArrayList();

    private static final String STUDENT_FILE_PATH = "src/main/resources/database/student.txt";
    private static final String ASSESSMENT_FILE_PATH = "src/main/resources/database/assessment.txt";
    private static final String FINAL_ASSESSMENT_FILE_PATH = "src/main/resources/database/FinalStudent_Assessment.txt";

    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private TableView<StudentData> table1;
    @FXML
    private Button buttonAllot;
    @FXML
    private Button buttonGroupAllot;

    @FXML
    private TableView<ProjectData2> table2;

    @FXML
    private TextField ShowAssID;
    @FXML
    private TextField ShowAssName;
    @FXML
    private TextField ShowDescription;
    @FXML
    private TextField ShowSecMarker;
    @FXML
    private TextField ShowSupervisor;
    @FXML
    private TextField ShowType;
    @FXML
    private TextField fieldStudentID;
    @FXML
    private TextField fieldStudentIntake;
    @FXML
    private TextField fieldStudentName;
    @FXML
    private TextField dueDateTextField;
    @FXML
    private TextField fieldIntake;
    @FXML
    private TextField tableStatusField;

    @FXML
    private TableColumn<StudentData, String> tableStudentID;
    @FXML
    private TableColumn<StudentData, String> tableStudentName;
    @FXML
    private TableColumn<StudentData, String> tableStudentIntake;
    @FXML
    private TableColumn<ProjectData2, String> tableAssID;
    @FXML
    private TableColumn<ProjectData2, String> tableAssName;
    @FXML
    private TableColumn<ProjectData2, String> tableDescription;
    @FXML
    private TableColumn<ProjectData2, String> tableIntake;
    @FXML
    private TableColumn<ProjectData2, String> tableType;
    @FXML
    private TableColumn<ProjectData2, String> tableSupervisor;
    @FXML
    private TableColumn<ProjectData2, String> tableMarker;
    @FXML
    private TableColumn<ProjectData2, String> tableDueDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStudentTable();
        initializeProjectTable();
        loadStudentData();
        loadAssessmentData();

        table1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fieldStudentID.setText(newSelection.getStudentId());
                fieldStudentName.setText(newSelection.getStudentName());
                fieldStudentIntake.setText(newSelection.getIntake());
            }
        });

        table2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ShowAssID.setText(newSelection.getAssId());
                ShowAssName.setText(newSelection.getAssName());
                ShowDescription.setText(newSelection.getDescription());
                ShowType.setText(newSelection.getTypes());
                fieldStudentIntake.setText(newSelection.getIntake());
                ShowSupervisor.setText(newSelection.getSupervisor());
                ShowSecMarker.setText(newSelection.getSecondMarker());
                dueDateTextField.setText(newSelection.getDueDate());
            }
        });
    }

    private void initializeStudentTable() {
        tableStudentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tableStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tableStudentIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
    }

    private void initializeProjectTable() {
        tableAssID.setCellValueFactory(new PropertyValueFactory<>("AssId"));
        tableAssName.setCellValueFactory(new PropertyValueFactory<>("AssName"));
        tableDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("types"));
        tableSupervisor.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
        tableMarker.setCellValueFactory(new PropertyValueFactory<>("SecondMarker"));
        tableDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    private void loadStudentData() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                StudentData student = new StudentData(data[0], data[1], data[3]); // 读取 Studentid, name 和 intakecode
                studentData.add(student);
            }
            table1.setItems(studentData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAssessmentData() {
        try (BufferedReader br = new BufferedReader(new FileReader(ASSESSMENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                ProjectData2 project = new ProjectData2(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                projectData.add(project);
            }
            table2.setItems(projectData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonAllot(ActionEvent event) {
        String studentId = fieldStudentID.getText();
        String studentName = fieldStudentName.getText();
        String studentIntake = fieldStudentIntake.getText();

        String assId = ShowAssID.getText();
        String assName = ShowAssName.getText();
        String description = ShowDescription.getText();
        String type = ShowType.getText();
        String intake = fieldStudentIntake.getText();
        String supervisor = ShowSupervisor.getText();
        String secondmarker = ShowSecMarker.getText();
        String dueDate = dueDateTextField.getText();

        String line = assId + "," + assName + "," + description + "," + type + "," + intake + "," +
                supervisor + "," + secondmarker + "," + dueDate + "," + studentId + "," +
                studentName + ",individual";

        if (isDuplicateAssignment(assId, studentId, "individual")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("This assignment has already been allocated to this student.");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to allot this assessment?");
        ButtonType confirmButtonType = new ButtonType("Yes");
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmAlert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FINAL_ASSESSMENT_FILE_PATH, true))) {
                writer.write(line);
                writer.newLine();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data has been successfully written to the file.");
                successAlert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleButtonGroupAllot(ActionEvent event) {
        String intakeCode = fieldIntake.getText();
        List<String> existingAssignments = loadExistingAssignments();

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[3].equals(intakeCode)) {
                    String studentId = data[0];
                    String studentName = data[1];
                    String assId = ShowAssID.getText();
                    String assName = ShowAssName.getText();
                    String description = ShowDescription.getText();
                    String type = ShowType.getText();
                    String intake = fieldStudentIntake.getText();
                    String supervisor = ShowSupervisor.getText();
                    String secondmarker = ShowSecMarker.getText();
                    String dueDate = dueDateTextField.getText();
                    String groupLine = assId + "," + assName + "," + description + "," + type + "," + intake + "," +
                            supervisor + "," + secondmarker + "," + dueDate + "," + studentId + "," +
                            studentName + ",group";

                    if (tableStatusField.getText().equals("Have") && isDuplicateAssignment(assId, studentId, "group")) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("This assignment has already been allocated to this student.");
                        errorAlert.showAndWait();
                        return;
                    }

                    if (!existingAssignments.contains(assId + "," + studentId + ",group")) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FINAL_ASSESSMENT_FILE_PATH, true))) {
                            writer.write(groupLine);
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Group allotment has been successfully written to the file.");
            successAlert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDuplicateAssignment(String assId, String studentId, String type) {
        List<String> existingAssignments = loadExistingAssignments();
        for (String assignment : existingAssignments) {
            String[] data = assignment.split(",");
            if (data[0].equals(assId) && data[1].equals(studentId) && data[2].equals(type)) {
                return true;
            }
        }
        return false;
    }

    private List<String> loadExistingAssignments() {
        List<String> existingAssignments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FINAL_ASSESSMENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 10) { // Ensure the array has at least 10 elements
                    existingAssignments.add(data[0] + "," + data[8] + "," + data[9]); // Ass_Id, Student_ID, and type
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingAssignments;
    }

    @FXML
    public void exit(ActionEvent event) {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectManagerProfile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

