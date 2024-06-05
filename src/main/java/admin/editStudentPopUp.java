package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class editStudentPopUp {
    @FXML
    private TextField SEditIDTextField;
    @FXML
    private TextField SEditNameTextField;
    @FXML
    private TextField SEditEmailTextField;
    @FXML
    private TextField SEditPasswordTextField;
    @FXML
    private ComboBox<String>SEditIntakeCombo;
    @FXML
    private Label SEditIDLabel, SEditNameLabel, SEditEmailLabel, SEditPasswordLabel;
    @FXML
    private Button SEditButton;
    private Stage stage;
    private AUserInfoTableView.User selectedUser;
    private TableView<AUserInfoTableView.User> AUserInfoTableView;

    private ObservableList<String> intakeList = FXCollections.observableArrayList(); // Declare intakeList

    public void initialize() {
        populateIntakeListFromFile();
        SEditIntakeCombo.setItems(intakeList);
        // Load student data from the file
        if (selectedUser != null) {
            loadStudentDataFromFile(selectedUser.getId());

        }
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

    private void loadStudentDataFromFile(String studentID) {
        String studentFilePath = "src/main/resources/database/student.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(studentID)) {
                    SEditIDTextField.setText(data[0]);
                    SEditNameTextField.setText(data[1]);
                    SEditEmailTextField.setText(data[2]);
                    SEditIntakeCombo.setValue(data[3]);
                    SEditPasswordTextField.setText(data[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editStudentData(String studentFilePath, String id, String name, String email, String intake, String password) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.startsWith(selectedUser.getId() + ",")) {
                    // Modify line
                    StringBuilder sb = new StringBuilder();
                    sb.append(id).append(",").append(name).append(",").append(email).append(",").append(intake).append(",").append(password);
                    data.add(sb.toString());
                } else {
                    data.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath))) {
            for (String str : data) {
                bw.write(str);
                bw.newLine();
            }
        }
    }

    public void editUserData(String userFilePath, String id, String name, String email, String password) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.startsWith(selectedUser.getId() + ",")) {
                    // Modify line but keep the role unchanged
                    String[] parts = line.split(",");
                    String role = parts[4];
                    StringBuilder sb = new StringBuilder();
                    sb.append(id).append(",").append(name).append(",").append(email).append(",").append(password).append(",").append(role);
                    data.add(sb.toString());
                } else {
                    data.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFilePath))) {
            for (String str : data) {
                bw.write(str);
                bw.newLine();
            }
        }
    }

    private boolean validateInputFields() {
        boolean validated = true;

        if (SEditIDTextField.getText() == null || SEditIDTextField.getText().isEmpty()) {
            SEditIDLabel.setVisible(true);
            validated = false;
        } else {
            SEditIDLabel.setVisible(false);
        }

        if (SEditNameTextField.getText() == null || SEditNameTextField.getText().isEmpty()) {
            SEditNameLabel.setVisible(true);
            validated = false;
        } else {
            SEditNameLabel.setVisible(false);
        }

        if (SEditEmailTextField.getText() == null || SEditEmailTextField.getText().isEmpty()) {
            SEditEmailLabel.setVisible(true);
            validated = false;
        } else {
            SEditEmailLabel.setVisible(false);
        }

        if (SEditPasswordTextField.getText() == null || SEditPasswordTextField.getText().isEmpty()) {
            SEditPasswordLabel.setVisible(true);
            validated = false;
        } else {
            SEditPasswordLabel.setVisible(false);
        }

        return validated;
    }

    @FXML
    private void callEditStudentData() {
        if (!validateInputFields()) {
            return; // Exit if any field is invalid
        }

        String studentFilePath = "src/main/resources/database/student.txt";
        String userFilePath = "src/main/resources/database/user.txt";
        String id = SEditIDTextField.getText();
        String name = SEditNameTextField.getText();
        String email = SEditEmailTextField.getText();
        String intake = SEditIntakeCombo.getValue();
        String password = SEditPasswordTextField.getText();

        try {
            editStudentData(studentFilePath, id, name, email, intake, password);
            editUserData(userFilePath, id, name, email, password);

            // Update the table view or perform any other necessary operations
            if (AUserInfoTableView != null) {
                ObservableList<AUserInfoTableView.User> observableList = AUserInfoTableView.getItems();
                for (AUserInfoTableView.User userInfo : observableList) {
                    if (userInfo.getId().equals(selectedUser.getId())) {
                        userInfo.setName(name);
                        userInfo.setEmail(email);
                    }
                }
                AUserInfoTableView.setItems(observableList);
                AUserInfoTableView.refresh();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the customer modify form
        Stage stage = (Stage) SEditButton.getScene().getWindow();
        stage.close();
    }

    public void setSelectedUser(AUserInfoTableView.User selectedUser) {
        this.selectedUser = selectedUser;
        loadStudentDataFromFile(selectedUser.getId());
    }

    public void setAUserInfoTableView(TableView<AUserInfoTableView.User> AUserInfoTableView) {
        this.AUserInfoTableView = AUserInfoTableView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
