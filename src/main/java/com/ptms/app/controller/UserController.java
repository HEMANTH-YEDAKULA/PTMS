package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.UserService;
import com.ptms.app.service.UserServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UserController {

    private final Scanner scanner;
    private final UserService userService;

    public UserController(Scanner scanner) {
        this.scanner = scanner;
        this.userService = new UserServiceImpl();
    }

    public void createUser(User loggedInUser) {

        System.out.println("\n--- Create User ---");

        try {
            User user = new User();

            System.out.print("First name: ");
            user.setFirstName(scanner.nextLine());

            System.out.print("Last name: ");
            user.setLastName(scanner.nextLine());

            System.out.print("Username: ");
            user.setUsername(scanner.nextLine());

            System.out.print("Email: ");
            user.setEmail(scanner.nextLine());

            System.out.print("Password: ");
            user.setPassword(scanner.nextLine());

            System.out.print(
                    "Role (ADMIN/PROJECT_MANAGER/TEAM_LEAD/EMPLOYEE): "
            );
            user.setRoleName(scanner.nextLine().toUpperCase());

            System.out.print("Date of birth (YYYY-MM-DD, optional): ");
            String dob = scanner.nextLine();

            if (!dob.isBlank()) {
                user.setDateOfBirth(LocalDate.parse(dob));
            }

            System.out.print("Mobile number: ");
            user.setMobileNumber(scanner.nextLine());

            System.out.print("Gender: ");
            user.setGender(scanner.nextLine());

            boolean created =
                    userService.createUser(user, loggedInUser);

            System.out.println(
                    created
                            ? "User created successfully."
                            : "Failed to create user."
            );

        } catch (Exception e) {
            System.out.println(
                    "Unable to create user: " + e.getMessage()
            );
        }
    }

    public void viewUser(User loggedInUser) {

        System.out.print("Enter user ID: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());

            User user = userService.getUserById(id, loggedInUser);

            if (user == null) {
                System.out.println("User not found.");
            } else {
                System.out.println(user);
            }

        } catch (Exception e) {
            System.out.println(
                    "Unable to view user: " + e.getMessage()
            );
        }
    }

    public void viewAllUsers(User loggedInUser) {

        try {
            List<User> users =
                    userService.getAllUsers(loggedInUser);

            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            for (User user : users) {
                System.out.println(user);
            }

        } catch (Exception e) {
            System.out.println(
                    "Unable to fetch users: " + e.getMessage()
            );
        }
    }

    public void updateUser(User loggedInUser) {

        System.out.print("Enter user ID to update: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());

            User user =
                    userService.getUserById(id, loggedInUser);

            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.println("Current user: " + user);

            System.out.print("First name: ");
            user.setFirstName(scanner.nextLine());

            System.out.print("Last name: ");
            user.setLastName(scanner.nextLine());

            System.out.print("Username: ");
            user.setUsername(scanner.nextLine());

            System.out.print("Email: ");
            user.setEmail(scanner.nextLine());

            System.out.print("Password: ");
            user.setPassword(scanner.nextLine());

            System.out.print(
                    "Role (ADMIN/PROJECT_MANAGER/TEAM_LEAD/EMPLOYEE): "
            );
            user.setRoleName(scanner.nextLine().toUpperCase());

            System.out.print("Date of birth (YYYY-MM-DD, optional): ");
            String dob = scanner.nextLine();

            if (!dob.isBlank()) {
                user.setDateOfBirth(LocalDate.parse(dob));
            }

            System.out.print("Mobile number: ");
            user.setMobileNumber(scanner.nextLine());

            System.out.print("Gender: ");
            user.setGender(scanner.nextLine());

            boolean updated =
                    userService.updateUser(user, loggedInUser);

            System.out.println(
                    updated
                            ? "User updated successfully."
                            : "Failed to update user."
            );

        } catch (Exception e) {
            System.out.println(
                    "Unable to update user: " + e.getMessage()
            );
        }
    }

    public void deleteUser(User loggedInUser) {

        System.out.print("Enter user ID to delete: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());

            boolean deleted =
                    userService.deleteUser(id, loggedInUser);

            System.out.println(
                    deleted
                            ? "User deleted successfully."
                            : "User not found."
            );

        } catch (Exception e) {
            System.out.println(
                    "Unable to delete user: " + e.getMessage()
            );
        }
    }

    public void findUsersByRole(User loggedInUser) {

        System.out.print("Enter role: ");

        try {
            String role = scanner.nextLine().toUpperCase();

            List<User> users =
                    userService.getUsersByRole(
                            role,
                            loggedInUser
                    );

            if (users.isEmpty()) {
                System.out.println("No users found for this role.");
                return;
            }

            for (User user : users) {
                System.out.println(user);
            }

        } catch (Exception e) {
            System.out.println(
                    "Unable to find users: " + e.getMessage()
            );
        }
    }
}