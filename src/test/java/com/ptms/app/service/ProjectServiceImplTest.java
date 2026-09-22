package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectDAO projectDAO;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_validProject_callsDao() {

        Project project = createValidProject();

        when(projectDAO.create(project)).thenReturn(true);

        boolean result = projectService.createProject(project);

        assertTrue(result);

        verify(projectDAO).create(project);
    }

    @Test
    void createProject_invalidDates_doesNotCallDao() {

        Project project = createValidProject();

        project.setStartDate(LocalDate.of(2026, 12, 31));
        project.setDeadline(LocalDate.of(2026, 1, 1));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> projectService.createProject(project)
        );

        assertEquals(
                "Deadline cannot be before start date",
                exception.getMessage()
        );

        verifyNoInteractions(projectDAO);
    }

    private Project createValidProject() {

        Project project = new Project();

        project.setName("Online Payment App");
        project.setRequirements("Server");
        project.setManagerId(1);
        project.setDomain("FINANCE");
        project.setCost(new BigDecimal("4500000"));
        project.setTeamSize(3);
        project.setStartDate(LocalDate.of(2026, 1, 1));
        project.setDeadline(LocalDate.of(2026, 12, 31));
        project.setPriority("MEDIUM");
        project.setStatus("PLANNED");

        return project;
    }
}