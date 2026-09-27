package com.ptms.app.dao;

import com.ptms.app.model.User;

import java.util.List;

public interface UserDAO {


    User findByEmail(String email);


    boolean create(User user);

    User findById(int id);

    List<User> findAll();

    boolean update(User user);

    boolean delete(int id);

    List<User> findByRole(String roleName);
}