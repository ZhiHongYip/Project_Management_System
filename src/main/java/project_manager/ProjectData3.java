package project_manager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectData3 {
    private StringProperty reportID;
    private StringProperty name;
    private StringProperty studentID;
    private StringProperty intake;
    private StringProperty assessmentID;
    private StringProperty moodleLink;
    private StringProperty submissionDate;
    private StringProperty grade;
    private StringProperty status;
    private StringProperty feedback;
    private StringProperty supervisorID;
    private StringProperty secondMarkerID;

    // Constructor with 13 parameters
    public ProjectData3(String reportID, String name, String studentID, String intake, String assessmentID, String moodleLink, String submissionDate, String grade, String status, String feedback, String supervisorID, String secondMarkerID) {
        this.reportID = new SimpleStringProperty(reportID);
        this.name = new SimpleStringProperty(name);
        this.studentID = new SimpleStringProperty(studentID);
        this.intake = new SimpleStringProperty(intake);
        this.assessmentID = new SimpleStringProperty(assessmentID);
        this.moodleLink = new SimpleStringProperty(moodleLink);
        this.submissionDate = new SimpleStringProperty(submissionDate);
        this.grade = new SimpleStringProperty(grade);
        this.status = new SimpleStringProperty(status);
        this.feedback = new SimpleStringProperty(feedback);
        this.supervisorID = new SimpleStringProperty(supervisorID);
        this.secondMarkerID = new SimpleStringProperty(secondMarkerID);
    }

    // Getters for JavaFX properties
    public StringProperty reportIDProperty() {
        return reportID;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty studentIDProperty() {
        return studentID;
    }

    public StringProperty intakeProperty() {
        return intake;
    }

    public StringProperty assessmentIDProperty() {
        return assessmentID;
    }

    public StringProperty moodleLinkProperty() {
        return moodleLink;
    }

    public StringProperty submissionDateProperty() {
        return submissionDate;
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty feedbackProperty() {
        return feedback;
    }

    public StringProperty supervisorIDProperty() {
        return supervisorID;
    }

    public StringProperty secondMarkerIDProperty() {
        return secondMarkerID;
    }

    // Simple getters for the values
    public String getReportID() {
        return reportID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getStudentID() {
        return studentID.get();
    }

    public String getIntake() {
        return intake.get();
    }

    public String getAssessmentID() {
        return assessmentID.get();
    }

    public String getMoodleLink() {
        return moodleLink.get();
    }

    public String getSubmissionDate() {
        return submissionDate.get();
    }

    public String getGrade() {
        return grade.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getFeedback() {
        return feedback.get();
    }

    public String getSupervisorID() {
        return supervisorID.get();
    }

    public String getSecondMarkerID() {
        return secondMarkerID.get();
    }
}
