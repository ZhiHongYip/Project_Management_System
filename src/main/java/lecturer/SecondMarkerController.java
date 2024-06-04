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

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SecondMarkerController extends CellTable implements Initializable {
    @FXML
    private TableColumn<?, ?> columnDate;

    @FXML
    private TableColumn<?, ?> columnName;

    @FXML
    private TableColumn<?, ?> columnSecondMarkerApproval;

    @FXML
    private TableColumn<?, ?> columnSlot;

    @FXML
    private TableColumn<?, ?> columnStatus;

    @FXML
    private TableColumn<?, ?> columnStudentID;

    @FXML
    private TableColumn<?, ?> columnStudentNo;

    @FXML
    private TableColumn<?, ?> columnSupervisorApproval;

    @FXML
    private Label label_username;

    @FXML
    private TableView<Dashboard> secondMarkerList;
    @FXML
    ObservableList<Dashboard> presentationRequestData;
    public int i = 1;
    public SimpleStringProperty temp = new SimpleStringProperty();

    public String getTemp() {
        return temp.get();
    }

    public SimpleStringProperty tempProperty() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp.set(temp);
    }

    ContextMenu contextMenu1 = new ContextMenu();
    MenuItem approve = new MenuItem("Approve");
    MenuItem reject = new MenuItem("Reject");
    String check;
    Dashboard selectedSupervisee;
    public static SimpleStringProperty dummyStudentID;
    public static SimpleStringProperty dummyDate;
    public static SimpleStringProperty dummySlot;

    public SecondMarkerController(){
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
    public void setCellTable(){
        this.columnStudentNo.setCellValueFactory(new PropertyValueFactory<>("studentNo"));
        this.columnStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        this.columnName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnSlot.setCellValueFactory(new PropertyValueFactory<>("slot"));
        this.columnSupervisorApproval.setCellValueFactory(new PropertyValueFactory<>("supervisorApproval"));
        this.columnSecondMarkerApproval.setCellValueFactory(new PropertyValueFactory<>("secondMarkerApproval"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() throws IOException {
        System.out.println("Loading Data");
        String line = null;
        String line1 = null;
        String studentID = null;
        String studentName = null;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/student.txt"));
        while ((line = reader.readLine()) != null) {
            String[] info = line.split(",");
            studentID = info[0];
            System.out.println("StudentID: " + info[0]);
            BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
            while ((line1 = reader2.readLine()) != null) {
                String[] student = line1.split(",");
                if (student[1].equals(studentID) && student[9].equals(PMS_Controller.lecturerID)) {
                    studentName = info[1];
                    System.out.println(student[11]);
                    if (student[11].equals("Pending")){
                        this.presentationRequestData.add(new Dashboard(this.i,student[1],studentName,student[5],student[6],student[8],student[10],student[11]));
                        this.i++;
                    }
                }
            }
        }
        System.out.println(presentationRequestData);
        this.secondMarkerList.setItems(this.presentationRequestData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showStudentInfo();
        setCellTable();
        this.i = 1;
        this.secondMarkerList.getItems().clear();
        this.setCellTable();
        this.presentationRequestData = FXCollections.observableArrayList();
        try {
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.contextMenu1.getItems().addAll(this.approve,this.reject);
        this.contextMenu1.setAutoHide(true);
        this.DashBoardMenuItem();
    }

    public int locationX(){
        PointerInfo pointer = MouseInfo.getPointerInfo();
        Point point = pointer.getLocation();
        int x = (int) point.getX();
        return x;
    }

    public int locationY(){
        PointerInfo pointer = MouseInfo.getPointerInfo();
        Point point = pointer.getLocation();
        int y = (int) point.getY();
        return y;
    }

    private void DashBoardMenuValidation(int x, int y){
        if (this.getTemp().equals("Pending")) {
            this.secondMarkerList.setContextMenu(this.contextMenu1);
            this.contextMenu1.show(this.secondMarkerList, x, y);
        }
    }

    private void DashBoardMenuItem(){
        this.secondMarkerList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            public void handle(MouseEvent mouseClick){
                SecondMarkerController.this.temp = SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem().status;
                if (mouseClick.getButton() == MouseButton.SECONDARY){
                    SecondMarkerController.this.DashBoardMenuValidation(SecondMarkerController.this.locationX(), SecondMarkerController.this.locationY());
                }
            }
        });
        this.approve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event){
                SecondMarkerController.this.selectedSupervisee =SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem();
                SecondMarkerController.this.check = "Approve";
                System.out.println("Approve");
                try {
                    SecondMarkerController.this.update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Presentation Request Has Been Approve");
                alert.showAndWait();
            }
        });
        this.reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SecondMarkerController.this.selectedSupervisee = SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem();
                SecondMarkerController.this.check = "Reject";
                try {
                    SecondMarkerController.this.update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Presentation Request Has Been Reject");
                alert.showAndWait();
            }
        });
    }
    public void update()throws IOException{
        String line = null;
        String line2 = null;
        ArrayList<String> oldContent2 = new ArrayList<>();
        ArrayList<String> oldContent = new ArrayList<>();
        this.selectedSupervisee = this.secondMarkerList.getSelectionModel().getSelectedItem();
        System.out.println("selectedSupervisee: " + this.selectedSupervisee);

        //presentation_schedule.txt
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/presentation_schedule.txt"));
        while ((line = reader.readLine()) != null ){
            String [] info = line.split(",");
            String selectedStudentID = this.selectedSupervisee.getStudentID();
            String selectedDate = this.selectedSupervisee.getDate();
            String selectedSlot = this.selectedSupervisee.getSlot();
            String selectedSupervisorApproval = this.selectedSupervisee.getSupervisorApproval();
            String selectedSecondMarkerApproval = this.selectedSupervisee.getSecondMarkerApproval();

            if (info[1].equals(selectedStudentID) && info[5].equals(selectedDate) && info[6].equals(selectedSlot)
                    && info[8].equals(selectedSupervisorApproval )&& info[10].equals(selectedSecondMarkerApproval)){
                System.out.println(STR."check value: \{check}");
                if (this.check.equals("Approve") && info[9].equals(PMS_Controller.lecturerID)){
                    info[10]="Approve";
                    if (info[8].equals("Approve")){
                        info[11] ="Approve";
                    } else if (info[8].equals("Reject")) {
                        info[11] ="Reject";
                    }
                }
                if (this.check.equals("Reject") && info[9].equals(PMS_Controller.lecturerID)){
                    info[10]="Reject";
                    if (info[8].equals("Approve")){
                        info[11] ="Reject";
                    } else if (info[8].equals("Reject")) {
                        info[11] ="Reject";
                    }
                }

            }

            StringBuilder sb = new StringBuilder();
            sb.append(info[0]).append(",");
            sb.append(info[1]).append(",");
            sb.append(info[2]).append(",");
            sb.append(info[3]).append(",");
            sb.append(info[4]).append(",");
            sb.append(info[5]).append(",");
            sb.append(info[6]).append(",");
            sb.append(info[7]).append(",");
            sb.append(info[8]).append(",");
            sb.append(info[9]).append(",");
            sb.append(info[10]).append(",");
            sb.append(info[11]);

            oldContent.add(String.valueOf(sb));
        }


        File file2 = new File("src/main/resources/database/presentation_schedule.txt");
        FileWriter writer2 = new FileWriter(file2);
        BufferedWriter br2 = new BufferedWriter(writer2);
        for (String s : oldContent) {
            br2.write(s);
            br2.newLine();
        }
        br2.close();

        this.secondMarkerList.getItems().clear();
        this.i =1;
        this.loadData();
    }

    private void showStudentInfo(){
        this.secondMarkerList.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2){
                SecondMarkerController.dummyStudentID =SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem().studentID;
                System.out.println(SecondMarkerController.dummyStudentID.getValue());
                SecondMarkerController.dummyDate =SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem().date;
                System.out.println(SecondMarkerController.dummyDate.getValue());
                SecondMarkerController.dummySlot =SecondMarkerController.this.secondMarkerList.getSelectionModel().getSelectedItem().slot;
                System.out.println(SecondMarkerController.dummySlot.getValue());
                FXMLLoader Loader = new FXMLLoader();
                Loader.setLocation(this.getClass().getResource("/lecturer/SupervisorCalendar.fxml"));
                try {
                    Loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Parent p = (Parent)Loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.setTitle("Supervisor Schedule");
                stage.show();
            }
        });
    }
    
}
