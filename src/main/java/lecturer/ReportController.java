package lecturer;

import com.example.project_management_system.PMS_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ReportController extends CellTable implements Initializable  {
    @FXML
    private CheckBox Graded;

    @FXML
    private CheckBox Ungrade;

    @FXML
    private TextField assessmentIDTBox;

    @FXML
    private TableColumn<?, ?> columnAssessmentID;

    @FXML
    private TableColumn<?, ?> columnGrade;

    @FXML
    private TableColumn<?, ?> columnIntake;

    @FXML
    private TableColumn<?, ?> columnReportID;

    @FXML
    private TableColumn<?, ?> columnStatus;

    @FXML
    private TableColumn<?, ?> columnStudentID;

    @FXML
    private TableColumn<?, ?> columnStudentNo;

    @FXML
    private TableColumn<?, ?> columnSubmissionDate;

    @FXML
    private TextField intakeTBox;

    @FXML
    private Label label_username;

    @FXML
    private TableView<Report> reportList;
    @FXML
    ObservableList<Report> reportData;
    String studentID;
    String intake;
    String assessmentID;

    public int i = 1;
    @FXML
    private TableColumn<?, ?> columnReportNo;

    @FXML
    private RadioButton selectAll;

    @FXML
    private TextField studentIDTBox;

    public static SimpleStringProperty dummyReportID;
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

    public void select_All(ActionEvent event){
        if (this.selectAll.isSelected()){
            this.Graded.setSelected(true);
            this.Ungrade.setSelected(true);
        }else {
            this.Graded.setSelected(false);
            this.Ungrade.setSelected(false);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.showReportInfo();
    }
    @Override
    public void setCellTable(){
        this.columnReportNo.setCellValueFactory(new PropertyValueFactory<>("reportNo"));
        this.columnReportID.setCellValueFactory(new PropertyValueFactory<>("reportID"));
        this.columnStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        this.columnIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        this.columnAssessmentID.setCellValueFactory(new PropertyValueFactory<>("assessmentID"));
        this.columnSubmissionDate.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));
        this.columnGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void addGraded(String[]report)throws IOException{
        if (report[7].equals("Graded")){
            this.reportData.add(new Report(this.i,report[0],report[2],report[3],report[4],report[6],report[7],report[8]));
            this.i++;
        }
    }

    private void addUngrade(String[]report)throws IOException{
        if (report[7].equals("Ungraded")){
            this.reportData.add(new Report(this.i,report[0],report[2],report[3],report[4],report[6],report[7],report[8]));
            this.i++;
        }
    }
    private void loadData()throws IOException{
        String line = null;
        String line1 = null;
        String line2 = null;
        String studentID = null;
        String studentName = null;
        String assessmentID = null;
        String intake = null;
        this.studentID = this.studentIDTBox.getText();
        this.assessmentID = this.assessmentIDTBox.getText();
        this.intake = this.intakeTBox.getText();
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
        BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/assessment.txt"));
        if (!this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && this.assessmentIDTBox.getText().trim().isEmpty()){
            System.out.println(this.studentIDTBox.getText().trim());
            System.out.println("search");
            System.out.println(this.studentID);
            while ((line = reader.readLine()) != null){
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[2].contains(this.studentID)){
                    System.out.println(info[2]);
                    studentID = info[2];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] report = line1.split(",");
                        if (report[2].equals(studentID) && (report[10].equals(PMS_Controller.lecturerID))) {
                            studentName = info[1];
                            if(this.Graded.isSelected()){
                                this.addGraded(report);
                            }
                            if (this.Ungrade.isSelected()){
                                this.addUngrade(report);
                            }
                        }
                    }

                }
            }
        } else if (this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && this.assessmentIDTBox.getText().trim().isEmpty()) {
            System.out.println("sort by intake");
            while ((line = reader.readLine()) != null){
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[3].contains(this.intake)){
                    System.out.println(info[3]);
                    intake = info[3];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] report = line1.split(",");
                        if (report[3].equals(intake) && (report[10].equals(PMS_Controller.lecturerID))) {
                            studentName = info[1];
                            if(this.Graded.isSelected()){
                                this.addGraded(report);
                            }
                            if (this.Ungrade.isSelected()){
                                this.addUngrade(report);
                            }
                        }
                    }

                }
            }
        } else if (this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && !this.assessmentIDTBox.getText().trim().isEmpty()) {
            System.out.println("sort by assessmentID");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null) {
                String[] info = line2.split(",");
                if (info[0].contains(this.assessmentID)) {
                    System.out.println("AssessmentID: " + info[0]);
                    assessmentID = info[0];
                    if (!printedAssessmentTypes.contains(assessmentID)) {
                        System.out.println("AssessmentID: " + assessmentID);
                        printedAssessmentTypes.add(assessmentID);
                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] report = line1.split(",");
                            if (report[4].equals(assessmentID) && (report[10].equals(PMS_Controller.lecturerID))) {
                                System.out.println(Arrays.toString(report));
                                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                                while ((line = reader4.readLine()) != null) {
                                    String[] info1 = line.split(",");
                                    if (report[2].equals(info1[0])) {
                                        studentName = info1[1];
                                        if(this.Graded.isSelected()){
                                            this.addGraded(report);
                                        }
                                        if (this.Ungrade.isSelected()){
                                            this.addUngrade(report);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }
        } else if (!this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && this.assessmentIDTBox.getText().trim().isEmpty()) {
            System.out.println("Sort by studentID and intake");
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                System.out.println(Arrays.toString(info));
                if (info[2].contains(this.studentID) && info[3].contains(this.intake)) {
                    System.out.println(info[2]);
                    studentID = info[2];
                    intake = info[3];
                    BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                    while ((line1 = reader1.readLine()) != null) {
                        String[] report = line1.split(",");
                        if (report[2].equals(studentID) && report[3].equals(intake) && (report[10].equals(PMS_Controller.lecturerID))) {
                            studentName = info[1];
                            if(this.Graded.isSelected()){
                                this.addGraded(report);
                            }
                            if (this.Ungrade.isSelected()){
                                this.addUngrade(report);
                            }
                        }
                    }
                }
            }
        } else if (!this.studentIDTBox.getText().trim().isEmpty() && this.intakeTBox.getText().trim().isEmpty() && !this.assessmentIDTBox.getText().trim().isEmpty()) {
            System.out.println("sort by studentID and assessmentID");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                System.out.println(Arrays.toString(info));
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    System.out.println(Arrays.toString(info2));
                    if (info[0].contains(this.assessmentID) && info2[2].contains(this.studentID) && info2[10].equals(PMS_Controller.lecturerID)) {
                        System.out.println("Stage 2");
                        assessmentID = info[0];
                        System.out.println("assessmentID" + assessmentID);
                        studentID = info2[2];
                        System.out.println("studentID" + studentID);
                        if (!printedAssessmentTypes.contains(assessmentID)) {
                            System.out.println("AssessmentID: " + assessmentID);
                            printedAssessmentTypes.add(assessmentID);
                            BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                            while ((line1 = reader1.readLine()) != null) {
                                String[] report = line1.split(",");
                                System.out.println(Arrays.toString(report));
                                if (report[4].equals(assessmentID) && report[2].equals(studentID) && (report[10].equals(PMS_Controller.lecturerID))) {
                                    studentName = info2[1];
                                    if(this.Graded.isSelected()){
                                        this.addGraded(report);
                                    }
                                    if (this.Ungrade.isSelected()){
                                        this.addUngrade(report);
                                    }
                                }
                            }

                        }

                    }
                }
            }

        }else if (this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && !this.assessmentIDTBox.getText().trim().isEmpty()){
            System.out.println("sort by intake and assessmentID");
            Set<String> printedAssessmentTypes = new HashSet<>();
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                System.out.println(Arrays.toString(info));
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    if (info[0].contains(this.assessmentID) && info2[3].contains(this.intake)) {
                        System.out.println("Stage 2");
                        assessmentID = info[0];
                        System.out.println("assessmentID: " + assessmentID);
                        intake = info2[3];
                        System.out.println("intake: " + intake);

                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] report = line1.split(",");
                            if (report[4].equals(assessmentID) && report[3].equals(intake) && (report[10].equals(PMS_Controller.lecturerID))) {
                                studentName = info2[1];
                                if (!printedAssessmentTypes.contains(assessmentID)) {
                                    System.out.println("AssessmentID: " + assessmentID);
                                    printedAssessmentTypes.add(assessmentID);
                                    if (this.Graded.isSelected()) {
                                        this.addGraded(report);
                                    }
                                    if (this.Ungrade.isSelected()) {
                                        this.addUngrade(report);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }else if (!this.studentIDTBox.getText().trim().isEmpty() && !this.intakeTBox.getText().trim().isEmpty() && !this.assessmentIDTBox.getText().trim().isEmpty()){
            System.out.println("sort by all");
            while ((line2 = reader2.readLine()) != null){
                System.out.println("stage 1");
                String[] info = line2.split(",");
                BufferedReader reader4 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                while ((line = reader4.readLine())!= null){
                    String[] info2 = line.split(",");
                    if (info[0].contains(this.assessmentID) && info2[3].contains(this.intake) && info2[2].contains(this.studentID) && info2[10].equals(PMS_Controller.lecturerID)) {
                        System.out.println("Stage 2");
                        assessmentID = info[0];
                        System.out.println("assessmentID" + assessmentID);
                        intake = info2[3];
                        System.out.println("intake" + intake);
                        studentID = info2[2];
                        System.out.println("studentID: "+ studentID);
                        BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                        while ((line1 = reader1.readLine()) != null) {
                            String[] report = line1.split(",");
                            if (report[4].equals(assessmentID) && report[3].equals(intake) && report[2].equals(studentID) && (report[10].equals(PMS_Controller.lecturerID))) {
                                studentName = info2[2];
                                if(this.Graded.isSelected()){
                                    this.addGraded(report);
                                }
                                if (this.Ungrade.isSelected()){
                                    this.addUngrade(report);
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
                studentID = info[2];
                System.out.println(studentID);
                BufferedReader reader1 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
                while ((line1 = reader1.readLine()) != null) {
                    String[] report = line1.split(",");
                    System.out.println(Arrays.toString(report));
                    System.out.println(PMS_Controller.lecturerID);
                    if (report[2].equals(studentID) && (report[10].equals(PMS_Controller.lecturerID))) {
                        System.out.println("Linking");
                        studentName = info[1];
                        if(this.Graded.isSelected()){
                            this.addGraded(report);
                        }
                        if (this.Ungrade.isSelected()){
                            this.addUngrade(report);
                        }
                    }
                }
            }

        }
        System.out.println(reportData);
        this.reportList.setItems(this.reportData);
    }
    public void search(ActionEvent event) throws IOException {
        this.i = 1;
        this.reportList.getItems().clear();
        this.setCellTable();
        this.reportData = FXCollections.observableArrayList();
        this.loadData();
    }

    private void showReportInfo(){
        this.reportList.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2){
                ReportController.dummyReportID =ReportController.this.reportList.getSelectionModel().getSelectedItem().reportID;
                if (ReportController.this.reportList.getSelectionModel().getSelectedItem().status.getValue().equals("Submitted")){
                    System.out.println(ReportController.dummyReportID.getValue());
                    FXMLLoader Loader = new FXMLLoader();
                    Loader.setLocation(this.getClass().getResource("/lecturer/ReportInfo.fxml"));
                    try {
                        Loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Parent p = (Parent)Loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(p));
                    stage.setTitle("Report Info");
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Report is not submitted");
                    alert.showAndWait();
                }

            }
        });
    }
}
