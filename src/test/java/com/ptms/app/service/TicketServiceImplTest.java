package com.ptms.app.service;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.dao.TicketDAO;
import com.ptms.app.model.Ticket;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketDAO ticketDAO;
    private ProjectMemberDAO projectMemberDAO;
    private TicketServiceImpl service;

    private User teamLead;

    @BeforeEach
    void setUp() {

        ticketDAO = mock(TicketDAO.class);

        projectMemberDAO =
                mock(ProjectMemberDAO.class);

        service =
                new TicketServiceImpl(
                        ticketDAO,
                        projectMemberDAO
                );

        teamLead = new User();
        teamLead.setId(4);
        teamLead.setRoleName("TEAM_LEAD");
    }

    @Test
    void shouldCreateTicket() {

        Ticket ticket = new Ticket();

        ticket.setProjectId(3);
        ticket.setTitle("Payment API");
        ticket.setDescription(
                "Implement payment API"
        );
        ticket.setPriority("HIGH");
        ticket.setStatus("TODO");

        when(ticketDAO.create(ticket))
                .thenReturn(true);

        boolean result =
                service.createTicket(
                        ticket,
                        teamLead
                );

        assertTrue(result);

        verify(ticketDAO)
                .create(ticket);
    }

    @Test
    void shouldRejectEmptyTitle() {

        Ticket ticket = new Ticket();

        ticket.setProjectId(3);
        ticket.setTitle("");
        ticket.setPriority("HIGH");
        ticket.setStatus("TODO");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createTicket(
                        ticket,
                        teamLead
                )
        );

        verify(
                ticketDAO,
                never()
        ).create(any());
    }
}