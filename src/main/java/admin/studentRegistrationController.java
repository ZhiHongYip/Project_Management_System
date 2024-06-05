package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;

public class studentRegistrationController {

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> studentColumnEmail;

    @FXML
    private TableColumn<Student, String> studentColumnIntake;

    @FXML
    private TableColumn<Student, String> studentColumnName;

    @FXML
    private TableColumn<Student, String> studentColumnID;

    @FXML
    private TextField studentEmail;

    @FXML
    private TextField studentID;

    @FXML
    private TextField studentName;

    @FXML
    private TextField studentPassword;

    @FXML
    private ComboBox<String> intakeCombox;

    @FXML
    private Label SIDLabel, SNameLabel, SEmailLabel, SPasswordLabel;

    @FXML
    private Button addStudentBtn;

    @FXML
    private Button addStudentbyGrpBtn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() {
        populateStudentListFromFile();
        studentColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentColumnIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        studentColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentTableView.setItems(studentList);
        populateIntakeListFromFile();
        intakeCombox.setItems(intakeList);
    }

    private ObservableList<String> intakeList = FXCollections.observableArrayList();

    private void populateIntakeListFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/intakeCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                intakeList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent(ActionEvent event) {
        if (submitButtonClicked()) {
            String id = studentID.getText();
            String name = studentName.getText();
            String email = studentEmail.getText();
            String intake = intakeCombox.getValue();
            String password = studentPassword.getText();
            Student newStudent = new Student(id, name, email, intake);
            studentList.add(newStudent);
            writeStudentToFile(id, name, email, intake, password);
            writeStudentToUserFile(id, name, email, intake, password);
            clearFields();
        }
    }

    @FXML
    private void showAddStudentByGroupWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addStudentbyGrp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Student by Group");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean submitButtonClicked() {
        boolean validated = true;
        if (studentID.getText() == null || studentID.getText().isEmpty()) {
            SIDLabel.setVisible(true);
            validated = false;
        } else {
            SIDLabel.setVisible(false);
        }
        if (studentName.getText() == null || studentName.getText().isEmpty()) {
            SNameLabel.setVisible(true);
            validated = false;
        } else {
            SNameLabel.setVisible(false);
        }
        if (studentEmail.getText() == null || studentEmail.getText().isEmpty()) {
            SEmailLabel.setVisible(true);
            validated = false;
        } else {
            SEmailLabel.setVisible(false);
        }
//        if (studentIntake.getText() == null || studentIntake.getText().isEmpty()) {
//            SIntakeLabel.setVisible(true);
//            validated = false;
//        } else {
//            SIntakeLabel.setVisible(false);
//        }
        if (studentPassword.getText() == null || studentPassword.getText().isEmpty()) {
            SPasswordLabel.setVisible(true);
            validated = false;
        } else {
            SPasswordLabel.setVisible(false);
        }
        return validated;
    }

    private void populateStudentListFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/student.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String id = data[0];
                    String name = data[1];
                    String email = data[2];
                    String intake = data[3];
                    Student student = new Student(id, name, email, intake);
                    studentList.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("populateStudentList from file");
    }

    private void writeStudentToFile(String id, String name, String email, String intake, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/student.txt", true))) {
            writer.write(id + "," + name + "," + email + "," + intake + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write student to file");
    }

    private void writeStudentToUserFile(String id, String name, String email, String intake, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/user.txt", true))) {
            writer.write(id + "," + name + "," + email + "," + password + "," + "student");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write student to user file");
    }

    private void clearFields() {
        studentID.clear();
        studentName.clear();
        studentEmail.clear();
//        studentIntake.clear();
        studentPassword.clear();
    }

    public static class Student {
        private String id;
        private String name;
        private String email;
        private String intake;

        public Student(String id, String name, String email, String intake) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.intake = intake;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIntake() {
            return intake;
        }

        public void setIntake(String intake) {
            this.intake = intake;
        }
    }
}
