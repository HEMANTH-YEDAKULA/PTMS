package com.ptms.app;

import com.ptms.app.controller.AuthController;
import com.ptms.app.controller.ProjectController;
import com.ptms.app.model.User;
import com.ptms.app.service.AuthServiceImpl;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuthController authController=new AuthController(scanner);
        ProjectController projectController =
                new ProjectController();
        User loggedInUser;

        try {

            loggedInUser = authController.login();

            System.out.println(
                    "\nLogin successful. Welcome, "
                            + loggedInUser.getName()
            );

            System.out.println(
                    "Role: " + loggedInUser.getRolename()
            );

        } catch (Exception e) {

            System.out.println(
                    "Login failed: " + e.getMessage()
            );

            scanner.close();
            return;
        }
        boolean running = true;

        while (running) {

            System.out.println("\n==============================");
            System.out.println(" PROJECT TRACKING MANAGEMENT");
            System.out.println("==============================");
            System.out.println("1. Create Project");
            System.out.println("2. View Project");
            System.out.println("3. View All Projects");
            System.out.println("4. Update Project");
            System.out.println("5. Delete Project");
            System.out.println("6. Find Projects by Domain");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                switch (input) {

                    case "1":
                        projectController.createProject();
                        break;

                    case "2":
                        projectController.viewProject();
                        break;

                    case "3":
                        projectController.viewAllProjects();
                        break;

                    case "4":
                        projectController.updateProject();
                        break;
                    case "5":
                        projectController.deleteProject();
                        break;
                    case "6":
                        projectController.findProjectsByDomain();
                        break;


                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage()
                );
                e.printStackTrace();
            }
        }

        scanner.close();

        System.out.println("Application closed.");
    }
}