package student;

import com.example.project_management_system.PMS_Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Dashboard {
    private final Map<String, Integer> countMap = new HashMap<>();
    @FXML
    private ListView<String> upcomingSubmissionsListView;

    private final VBox chartContainer = new VBox();

    public VBox getChartContainer() {
        return chartContainer;
    }

    @FXML
    private PieChart assessmentPieChart;

    public void initialize() {
        String currentUserID = getLoggedInUserID();
        String userIntake = getUserIntakeFromLogin(currentUserID);
        displayUpcomingSubmissions(userIntake, currentUserID);

        if (userIntake != null) {
            initAssessmentPieChart(userIntake, currentUserID);
            startLiveUpdate(userIntake, currentUserID);
        } else {
            System.out.println("Failed to retrieve user intake.");
        }
    }

    private void displayUpcomingSubmissions(String userIntake, String currentUserID) {
        upcomingSubmissionsListView.getItems().clear();

        List<Assessment> upcomingSubmissions = getRemainingAssessments(userIntake, currentUserID);

        for (Assessment assessment : upcomingSubmissions) {
            String item = assessment.getName() + " - Due Date: " + assessment.getDueDate();
            upcomingSubmissionsListView.getItems().add(item);
        }
    }

    private List<Assessment> getRemainingAssessments(String userIntake, String currentUserID) {
        List<Assessment> remainingAssessments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/FinalStudent_Assessment.txt"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String id = parts[0].trim();
                    String intake = parts[4].trim();
                    String studentID = parts[8].trim();
                    if (intake.equals(userIntake) && studentID.equals(currentUserID)) {
                        String name = parts[1].trim();
                        LocalDate dueDate = LocalDate.parse(parts[7].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                        Assessment assessment = new Assessment(id, name, dueDate);
                        if (!isAssessmentSubmitted(id, userIntake, currentUserID)) {
                            remainingAssessments.add(assessment);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return remainingAssessments;
    }


    private Set<String> getSubmittedAssessments(String userIntake, String currentUserID) {
        Set<String> submittedAssessmentIDs = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 12) {
                    String assessmentID = fields[4].trim();
                    String intake = fields[3].trim();
                    String userID = fields[2].trim();
                    if (intake.equals(userIntake) && userID.equals(currentUserID)) {
                        submittedAssessmentIDs.add(assessmentID);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return submittedAssessmentIDs;
    }

    private void startLiveUpdate(String userIntake, String currentUserID) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> refreshDashboard(userIntake, currentUserID)), 0, 30, TimeUnit.SECONDS);
    }

    private void refreshDashboard(String userIntake, String currentUserID) {
        initAssessmentPieChart(userIntake, currentUserID);
    }

    private void initAssessmentPieChart(String userIntake, String currentUserID) {
        System.out.println("Initializing Assessment Pie Chart");

        countMap.clear();
        countMap.put("Submitted", 0);
        countMap.put("Not Submitted", 0);

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/FinalStudent_Assessment.txt"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String assessmentID = parts[0].trim();
                    String intake = parts[4].trim();
                    String studentID = parts[8].trim();
                    if (intake.equals(userIntake) && studentID.equals(currentUserID)) {
                        boolean isSubmitted = isAssessmentSubmitted(assessmentID, userIntake, currentUserID);
                        String key = isSubmitted ? "Submitted" : "Not Submitted";
                        countMap.put(key, countMap.get(key) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Submitted Count: " + countMap.get("Submitted"));
        System.out.println("Not Submitted Count: " + countMap.get("Not Submitted"));

        generatePieChartDataAndTooltips();

        System.out.println("Assessment Pie Chart Initialized");
    }

    private boolean isAssessmentSubmitted(String assessmentID, String userIntake, String currentUserID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 12) {
                    String reportAssessmentID = fields[4].trim();
                    String intake = fields[3].trim();
                    String userID = fields[2].trim();
                    String status = fields[8].trim();
                    if (reportAssessmentID.equals(assessmentID) && intake.equals(userIntake) && userID.equals(currentUserID) && status.equalsIgnoreCase("Submitted")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void generatePieChartDataAndTooltips() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Submitted", countMap.get("Submitted")),
                new PieChart.Data("Not Submitted", countMap.get("Not Submitted"))
        );

        assessmentPieChart.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            String name = data.getName();
            Tooltip tooltip = new Tooltip(name + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);

            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                    tooltip.setText(name + ": " + newValue.intValue()));

            data.getNode().setOnMouseEntered(e -> tooltip.show(data.getNode(), e.getScreenX(), e.getScreenY()));
            data.getNode().setOnMouseExited(e -> tooltip.hide());
        }
    }

    private String getUserIntakeFromLogin(String currentUserID) {
        return DataReader.getUserIntakeFromLogin(currentUserID);
    }


    private String getLoggedInUserID() {
        return PMS_Controller.getLoggedInUserID();
    }
}
