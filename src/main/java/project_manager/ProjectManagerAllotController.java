package project_manager;

import java.io.*;
import java.net.URL;
import java.util.*;

import com.example.project_management_system.PMS_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lecturer.ReportController;

public class ProjectManagerAllotController implements Initializable {

    private ObservableList<StudentData> studentData = FXCollections.observableArrayList();
    private ObservableList<ProjectData2> projectData = FXCollections.observableArrayList();

    private static final String STUDENT_FILE_PATH = "src/main/resources/database/student.txt";
    private static final String ASSESSMENT_FILE_PATH = "src/main/resources/database/assessment.txt";
    private static final String FINAL_ASSESSMENT_FILE_PATH = "src/main/resources/database/FinalStudent_Assessment.txt";

    @FXML
    private Button exitButton;
    @FXML
    private Button clearButton;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private TableView<StudentData> table1;
    @FXML
    private Button buttonAllot;
    @FXML
    private Button buttonGroup;
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
    private TableColumn tableColumnStatus;
    @FXML
    private  TextField fieldSearch;

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
    @FXML
    private ChoiceBox<String> choiceboxIntake; // 添加ChoiceBox

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStudentTable();
        initializeProjectTable();
        loadStudentData();
        loadAssessmentData();
        this.comboBox();
//        ObservableList<String> intakeOptions = FXCollections.observableArrayList(
//                "UCDF2101", "UCDF2102", "UCDF2103", "UCDF2104", "UCDF2105"
//        );
//        choiceboxIntake.setItems(intakeOptions);

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
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
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
                boolean isAllocated = isStudentAllocated(data[0]);
                StudentData student = new StudentData(data[0], data[1], data[3], isAllocated ? "have" : "haven't"); // 读取 Studentid, name 和 intakecode
                studentData.add(student);
            }
            table1.setItems(studentData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isStudentAllocated(String studentId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FINAL_ASSESSMENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[8].equals(studentId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
    public void handleButtonAllot(ActionEvent event) throws IOException {
        if (fieldStudentID.getText().isEmpty()||fieldStudentName.getText().isEmpty()||
                ShowAssID.getText().isEmpty()||ShowAssName.getText().isEmpty()||ShowDescription.getText().isEmpty()||
                ShowType.getText().isEmpty()||fieldStudentIntake.getText().isEmpty()||ShowSupervisor.getText().isEmpty()||
                ShowSecMarker.getText().isEmpty()||dueDateTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up!");
            alert.showAndWait();
        }else {
            ArrayList<String> oldContent2 = new ArrayList<>();
            ArrayList<String> oldContent = new ArrayList<>();
            String line3 = null;
            String line2 = null;
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

                    // Update the student's status in the table
                    updateStudentStatus(studentId, "have");
                    table1.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                while ((line2= reader2.readLine())!= null){
                    String[] info2 = line2.split(",");
                    if (info2[0].equals(fieldStudentID.getText())) {
                        System.out.println("Student found in student.txt");
                        info2[4] = ShowSupervisor.getText();
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(info2[0]).append(",");
                    sb2.append(info2[1]).append(",");
                    sb2.append(info2[2]).append(",");
                    sb2.append(info2[3]).append(",");
                    sb2.append(info2[4]).append(",");
                    sb2.append(info2[5]);

                    oldContent2.add(String.valueOf(sb2));
                    System.out.println("oldContent2: "+oldContent2);
                }

                File file = new File("src/main/resources/database/student.txt");
                FileWriter writer = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(writer);
                for (String s : oldContent2) {
                    br.write(s);
                    br.newLine();
                }
                br.close();
            }
        }

    }

    //

    @FXML
    public void handleButtonGroup(ActionEvent event) throws IOException {
        String selectedIntakeCode = choiceboxIntake.getValue();

        if (selectedIntakeCode == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please select an IntakeCode.");
            errorAlert.showAndWait();
            return;
        }

        // 根据选定的IntakeCode执行群组分配操作
        groupAllot(selectedIntakeCode);
    }

    private void groupAllot(String selectedIntakeCode) throws IOException {
        ArrayList<String> oldContent2 = new ArrayList<>();
        ArrayList<String> oldContent = new ArrayList<>();
        String line3 = null;
        String line2 = null;
        for (StudentData student : studentData) {
            if (student.getIntake().equals(selectedIntakeCode)) {

                String studentId = student.getStudentId();
                String studentName = student.getStudentName();
                String studentIntake = student.getIntake();

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
                        studentName + ",Group"; // 在属性末尾加上“Group”字眼

                if (isDuplicateAssignment(assId, studentId, "Group")) {
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

                        // Update the student's status in the table
                        updateStudentStatus(studentId, "have");
                        table1.refresh();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                    while ((line2= reader2.readLine())!= null){
                        String[] info2 = line2.split(",");
                        if (info2[0].equals(student.getStudentId())) {
                            System.out.println("Student found in student.txt");
                            info2[4] = ShowSupervisor.getText();
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(info2[0]).append(",");
                        sb2.append(info2[1]).append(",");
                        sb2.append(info2[2]).append(",");
                        sb2.append(info2[3]).append(",");
                        sb2.append(info2[4]).append(",");
                        sb2.append(info2[5]);

                        oldContent2.add(String.valueOf(sb2));
                        System.out.println("oldContent2: "+oldContent2);
                    }

                    File file = new File("src/main/resources/database/student.txt");
                    FileWriter writer = new FileWriter(file);
                    BufferedWriter br = new BufferedWriter(writer);
                    for (String s : oldContent2) {
                        br.write(s);
                        br.newLine();
                    }
                    br.close();
                }
            }
        }
    }

    //
    private void updateStudentStatus(String studentId, String status) {
        for (StudentData student : studentData) {
            if (student.getStudentId().equals(studentId)) {
                student.setStatus(status);
                table1.refresh();
                break;
            }
        }
    }
    private boolean isDuplicateAssignment(String assId, String studentId, String type) {
        List<String> existingAssignments = loadExistingAssignments();
        for (String assignment : existingAssignments) {
            String[] data = assignment.split(",");
            if (data.length >= 3 && data[0].equals(assId) && data[1].equals(studentId) && data[2].equals(type)) {
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
    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = fieldSearch.getText().trim(); // 获取搜索框中的内容并去除空格

        if (searchText.isEmpty()) {
            // 如果搜索框为空，不执行搜索操作
            return;
        }

        ObservableList<StudentData> searchResults = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // 在 Student.txt 中搜索匹配的数据，可以根据需要修改搜索条件
                if (data[0].contains(searchText) || data[1].contains(searchText) || data[3].contains(searchText)) {
                    StudentData student = new StudentData(data[0], data[1], data[3],"");
                    searchResults.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table1.setItems(searchResults); // 将搜索结果显示在表格中
    }
    @FXML
    public void handleClear(ActionEvent event) {
        // 清除搜索框中的文本内容
        fieldSearch.clear();
        ShowSecMarker.clear();
        ShowType.clear();
        ShowSupervisor.clear();
        ShowAssName.clear();
        ShowDescription.clear();
        ShowAssID.clear();
        dueDateTextField.clear();
        fieldStudentID.clear();
        fieldStudentName.clear();
        fieldStudentIntake.clear();

        // 恢复表格显示原始的学生数据
        loadStudentData();
    }

    public void comboBox(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/intakeCode.txt"));
            ArrayList<String> roomType = new ArrayList<String>();
            String line = null;
            while((line = reader.readLine()) != null){
                this.choiceboxIntake.getItems().add(line);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        this.choiceboxIntake.setValue("Any");
    }
}


