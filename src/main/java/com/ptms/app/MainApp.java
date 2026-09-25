package com.ptms.app;

import com.ptms.app.controller.AuthController;
import com.ptms.app.controller.ProjectController;
import com.ptms.app.model.User;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AuthController authController =
                new AuthController(scanner);

        ProjectController projectController =
                new ProjectController(scanner);

        User loggedInUser;

        // -------------------------------
        // Authentication
        // -------------------------------

        try {

            loggedInUser = authController.login();

            System.out.println(
                    "\nLogin successful. Welcome, "
                            + loggedInUser.getFirstName()
                            + " "
                            + loggedInUser.getLastName()
            );

            System.out.println(
                    "Role: " + loggedInUser.getRoleName()
            );

        } catch (Exception e) {

            System.out.println(
                    "Login failed: " + e.getMessage()
            );

            scanner.close();
            return;
        }

        // -------------------------------
        // Main application
        // -------------------------------

        boolean running = true;

        while (running) {

            System.out.println(
                    "\n=============================="
            );
            System.out.println(
                    " PROJECT TRACKING MANAGEMENT"
            );
            System.out.println(
                    "=============================="
            );

            String role = loggedInUser.getRoleName();

            if ("ADMIN".equals(role)
                    || "PROJECT_MANAGER".equals(role)) {

                System.out.println("1. Create Project");
                System.out.println("2. View Project");
                System.out.println("3. View All Projects");
                System.out.println("4. Update Project");
                System.out.println("5. Delete Project");
                System.out.println("6. Find Projects by Domain");

            } else {

                System.out.println(
                        "No project management operations available "
                                + "for your current role."
                );
            }

            System.out.println("0. Logout");

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                switch (input) {

                    case "1":
                        checkProjectAccess(loggedInUser);
                        projectController.createProject(
                                loggedInUser
                        );
                        break;

                    case "2":
                        checkProjectAccess(loggedInUser);
                        projectController.viewProject();
                        break;

                    case "3":
                        checkProjectAccess(loggedInUser);
                        projectController.viewAllProjects();
                        break;

                    case "4":
                        checkProjectAccess(loggedInUser);
                        projectController.updateProject(
                                loggedInUser
                        );
                        break;

                    case "5":
                        checkProjectAccess(loggedInUser);
                        projectController.deleteProject(
                                loggedInUser
                        );
                        break;

                    case "6":
                        checkProjectAccess(loggedInUser);
                        projectController.findProjectsByDomain();
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: " + e.getMessage()
                );
            }
        }

        scanner.close();

        System.out.println("\nLogged out.");
        System.out.println("Application closed.");
    }

    private static void checkProjectAccess(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must be logged in."
            );
        }

        String role = user.getRoleName();

        if (!"ADMIN".equals(role)
                && !"PROJECT_MANAGER".equals(role)) {

            throw new IllegalArgumentException(
                    "You do not have permission to manage projects."
            );
        }
    }
}