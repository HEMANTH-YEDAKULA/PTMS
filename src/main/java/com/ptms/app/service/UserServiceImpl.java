package com.ptms.app.service;

import com.ptms.app.dao.UserDAO;
import com.ptms.app.dao.UserDAOImpl;
import com.ptms.app.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean createUser(User user, User loggedInUser) {

        checkAdminAccess(loggedInUser);
        validateUser(user);

        return userDAO.create(user);
    }

    @Override
    public User getUserById(int id, User loggedInUser) {

        checkAdminAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return userDAO.findById(id);
    }

    @Override
    public List<User> getAllUsers(User loggedInUser) {

        checkAdminAccess(loggedInUser);

        return userDAO.findAll();
    }

    @Override
    public boolean updateUser(User user, User loggedInUser) {

        checkAdminAccess(loggedInUser);
        validateUser(user);

        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int id, User loggedInUser) {

        checkAdminAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (id == loggedInUser.getId()) {
            throw new IllegalArgumentException(
                    "Admin cannot delete the currently logged-in user"
            );
        }

        return userDAO.delete(id);
    }

    @Override
    public List<User> getUsersByRole(
            String roleName,
            User loggedInUser) {

        checkAdminAccess(loggedInUser);

        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }

        return userDAO.findByRole(roleName);
    }

    private void checkAdminAccess(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (!"ADMIN".equals(user.getRoleName())) {
            throw new IllegalArgumentException(
                    "Only ADMIN can manage users"
            );
        }
    }

    private void validateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getFirstName() == null ||
                user.getFirstName().isBlank()) {
            throw new IllegalArgumentException(
                    "First name is required"
            );
        }

        if (user.getLastName() == null ||
                user.getLastName().isBlank()) {
            throw new IllegalArgumentException(
                    "Last name is required"
            );
        }

        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        if (user.getRoleName() == null ||
                user.getRoleName().isBlank()) {
            throw new IllegalArgumentException(
                    "Role is required"
            );
        }
    }
}