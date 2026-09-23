package com.ptms.app.controller;

import com.ptms.app.model.Project;
import com.ptms.app.service.ProjectService;
import com.ptms.app.service.ProjectServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProjectController {

    private final ProjectService projectService;
    private final Scanner scanner;

    public ProjectController() {
        this.projectService = new ProjectServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void createProject() {

        System.out.println("\n--- Create Project ---");

        System.out.print("Project name: ");
        String name = scanner.nextLine();

        System.out.print("Requirements: ");
        String requirements = scanner.nextLine();

        System.out.print("Manager ID: ");
        int managerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Domain: ");
        String domain = scanner.nextLine();

        System.out.print("Cost: ");
        BigDecimal cost =
                new BigDecimal(scanner.nextLine());

        System.out.print("Team size: ");
        int teamSize = Integer.parseInt(scanner.nextLine());

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate startDate =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Deadline (YYYY-MM-DD): ");
        LocalDate deadline =
                LocalDate.parse(scanner.nextLine());

        Project project = new Project(
                name,
                requirements,
                managerId,
                null,
                null,
                domain,
                cost,
                teamSize,
                startDate,
                deadline,
                "MEDIUM",
                "PLANNED"
        );

        boolean created =
                projectService.createProject(project);

        if (created) {
            System.out.println(
                    "Project created successfully. ID = "
                            + project.getId()
            );
        }
    }

    public void viewProject() {

        System.out.print("Enter project ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        Project project =
                projectService.getProjectById(id);

        if (project == null) {
            System.out.println("Project not found.");
        } else {
            System.out.println(project);
        }
    }

    public void viewAllProjects() {

        List<Project> projects =
                projectService.getAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        projects.forEach(System.out::println);
    }

    public void deleteProject() {

        System.out.print("Enter project ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        boolean deleted =
                projectService.deleteProject(id);

        System.out.println(
                deleted
                        ? "Project deleted successfully."
                        : "Project not found."
        );
    }
    public void updateProject() {

        System.out.println("\n--- Update Project ---");

        System.out.print("Enter project ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Project project = projectService.getProjectById(id);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.println("Current project:");
        System.out.println(project);

        System.out.print("New project name: ");
        String name = scanner.nextLine();

        System.out.print("New requirements: ");
        String requirements = scanner.nextLine();

        System.out.print("Manager ID: ");
        int managerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Domain: ");
        String domain = scanner.nextLine();

        System.out.print("Cost: ");
        BigDecimal cost = new BigDecimal(scanner.nextLine());

        System.out.print("Team size: ");
        int teamSize = Integer.parseInt(scanner.nextLine());

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Deadline (YYYY-MM-DD): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine());

        System.out.print("Priority: ");
        String priority = scanner.nextLine();

        System.out.print("Status: ");
        String status = scanner.nextLine();

        Project updatedProject = new Project(
                name,
                requirements,
                managerId,
                project.getTeamLeadId(),
                project.getClientId(),
                domain,
                cost,
                teamSize,
                startDate,
                deadline,
                priority,
                status
        );

        updatedProject.setId(id);

        boolean updated =
                projectService.updateProject(updatedProject);

        if (updated) {
            System.out.println("Project updated successfully.");
        } else {
            System.out.println("Project update failed.");
        }
    }
    public void findProjectsByDomain() {

        System.out.println("\n--- Find Projects By Domain ---");

        System.out.print("Enter domain: ");
        String domain = scanner.nextLine();

        List<Project> projects =
                projectService.getProjectsByDomain(domain);

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        projects.forEach(System.out::println);
    }



}