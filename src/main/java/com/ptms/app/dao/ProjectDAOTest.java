package com.ptms.app.dao;

import com.ptms.app.model.Project;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectDAOTest {

    public static void main(String[] args) {

        ProjectDAO projectDAO = new ProjectDAOImpl();

        Project project = new Project(
                "PTMS Development",
                "Project Tracking Management System",
                1,
                null,
                null,
                "SOFTWARE",
                new BigDecimal("500000.00"),
                5,
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "HIGH",
                "PLANNED"
        );

        boolean created = projectDAO.create(project);

        System.out.println("Created: " + created);
        System.out.println("Generated ID: " + project.getId());

        Project found = projectDAO.findById(project.getId());

        System.out.println("Found: " + found);

        System.out.println(
                "Exists: " + projectDAO.existsById(project.getId())
        );

        System.out.println("\nAll Projects:");

        projectDAO.findAll()
                .forEach(System.out::println);
    }
}