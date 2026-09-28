package com.ptms.app.service;

import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;

import java.util.List;

public interface TicketService {

    boolean createTicket(Ticket ticket, User loggedInUser);

    Ticket getTicketById(int id, User loggedInUser);

    List<Ticket> getAllTickets(User loggedInUser);

    boolean updateTicket(Ticket ticket, User loggedInUser);

    boolean deleteTicket(int id, User loggedInUser);

    boolean assignTicket(
            int ticketId,
            int userId,
            User loggedInUser
    );

    List<Ticket> getTicketsByProject(
            int projectId,
            User loggedInUser
    );

    List<Ticket> getTicketsByStatus(
            String status,
            User loggedInUser
    );

    List<Ticket> getTicketsByPriority(
            String priority,
            User loggedInUser
    );

    List<Ticket> searchTickets(
            String title,
            User loggedInUser
    );
}