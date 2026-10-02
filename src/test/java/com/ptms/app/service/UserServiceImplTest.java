package com.ptms.app.service;

import com.ptms.app.dao.UserDAO;
import com.ptms.app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserDAO userDAO;
    private UserServiceImpl userService;

    private User admin;

    @BeforeEach
    void setUp() {

        userDAO = mock(UserDAO.class);

        userService =
                new UserServiceImpl(userDAO);

        admin = new User();
        admin.setId(1);
        admin.setRoleName("ADMIN");
    }

    @Test
    void shouldCreateUserAsAdmin() {

        User user = new User();

        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("testuser");
        user.setEmail("test@ptms.com");
        user.setPassword("test123");
        user.setRoleName("EMPLOYEE");

        when(userDAO.create(user))
                .thenReturn(true);

        boolean result =
                userService.createUser(
                        user,
                        admin
                );

        assertTrue(result);

        verify(userDAO)
                .create(user);
    }

    @Test
    void shouldRejectNonAdminUser() {

        User employee = new User();

        employee.setId(7);
        employee.setRoleName("EMPLOYEE");

        User user = new User();

        user.setEmail("test@ptms.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(
                        user,
                        employee
                )
        );

        verify(
                userDAO,
                never()
        ).create(any());
    }
}