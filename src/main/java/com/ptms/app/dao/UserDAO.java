package com.ptms.app.dao;
import com.ptms.app.model.User;
public interface UserDAO {
     User findUserByEmail(String email);
}
