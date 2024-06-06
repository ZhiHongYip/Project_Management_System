package lecturer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Report {
    public SimpleIntegerProperty reportNo = new SimpleIntegerProperty();
    public SimpleStringProperty reportID = new SimpleStringProperty();
    public SimpleStringProperty studentID = new SimpleStringProperty();
    public SimpleStringProperty intake = new SimpleStringProperty();
    public SimpleStringProperty assessmentID = new SimpleStringProperty();
    public SimpleStringProperty submissionDate = new SimpleStringProperty();
    public SimpleStringProperty grade = new SimpleStringProperty();
    public SimpleStringProperty status = new SimpleStringProperty();

    public LocalDate dateTemp;
    public DateTimeFormatter formatterTemp =DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Report(int reportNo, String reportID, String studentID, String intake, String assessmentID,
                  String submissionDate, String grade, String status){
        this.reportNo.set(reportNo);
        this.reportID.set(reportID);
        this.studentID.set(studentID);
        this.intake.set(intake);
        this.assessmentID.set(assessmentID);
        if (submissionDate != null && !submissionDate.trim().isEmpty()) {
            this.dateTemp = LocalDate.parse(submissionDate, formatterTemp);
            this.submissionDate.set(this.dateTemp.format(formatter));
        }
        this.grade.set(grade);
        this.status.set(status);
    }

    public int getReportNo() {
        return reportNo.get();
    }

    public SimpleIntegerProperty reportNoProperty() {
        return reportNo;
    }

    public void setReportNo(int reportNo) {
        this.reportNo.set(reportNo);
    }

    public String getReportID() {
        return reportID.get();
    }

    public SimpleStringProperty reportIDProperty() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID.set(reportID);
    }

    public String getStudentID() {
        return studentID.get();
    }

    public SimpleStringProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID.set(studentID);
    }

    public String getIntake() {
        return intake.get();
    }

    public SimpleStringProperty intakeProperty() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake.set(intake);
    }

    public String getAssessmentID() {
        return assessmentID.get();
    }

    public SimpleStringProperty assessmentIDProperty() {
        return assessmentID;
    }

    public void setAssessmentID(String assessmentID) {
        this.assessmentID.set(assessmentID);
    }

    public String getSubmissionDate() {
        return submissionDate.get();
    }

    public SimpleStringProperty submissionDateProperty() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate.set(submissionDate);
    }

    public String getGrade() {
        return grade.get();
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
