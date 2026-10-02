package com.ptms.app.service;

import com.ptms.app.dao.TicketDAO;
import com.ptms.app.dao.TicketDAOImpl;
import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.dao.TicketTrackingDAOImpl;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import java.util.logging.Logger;
import java.util.List;

public class TicketTrackingServiceImpl
        implements TicketTrackingService {
    private static final Logger LOGGER =
            Logger.getLogger(
                    TicketTrackingServiceImpl.class.getName()
            );
    private final TicketTrackingDAO trackingDAO;
    private final TicketDAO ticketDAO;

    public TicketTrackingServiceImpl() {
        this.trackingDAO = new TicketTrackingDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
    }

    // Constructor injection for unit testing
    public TicketTrackingServiceImpl(
            TicketTrackingDAO trackingDAO,
            TicketDAO ticketDAO) {

        this.trackingDAO = trackingDAO;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public boolean updateTicket(
            TicketTracking tracking,
            User loggedInUser) {

        checkLogin(loggedInUser);

        if (tracking == null) {
            throw new IllegalArgumentException(
                    "Tracking cannot be null");
        }

        if (tracking.getTicketId() <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than 0");
        }

        if (tracking.getProgress() < 0
                || tracking.getProgress() > 100) {

            throw new IllegalArgumentException(
                    "Progress must be between 0 and 100");
        }

        if (tracking.getStatus() == null
                || tracking.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "Status cannot be empty");
        }

        Ticket ticket =
                ticketDAO.findById(tracking.getTicketId());

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket ID " + tracking.getTicketId()
                            + " does not exist");
        }

        String role = loggedInUser.getRoleName();

        if ("EMPLOYEE".equalsIgnoreCase(role)) {

            if (ticket.getAssignedTo() == null) {

                throw new SecurityException(
                        "This ticket is not assigned to an employee");
            }

            if (ticket.getAssignedTo()
                    != loggedInUser.getId()) {

                throw new SecurityException(
                        "You can update only tickets assigned to you");
            }
        }

        else if ("TEAM_LEAD".equalsIgnoreCase(role)) {
            // Team Lead can review/update ticket tracking.
        }

        else {
            throw new SecurityException(
                    "Only Employee or Team Lead can update ticket tracking");
        }

        if ("COMPLETED".equalsIgnoreCase(
                tracking.getStatus())
                && tracking.getProgress() != 100) {

            throw new IllegalArgumentException(
                    "Completed ticket must have 100% progress");
        }

        tracking.setUpdatedBy(loggedInUser.getId());

        LOGGER.info(
                "Updating tracking for ticket ID: "
                        + tracking.getTicketId()
                        + " by user ID: "
                        + loggedInUser.getId()
                        + ", progress: "
                        + tracking.getProgress()
                        + "%"
        );

        boolean result =
                trackingDAO.create(tracking);

        if (result) {
            LOGGER.info(
                    "Ticket tracking updated successfully."
            );
        } else {
            LOGGER.warning(
                    "Ticket tracking update failed."
            );
        }

        return result;
    }

    @Override
    public List<TicketTracking> getTicketHistory(
            int ticketId,
            User loggedInUser) {

        checkLogin(loggedInUser);

        if (ticketId <= 0) {
            throw new IllegalArgumentException(
                    "Ticket ID must be greater than 0");
        }

        Ticket ticket = ticketDAO.findById(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket ID " + ticketId + " does not exist");
        }

        if ("EMPLOYEE".equalsIgnoreCase(
                loggedInUser.getRoleName())) {

            if (ticket.getAssignedTo() == null
                    || ticket.getAssignedTo()
                    != loggedInUser.getId()) {

                throw new SecurityException(
                        "You can view history only for your assigned tickets");
            }
        }

        return trackingDAO.findByTicket(ticketId);
    }

    @Override
    public List<TicketTracking> getMyUpdates(
            User loggedInUser) {

        checkLogin(loggedInUser);

        return trackingDAO.findByUser(
                loggedInUser.getId()
        );
    }

    private void checkLogin(User user) {

        if (user == null) {
            throw new SecurityException(
                    "User must be logged in");
        }
    }
}