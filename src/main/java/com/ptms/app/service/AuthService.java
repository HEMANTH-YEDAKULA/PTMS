package com.ptms.app.service;

import com.ptms.app.model.User;

public interface AuthService {
    User login(String email, String password);
}
