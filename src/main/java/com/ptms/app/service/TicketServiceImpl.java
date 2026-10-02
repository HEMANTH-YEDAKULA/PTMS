package com.ptms.app.service;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.dao.ProjectMemberDAOImpl;
import com.ptms.app.dao.TicketDAO;
import com.ptms.app.dao.TicketDAOImpl;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;
import java.util.logging.Logger;
import java.util.List;

public class TicketServiceImpl implements TicketService {

    private final TicketDAO ticketDAO;
    private final ProjectMemberDAO projectMemberDAO;
    private static final Logger LOGGER=Logger.getLogger(TicketServiceImpl.class.getName());
    public TicketServiceImpl() {
        this.ticketDAO = new TicketDAOImpl();
        this.projectMemberDAO = new ProjectMemberDAOImpl();
    }

    // Constructor injection for Mockito testing
    public TicketServiceImpl(
            TicketDAO ticketDAO,
            ProjectMemberDAO projectMemberDAO) {

        this.ticketDAO = ticketDAO;
        this.projectMemberDAO = projectMemberDAO;
    }

    @Override
    public boolean createTicket(
            Ticket ticket,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        validateTicket(ticket);

        LOGGER.info(
                "Creating ticket: "
                        + ticket.getTitle()
                        + " for project ID: "
                        + ticket.getProjectId()
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result = ticketDAO.create(ticket);

        if (result) {
            LOGGER.info(
                    "Ticket created successfully."
            );
        } else {
            LOGGER.warning(
                    "Ticket creation failed."
            );
        }

        return result;
    }

    @Override
    public Ticket getTicketById(
            int id,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than 0"
            );
        }

        return ticketDAO.findById(id);
    }

    @Override
    public List<Ticket> getAllTickets(
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        return ticketDAO.findAll();
    }

    @Override
    public boolean updateTicket(
            Ticket ticket,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        validateTicket(ticket);

        if (ticket.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than 0"
            );
        }

        LOGGER.info(
                "Updating ticket ID: "
                        + ticket.getId()
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result = ticketDAO.update(ticket);

        if (result) {
            LOGGER.info(
                    "Ticket updated successfully: "
                            + ticket.getId()
            );
        } else {
            LOGGER.warning(
                    "Ticket update failed: "
                            + ticket.getId()
            );
        }

        return result;
    }

    @Override
    public boolean deleteTicket(
            int id,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than 0"
            );
        }

        LOGGER.info(
                "Deleting ticket ID: "
                        + id
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result = ticketDAO.delete(id);

        if (result) {
            LOGGER.info(
                    "Ticket deleted successfully."
            );
        } else {
            LOGGER.warning(
                    "Ticket deletion failed."
            );
        }

        return result;
    }

    @Override
    public boolean assignTicket(
            int ticketId,
            int userId,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (ticketId <= 0 || userId <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID and User ID must be greater than 0"
            );
        }

        Ticket ticket = ticketDAO.findById(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket not found"
            );
        }

        if (!projectMemberDAO.isMember(
                ticket.getProjectId(),
                userId)) {

            throw new IllegalArgumentException(
                    "User is not a member of this project"
            );
        }

        LOGGER.info(
                "Assigning ticket ID: "
                        + ticketId
                        + " to user ID: "
                        + userId
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result =
                ticketDAO.assignTicket(
                        ticketId,
                        userId
                );

        if (result) {
            LOGGER.info(
                    "Ticket assigned successfully."
            );
        } else {
            LOGGER.warning(
                    "Ticket assignment failed."
            );
        }

        return result;
    }

    @Override
    public List<Ticket> getTicketsByProject(
            int projectId,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than 0"
            );
        }

        return ticketDAO.findByProject(projectId);
    }

    @Override
    public List<Ticket> getTicketsByStatus(
            String status,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Status cannot be empty"
            );
        }

        return ticketDAO.findByStatus(status);
    }

    @Override
    public List<Ticket> getTicketsByPriority(
            String priority,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException(
                    "Priority cannot be empty"
            );
        }

        return ticketDAO.findByPriority(priority);
    }

    @Override
    public List<Ticket> searchTickets(
            String title,
            User loggedInUser) {

        checkTeamLeadAccess(loggedInUser);

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Search title cannot be empty"
            );
        }

        return ticketDAO.findByTitle(title);
    }

    private void checkTeamLeadAccess(User loggedInUser) {

        if (loggedInUser == null) {
            throw new SecurityException(
                    "User must be logged in"
            );
        }

        if (!"TEAM_LEAD".equalsIgnoreCase(
                loggedInUser.getRoleName())) {

            throw new SecurityException(
                    "Only Team Lead can manage tickets"
            );
        }
    }

    private void validateTicket(Ticket ticket) {

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket cannot be null"
            );
        }

        if (ticket.getProjectId() <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than 0"
            );
        }

        if (ticket.getTitle() == null
                || ticket.getTitle().isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket title cannot be empty"
            );
        }

        if (ticket.getPriority() == null
                || ticket.getPriority().isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket priority cannot be empty"
            );
        }

        if (ticket.getStatus() == null
                || ticket.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket status cannot be empty"
            );
        }

        if (ticket.getDeadline() != null
                && ticket.getDeadline().isBefore(
                java.time.LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Ticket deadline cannot be in the past"
            );
        }
    }
}