package com.ptms.app;

import com.ptms.app.controller.AuthController;
import com.ptms.app.controller.ProjectController;
import com.ptms.app.controller.UserController;
import com.ptms.app.model.User;

import java.util.Scanner;

public class MainApp {

    private static UserController userController;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AuthController authController =
                new AuthController(scanner);

        ProjectController projectController =
                new ProjectController(scanner);

        userController =
                new UserController(scanner);

        try {

            User loggedInUser = authController.login();

            System.out.println();
            System.out.println(
                    "Login successful. Welcome, "
                            + loggedInUser.getFirstName()
                            + " "
                            + loggedInUser.getLastName()
            );

            System.out.println(
                    "Role: " + loggedInUser.getRoleName()
            );

            showDashboard(
                    loggedInUser,
                    projectController,
                    scanner
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Operation failed: " + e.getMessage()
            );

        } finally {

            scanner.close();

            System.out.println();
            System.out.println("Application closed.");
        }
    }



    private static void showDashboard(
            User loggedInUser,
            ProjectController projectController,
            Scanner scanner) {

        String role = loggedInUser.getRoleName();

        switch (role) {

            case "ADMIN":

                showAdminDashboard(
                        loggedInUser,
                        projectController,
                        scanner
                );

                break;


            case "PROJECT_MANAGER":

                showProjectManagerDashboard(
                        loggedInUser,
                        projectController,
                        scanner
                );

                break;


            case "TEAM_LEAD":

                showTeamLeadDashboard(loggedInUser);

                break;


            case "EMPLOYEE":

                showEmployeeDashboard(loggedInUser);

                break;


            default:

                System.out.println(
                        "No dashboard available for role: "
                                + role
                );
        }
    }



    private static void showAdminDashboard(
            User loggedInUser,
            ProjectController projectController,
            Scanner scanner) {

        while (true) {

            System.out.println();
            System.out.println("==============================");
            System.out.println(" ADMIN DASHBOARD");
            System.out.println("==============================");

            System.out.println("1. User Management");
            System.out.println("2. Project Management");
            System.out.println("0. Logout");

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                int option = Integer.parseInt(input);

                switch (option) {

                    case 1:

                        showUserManagementMenu(
                                loggedInUser,
                                scanner
                        );

                        break;


                    case 2:

                        showProjectManagementMenu(
                                loggedInUser,
                                projectController,
                                scanner,
                                true
                        );

                        break;


                    case 0:

                        System.out.println("Logged out.");

                        return;


                    default:

                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }




    private static void showUserManagementMenu(
            User loggedInUser,
            Scanner scanner) {

        while (true) {

            System.out.println();
            System.out.println("==============================");
            System.out.println(" USER MANAGEMENT");
            System.out.println("==============================");

            System.out.println("1. Create User");
            System.out.println("2. View User");
            System.out.println("3. View All Users");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Find Users by Role");
            System.out.println("0. Back");

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                int option = Integer.parseInt(input);

                switch (option) {

                    case 1:

                        userController.createUser(
                                loggedInUser
                        );

                        break;


                    case 2:

                        userController.viewUser(
                                loggedInUser
                        );

                        break;


                    case 3:

                        userController.viewAllUsers(
                                loggedInUser
                        );

                        break;


                    case 4:

                        userController.updateUser(
                                loggedInUser
                        );

                        break;


                    case 5:

                        userController.deleteUser(
                                loggedInUser
                        );

                        break;


                    case 6:

                        userController.findUsersByRole(
                                loggedInUser
                        );

                        break;


                    case 0:

                        return;


                    default:

                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage()
                );
            }
        }
    }




    private static void showProjectManagerDashboard(
            User loggedInUser,
            ProjectController projectController,
            Scanner scanner) {

        while (true) {

            System.out.println();
            System.out.println("==============================");
            System.out.println(" PROJECT MANAGER DASHBOARD");
            System.out.println("==============================");

            System.out.println("1. Project Management");
            System.out.println("0. Logout");

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                int option = Integer.parseInt(input);

                switch (option) {

                    case 1:

                        showProjectManagementMenu(
                                loggedInUser,
                                projectController,
                                scanner,
                                false
                        );

                        break;


                    case 0:

                        System.out.println("Logged out.");

                        return;


                    default:

                        System.out.println(
                                "Invalid option."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }



    private static void showProjectManagementMenu(
            User loggedInUser,
            ProjectController projectController,
            Scanner scanner,
            boolean admin) {

        while (true) {

            System.out.println();
            System.out.println("==============================");
            System.out.println(" PROJECT MANAGEMENT");
            System.out.println("==============================");

            System.out.println("1. Create Project");
            System.out.println("2. View Project");
            System.out.println("3. View All Projects");
            System.out.println("4. Update Project");

            if (admin) {

                System.out.println("5. Delete Project");
                System.out.println("6. Find Projects by Domain");

            } else {

                System.out.println("5. Find Projects by Domain");
            }

            System.out.println("0. Back");

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                int option = Integer.parseInt(input);

                if (admin) {

                    switch (option) {

                        case 1:

                            projectController.createProject(
                                    loggedInUser
                            );

                            break;


                        case 2:

                            projectController.viewProject();

                            break;


                        case 3:

                            projectController.viewAllProjects();

                            break;


                        case 4:

                            projectController.updateProject(
                                    loggedInUser
                            );

                            break;


                        case 5:

                            projectController.deleteProject(
                                    loggedInUser
                            );

                            break;


                        case 6:

                            projectController.findProjectsByDomain();

                            break;


                        case 0:

                            return;


                        default:

                            System.out.println(
                                    "Invalid option."
                            );
                    }

                } else {

                    switch (option) {

                        case 1:

                            projectController.createProject(
                                    loggedInUser
                            );

                            break;


                        case 2:

                            projectController.viewProject();

                            break;


                        case 3:

                            projectController.viewAllProjects();

                            break;


                        case 4:

                            projectController.updateProject(
                                    loggedInUser
                            );

                            break;


                        case 5:

                            projectController.findProjectsByDomain();

                            break;


                        case 0:

                            return;


                        default:

                            System.out.println(
                                    "Invalid option."
                            );
                    }
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage()
                );
            }
        }
    }



    private static void showTeamLeadDashboard(
            User loggedInUser) {

        System.out.println();

        System.out.println("==============================");
        System.out.println(" TEAM LEAD DASHBOARD");
        System.out.println("==============================");

        System.out.println(
                "Ticket and task operations will "
                        + "be available in the next module."
        );

        System.out.println();

        System.out.println("Logged out.");
    }


    private static void showEmployeeDashboard(
            User loggedInUser) {

        System.out.println();

        System.out.println("==============================");
        System.out.println(" EMPLOYEE DASHBOARD");
        System.out.println("==============================");

        System.out.println(
                "Task and task-update operations will "
                        + "be available in the next module."
        );

        System.out.println();

        System.out.println("Logged out.");
    }
}