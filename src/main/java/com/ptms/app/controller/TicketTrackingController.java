package com.ptms.app.controller;

import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import com.ptms.app.service.TicketTrackingService;
import com.ptms.app.service.TicketTrackingServiceImpl;

import java.util.List;
import java.util.Scanner;

public class TicketTrackingController {

    private final Scanner scanner;
    private final TicketTrackingService trackingService;

    public TicketTrackingController(Scanner scanner) {
        this.scanner = scanner;
        this.trackingService =
                new TicketTrackingServiceImpl();
    }

    public void showEmployeeMenu(User loggedInUser) {

        while (true) {

            System.out.println("\n===== EMPLOYEE TICKET TRACKING =====");
            System.out.println("1. Update My Ticket");
            System.out.println("2. View My Updates");
            System.out.println("3. View Ticket History");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice =
                    scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1" ->
                            updateTicket(loggedInUser);

                    case "2" ->
                            viewMyUpdates(loggedInUser);

                    case "3" ->
                            viewHistory(loggedInUser);

                    case "0" -> {
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice.");

                }

            } catch (RuntimeException e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage());
            }
        }
    }

    public void showTeamLeadMenu(User loggedInUser) {

        while (true) {

            System.out.println("\n===== TICKET TRACKING =====");
            System.out.println("1. View Ticket History");
            System.out.println("2. View My Updates");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice =
                    scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1" ->
                            viewHistory(loggedInUser);

                    case "2" ->
                            viewMyUpdates(loggedInUser);

//                    case "0" ->
//                             return;

                    default ->
                            System.out.println(
                                    "Invalid choice.");
                }

            } catch (RuntimeException e) {

                System.out.println(
                        "Operation failed: "
                                + e.getMessage());
            }
        }
    }

    private void updateTicket(User loggedInUser) {

        System.out.print("Ticket ID: ");
        int ticketId =
                Integer.parseInt(scanner.nextLine().trim());

        System.out.print(
                "Status (TODO/IN_PROGRESS/COMPLETED/BLOCKED): "
        );

        String status =
                scanner.nextLine().trim().toUpperCase();

        System.out.print("Progress (0-100): ");

        int progress =
                Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Comment: ");

        String comment =
                scanner.nextLine().trim();

        TicketTracking tracking =
                new TicketTracking(
                        ticketId,
                        status,
                        progress,
                        comment,
                        loggedInUser.getId()
                );

        boolean success =
                trackingService.updateTicket(
                        tracking,
                        loggedInUser
                );

        System.out.println(
                success
                        ? "Ticket update submitted successfully."
                        : "Failed to submit ticket update."
        );
    }

    private void viewMyUpdates(User loggedInUser) {

        List<TicketTracking> updates =
                trackingService.getMyUpdates(
                        loggedInUser
                );

        printTracking(updates);
    }

    private void viewHistory(User loggedInUser) {

        System.out.print("Ticket ID: ");

        int ticketId =
                Integer.parseInt(scanner.nextLine().trim());

        List<TicketTracking> history =
                trackingService.getTicketHistory(
                        ticketId,
                        loggedInUser
                );

        printTracking(history);
    }

    private void printTracking(
            List<TicketTracking> trackingList) {

        if (trackingList.isEmpty()) {

            System.out.println(
                    "No tracking records found.");

            return;
        }

        System.out.println(
                "\n========== TICKET TRACKING =========="
        );

        for (TicketTracking tracking : trackingList) {

            System.out.println(
                    "Tracking ID : "
                            + tracking.getId());

            System.out.println(
                    "Ticket ID   : "
                            + tracking.getTicketId());

            System.out.println(
                    "Status      : "
                            + tracking.getStatus());

            System.out.println(
                    "Progress    : "
                            + tracking.getProgress()
                            + "%");

            System.out.println(
                    "Comment     : "
                            + tracking.getComment());

            System.out.println(
                    "Updated By  : "
                            + tracking.getUpdatedBy());

            System.out.println(
                    "Updated At  : "
                            + tracking.getUpdatedAt());

            System.out.println(
                    "------------------------------------"
            );
        }
    }
}