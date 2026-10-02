package com.ptms.app.service;

import com.ptms.app.dao.TicketDAO;
import com.ptms.app.dao.TicketTrackingDAO;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.TicketTracking;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketTrackingServiceImplTest {

    private TicketTrackingDAO trackingDAO;
    private TicketDAO ticketDAO;
    private TicketTrackingServiceImpl service;

    private User employee;

    @BeforeEach
    void setUp() {

        trackingDAO = mock(TicketTrackingDAO.class);
        ticketDAO = mock(TicketDAO.class);

        service = new TicketTrackingServiceImpl(
                trackingDAO,
                ticketDAO
        );

        employee = new User();
        employee.setId(7);
        employee.setRoleName("EMPLOYEE");
    }

    @Test
    void shouldUpdateAssignedTicket() {

        Ticket ticket = new Ticket();
        ticket.setId(2);
        ticket.setAssignedTo(7);

        when(ticketDAO.findById(2))
                .thenReturn(ticket);

        TicketTracking tracking = new TicketTracking();
        tracking.setTicketId(2);
        tracking.setStatus("IN_PROGRESS");
        tracking.setProgress(50);
        tracking.setComment(
                "Development is partially completed."
        );

        when(trackingDAO.create(tracking))
                .thenReturn(true);

        boolean result = service.updateTicket(
                tracking,
                employee
        );

        assertTrue(result);

        assertEquals(
                7,
                tracking.getUpdatedBy()
        );

        verify(trackingDAO)
                .create(tracking);
    }

    @Test
    void shouldRejectUnassignedTicket() {

        Ticket ticket = new Ticket();
        ticket.setId(2);
        ticket.setAssignedTo(4);

        when(ticketDAO.findById(2))
                .thenReturn(ticket);

        TicketTracking tracking = new TicketTracking();
        tracking.setTicketId(2);
        tracking.setStatus("IN_PROGRESS");
        tracking.setProgress(50);

        assertThrows(
                SecurityException.class,
                () -> service.updateTicket(
                        tracking,
                        employee
                )
        );

        verify(
                trackingDAO,
                never()
        ).create(any());
    }

    @Test
    void shouldRejectInvalidProgress() {

        TicketTracking tracking = new TicketTracking();
        tracking.setTicketId(2);
        tracking.setStatus("IN_PROGRESS");
        tracking.setProgress(120);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTicket(
                        tracking,
                        employee
                )
        );

        verify(
                trackingDAO,
                never()
        ).create(any());
    }

    @Test
    void shouldRejectCompletedTicketBelow100Percent() {

        Ticket ticket = new Ticket();
        ticket.setId(2);
        ticket.setAssignedTo(7);

        when(ticketDAO.findById(2))
                .thenReturn(ticket);

        TicketTracking tracking = new TicketTracking();
        tracking.setTicketId(2);
        tracking.setStatus("COMPLETED");
        tracking.setProgress(80);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTicket(
                        tracking,
                        employee
                )
        );

        verify(
                trackingDAO,
                never()
        ).create(any());
    }
}