package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    private ProjectDAO projectDAO;
    private ProjectServiceImpl projectService;

    private User admin;
    private User manager;

    @BeforeEach
    void setUp() {

        projectDAO = mock(ProjectDAO.class);

        projectService =
                new ProjectServiceImpl(projectDAO);

        admin = new User();
        admin.setId(1);
        admin.setRoleName("ADMIN");

        manager = new User();
        manager.setId(2);
        manager.setRoleName("PROJECT_MANAGER");
    }

    private Project createValidProject() {

        Project project = new Project();

        project.setName("Test Project");
        project.setDomain("FINANCE");
        project.setCost(new java.math.BigDecimal("100000"));
        project.setStartDate(
                LocalDate.of(2026, 10, 1)
        );
        project.setDeadline(
                LocalDate.of(2026, 12, 31)
        );

        return project;
    }

    @Test
    void shouldCreateProject() {

        Project project = createValidProject();

        when(projectDAO.create(project))
                .thenReturn(true);

        boolean result =
                projectService.createProject(
                        project,
                        manager
                );

        assertTrue(result);

        verify(projectDAO)
                .create(project);
    }

    @Test
    void shouldRejectInvalidDates() {

        Project project = createValidProject();

        project.setStartDate(
                LocalDate.of(2026, 12, 31)
        );

        project.setDeadline(
                LocalDate.of(2026, 10, 1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.createProject(
                        project,
                        manager
                )
        );

        verify(
                projectDAO,
                never()
        ).create(any());
    }

    @Test
    void shouldRejectUnauthorizedUser() {

        Project project = createValidProject();

        User employee = new User();
        employee.setId(7);
        employee.setRoleName("EMPLOYEE");

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.createProject(
                        project,
                        employee
                )
        );

        verify(
                projectDAO,
                never()
        ).create(any());
    }

    @Test
    void name() {
    }

    @Test
    void shouldDeleteProjectAsAdmin() {

        when(projectDAO.delete(3))
                .thenReturn(true);

        boolean result =
                projectService.deleteProject(
                        3,
                        admin
                );

        assertTrue(result);

        verify(projectDAO)
                .delete(3);
    }
}