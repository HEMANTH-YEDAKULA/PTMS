package com.ptms.app.service;

import com.ptms.app.model.User;
import com.ptms.app.dao.UserDAO;
import com.ptms.app.dao.UserDAOImpl;
public class AuthServiceImpl implements AuthService {
    @Override
    public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required"
            );
        }
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.findUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return user;
    }
}
