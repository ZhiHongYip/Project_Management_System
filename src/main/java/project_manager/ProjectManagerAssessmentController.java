package project_manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProjectManagerAssessmentController implements Initializable {
    @FXML
    private Button exit;
    @FXML
    private Button deleteButton;
    @FXML
    private DatePicker selectDate;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private TableColumn<ProjectData, Integer> projectIdColumn;
    @FXML
    private TableColumn<ProjectData, String> Project;
    @FXML
    private TableColumn<ProjectData, String> Description;
    @FXML
    private TableColumn<ProjectData, String> Types;
    @FXML
    private TableColumn<ProjectData, String> Intake;
    @FXML
    private TableColumn<ProjectData, String> Lecture;
    @FXML
    private TableColumn<ProjectData, String> Marker;
    @FXML
    private TableColumn<ProjectData, LocalDate> dueDateColumn;
    @FXML
    private TableView<ProjectData> tableAssessment;
    @FXML
    private Button createbutton;
    @FXML
    private Button updatebutton;
    @FXML
    private TextField fieldProjectName;
    @FXML
    private TextField fieldDescription;
    @FXML
    private ChoiceBox<String> fieldIntake;
    @FXML
    private ChoiceBox<String> fieldLecture;
    private ObservableList<String> lectureOptions = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> fieldLecture2;

    private ObservableList<String> intakeOption = FXCollections.observableArrayList();
    private ObservableList<String> lectureOptions2 = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> fieldTypes;
    private String[] types = {"Internship", "Investigation Report", "RMCP", "Capstone Project-P1", "Capstone Project-P2", "FYP"};
    private int projectId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Project.setCellValueFactory(new PropertyValueFactory<>("project"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Types.setCellValueFactory(new PropertyValueFactory<>("types"));
        Intake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        Lecture.setCellValueFactory(new PropertyValueFactory<>("lecturerId"));
        Marker.setCellValueFactory(new PropertyValueFactory<>("markerId"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        Types.setCellFactory(tc -> new ChoiceBoxTableCell<>(FXCollections.observableArrayList(types)));
        Intake.setCellFactory(tc -> new ChoiceBoxTableCell<>(intakeOption));
        Lecture.setCellFactory(tc -> new ChoiceBoxTableCell<>(lectureOptions2));
        Marker.setCellFactory(tc -> new ChoiceBoxTableCell<>(lectureOptions));


        fieldTypes.getItems().addAll(types);

        readIntakeFromFile();
        fieldIntake.getItems().addAll(intakeOption);

        readLectureOptionsFromFile();
        fieldLecture.setItems(lectureOptions);
        fieldLecture2.setItems(lectureOptions2);

        projectIdColumn.setCellValueFactory(new PropertyValueFactory<>("projectId"));

        loadDatabaseData();
    }

    private void readLectureOptionsFromFile() {
        this.lectureOptions2.add("");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/lecturer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String lecturerId = parts[0].trim();
                    String markerId = parts[0].trim();
                    lectureOptions.add(lecturerId);
                    lectureOptions2.add(markerId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickUpdate(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to update the projects?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/assessment.txt", false))) {
                for (ProjectData data : tableAssessment.getItems()) {
                    writer.write(data.getProjectId() + "," + data.getProject() + "," + data.getDescription() + "," + data.getTypes() + "," +
                            data.getIntake() + "," + data.getLecturerId() + ","  +  data.getMarkerId()+  ","  + data.getDueDate() + "\n");
                }
                System.out.println("Data has been updated in the file.");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    private int findNextAvailableProjectId() {
        int maxProjectId = 0;
        for (ProjectData data : tableAssessment.getItems()) {
            if (data.getProjectId() > maxProjectId) {
                maxProjectId = data.getProjectId();
            }
        }
        return maxProjectId + 1;
    }

    @FXML
    private void clickCreate(ActionEvent event) {
        if (!fieldProjectName.getText().isEmpty() || !fieldDescription.getText().isEmpty()) {
            String projectName = fieldProjectName.getText();
            String description = fieldDescription.getText();
            String intake = fieldIntake.getValue();
            String types = fieldTypes.getValue();
            String lecture = fieldLecture.getValue();
            String marker = fieldLecture2.getValue();

            LocalDate selectedDate = selectDate.getValue();

            if (selectedDate != null) {
                int nextProjectId = findNextAvailableProjectId();
                ProjectData projectData = new ProjectData(
                        nextProjectId,
                        projectName,
                        description,
                        types,
                        intake,
                        lecture, // 直接使用 lecture 和 marker 作为字符串
                        marker
                );
                projectData.setDueDate(selectedDate);

                tableAssessment.getItems().add(projectData);

                fieldProjectName.clear();
                fieldDescription.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a date.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up!");
            alert.showAndWait();
        }
    }

    @FXML
    public void exit(ActionEvent event) {
        Stage stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("Success");
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

    private void loadDatabaseData() {
        int maxProjectId = 0;

        ObservableList<ProjectData> projectDataList = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 8) {
                        try {
                            int projectId = Integer.parseInt(parts[0]);
                            String project = parts[1];
                            String description = parts[2];
                            String types = parts[3];
                            String intake = parts[4];
                            String lectureId = parts[5]; // 读取为 String
                            String markerId = parts[6]; // 读取为 String
                            LocalDate dueDate = LocalDate.parse(parts[7]);

                            ProjectData projectData = new ProjectData(projectId, project, description, types, intake, lectureId, markerId);
                            projectData.setDueDate(dueDate);
                            projectDataList.add(projectData);

                            if (projectId > maxProjectId) {
                                maxProjectId = projectId;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid projectId format: " + parts[0]);
                        }
                    } else {
                        System.err.println("Invalid data format in database.txt: " + line);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        projectId = maxProjectId + 1;
        tableAssessment.setItems(projectDataList);
    }

    @FXML
    public void setDeleteButton(ActionEvent event) {
        ProjectData selectedProject = tableAssessment.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this project?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                tableAssessment.getItems().remove(selectedProject);
                System.out.println("Project deleted successfully.");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a project to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    public void setSelectDate(ActionEvent event) {
        ProjectData selectedProject = tableAssessment.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            LocalDate selectedDate = selectedProject.getDueDate();
            if (selectedDate != null) {
                System.out.println("Selected date: " + selectedDate);
            } else {
                System.out.println("Selected date is null");
            }
        } else {
            System.out.println("No project selected");
        }
    }
    @FXML
    public void handleClear(ActionEvent event) {
        fieldProjectName.clear();
        fieldDescription.clear();
    }

    private void readIntakeFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/intakeCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String Intake = parts[0].trim();
                    intakeOption.add(Intake);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
