package lecturer;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.PageBase;
import com.calendarfx.view.page.WeekPage;
import com.example.project_management_system.PMS_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CalendarController extends CalendarEntryCreatorAbstract implements Initializable {

    @FXML
    private WeekPage calendar;
    public CalendarController(){

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

    private List<lecturer.Calendar> loadPresentationEntriesFromFile(String filename, String filename2) {
        List<lecturer.Calendar> entries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String line2;
            String name;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                BufferedReader reader4 = new BufferedReader(new FileReader(filename2));
                while ((line2 = reader4.readLine()) != null) {
                    String[] info1 = line2.split(",");
                    if (parts[1].equals(info1[0])) {
                        name = info1[1];
                        if (parts[7].equals(PMS_Controller.lecturerID) || parts[9].equals(PMS_Controller.lecturerID)) {
                            // Assuming the order of fields in the text file matches the order of fields in the AssessmentEvent class
                            String presentationId = parts[0];
                            String studentId = parts[1];
                            String studentName = name;
                            String assessmentType = parts[2];
                            String intake = parts[3];
                            String reportId = parts[4];
                            String date = parts[5];
                            String slot = parts[6];
                            String lecturerId1 = parts[7];
                            String approval1 = parts[8];
                            String lecturerId2 = parts[9];
                            String approval2 = parts[10];
                            String status = parts[11];

                            // Create AssessmentEvent object and add it to the list
                            lecturer.Calendar event = new lecturer.Calendar(presentationId, studentId, studentName, assessmentType, intake,
                                    reportId, date, slot, lecturerId1, approval1, lecturerId2, approval2, status);
                            entries.add(event);

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    @Override
    protected Entry<?> createCalendarEntry(Calendar calendar1) {
        Entry<?> entry = new Entry<>();
        entry.setTitle(calendar1.getStudentName());
        entry.setInterval(calendar1.getDate().atTime(calendar1.getSlot()),
                calendar1.getDate().atTime(calendar1.getSlot()).plusHours(1));
        setStyleBasedOnStatus(entry, calendar1.getStatus());
        return entry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Calendar loaded");
        List<lecturer.Calendar> events = loadPresentationEntriesFromFile("src/main/resources/database/presentation_schedule.txt", "src/main/resources/database/student.txt");
        for (lecturer.Calendar event : events) {
            Entry<?> calendarEntry = createCalendarEntry(event);
            calendar.getCalendarSources().get(0).getCalendars().get(0).addEntry(calendarEntry);
        }
    }

}
