package student;

public class SubmittedTable {

    private String reportID;
    private String name;
    private String studentID;
    private String intake;
    private String assessmentID;
    private String moodleLink;
    private String submissionDate;
    private String grade;
    private String status;
    private String feedback;
    private String supervisorID;
    private String secondMarkerID;

    public SubmittedTable(String reportID, String name, String studentID, String intake,
                          String assessmentID,
                          String moodleLink, String submissionDate, String grade,
                          String status, String feedback, String supervisorID, String secondMarkerID) {
        this.reportID = reportID;
        this.name = name;
        this.studentID = studentID;
        this.assessmentID = assessmentID;
        this.moodleLink = moodleLink;
        this.submissionDate = submissionDate;
        this.grade = grade;
        this.status = status;
        this.intake = intake;
        this.feedback = feedback;
        this.supervisorID = supervisorID;
        this.secondMarkerID = secondMarkerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getSecondMarkerID() {
        return secondMarkerID;
    }

    public void setSecondMarkerID(String secondMarkerID) {
        this.secondMarkerID = secondMarkerID;
    }

    public String getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(String supervisorID) {
        this.supervisorID = supervisorID;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getMoodleLink() {
        return moodleLink;
    }

    public void setMoodleLink(String moodleLink) {
        this.moodleLink = moodleLink;
    }

    public String getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
