package project_manager;

public class StudentData {
    private String studentId;
    private String studentName;
    private String intake;
    private String status;

    public StudentData(String studentId, String studentName, String intake, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.intake = intake;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
