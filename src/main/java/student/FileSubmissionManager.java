package student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static student.DataReader.generateReportID;
import static student.DataReader.getUserIntakeFromLogin;

public class FileSubmissionManager implements SubmissionManager {

    private int reportCounter;

    private DataReader fileDataReader;

    public FileSubmissionManager(DataReader fileDataReader) {
        this.fileDataReader = fileDataReader;
    }

    @Override
    public List<SubmissionTable> getSubmissionsForUser(String userID, String intake) {
        List<SubmissionTable> submissions = new ArrayList<>( );
        Set<String> submittedAssessmentIDs = fileDataReader.getSubmittedAssessmentIDs(userID);

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/FinalStudent_Assessment.txt"))) {
            String line;
            while ((line = reader.readLine( )) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11) {
                    String assessmentID = fields[0];
                    String assessmentName = fields[1];
                    String description = fields[2];
                    String type = fields[3];
                    String assessmentIntake = fields[4];
                    String supervisor = fields[5];
                    String secondMarker = fields[6];
                    String dueDate = fields[7];
                    String studentID = fields[8];
                    String studentName = fields[9];
                    String assignType = fields[10];

                    if (intake.equals(assessmentIntake) && userID.equals(studentID) && !submittedAssessmentIDs.contains(assessmentID)) {
                        SubmissionTable submission = new SubmissionTable(assessmentID,assessmentName,description,type,assessmentIntake,supervisor,secondMarker,dueDate,studentID, studentName, assignType);
                        submissions.add(submission);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace( );
        }

        return submissions;
    }

    @Override
    public boolean submitAssessment(String userID, String assessmentID, String filePath) {
        String intake = getUserIntakeFromLogin(userID);
        String courseName = DataReader.getAssessmentName(assessmentID);
        String supervisorID = DataReader.getSupervisorID(assessmentID);
        String secondMarkerID = DataReader.getSecondMarkerID(assessmentID);

        String folderName = String.format("%s-%s", userID, courseName.replaceAll("\\s", "_"));
        String submissionsDirectory = "src/main/resources/submissions";
        Path folderPath = Paths.get(submissionsDirectory, folderName);
        Path destinationFilePath = folderPath.resolve(Paths.get(filePath).getFileName( ));

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                e.printStackTrace( );
                return false;
            }
        }

        try {
            Files.copy(Paths.get(filePath), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved at: " + destinationFilePath);
            int reportID = generateReportID();
            String formattedFilePath = destinationFilePath.toString( ).replace("\\", "/");

            saveSubmissionData(reportID,courseName,userID,intake,assessmentID,formattedFilePath,LocalDateTime.now( ),"Ungraded","",supervisorID,secondMarkerID);
            return true;
        } catch (IOException e) {
            e.printStackTrace( );
            return false;
        }
    }

    @Override
    public boolean isAssessmentSubmitted(String userID, String assessmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"))) {
            String line;
            while ((line = reader.readLine( )) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11) {
                    String reportAssessmentID = fields[4];
                    String studentID = fields[2];
                    if (reportAssessmentID.equals(assessmentID) && studentID.equals(userID)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace( );
        }
        return false;
    }

    @Override
    public void saveSubmissionData(int reportID, String courseName, String userID, String intake, String assessmentID, String filePath, LocalDateTime submissionTime, String status, String feedback, String supervisorID, String secondMarkerStatus) {
        String submissionData = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                reportID,
                courseName,
                userID,
                intake,
                assessmentID,
                filePath,
                submissionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                status,
                "Submitted",
                "",
                supervisorID,
                secondMarkerStatus);
        DataReader.saveSubmissionDataToFile(submissionData);
    }
}