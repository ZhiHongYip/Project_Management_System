package admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;

public class addIntakeController {

    @FXML
    private Button addIntakeBtn;

    @FXML
    private Button deleteIntakeBtn;

    @FXML
    private TableColumn<Intake, String> intakeCodeClm;

    @FXML
    private Label intakeCodeLabel;

    @FXML
    private TextField intakeCodeTextfield;

    @FXML
    private TableView<Intake> intakeTableView;

    private ObservableList<Intake> intakeList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up TableView column
        intakeCodeClm.setCellValueFactory(new PropertyValueFactory<>("intakeCode"));

        // Load existing intake codes from file
        populateIntakeListFromFile();

        // Assign the observable list to the TableView
        intakeTableView.setItems(intakeList);
    }

    @FXML
    private void addIntakeCode(ActionEvent event) throws IOException {
        if (submitButtonClicked()) {
            int exist = checkIntake(intakeCodeTextfield.getText());
            if(exist == 1){
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Intake Code Has Been Taken!");
                alert.showAndWait();
            }else{
                String intakeCode = intakeCodeTextfield.getText();

                // Create a new Intake object
                Intake newIntake = new Intake(intakeCode);

                // Add the new intake to the TableView
                intakeList.add(newIntake);

                // Write intake code to file
                writeIntakeToFile(intakeCode);

                // Clear input field after adding
                intakeCodeTextfield.clear();
            }
        }
    }

    public int checkIntake(String checker)throws IOException{
        int exist = 0;
        String line = null;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/intakeCode.txt"));
        while ((line = reader.readLine()) != null ) {
            String[] info = line.split(",");
            if (checker.equals(info[0])){
                exist = 1;
                break;
            }
        }
        return exist;
    }

    private boolean submitButtonClicked() {
        boolean validated = true;

        if (intakeCodeTextfield.getText() == null || intakeCodeTextfield.getText().isEmpty()) {
            intakeCodeLabel.setVisible(true);
            validated = false;
        } else {
            intakeCodeLabel.setVisible(false);
        }

        return validated;
    }

    private void populateIntakeListFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/intakeCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Create a new Intake object and add it to the list
                intakeList.add(new Intake(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file reading error
        }
    }

    private void writeIntakeToFile(String intakeCode) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/database/intakeCode.txt", true))) {
            writer.write(intakeCode);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file writing error
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String intakeCode = null;
        try {
            // Get the selected intake from the table view
            intakeCode = intakeTableView.getSelectionModel().getSelectedItem().getIntakeCode();
            if (intakeCode != null) {
                // Delete the intake data from the file
                deleteIntakeData("src/main/resources/database/intakeCode.txt", intakeCode);
                // Remove the intake from the table view
                intakeTableView.getItems().remove(intakeTableView.getSelectionModel().getSelectedItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteIntakeData(String fileName, String intakeCode) {
        File inputFile = new File(fileName);
        File tempFile = new File("tempFile.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains intake code to be deleted
                if (!line.equals(intakeCode)) {
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

    // Intake class definition
    public static class Intake {
        private String intakeCode;

        public Intake(String intakeCode) {
            this.intakeCode = intakeCode;
        }

        public String getIntakeCode() {
            return intakeCode;
        }

        public void setIntakeCode(String intakeCode) {
            this.intakeCode = intakeCode;
        }
    }
}
