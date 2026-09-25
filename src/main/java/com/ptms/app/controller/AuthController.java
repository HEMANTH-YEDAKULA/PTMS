package com.ptms.app.controller;

import com.ptms.app.model.User;
import com.ptms.app.service.AuthService;
import com.ptms.app.service.AuthServiceImpl;

import java.util.Scanner;

public class AuthController {

    private final Scanner scanner;
    private final AuthService authService;

    public AuthController(Scanner scanner) {
        this.scanner = scanner;
        this.authService = new AuthServiceImpl();
    }

    public User login() {

        System.out.println("\n--- Login ---");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        return authService.login(email, password);
    }
}