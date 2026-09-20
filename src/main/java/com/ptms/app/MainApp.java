package com.ptms.app;

import com.ptms.app.controller.ProjectController;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ProjectController projectController =
                new ProjectController();

        boolean running = true;

        while (running) {

            System.out.println("\n==============================");
            System.out.println(" PROJECT TRACKING MANAGEMENT");
            System.out.println("==============================");
            System.out.println("1. Create Project");
            System.out.println("2. View Project");
            System.out.println("3. View All Projects");
            System.out.println("4. Delete Project");
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
                        projectController.deleteProject();
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
            }
        }

        scanner.close();

        System.out.println("Application closed.");
    }
}