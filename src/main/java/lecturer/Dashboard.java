package lecturer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dashboard {
    public SimpleIntegerProperty studentNo = new SimpleIntegerProperty();
    public SimpleIntegerProperty studentID = new SimpleIntegerProperty();
    public SimpleStringProperty studentName = new SimpleStringProperty();
    public SimpleStringProperty date = new SimpleStringProperty();
    public SimpleStringProperty slot = new SimpleStringProperty();
    public SimpleStringProperty supervisorApproval = new SimpleStringProperty();
    public SimpleStringProperty secondMarkerApproval = new SimpleStringProperty();
    public SimpleStringProperty status = new SimpleStringProperty();
    public LocalDate dateTemp;
    public DateTimeFormatter formatterTemp =DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Dashboard(int studentNo, int studentID, String studentName, String date, String slot,
                     String supervisorApproval, String secondMarkerApproval, String status){
        this.studentNo.set(studentNo);
        this.studentID.set(studentID);
        this.studentName.set(studentName);
        if (date != null && !date.trim().isEmpty()) {
            this.dateTemp = LocalDate.parse(date, formatterTemp);
            this.date.set(this.dateTemp.format(formatter));
        }
        this.slot.set(slot);
        this.supervisorApproval.set(supervisorApproval);
        this.secondMarkerApproval.set(secondMarkerApproval);
        this.status.set(status);
    }

    public int getStudentNo() {
        return studentNo.get();
    }

    public SimpleIntegerProperty studentNoProperty() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo.set(studentNo);
    }

    public int getStudentID() {
        return studentID.get();
    }

    public SimpleIntegerProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID.set(studentID);
    }

    public String getStudentName() {
        return studentName.get();
    }

    public SimpleStringProperty studentNameProperty() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getSlot() {
        return slot.get();
    }

    public SimpleStringProperty slotProperty() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot.set(slot);
    }

    public String getSupervisorApproval() {
        return supervisorApproval.get();
    }

    public SimpleStringProperty supervisorApprovalProperty() {
        return supervisorApproval;
    }

    public void setSupervisorApproval(String supervisorApproval) {
        this.supervisorApproval.set(supervisorApproval);
    }

    public String getSecondMarkerApproval() {
        return secondMarkerApproval.get();
    }

    public SimpleStringProperty secondMarkerApprovalProperty() {
        return secondMarkerApproval;
    }

    public void setSecondMarkerApproval(String secondMarkerApproval) {
        this.secondMarkerApproval.set(secondMarkerApproval);
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
