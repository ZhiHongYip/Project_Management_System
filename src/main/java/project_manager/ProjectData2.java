package project_manager;

public class ProjectData2 {
    private String AssId;
    private String AssName;
    private String description;
    private String types;
    private String intake;
    private String supervisor;
    private String secondMarker;
    private String dueDate;

    public ProjectData2(String AssId, String AssName, String description, String types, String intake, String supervisor,String secondMarker, String dueDate) {
        this.AssId = AssId;
        this.AssName = AssName;
        this.description = description;
        this.types = types;
        this.intake = intake;
        this.supervisor = supervisor;
        this.secondMarker = secondMarker;
        this.dueDate = dueDate; // 设置 dueDate
    }

    public String getAssId() {
        return AssId;
    }

    public String getAssName() {
        return AssName;
    }

    public void setProject(String project) {
        this.AssName = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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
        this.secondMarker= secondMarker;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}

