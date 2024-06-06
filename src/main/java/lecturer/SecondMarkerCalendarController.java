package lecturer;

import com.calendarfx.model.Entry;
import com.calendarfx.view.page.WeekPage;
import com.example.project_management_system.PMS_Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SecondMarkerCalendarController extends CalendarEntryCreatorAbstract implements Initializable {
    @FXML
    private WeekPage secondMarkerCalendar;

    public SecondMarkerCalendarController(){

    }
    private List<lecturer.Calendar> loadPresentationEntriesFromFile(String filename, String filename2) {
        System.out.println("Running");
        List<lecturer.Calendar> entries = new ArrayList<>();
        String secondMarkerID = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            System.out.println(DashBoardController.dummyStudentID);
            System.out.println(DashBoardController.dummyDate);
            System.out.println(DashBoardController.dummySlot);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(String.valueOf(DashBoardController.dummyStudentID.getValue())) && parts[5].equals(DashBoardController.dummyDate.getValue()) && parts[6].equals(DashBoardController.dummySlot.getValue())) {
                    secondMarkerID = parts[9];
                    System.out.println("SecondMarkerID: " + secondMarkerID);
                    break;
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try (BufferedReader br2 = new BufferedReader(new FileReader(filename))) {
            String line;
            String line2;
            String name;
            while ((line = br2.readLine()) != null) {
                String[] parts = line.split(",");
                BufferedReader reader4 = new BufferedReader(new FileReader(filename2));
                while ((line2 = reader4.readLine()) != null) {
                    String[] info1 = line2.split(",");
                    if (parts[1].equals(info1[0])) {
                        name = info1[1];
                        if (parts[9].equals(secondMarkerID) || parts[7].equals(secondMarkerID)) {
                            System.out.println("Adding Entry");
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
                            Calendar event = new Calendar(presentationId, studentId, studentName, assessmentType, intake,
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
        System.out.println("SecondMarkerCalendar loaded");
        List<lecturer.Calendar> events = loadPresentationEntriesFromFile("src/main/resources/database/presentation_schedule.txt", "src/main/resources/database/student.txt");
        for (lecturer.Calendar event : events) {
            Entry<?> calendarEntry = createCalendarEntry(event);
            secondMarkerCalendar.getCalendarSources().get(0).getCalendars().get(0).addEntry(calendarEntry);
        }
    }
}