package student;

public class SubmissionTable {
    private String assessmentID;
    private String assessmentName;
    private String description;
    private String type;
    private String intake;
    private String supervisor;
    private String secondMarker;
    private String dueDate;
    private String studentID;
    private String studentName;
    private String assignType;

    public SubmissionTable(String assessmentID, String assessmentName, String description, String type,
                           String intake, String supervisor, String secondMarker, String dueDate,
                           String studentID, String studentName, String assignType) {
        this.assessmentID = assessmentID;
        this.assessmentName = assessmentName;
        this.description = description;
        this.type = type;
        this.intake = intake;
        this.supervisor = supervisor;
        this.secondMarker = secondMarker;
        this.dueDate = dueDate;
        this.studentID = studentID;
        this.studentName = studentName;
        this.assignType = assignType;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(String assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSecondMarker() {
        return secondMarker;
    }

    public void setSecondMarker(String secondMarker) {
        this.secondMarker = secondMarker;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
