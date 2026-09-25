package com.ptms.app.dao;
import com.ptms.app.model.User;
public interface UserDAO {
    User findByEmail(String email);

    User findUserByEmail(String email);
}
