package student;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmissionManager {
    List<SubmissionTable> getSubmissionsForUser(String userID, String intake);
    boolean submitAssessment(String userID, String assessmentID, String filePath);
    boolean isAssessmentSubmitted(String userID, String assessmentID);
    void saveSubmissionData(int reportID,String courseName,String userID,String intake,String assessmentID,String filePath,LocalDateTime submissionTime,String status,String feedback,String supervisorID,String secondMarkerID);
}