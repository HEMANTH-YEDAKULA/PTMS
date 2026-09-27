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
        this.scanner = scanner;
        this.projectService = new ProjectServiceImpl();
    }



    public void createProject(User loggedInUser) {

        System.out.println();
        System.out.println("--- Create Project ---");

        System.out.print("Project name: ");
        String name = scanner.nextLine();

        System.out.print("Requirements: ");
        String requirements = scanner.nextLine();

        System.out.print(
                "Team Lead ID (press Enter for none): "
        );

        String teamLeadInput = scanner.nextLine();

        Integer teamLeadId = null;

        if (!teamLeadInput.isBlank()) {
            teamLeadId = Integer.parseInt(teamLeadInput);
        }

        System.out.print(
                "Client ID (press Enter for none): "
        );

        String clientInput = scanner.nextLine();

        Integer clientId = null;

        if (!clientInput.isBlank()) {
            clientId = Integer.parseInt(clientInput);
        }

        System.out.print("Domain: ");
        String domain = scanner.nextLine();

        System.out.print("Cost: ");
        BigDecimal cost =
                new BigDecimal(scanner.nextLine());

        System.out.print(
                "Start date (YYYY-MM-DD): "
        );

        LocalDate startDate =
                LocalDate.parse(scanner.nextLine());

        System.out.print(
                "Deadline (YYYY-MM-DD): "
        );

        LocalDate deadline =
                LocalDate.parse(scanner.nextLine());

        System.out.print(
                "Priority (LOW/MEDIUM/HIGH): "
        );

        String priority = scanner.nextLine();

        System.out.print(
                "Status (PLANNED/IN_PROGRESS/COMPLETED): "
        );

        String status = scanner.nextLine();

        Project project = new Project(
                0,
                name,
                requirements,
                loggedInUser.getId(),
                teamLeadId,
                clientId,
                domain,
                cost,
                startDate,
                deadline,
                priority,
                status
        );
        try {
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

            } else {

                System.out.println(
                        "Project creation failed."
                );
            }
        } catch (Exception ex) {
            System.out.println("Unable to create project: " + ex.getMessage());
        }
    }




    public void viewProject() {

        System.out.println();
        System.out.println("--- View Project ---");

        System.out.print("Enter project ID: ");

        int id = Integer.parseInt(
                scanner.nextLine()
        );

        Project project =
                projectService.getProjectById(id);

        if (project == null) {

            System.out.println(
                    "Project not found."
            );

        } else {

            System.out.println(project);
        }
    }



    public void viewAllProjects() {

        System.out.println();
        System.out.println("--- All Projects ---");

        List<Project> projects =
                projectService.getAllProjects();

        if (projects.isEmpty()) {

            System.out.println(
                    "No projects found."
            );

            return;
        }

        projects.forEach(
                System.out::println
        );
    }



    public void updateProject(User loggedInUser) {

        System.out.println();
        System.out.println("--- Update Project ---");

        System.out.print("Enter project ID: ");

        int projectId = Integer.parseInt(
                scanner.nextLine()
        );

        Project currentProject =
                projectService.getProjectById(projectId);

        if (currentProject == null) {

            System.out.println(
                    "Project not found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "Current project:"
        );
        System.out.println(currentProject);

        System.out.print("New project name: ");
        String name = scanner.nextLine();

        System.out.print("New requirements: ");
        String requirements = scanner.nextLine();


        int managerId =
                currentProject.getManagerId();

        if ("ADMIN".equals(
                loggedInUser.getRoleName())) {

            System.out.print("Manager ID: ");

            managerId = Integer.parseInt(
                    scanner.nextLine()
            );
        }

        System.out.print("Domain: ");
        String domain = scanner.nextLine();

        System.out.print("Cost: ");

        BigDecimal cost =
                new BigDecimal(scanner.nextLine());

        System.out.print(
                "Start date (YYYY-MM-DD): "
        );

        LocalDate startDate =
                LocalDate.parse(scanner.nextLine());

        System.out.print(
                "Deadline (YYYY-MM-DD): "
        );

        LocalDate deadline =
                LocalDate.parse(scanner.nextLine());

        System.out.print(
                "Priority (LOW/MEDIUM/HIGH): "
        );

        String priority =
                scanner.nextLine();

        System.out.print(
                "Status (PLANNED/IN_PROGRESS/COMPLETED): "
        );

        String status =
                scanner.nextLine();

        Project updatedProject =
                new Project(
                        projectId,
                        name,
                        requirements,
                        managerId,
                        currentProject.getTeamLeadId(),
                        currentProject.getClientId(),
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



    public void deleteProject(User loggedInUser) {

        System.out.println();
        System.out.println("--- Delete Project ---");

        System.out.print("Enter project ID: ");

        int id = Integer.parseInt(
                scanner.nextLine()
        );

        boolean deleted =
                projectService.deleteProject(
                        id,
                        loggedInUser
                );

        if (deleted) {

            System.out.println(
                    "Project deleted successfully."
            );

        } else {

            System.out.println(
                    "Project deletion failed."
            );
        }
    }



    public void findProjectsByDomain() {

        System.out.println();
        System.out.println(
                "--- Find Projects By Domain ---"
        );

        System.out.print("Enter domain: ");

        String domain =
                scanner.nextLine();

        List<Project> projects =
                projectService.getProjectsByDomain(
                        domain
                );

        if (projects.isEmpty()) {

            System.out.println(
                    "No projects found for domain: "
                            + domain
            );

            return;
        }

        projects.forEach(
                System.out::println
        );
    }
}