package project_manager;

public class StudentData {
    private String studentId;
    private String studentName;
    private String intake;

    public StudentData(String studentId, String studentName, String intake) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.intake = intake;
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
}
