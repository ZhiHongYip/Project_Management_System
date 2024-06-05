package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class lecturerRegistrationController {
    @FXML
    private TableView<admin.lecturerTableView.Lecturer> lecturerTableView;

    @FXML
    private TableColumn<admin.lecturerTableView.Lecturer, String> lecturerIDColumn;

    @FXML
    private TableColumn<admin.lecturerTableView.Lecturer, String> lecturerNameColumn;

    @FXML
    private TableColumn<admin.lecturerTableView.Lecturer, String> lecturerEmailColumn;

    @FXML
    private TextField lecturerID;

    @FXML
    private TextField lecturerName;

    @FXML
    private TextField lecturerEmail;

    @FXML
    private TextField lecturerPassword;

    @FXML
    private Label LIDLabel, LNameLabel, LEmailLabel, LPasswordLabel;

    @FXML
    private Button addLecturerBtn;
    @FXML
    private Button addLectbyGrpBtn;

    private ObservableList<admin.lecturerTableView.Lecturer> LecturerList = FXCollections.observableArrayList();
    private List<admin.lecturerTableView.Lecturer> allLecturer = new ArrayList<>();

    public void initialize() {
        populateLecturerListFromFile();
        // Set up TableView columns
        lecturerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lecturerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lecturerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Assign the observable list to the TableView
        lecturerTableView.setItems(LecturerList);
    }

    @FXML
    private void addLecturer(ActionEvent event) {
        if (submitButtonClicked()) {
            // Retrieve data from text fields
            String id = lecturerID.getText();
            String name = lecturerName.getText();
            String email = lecturerEmail.getText();
            String password = lecturerPassword.getText();

            // Create a new Lecturer object
            admin.lecturerTableView.Lecturer newLecturer = new admin.lecturerTableView.Lecturer(id, name, email);

            // Add the new Lecturer to the TableView
            LecturerList.add(newLecturer);

            // Write lecturer information to file
            writeLecturerToFile(newLecturer, password);
            writeLecturerToUserFile(newLecturer, password);

            // Clear input fields after adding
            clearFields();
        }
    }

    @FXML
    private void handleAddLecturerbyGrp(ActionEvent event) {
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Lecturer Data File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 4) {
                        admin.lecturerTableView.Lecturer lecturer = new admin.lecturerTableView.Lecturer(data[0], data[1], data[2]);
                        allLecturer.add(lecturer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add lecturer to TableView
            LecturerList.clear();
            LecturerList.addAll(allLecturer);

            // Write lecturer information to files
            for (admin.lecturerTableView.Lecturer lecturer : allLecturer) {
                writeLecturerToFile(lecturer, "defaultPassword");  // Replace "defaultPassword" with the actual password
                writeLecturerToUserFile(lecturer, "defaultPassword");
            }

            // Remove added lecturer from the selected file
            removeLecturerFromFile(allLecturer, selectedFile);
        }
    }

    private void writeLecturerToFile(admin.lecturerTableView.Lecturer lecturer, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/lecturer.txt", true))) {
            writer.write(lecturer.getId() + "," + lecturer.getName() + "," + lecturer.getEmail() + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLecturerToUserFile(admin.lecturerTableView.Lecturer lecturer, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/user.txt", true))) {
            writer.write(lecturer.getId() + "," + lecturer.getName() + "," + lecturer.getEmail() + "," + password + ",lecturer");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLecturerFromFile(List<admin.lecturerTableView.Lecturer> lecturersToRemove, File originalFile) {
        File tempFile = new File("src/main/resources/database/tempLecturerData.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                boolean shouldWrite = true;
                for (admin.lecturerTableView.Lecturer lecturer : lecturersToRemove) {
                    if (line.startsWith(lecturer.getId() + ",")) {
                        shouldWrite = false;
                        break;
                    }
                }
                if (shouldWrite) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        } else {
        }
    }

    private boolean submitButtonClicked() {
        boolean validated = true;

        if (lecturerID.getText() == null || lecturerID.getText().isEmpty()) {
            LIDLabel.setVisible(true);
            validated = false;
        } else {
            LIDLabel.setVisible(false);
        }

        if (lecturerName.getText() == null || lecturerName.getText().isEmpty()) {
            LNameLabel.setVisible(true);
            validated = false;
        } else {
            LNameLabel.setVisible(false);
        }

        if (lecturerEmail.getText() == null || lecturerEmail.getText().isEmpty()) {
            LEmailLabel.setVisible(true);
            validated = false;
        } else {
            LEmailLabel.setVisible(false);
        }

        if (lecturerPassword.getText() == null || lecturerPassword.getText().isEmpty()) {
            LPasswordLabel.setVisible(true);
            validated = false;
        } else {
            LPasswordLabel.setVisible(false);
        }

        return validated;
    }

    private void populateLecturerListFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/lecturer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String id = data[0];
                    String name = data[1];
                    String email = data[2];
                    admin.lecturerTableView.Lecturer lecturer = new admin.lecturerTableView.Lecturer(id, name, email);
                    LecturerList.add(lecturer);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        lecturerID.clear();
        lecturerName.clear();
        lecturerEmail.clear();
        lecturerPassword.clear();
    }
}
