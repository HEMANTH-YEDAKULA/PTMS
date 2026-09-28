package com.ptms.app.controller;

import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;
import com.ptms.app.service.TicketService;
import com.ptms.app.service.TicketServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TicketController {

    private final Scanner scanner;
    private final TicketService ticketService;

    public TicketController(Scanner scanner) {
        this.scanner = scanner;
        this.ticketService = new TicketServiceImpl();
    }

    public void showMenu(User loggedInUser) {

        while (true) {

            System.out.println("\n===== TICKET MANAGEMENT =====");
            System.out.println("1. Create Ticket");
            System.out.println("2. View Ticket");
            System.out.println("3. View All Tickets");
            System.out.println("4. Update Ticket");
            System.out.println("5. Assign Ticket");
            System.out.println("6. Delete Ticket");
            System.out.println("7. Search Ticket");
            System.out.println("8. Filter by Status");
            System.out.println("9. Filter by Priority");
            System.out.println("10. View Project Tickets");
            System.out.println("0. Back");

            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1" -> createTicket(loggedInUser);

                    case "2" -> viewTicket(loggedInUser);

                    case "3" -> viewAllTickets(loggedInUser);

                    case "4" -> updateTicket(loggedInUser);

                    case "5" -> assignTicket(loggedInUser);

                    case "6" -> deleteTicket(loggedInUser);

                    case "7" -> searchTicket(loggedInUser);

                    case "8" -> filterStatus(loggedInUser);

                    case "9" -> filterPriority(loggedInUser);

                    case "10" -> viewProjectTickets(loggedInUser);

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

    private void createTicket(User loggedInUser) {

        System.out.print("Project ID: ");
        int projectId =
                Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Priority (LOW/MEDIUM/HIGH): ");
        String priority =
                scanner.nextLine().trim().toUpperCase();

        System.out.print("Deadline (YYYY-MM-DD, blank for none): ");
        String deadlineInput = scanner.nextLine().trim();

        LocalDate deadline = deadlineInput.isBlank()
                ? null
                : LocalDate.parse(deadlineInput);

        System.out.print(
                "Status (TODO/IN_PROGRESS/COMPLETED/BLOCKED): "
        );
        String status =
                scanner.nextLine().trim().toUpperCase();

        Ticket ticket = new Ticket(
                projectId,
                title,
                description,
                priority,
                deadline,
                null,
                status
        );

        boolean created =
                ticketService.createTicket(
                        ticket,
                        loggedInUser
                );

        if (created) {
            System.out.println(
                    "Ticket created successfully. ID: "
                            + ticket.getId()
            );
        } else {
            System.out.println("Failed to create ticket.");
        }
    }

    private void viewTicket(User loggedInUser) {

        System.out.print("Ticket ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Ticket ticket =
                ticketService.getTicketById(
                        id,
                        loggedInUser
                );

        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        printTicket(ticket);
    }

    private void viewAllTickets(User loggedInUser) {

        List<Ticket> tickets =
                ticketService.getAllTickets(loggedInUser);

        printTickets(tickets);
    }

    private void updateTicket(User loggedInUser) {

        System.out.print("Ticket ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Ticket ticket =
                ticketService.getTicketById(
                        id,
                        loggedInUser
                );

        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        System.out.print("Title: ");
        ticket.setTitle(scanner.nextLine().trim());

        System.out.print("Description: ");
        ticket.setDescription(scanner.nextLine().trim());

        System.out.print("Priority: ");
        ticket.setPriority(
                scanner.nextLine().trim().toUpperCase()
        );

        System.out.print("Deadline (YYYY-MM-DD, blank for none): ");
        String deadlineInput = scanner.nextLine().trim();

        ticket.setDeadline(
                deadlineInput.isBlank()
                        ? null
                        : LocalDate.parse(deadlineInput)
        );

        System.out.print("Status: ");
        ticket.setStatus(
                scanner.nextLine().trim().toUpperCase()
        );

        boolean updated =
                ticketService.updateTicket(
                        ticket,
                        loggedInUser
                );

        System.out.println(
                updated
                        ? "Ticket updated successfully."
                        : "Failed to update ticket."
        );
    }

    private void assignTicket(User loggedInUser) {

        System.out.print("Ticket ID: ");
        int ticketId =
                Integer.parseInt(scanner.nextLine().trim());

        System.out.print("User ID: ");
        int userId =
                Integer.parseInt(scanner.nextLine().trim());

        boolean assigned =
                ticketService.assignTicket(
                        ticketId,
                        userId,
                        loggedInUser
                );

        System.out.println(
                assigned
                        ? "Ticket assigned successfully."
                        : "Failed to assign ticket."
        );
    }

    private void deleteTicket(User loggedInUser) {

        System.out.print("Ticket ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        boolean deleted =
                ticketService.deleteTicket(
                        id,
                        loggedInUser
                );

        System.out.println(
                deleted
                        ? "Ticket deleted successfully."
                        : "Ticket not found."
        );
    }

    private void searchTicket(User loggedInUser) {

        System.out.print("Enter title keyword: ");
        String title = scanner.nextLine().trim();

        List<Ticket> tickets =
                ticketService.searchTickets(
                        title,
                        loggedInUser
                );

        printTickets(tickets);
    }

    private void filterStatus(User loggedInUser) {

        System.out.print("Status: ");
        String status =
                scanner.nextLine().trim().toUpperCase();

        List<Ticket> tickets =
                ticketService.getTicketsByStatus(
                        status,
                        loggedInUser
                );

        printTickets(tickets);
    }

    private void filterPriority(User loggedInUser) {

        System.out.print("Priority: ");
        String priority =
                scanner.nextLine().trim().toUpperCase();

        List<Ticket> tickets =
                ticketService.getTicketsByPriority(
                        priority,
                        loggedInUser
                );

        printTickets(tickets);
    }

    private void viewProjectTickets(User loggedInUser) {

        System.out.print("Project ID: ");
        int projectId =
                Integer.parseInt(scanner.nextLine().trim());

        List<Ticket> tickets =
                ticketService.getTicketsByProject(
                        projectId,
                        loggedInUser
                );

        printTickets(tickets);
    }

    private void printTickets(List<Ticket> tickets) {

        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            return;
        }

        System.out.println("\n===== TICKETS =====");

        for (Ticket ticket : tickets) {
            printTicket(ticket);
        }
    }

    private void printTicket(Ticket ticket) {

        System.out.println("----------------------------");
        System.out.println("ID          : " + ticket.getId());
        System.out.println("Project ID  : " + ticket.getProjectId());
        System.out.println("Title       : " + ticket.getTitle());
        System.out.println("Description : " + ticket.getDescription());
        System.out.println("Priority    : " + ticket.getPriority());
        System.out.println("Deadline    : " + ticket.getDeadline());
        System.out.println("Assigned To : " + ticket.getAssignedTo());
        System.out.println("Status      : " + ticket.getStatus());
        System.out.println("Created At  : " + ticket.getCreatedAt());
    }
}