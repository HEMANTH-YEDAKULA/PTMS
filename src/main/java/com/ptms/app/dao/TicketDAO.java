package com.ptms.app.dao;

import com.ptms.app.model.Ticket;

import java.util.List;

public interface TicketDAO {

    boolean create(Ticket ticket);

    Ticket findById(int id);

    List<Ticket> findAll();

    boolean update(Ticket ticket);

    boolean delete(int id);

    boolean assignTicket(int ticketId, int userId);

    List<Ticket> findByProject(int projectId);

    List<Ticket> findByStatus(String status);

    List<Ticket> findByPriority(String priority);

    List<Ticket> findByTitle(String title);
}