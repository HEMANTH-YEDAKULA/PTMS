package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private ProjectServiceImpl projectService;

    private User projectManager;

    @BeforeEach
    void setUp() {

        projectService = new ProjectServiceImpl(projectDAO);

        projectManager = new User();

        projectManager.setId(1);
        projectManager.setFirstName("Test");
        projectManager.setLastName("Manager");
        projectManager.setUsername("test.manager");
        projectManager.setEmail("test.manager@ptms.com");
        projectManager.setPassword("TEMP_PASSWORD");
        projectManager.setRoleName("PROJECT_MANAGER");
    }

    @Test
    void createProject_validProject_callsDao() {

        Project project = new Project(
                0,
                "PTMS Development",
                "Project Tracking Management System",
                1,
                null,
                null,
                "SOFTWARE",
                new BigDecimal("500000.00"),
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "HIGH",
                "PLANNED"
        );

        when(projectDAO.create(project)).thenReturn(true);

        boolean result =
                projectService.createProject(project, projectManager);

        assertTrue(result);

        verify(projectDAO).create(project);
    }

    @Test
    void createProject_invalidDates_doesNotCallDao() {

        Project project = new Project(
                0,
                "PTMS Development",
                "Project Tracking Management System",
                1,
                null,
                null,
                "SOFTWARE",
                new BigDecimal("500000.00"),
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 5),
                "HIGH",
                "PLANNED"
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> projectService.createProject(
                                project,
                                projectManager
                        )
                );

        assertEquals(
                "Deadline cannot be before start date",
                exception.getMessage()
        );

        verifyNoInteractions(projectDAO);
    }

    @Test
    void createProject_unauthorizedUser_doesNotCallDao() {

        User employee = new User();

        employee.setId(2);
        employee.setFirstName("Test");
        employee.setLastName("Employee");
        employee.setUsername("test.employee");
        employee.setEmail("test.employee@ptms.com");
        employee.setPassword("TEMP_PASSWORD");
        employee.setRoleName("EMPLOYEE");

        Project project = new Project(
                0,
                "PTMS Development",
                "Project Tracking Management System",
                1,
                null,
                null,
                "SOFTWARE",
                new BigDecimal("500000.00"),
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "HIGH",
                "PLANNED"
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> projectService.createProject(
                                project,
                                employee
                        )
                );

        assertEquals(
                "Only ADMIN or PROJECT_MANAGER can manage projects",
                exception.getMessage()
        );

        verifyNoInteractions(projectDAO);
    }
}