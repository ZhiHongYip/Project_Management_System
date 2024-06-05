package project_manager;

import java.time.LocalDate;

public class ProjectData {
    private int projectId;
    private String project;
    private String description;
    private String types;
    private String intake;
    private String lecturerId; // 修改类型为 String
    private String markerId; // 修改类型为 String
    private LocalDate dueDate;

    public ProjectData(int projectId, String project, String description, String types, String intake, String lecturerId, String markerId) {
        this.projectId = projectId;
        this.project = project;
        this.description = description;
        this.types = types;
        this.intake = intake;
        this.lecturerId = lecturerId;
        this.markerId = markerId;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }

    public String getTypes() {
        return types;
    }

    public String getIntake() {
        return intake;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public String getMarkerId() {
        return markerId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
