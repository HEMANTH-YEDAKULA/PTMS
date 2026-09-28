package com.ptms.app.service;

import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;

import java.util.List;

public interface TicketTrackingService {

    boolean updateTicket(
            TicketTracking tracking,
            User loggedInUser);

    List<TicketTracking> getTicketHistory(
            int ticketId,
            User loggedInUser);

    List<TicketTracking> getMyUpdates(
            User loggedInUser);
}