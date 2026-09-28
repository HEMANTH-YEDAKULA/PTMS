package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.ProjectMemberService;
import com.ptms.app.service.ProjectMemberServiceImpl;

import java.util.List;
import java.util.Scanner;

public class ProjectMemberController {

    private final Scanner scanner;
    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(Scanner scanner) {
        this.scanner = scanner;
        this.projectMemberService = new ProjectMemberServiceImpl();
    }

    public void showMenu(User loggedInUser) {

        while (true) {

            System.out.println("\n===== PROJECT MEMBER MANAGEMENT =====");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. View Project Members");
            System.out.println("4. Check Membership");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1" -> addMember(loggedInUser);

                    case "2" -> removeMember(loggedInUser);

                    case "3" -> viewMembers(loggedInUser);

                    case "4" -> checkMembership();

                    case "0" -> {
                        return;
                    }

                    default ->
                            System.out.println("Invalid choice.");

                }

            } catch (RuntimeException e) {

                System.out.println(
                        "Operation failed: " + e.getMessage()
                );
            }
        }
    }

    private void addMember(User loggedInUser) {

        System.out.print("Project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("User ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Role in Project: ");
        String role = scanner.nextLine().trim();

        boolean added = projectMemberService.addMember(
                projectId,
                userId,
                role,
                loggedInUser
        );

        if (added) {
            System.out.println("Project member added successfully.");
        } else {
            System.out.println("Failed to add project member.");
        }
    }

    private void removeMember(User loggedInUser) {

        System.out.print("Project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("User ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        boolean removed = projectMemberService.removeMember(
                projectId,
                userId,
                loggedInUser
        );

        if (removed) {
            System.out.println("Project member removed successfully.");
        } else {
            System.out.println("Project member not found.");
        }
    }

    private void viewMembers(User loggedInUser) {

        System.out.print("Project ID: ");

        String input = scanner.nextLine().trim();

        if (input.isBlank()) {
            System.out.println("Project ID cannot be empty.");
            return;
        }

        int projectId;

        try {
            projectId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Project ID must be a number.");
            return;
        }

        List<User> members =
                projectMemberService.getProjectMembers(
                        projectId,
                        loggedInUser
                );

        if (members.isEmpty()) {
            System.out.println(
                    "No members found for Project ID: " + projectId
            );
            return;
        }

        System.out.println();

        System.out.println("              PROJECT MEMBERS - PROJECT " + projectId);


        for (User user : members) {

            System.out.println("Member ID     : " + user.getId());

            String fullName =
                    ((user.getFirstName() == null)
                            ? ""
                            : user.getFirstName())
                            + " "
                            + ((user.getLastName() == null)
                            ? ""
                            : user.getLastName());

            System.out.println("Name          : " + fullName.trim());
            System.out.println("Username      : " + user.getUsername());
            System.out.println("Email         : " + user.getEmail());
            System.out.println("Role          : " + user.getRoleName());
            System.out.println("Mobile        : " + user.getMobileNumber());
            System.out.println("Gender        : " + user.getGender());
            System.out.println("Date of Birth : " + user.getDateOfBirth());

            System.out.println("------------------------------------------------------------");
        }

        System.out.println(
                "Total Members : " + members.size()
        );

        System.out.println(
                "============================================================"
        );
    }

    private void checkMembership() {

        System.out.print("Project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("User ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        boolean member =
                projectMemberService.isMember(projectId, userId);

        if (member) {
            System.out.println("User is a member of this project.");
        } else {
            System.out.println("User is NOT a member of this project.");
        }
    }
}