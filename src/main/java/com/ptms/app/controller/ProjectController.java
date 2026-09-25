package com.ptms.app.controller;

import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import com.ptms.app.service.ProjectService;
import com.ptms.app.service.ProjectServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProjectController {

    private final ProjectService projectService;
    private final Scanner scanner;

    public ProjectController(Scanner scanner) {
        this.projectService = new ProjectServiceImpl();
        this.scanner = scanner;
    }

    // ---------------------------------------------------------
    // CREATE PROJECT
    // ---------------------------------------------------------

    public void createProject(User loggedInUser) {

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

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate startDate =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Deadline (YYYY-MM-DD): ");
        LocalDate deadline =
                LocalDate.parse(scanner.nextLine());

        Project project = new Project(
                0,
                name,
                requirements,
                managerId,
                null,
                null,
                domain,
                cost,
                startDate,
                deadline,
                "MEDIUM",
                "PLANNED"
        );

        boolean created =
                projectService.createProject(
                        project,
                        loggedInUser
                );

        if (created) {
            System.out.println(
                    "Project created successfully. ID = "
                            + project.getId()
            );
        }
    }

    // ---------------------------------------------------------
    // VIEW PROJECT
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // VIEW ALL PROJECTS
    // ---------------------------------------------------------

    public void viewAllProjects() {

        List<Project> projects =
                projectService.getAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        projects.forEach(System.out::println);
    }

    // ---------------------------------------------------------
    // UPDATE PROJECT
    // ---------------------------------------------------------

    public void updateProject(User loggedInUser) {

        System.out.println("\n--- Update Project ---");

        System.out.print("Enter project ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Project existingProject =
                projectService.getProjectById(id);

        if (existingProject == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.println("\nCurrent project:");
        System.out.println(existingProject);

        System.out.print("New project name: ");
        String name = scanner.nextLine();

        System.out.print("New requirements: ");
        String requirements = scanner.nextLine();

        System.out.print("Manager ID: ");
        int managerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Domain: ");
        String domain = scanner.nextLine();

        System.out.print("Cost: ");
        BigDecimal cost =
                new BigDecimal(scanner.nextLine());

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate startDate =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Deadline (YYYY-MM-DD): ");
        LocalDate deadline =
                LocalDate.parse(scanner.nextLine());

        System.out.print("Priority: ");
        String priority = scanner.nextLine();

        System.out.print("Status: ");
        String status = scanner.nextLine();

        Project updatedProject = new Project(
                id,
                name,
                requirements,
                managerId,
                existingProject.getTeamLeadId(),
                existingProject.getClientId(),
                domain,
                cost,
                startDate,
                deadline,
                priority,
                status
        );

        boolean updated =
                projectService.updateProject(
                        updatedProject,
                        loggedInUser
                );

        if (updated) {
            System.out.println(
                    "Project updated successfully."
            );
        } else {
            System.out.println(
                    "Project update failed."
            );
        }
    }

    // ---------------------------------------------------------
    // DELETE PROJECT
    // ---------------------------------------------------------

    public void deleteProject(User loggedInUser) {

        System.out.print("Enter project ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        boolean deleted =
                projectService.deleteProject(
                        id,
                        loggedInUser
                );

        System.out.println(
                deleted
                        ? "Project deleted successfully."
                        : "Project not found."
        );
    }

    // ---------------------------------------------------------
    // FIND PROJECTS BY DOMAIN
    // ---------------------------------------------------------

    public void findProjectsByDomain() {

        System.out.println(
                "\n--- Find Projects By Domain ---"
        );

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

    public void getProject(Scanner scanner) {
    }

    public void getAllProjects() {
    }
}