package com.ptms.app.service;

import com.ptms.app.dao.ProjectDAO;
import com.ptms.app.dao.ProjectDAOImpl;
import com.ptms.app.model.Project;
import com.ptms.app.model.User;

import java.util.List;
import java.util.logging.Logger;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectDAO projectDAO;

    private static final Logger LOGGER =
            Logger.getLogger(ProjectServiceImpl.class.getName());

    public ProjectServiceImpl() {
        this.projectDAO = new ProjectDAOImpl();
    }

    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public boolean createProject(
            Project project,
            User loggedInUser) {

        validateProject(project);
        checkProjectManagementAccess(loggedInUser);

        if ("PROJECT_MANAGER".equals(
                loggedInUser.getRoleName())) {

            project.setManagerId(
                    loggedInUser.getId()
            );
        }

        LOGGER.info(
                "Creating project: "
                        + project.getName()
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result = projectDAO.create(project);

        if (result) {
            LOGGER.info(
                    "Project created successfully: "
                            + project.getName()
            );
        } else {
            LOGGER.warning(
                    "Project creation failed: "
                            + project.getName()
            );
        }

        return result;
    }

    @Override
    public Project getProjectById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be positive"
            );
        }

        LOGGER.info(
                "Fetching project ID: " + id
        );

        return projectDAO.findById(id);
    }

    @Override
    public List<Project> getAllProjects() {

        LOGGER.info("Fetching all projects");

        return projectDAO.findAll();
    }

    @Override
    public boolean updateProject(
            Project project,
            User loggedInUser) {

        validateProject(project);
        checkProjectManagementAccess(loggedInUser);

        LOGGER.info(
                "Updating project ID: "
                        + project.getId()
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result = projectDAO.update(project);

        if (result) {
            LOGGER.info(
                    "Project updated successfully: "
                            + project.getId()
            );
        } else {
            LOGGER.warning(
                    "Project update failed: "
                            + project.getId()
            );
        }

        return result;
    }

    @Override
    public boolean deleteProject(
            int id,
            User loggedInUser) {

        checkAdminAccess(loggedInUser);

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Project ID must be positive"
            );
        }

        LOGGER.info(
                "Deleting project ID: "
                        + id
                        + " by admin ID: "
                        + loggedInUser.getId()
        );

        boolean result = projectDAO.delete(id);

        if (result) {
            LOGGER.info(
                    "Project deleted successfully: " + id
            );
        } else {
            LOGGER.warning(
                    "Project deletion failed: " + id
            );
        }

        return result;
    }

    @Override
    public List<Project> getProjectsByDomain(
            String domain) {

        if (domain == null
                || domain.isBlank()) {

            throw new IllegalArgumentException(
                    "Domain is required"
            );
        }

        LOGGER.info(
                "Searching projects by domain: "
                        + domain
        );

        return projectDAO.findByDomain(domain);
    }

    private void checkProjectManagementAccess(
            User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must be logged in"
            );
        }

        String role = user.getRoleName();

        if (!"ADMIN".equals(role)
                && !"PROJECT_MANAGER".equals(role)) {

            throw new IllegalArgumentException(
                    "Only ADMIN or PROJECT_MANAGER "
                            + "can manage projects"
            );
        }
    }

    private void checkAdminAccess(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must be logged in"
            );
        }

        if (!"ADMIN".equals(
                user.getRoleName())) {

            throw new IllegalArgumentException(
                    "Only ADMIN can delete projects"
            );
        }
    }

    private void validateProject(
            Project project) {

        if (project == null) {
            throw new IllegalArgumentException(
                    "Project cannot be null"
            );
        }

        if (project.getName() == null
                || project.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Project name is required"
            );
        }

        if (project.getDomain() == null
                || project.getDomain().isBlank()) {

            throw new IllegalArgumentException(
                    "Project domain is required"
            );
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
                .isBefore(
                        project.getStartDate()
                )) {

            throw new IllegalArgumentException(
                    "Deadline cannot be before "
                            + "start date"
            );
        }
    }
}