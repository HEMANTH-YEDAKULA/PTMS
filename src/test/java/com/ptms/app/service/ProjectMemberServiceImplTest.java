package com.ptms.app.service;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectMemberServiceImplTest {

    private ProjectMemberDAO projectMemberDAO;
    private ProjectMemberServiceImpl service;

    private User teamLead;

    @BeforeEach
    void setUp() {

        projectMemberDAO =
                mock(ProjectMemberDAO.class);

        service =
                new ProjectMemberServiceImpl(
                        projectMemberDAO
                );

        teamLead = new User();
        teamLead.setId(4);
        teamLead.setRoleName("TEAM_LEAD");
    }

    @Test
    void shouldAddMember() {

        when(
                projectMemberDAO.isMember(3, 7)
        ).thenReturn(false);

        when(
                projectMemberDAO.addMember(
                        3,
                        7,
                        "Developer"
                )
        ).thenReturn(true);

        boolean result =
                service.addMember(
                        3,
                        7,
                        "Developer",
                        teamLead
                );

        assertTrue(result);

        verify(projectMemberDAO)
                .addMember(
                        3,
                        7,
                        "Developer"
                );
    }

    @Test
    void shouldRejectDuplicateMember() {

        when(
                projectMemberDAO.isMember(3, 7)
        ).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addMember(
                        3,
                        7,
                        "Developer",
                        teamLead
                )
        );

        verify(
                projectMemberDAO,
                never()
        ).addMember(
                anyInt(),
                anyInt(),
                anyString()
        );
    }
}