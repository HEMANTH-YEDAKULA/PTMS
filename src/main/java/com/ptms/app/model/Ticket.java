package com.ptms.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket {

    private int id;
    private int projectId;
    private String title;
    private String description;
    private String priority;
    private LocalDate deadline;
    private Integer assignedTo;
    private LocalDateTime createdAt;
    private String status;

    public Ticket() {
    }

    public Ticket(int projectId,
                  String title,
                  String description,
                  String priority,
                  LocalDate deadline,
                  Integer assignedTo,
                  String status) {

        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return "Ticket{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", title='" + title + '\'' +
                ", priority='" + priority + '\'' +
                ", deadline=" + deadline +
                ", assignedTo=" + assignedTo +
                ", status='" + status + '\'' +
                '}';
    }
}