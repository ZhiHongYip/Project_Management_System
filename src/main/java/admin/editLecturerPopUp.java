package admin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class editLecturerPopUp {
    @FXML
    private TextField LEditIDTextField;
    @FXML
    private TextField LEditNameTextField;
    @FXML
    private TextField LEditEmailTextField;
    @FXML
    private TextField LEditPasswordTextField;
    @FXML
    private ComboBox<String> editLecturerRole;
    @FXML
    private Label LEditIDLabel, LEditNameLabel, LEditEmailLabel, LEditPasswordLabel, LEditRoleLabel;
    @FXML
    private Button LEditButton;
    private Stage stage;
    private AUserInfoTableView.User selectedUser;
    private TableView<AUserInfoTableView.User> AUserInfoTableView;

    public void initialize() {
        // Initialize the form with the selected lecturer's information
        editLecturerRole.getItems().addAll("lecturer", "projectmanager");

        if (selectedUser != null) {
            loadUserDataFromFile(selectedUser.getId());
        }
    }

    private boolean validateInputFields() {
        boolean validated = true;

        if (LEditIDTextField.getText() == null || LEditIDTextField.getText().isEmpty()) {
            LEditIDLabel.setVisible(true);
            validated = false;
        } else {
            LEditIDLabel.setVisible(false);
        }

        if (LEditNameTextField.getText() == null || LEditNameTextField.getText().isEmpty()) {
            LEditNameLabel.setVisible(true);
            validated = false;
        } else {
            LEditNameLabel.setVisible(false);
        }

        if (LEditEmailTextField.getText() == null || LEditEmailTextField.getText().isEmpty()) {
            LEditEmailLabel.setVisible(true);
            validated = false;
        } else {
            LEditEmailLabel.setVisible(false);
        }

        if (LEditPasswordTextField.getText() == null || LEditPasswordTextField.getText().isEmpty()) {
            LEditPasswordLabel.setVisible(true);
            validated = false;
        } else {
            LEditPasswordLabel.setVisible(false);
        }

        return validated;
    }

    private void editLecturerData(String lecturerFilePath, String userFilePath, String id, String name, String email, String password, String role) throws IOException {
        File inputFile = new File(lecturerFilePath);
        File tempFile = new File("src/main/resources/database/tempLecturer.txt");

        boolean isUpdated = false;

        // Read from the original file and write to the temporary file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                // Check if the line contains user data to be deleted
                if (!user[0].equals(selectedUser.getId())) {
                    writer.write(line + System.lineSeparator());
                } else {
                    isUpdated = true;
                }
            }

            // If the role changed from 'Project Manager' to 'Lecturer', add the updated line
            if (selectedUser.getRole().equals("projectmanager") && role.equals("lecturer")) {
                writer.write(id + "," + name + "," + email + "," + password + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the original file and rename the temporary file
        if (!inputFile.delete()) {
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
        }

        if (!isUpdated) {
        }
    }

    public void editUserData(String userFilePath, String id, String name, String email, String password, String role) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.startsWith(selectedUser.getId() + ",")) {
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

    public int checkLecture(String checker)throws IOException{
        int exist = 0;
        String line = null;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/lecturer.txt"));
        while ((line = reader.readLine()) != null ) {
            String[] info = line.split(",");
            if (checker.equals(info[0])){
                exist = 1;
                break;
            }
        }
        return exist;
    }

    @FXML
    private void callEditLecturerData() throws IOException {
        if (!validateInputFields()) {
            return; // Exit if any field is invalid
        }else {
            int exist = checkLecture(LEditIDTextField.getText());
            if (exist == 1) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Lecture ID Has Been Taken!");
                alert.showAndWait();
            } else {
                String lecturerFilePath = "src/main/resources/database/lecturer.txt";
                String userFilePath = "src/main/resources/database/user.txt";
                String id = LEditIDTextField.getText();
                String name = LEditNameTextField.getText();
                String email = LEditEmailTextField.getText();
                String password = LEditPasswordTextField.getText();
                String role = editLecturerRole.getValue();

                try {
                    // Update the user data in user.txt
                    editUserData(userFilePath, id, name, email, password, role);
                    // Then, remove or add the lecturer's data from/to lecturer.txt
                    editLecturerData(lecturerFilePath, userFilePath, id, name, email, password, role);

                    // Update the table view or perform any other necessary operations
                    if (AUserInfoTableView != null) {
                        ObservableList<AUserInfoTableView.User> observableList = AUserInfoTableView.getItems();
                        for (AUserInfoTableView.User userInfo : observableList) {
                            if (userInfo.getId().equals(selectedUser.getId())) {
                                userInfo.setName(name);
                                userInfo.setEmail(email);
                                userInfo.setRole(role);
                            }
                        }
                        AUserInfoTableView.setItems(observableList);
                        AUserInfoTableView.refresh();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Close the lecturer modify form
                Stage stage = (Stage) LEditButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    public void setSelectedUser(AUserInfoTableView.User selectedUser) {
        this.selectedUser = selectedUser;
        loadUserDataFromFile(selectedUser.getId());
    }

    private void loadUserDataFromFile(String userID) {
        String studentFilePath = "src/main/resources/database/user.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(userID)) {
                    LEditIDTextField.setText(data[0]);
                    LEditNameTextField.setText(data[1]);
                    LEditEmailTextField.setText(data[2]);
                    LEditPasswordTextField.setText(data[3]);
                    editLecturerRole.setValue(data[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setAUserInfoTableView(TableView<AUserInfoTableView.User> AUserInfoTableView) {
        this.AUserInfoTableView = AUserInfoTableView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
