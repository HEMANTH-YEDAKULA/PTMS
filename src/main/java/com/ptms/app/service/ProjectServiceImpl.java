package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.dao.ProjectDAOImpl;
import com.ptms.app.model.Project;

import java.math.BigDecimal;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;

    public ProjectServiceImpl() {
        this.projectDAO = new ProjectDAOImpl();
    }
    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public boolean createProject(Project project) {

        validateProject(project);

        return projectDAO.create(project);
    }



    @Override
    public Project getProjectById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than 0"
            );
        }

        return projectDAO.findById(id);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectDAO.findAll();
    }

    @Override
    public boolean updateProject(Project project) {

        if (project.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than 0"
            );
        }

        validateProject(project);

        if (!projectDAO.existsById(project.getId())) {
            return false;
        }

        return projectDAO.update(project);
    }

    @Override
    public boolean deleteProject(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be greater than 0"
            );
        }

        return projectDAO.deleteById(id);
    }

    @Override
    public List<Project> getProjectsByDomain(String domain) {

        if (domain == null || domain.isBlank()) {
            throw new IllegalArgumentException(
                    "Domain cannot be empty"
            );
        }

        return projectDAO.findByDomain(domain);
    }



    private void validateProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException(
                    "Project cannot be null"
            );
        }

        if (project.getName() == null ||
                project.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Project name cannot be empty"
            );
        }



        if (project.getDomain() == null ||
                project.getDomain().isBlank()) {

            throw new IllegalArgumentException(
                    "Project domain cannot be empty"
            );
        }

        if (project.getCost() == null ||
                project.getCost().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Project cost cannot be negative"
            );
        }

        if (project.getTeamSize() < 0) {
            throw new IllegalArgumentException(
                    "Team size cannot be negative"
            );
        }

        if (project.getStartDate() != null
                && project.getDeadline() != null
                && project.getDeadline().isBefore(project.getStartDate())) {

            throw new IllegalArgumentException(
                    "Deadline cannot be before start date"
            );
        }
    }
}