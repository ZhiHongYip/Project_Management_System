package lecturer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Calendar {
    public String presentationId;
    public String studentId;
    public String studentName;
    public String assessmentType;
    public String intake;
    public String reportId;
    LocalDate date;
    LocalTime slot;
    public String lecturerId1;
    public String approval1;
    public String lecturerId2;
    public String approval2;
    public String status;

    public Calendar(String presentationId, String studentId, String studentName, String assessmentType, String intake, String reportId, String date,
                    String slot,String lecturerId1, String approval1, String lecturerId2, String approval2, String status)
    {
        this.presentationId = presentationId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.assessmentType = assessmentType;
        this.intake = intake;
        this.reportId = reportId;
        this.date = LocalDate.parse(date);
        System.out.println(slot);
        this.slot = LocalTime.parse(slot, DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH));
        this.lecturerId1 = lecturerId1;
        this.approval1 = approval1;
        this.lecturerId2 = lecturerId2;
        this.approval2 = approval2;
        this.status = status;
    }

    public String getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(String presentationId) {
        this.presentationId = presentationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getSlot() {
        return slot;
    }

    public void setSlot(LocalTime slot) {
        this.slot = slot;
    }

    public String getLecturerId1() {
        return lecturerId1;
    }

    public void setLecturerId1(String lecturerId1) {
        this.lecturerId1 = lecturerId1;
    }

    public String getApproval1() {
        return approval1;
    }

    public void setApproval1(String approval1) {
        this.approval1 = approval1;
    }

    public String getLecturerId2() {
        return lecturerId2;
    }

    public void setLecturerId2(String lecturerId2) {
        this.lecturerId2 = lecturerId2;
    }

    public String getApproval2() {
        return approval2;
    }

    public void setApproval2(String approval2) {
        this.approval2 = approval2;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
