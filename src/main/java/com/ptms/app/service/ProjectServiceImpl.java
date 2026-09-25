package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.dao.ProjectDAOImpl;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;

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
    public boolean createProject(Project project, User loggedInUser) {

        validateProject(project);
        checkProjectManagementAccess(loggedInUser);

        if ("PROJECT_MANAGER".equals(loggedInUser.getRoleName())) {
            project.setManagerId(loggedInUser.getId());
        }

        return projectDAO.create(project);
    }

    @Override
    public Project getProjectById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Project ID must be positive");
        }

        return projectDAO.findById(id);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectDAO.findAll();
    }

    @Override
    public boolean updateProject(Project project, User loggedInUser) {

        validateProject(project);
        checkProjectManagementAccess(loggedInUser);

        return projectDAO.update(project);
    }

    @Override
    public boolean deleteProject(int id, User loggedInUser) {

        checkProjectManagementAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException("Project ID must be positive");
        }

        return projectDAO.delete(id);
    }

    @Override
    public List<Project> getProjectsByDomain(String domain) {

        if (domain == null || domain.isBlank()) {
            throw new IllegalArgumentException("Domain is required");
        }

        return projectDAO.findByDomain(domain);
    }

    private void checkProjectManagementAccess(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User must be logged in");
        }

        String role = user.getRoleName();

        if (!"ADMIN".equals(role)
                && !"PROJECT_MANAGER".equals(role)) {

            throw new IllegalArgumentException(
                    "Only ADMIN or PROJECT_MANAGER can manage projects"
            );
        }
    }

    private void validateProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        if (project.getName() == null
                || project.getName().isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }

        if (project.getDomain() == null
                || project.getDomain().isBlank()) {
            throw new IllegalArgumentException("Project domain is required");
        }

        if (project.getCost() == null
                || project.getCost().signum() < 0) {
            throw new IllegalArgumentException(
                    "Project cost cannot be negative"
            );
        }

        if (project.getStartDate() != null
                && project.getDeadline() != null
                && project.getDeadline()
                .isBefore(project.getStartDate())) {

            throw new IllegalArgumentException(
                    "Deadline cannot be before start date"
            );
        }
    }
}