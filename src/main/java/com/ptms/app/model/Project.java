package com.ptms.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project {
    private int id;
    private String name;
    private String requirements;
    private int managerId;
    private Integer teamLeadId;
    private Integer clientId;
    private String domain;
    private BigDecimal cost;
    private int teamSize;
    private LocalDate startDate;
    private LocalDate deadline;
    private String priority;
    private String status;
   public Project()
   {

   }

    public Project(String name, String requirements, int managerId,
                   Integer teamLeadId, Integer clientId, String domain,
                   BigDecimal cost, int teamSize, LocalDate startDate,
                   LocalDate deadline, String priority, String status) {

        this.name = name;
        this.requirements = requirements;
        this.managerId = managerId;
        this.teamLeadId = teamLeadId;
        this.clientId = clientId;
        this.domain = domain;
        this.cost = cost;
        this.teamSize = teamSize;
        this.startDate = startDate;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setName(String name) {
       this.name = name;
    }
    public void setRequirements(String requirements) {
       this.requirements = requirements;
    }
    public void setManagerId(int managerId) {
       this.managerId = managerId;
    }
    public void setTeamLeadId(Integer teamLeadId)
        {
        this.teamLeadId = teamLeadId;
        }
        public void setClientId(Integer clientId) {
       this.clientId = clientId;
        }
    public void setDomain(String domain) {
       this.domain = domain;
    }
    public void setCost(BigDecimal cost) {
       this.cost = cost;
    }
    public void setTeamSize(int teamSize)
    {
        this.teamSize = teamSize;
    }
    public void setStartDate(LocalDate startDate) {
       this.startDate = startDate;
    }
    public void setDeadline(LocalDate deadline) {
       this.deadline = deadline;
    }
    public void setPriority(String priority) {
       this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRequirements() {
        return requirements;
    }

    public int getManagerId() {
        return managerId;
    }

    public Integer getTeamLeadId() {
        return teamLeadId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public String getDomain() {
        return domain;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", requirements='" + requirements + '\'' +
                ", managerId=" + managerId +
                ", teamLeadId=" + teamLeadId +
                ", clientId=" + clientId +
                ", domain='" + domain + '\'' +
                ", cost=" + cost +
                ", teamSize=" + teamSize +
                ", startDate=" + startDate +
                ", deadline=" + deadline +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

