package com.ptms.app.service;

import com.ptms.app.model.User;

import java.util.List;

public interface UserService {

    boolean createUser(User user, User loggedInUser);

    User getUserById(int id, User loggedInUser);

    List<User> getAllUsers(User loggedInUser);

    boolean updateUser(User user, User loggedInUser);

    boolean deleteUser(int id, User loggedInUser);

    List<User> getUsersByRole(String roleName, User loggedInUser);
}