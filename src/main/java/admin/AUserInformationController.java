package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;

public class AUserInformationController {

    @FXML
    private TableView<AUserInfoTableView.User> userInfoTableView;

    @FXML
    private TableColumn<AUserInfoTableView.User, String> userIDTableColumn;

    @FXML
    private TableColumn<AUserInfoTableView.User, String> userInfoNameTableColumn;

    @FXML
    private TableColumn<AUserInfoTableView.User, String> userInfoEmailTableColumn;

    @FXML
    private TableColumn<AUserInfoTableView.User, String> userInfoRoleTableColumn;

    @FXML
    private ComboBox<String> searchCombo;


    @FXML
    private Button SEditBtn;

    @FXML
    private Button SDeleteBtn;

    // Declare ObservableList to hold user data
    private ObservableList<AUserInfoTableView.User> userList = FXCollections.observableArrayList();

    public static String role;
    public void initialize() {
        searchCombo.getItems().addAll("Student", "lecturer", "ProjectManager");
        searchCombo.setValue("Option");
        populateUserListFromFile();
        // Set up TableView columns
        userIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userInfoNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userInfoEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userInfoRoleTableColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Assign the observable list to the TableView
        userInfoTableView.setItems(userList);

    }

    private void populateUserListFromFile() {
    }

    @FXML
    private void handleSearch() {
        String selectedRole = searchCombo.getValue();
        // Clear the current list
        userList.clear();
        populateUserListFromFile(selectedRole);
    }


    private void populateUserListFromFile(String selectedRole) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/user.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to extract user data
                String[] data = line.split(",");

                        String id = data[0];
                        String name = data[1];
                        String email = data[2];
                        String role = data[4];

                        if (role.equalsIgnoreCase(selectedRole)) {
                            // Create a new User object using the correct constructor name
                            AUserInfoTableView.User user = new AUserInfoTableView.User(id, name, email, role);

                            // Add the user to the list
                            userList.add(user);
                        }

                }
            } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleDelete() {
        String userID = null;
        try {
            // Get the selected user from the table view
            AUserInformationController.role = userInfoTableView.getSelectionModel().getSelectedItem().getRole();
            userID = userInfoTableView.getSelectionModel().getSelectedItem().getId();
            if (AUserInformationController.role != null) {
                // Delete the user data from the appropriate file based on their role
                String fileName = null;
                String fileName2 = null;
                switch (AUserInformationController.role) {
                    case "student":
                        fileName = "src/main/resources/database/student.txt";
                        fileName2 = "src/main/resources/database/user.txt";
                        break;
                    case "lecturer":
                        fileName = "src/main/resources/database/lecturer.txt";
                        fileName2 = "src/main/resources/database/user.txt";
                        break;
                    case "projectmanager":
                        fileName = "src/main/resources/database/user.txt";
                        break;
                }

                // Delete the user data from the file
                deleteUserData(fileName, userID);
                deleteUserData(fileName2,userID);
                // Remove the user from the table view
                userInfoTableView.getItems().remove(userInfoTableView.getSelectionModel().getSelectedItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUserData(String fileName, String userID) {
        File inputFile = new File(fileName);
        File tempFile = new File("tempFile.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                // Check if the line contains user data to be deleted
                if (!user[0].equals(userID)) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the original file
        if (!inputFile.delete()) {
            return;
        }

        // Rename the new file to the filename the original file had
        if (!tempFile.renameTo(inputFile)) {
        }
    }

    @FXML
    private void handleModifyButtonAction() {
        AUserInfoTableView.User selectedUser = userInfoTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            openEditPopup(selectedUser);
        }
    }

    private void openEditPopup(AUserInfoTableView.User selectedUser) {
        String role = selectedUser.getRole();
        String fxmlFile = "";

        // Determine which popup form to open based on the user's role
        switch (role) {
            case "student":
                fxmlFile = "editStudentPopUp.fxml";
                break;
            case "lecturer":
                fxmlFile = "editLecturerPopUp.fxml";
                break;
            case "projectmanager":
                fxmlFile = "editLecturerPopUp.fxml";
                break;
            default:
                break;
        }

        if (!fxmlFile.isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();

                // Pass the selected user to the controller of the popup form
                editStudentPopUp editStudentPopupController = null;
                if ("student".equals(role)) {
                    editStudentPopupController = loader.getController();
                    editStudentPopupController.setSelectedUser(selectedUser);
                    editStudentPopupController.setAUserInfoTableView(userInfoTableView);
                } else if ("lecturer".equals(role) || "projectmanager".equals(role)) {
                    editLecturerPopUp editLecturerPopupController = loader.getController();
                    editLecturerPopupController.setSelectedUser(selectedUser);
                    if (selectedUser != null){
                        System.out.println(selectedUser);}
                    editLecturerPopupController.setAUserInfoTableView(userInfoTableView);
                }

                Stage popupStage = new Stage();
                Scene scene = new Scene(root);
                popupStage.setScene(scene);
                popupStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
