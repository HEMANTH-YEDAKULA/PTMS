package com.ptms.app.dao;

import com.ptms.app.model.TicketTracking;

import java.util.List;

public interface TicketTrackingDAO {

    boolean create(TicketTracking tracking);

    List<TicketTracking> findByTicket(int ticketId);

    List<TicketTracking> findByUser(int userId);

    TicketTracking findLatestByTicket(int ticketId);
}