package lecturer;

import com.example.project_management_system.PMS_Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SuperviseeListController extends CellTable implements Initializable {

    @FXML
    private CheckBox Approve;

    @FXML
    private CheckBox Pending;

    @FXML
    private CheckBox Reject;

    @FXML
    private TextField assessmentTypeTBox;

    @FXML
    private TableColumn<?, ?> columnAssessmentType;

    @FXML
    private TableColumn<?, ?> columnDate;
    @FXML
    private TableColumn<?, ?> columnStudentName;

    @FXML
    private TableColumn<?, ?> columnIntake;

    @FXML
    private TableColumn<?, ?> columnSlot;

    @FXML
    private TableColumn<?, ?> columnStatus;

    @FXML
    private TableColumn<?, ?> columnStudentID;
    @FXML
    private TableColumn<?, ?> columnStudentNo;

    @FXML
    private TextField intakeTBox;

    @FXML
    private Label label_userstudentID;

    @FXML
    private RadioButton selectAll;

    @FXML
    private TextField studentIDTBox;

    @FXML
    private TableView<SuperviseeList> superviseeList;

    @FXML
    ObservableList<SuperviseeList> superviseeData;

    String studentID;
    String intake;
    String assessmentType;

    public int i = 1;

    public SuperviseeListController(){
    }

    public void select_All(ActionEvent event){
        if (this.selectAll.isSelected()){
            this.Pending.setSelected(true);
            this.Approve.setSelected(true);
            this.Reject.setSelected(true);
        }else {
            this.Pending.setSelected(false);
            this.Approve.setSelected(false);
            this.Reject.setSelected(false);
        }
    }
    public void goToDashBoard(ActionEvent event) throws IOException {
        Parent dashboard = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/DashBoard.fxml")));
        Scene scene = new Scene(dashboard);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goToCalendar(ActionEvent event) throws IOException{
        Parent calendar = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/Calendar.fxml")));
        Scene scene = new Scene(calendar);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goToSuperviseeList(ActionEvent event)throws IOException{
        Parent superviseelist = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/SuperviseeList.fxml")));
        Scene scene = new Scene(superviseelist);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goToSecondMarker(ActionEvent event)throws IOException{
        Parent secondmarker = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/SecondMarker.fxml")));
        Scene scene = new Scene(secondmarker);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void goToReport(ActionEvent event) throws IOException {
        Parent report = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/Report.fxml")));
        Scene scene = new Scene(report);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void signOut(ActionEvent event)throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out Confirmation");
        alert.setHeaderText((String)null);
        alert.setContentText("Confirm Sign Out?");
        alert.showAndWait().ifPresent((type) ->{
                    if (type == ButtonType.CANCEL){
                        event.consume();
                    } else if (type == ButtonType.OK) {
                        try {
                            Parent signOut = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/com/example/project_management_system/PMS_LoginPage.fxml")));
                            Scene scene = new Scene(signOut);
                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
        );
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @Override
    public void setCellTable(){
        this.columnStudentNo.setCellValueFactory(new PropertyValueFactory<>("studentNo"));
        this.columnStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        this.columnStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        this.columnAssessmentType.setCellValueFactory(new PropertyValueFactory<>("assessmentType"));
        this.columnIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnSlot.setCellValueFactory(new PropertyValueFactory<>("slot"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void search(ActionEvent event) throws IOException {
        this.i = 1;
        this.superviseeList.getItems().clear();
        this.setCellTable();
        this.superviseeData = FXCollections.observableArrayList();
        this.loadData();
    }

    private void loadData()throws IOException{
        String line = null;
        String line1 = null;
        String line2 = null;
        String studentID = null;
        String studentName = null;
        String assessmentType = null;
        String intake = null;
        this.studentID = this.studentIDTBox.getText();
        this.assessmentType = this.assessmentTypeTBox.getText();
        this.intake = this.intakeTBox.getText();
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
        BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"));
        if (!this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && this.assessmentTypeTBox.getText().trim().isEmpty()){
            System.out.println(this.studentIDTBox.getText().trim());
            System.out.println("search");
            System.out.println(this.studentID);
            while ((line = reader.readLine()) != null){
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[0].contains(this.studentID)){
                    System.out.println(info[2]);
                    studentID = info[0];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] student = line1.split(",");
                        if (student[1].equals(studentID) && student[7].equals(PMS_Controller.lecturerID)) {
                            studentName = info[1];
                            if(this.Pending.isSelected()){
                                this.addPending(student,studentName);
                            }
                            if (this.Approve.isSelected()){
                                this.addApprove(student,studentName);
                            }
                            if (this.Reject.isSelected()){
                                this.addReject(student,studentName);
                            }
                        }
                    }

                }
            }
        } else if (this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && this.assessmentTypeTBox.getText().trim().isEmpty()) {
            System.out.println("sort by intake");
            while ((line = reader.readLine()) != null){
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[3].contains(this.intake)){
                    System.out.println(info[3]);
                    intake = info[3];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] student = line1.split(",");
                        if (student[3].equals(intake) && student[7].equals(PMS_Controller.lecturerID)) {
                            studentName = info[1];
                            if(this.Pending.isSelected()){
                                this.addPending(student,studentName);
                            }
                            if (this.Approve.isSelected()){
                                this.addApprove(student,studentName);
                            }
                            if (this.Reject.isSelected()){
                                this.addReject(student,studentName);
                            }
                        }
                    }

                }
            }
        } else if (this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && !this.assessmentTypeTBox.getText().trim().isEmpty()) {
            System.out.println("sort by assessmentType");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null) {
                String[] info = line2.split(",");
                if (info[3].contains(this.assessmentType)) {
                    System.out.println("AssessmentType: " + info[3]);
                    assessmentType = info[3];
                    if (!printedAssessmentTypes.contains(assessmentType)) {
                        System.out.println("AssessmentType: " + assessmentType);
                        printedAssessmentTypes.add(assessmentType);
                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] student = line1.split(",");
                            if (student[2].equals(assessmentType) && student[7].equals(PMS_Controller.lecturerID)) {
                                System.out.println(Arrays.toString(student));
                                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                                while ((line = reader4.readLine()) != null) {
                                    String[] info1 = line.split(",");
                                    if (student[1].equals(info1[0])) {
                                        studentName = info1[1];
                                        if(this.Pending.isSelected()){
                                            this.addPending(student,studentName);
                                        }
                                        if (this.Approve.isSelected()){
                                            this.addApprove(student,studentName);
                                        }
                                        if (this.Reject.isSelected()){
                                            this.addReject(student,studentName);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }
        } else if (!this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && this.assessmentTypeTBox.getText().trim().isEmpty()) {
            System.out.println("Sort by studentID and intake");
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[0].contains(this.studentID) && info[3].contains(this.intake)) {
                    System.out.println(info[2]);
                    studentID = info[0];
                    intake = info[3];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] student = line1.split(",");
                        if (student[1].equals(studentID) && student[3].equals(intake) && student[7].equals(PMS_Controller.lecturerID)) {
                            studentName = info[1];
                            if(this.Pending.isSelected()){
                                this.addPending(student,studentName);
                            }
                            if (this.Approve.isSelected()){
                                this.addApprove(student,studentName);
                            }
                            if (this.Reject.isSelected()){
                                this.addReject(student,studentName);
                            }
                        }
                    }
                }
            }
        } else if (!this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && !this.assessmentTypeTBox.getText().trim().isEmpty()) {
            System.out.println("sort by studentID and assessmentType");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                System.out.println(Arrays.toString(info));
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    System.out.println(Arrays.toString(info2));
                    if (info[3].contains(this.assessmentType) && info2[0].contains(this.studentID)) {
                        System.out.println("Stage 2");
                        assessmentType = info[3];
                        System.out.println("assessmentType" + assessmentType);
                        studentID = info2[0];
                        System.out.println("studentID" + studentID);
                        if (!printedAssessmentTypes.contains(assessmentType)) {
                            System.out.println("AssessmentType: " + assessmentType);
                            printedAssessmentTypes.add(assessmentType);
                            BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                            while ((line1 = reader1.readLine()) != null) {
                                String[] student = line1.split(",");
                                System.out.println(Arrays.toString(student));
                                if (student[2].equals(assessmentType) && student[1].equals(studentID) && student[7].equals(PMS_Controller.lecturerID)) {
                                    studentName = info2[1];
                                    if(this.Pending.isSelected()){
                                        this.addPending(student,studentName);
                                    }
                                    if (this.Approve.isSelected()){
                                        this.addApprove(student,studentName);
                                    }
                                    if (this.Reject.isSelected()){
                                        this.addReject(student,studentName);
                                    }
                                }
                            }

                        }

                    }
                }
            }

        }else if (this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && !this.assessmentTypeTBox.getText().trim().isEmpty()){
            System.out.println("sort by intake and assessmentType");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                System.out.println(Arrays.toString(info));
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    if (info[3].contains(this.assessmentType) && info2[3].contains(this.intake)) {
                        System.out.println("Stage 2");
                        assessmentType = info[3];
                        System.out.println("assessmentType: " + assessmentType);
                        intake = info2[3];
                        System.out.println("intake: " + intake);

                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] student = line1.split(",");
                            if (student[2].equals(assessmentType) && student[3].equals(intake) && student[7].equals(PMS_Controller.lecturerID)) {
                                studentName = info2[1];
                                if (!printedAssessmentTypes.contains(assessmentType)) {
                                    System.out.println("AssessmentType: " + assessmentType);
                                    printedAssessmentTypes.add(assessmentType);
                                    if (this.Pending.isSelected()) {
                                        this.addPending(student, studentName);
                                    }
                                    if (this.Approve.isSelected()) {
                                        this.addApprove(student, studentName);
                                    }
                                    if (this.Reject.isSelected()) {
                                        this.addReject(student, studentName);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }else if (!this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && !this.assessmentTypeTBox.getText().trim().isEmpty()){
            System.out.println("sort by all");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    if (info[3].contains(this.assessmentType) && info2[3].contains(this.intake) && info2[0].contains(this.studentID) ) {
                        System.out.println("Stage 2");
                        assessmentType = info[3];
                        System.out.println("assessmentType" + assessmentType);
                        intake = info2[3];
                        System.out.println("intake" + intake);
                        studentID = info2[0];
                        System.out.println("studentID: "+ studentID);
                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] student = line1.split(",");
                            if (student[2].equals(assessmentType) && student[3].equals(intake) && student[1].equals(studentID) && student[7].equals(PMS_Controller.lecturerID)) {
                                System.out.println("Stage 3");
                                studentName = info2[1];
                                if (!printedAssessmentTypes.contains(assessmentType)) {
                                    System.out.println("AssessmentType: " + assessmentType);
                                    printedAssessmentTypes.add(assessmentType);
                                    if (this.Pending.isSelected()) {
                                        this.addPending(student, studentName);
                                    }
                                    if (this.Approve.isSelected()) {
                                        this.addApprove(student, studentName);
                                    }
                                    if (this.Reject.isSelected()) {
                                        this.addReject(student, studentName);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else {
            System.out.println("No Filter");
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                studentID = info[0];
                System.out.println(studentID);
                BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
                while ((line1 = reader1.readLine()) != null) {
                    String[] student = line1.split(",");
                    System.out.println(Arrays.toString(student));
                    if (student[1].equals(studentID) && student[7].equals(PMS_Controller.lecturerID)) {
                        System.out.println("Linking");
                        studentName = info[1];
                        System.out.println(studentName);
                        if(this.Pending.isSelected()){
                            this.addPending(student,studentName);
                        }
                        if (this.Approve.isSelected()){
                            this.addApprove(student,studentName);
                        }
                        if (this.Reject.isSelected()){
                            this.addReject(student,studentName);
                        }
                    }
                }
            }

        }
        System.out.println(superviseeData);
        this.superviseeList.setItems(this.superviseeData);
    }

    private void addPending(String[]student, String studentName)throws IOException{
        if (student[11].equals("Pending")){
            this.superviseeData.add(new SuperviseeList(this.i,student[1],studentName,student[2],student[3],student[5],student[6],student[11]));
            this.i++;
        }
    }

    private void addApprove(String[]student, String studentName)throws IOException{
        if (student[11].equals("Approve")){
            this.superviseeData.add(new SuperviseeList(this.i,student[1],studentName,student[2],student[3],student[5],student[6],student[11]));
            this.i++;
        }
    }

    private void addReject(String[]student, String studentName)throws IOException{
        if (student[11].equals("Reject")){
            this.superviseeData.add(new SuperviseeList(this.i,student[1],studentName,student[2],student[3],student[5],student[6],student[11]));
            this.i++;
        }
    }

}
