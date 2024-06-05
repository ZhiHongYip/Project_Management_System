package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class addStudentbyGrp {

    @FXML
    private TableColumn<Student, String> studentIDClm;

    @FXML
    private TableColumn<Student, String> studentNameClm;

    @FXML
    private TableColumn<Student, String> intakeCodeClm;

    @FXML
    private TableColumn<Student, String> emailClm;

    @FXML
    private TableView<Student> addStudentByGrpTableView;

    @FXML
    private ComboBox<String> selectStudentIntakeCombo;

    @FXML
    private Button addByIntake;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<String> intakeList = FXCollections.observableArrayList();
    private List<Student> allStudents = new ArrayList<>();

    public void initialize() {
        // Set up TableView columns
        studentIDClm.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameClm.setCellValueFactory(new PropertyValueFactory<>("name"));
        intakeCodeClm.setCellValueFactory(new PropertyValueFactory<>("intakeCode"));
        emailClm.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Assign the observable list to the TableView
        addStudentByGrpTableView.setItems(studentList);

        // Populate intake list from file and assign it to ComboBox
        populateIntakeListFromFile();
        selectStudentIntakeCombo.setItems(intakeList);
    }

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
    private void handleAddByIntake(ActionEvent event) {
        String selectedIntake = selectStudentIntakeCombo.getSelectionModel().getSelectedItem();
        if (selectedIntake != null) {
            // Open file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Student Data File");
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length == 4) {
                            Student student = new Student(data[0], data[1], data[2], selectedIntake,data[3]);
                            allStudents.add(student);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle file reading error
                }

                // Add students to TableView
                studentList.clear();
                studentList.addAll(allStudents);

                // Write student information to files
                for (Student student : allStudents) {
                    writeStudentToFile(student);
                    writeStudentToUserFile(student);
                }

                // Remove added students from the selected file
                removeStudentsFromFile(allStudents, selectedFile);
            }
        }
    }

    private void writeStudentToFile(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/student.txt", true))) {
            writer.write(student.getId() + "," + student.getName() + "," + student.getEmail() + "," + student.getIntakeCode() + "," + student.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStudentToUserFile(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/user.txt", true))) {
            writer.write(student.getId() + "," + student.getName() + "," + student.getEmail() + "," + student.getPassword() + ",student");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeStudentsFromFile(List<Student> studentsToRemove, File originalFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter("src/main/resources/database/studentWithoutIntake.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                boolean shouldWrite = true;
                for (Student student : studentsToRemove) {
                    if (line.startsWith(student.getId() + ",")) {
                        shouldWrite = false;
                        break;
                    }
                }
                if (shouldWrite) {
                    fileWriter.write(line);
                    fileWriter.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Replace the original file with the temp file
        File tempFile = new File("src/main/resources/database/studentWithoutIntake.txt");
        if (tempFile.renameTo(originalFile)) {
        } else {
        }
    }

    public static class Student {
        private String id;
        private String name;
        private String email;
        private String intakeCode;
        private String password;

        public Student(String id, String name, String email, String intakeCode, String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.intakeCode = intakeCode;
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getIntakeCode() {
            return intakeCode;
        }

        public String getPassword() {
            return password;
        }
    }
}