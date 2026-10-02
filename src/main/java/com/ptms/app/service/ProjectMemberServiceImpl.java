package com.ptms.app.service;

import com.ptms.app.dao.ProjectMemberDAO;
import com.ptms.app.dao.ProjectMemberDAOImpl;
import com.ptms.app.model.User;
import java.util.logging.Logger;

import java.util.List;

public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberDAO projectMemberDAO;
    private static final Logger LOGGER=Logger.getLogger(ProjectMemberServiceImpl.class.getName());
    public ProjectMemberServiceImpl() {
        this.projectMemberDAO = new ProjectMemberDAOImpl();
    }

    // Constructor injection for unit testing with Mockito
    public ProjectMemberServiceImpl(ProjectMemberDAO projectMemberDAO) {
        this.projectMemberDAO = projectMemberDAO;
    }

    @Override
    public boolean addMember(int projectId,
                             int userId,
                             String roleInProject,
                             User loggedInUser) {

        checkAccess(loggedInUser);

        if (projectId <= 0) {
            throw new IllegalArgumentException("Project ID must be greater than 0");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than 0");
        }

        if (roleInProject == null || roleInProject.isBlank()) {
            throw new IllegalArgumentException("Project role cannot be empty");
        }

        if (projectMemberDAO.isMember(projectId, userId)) {
            throw new IllegalArgumentException(
                    "User is already a member of this project"
            );
        }

        LOGGER.info(
                "Adding user ID: "
                        + userId
                        + " to project ID: "
                        + projectId
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result =
                projectMemberDAO.addMember(
                        projectId,
                        userId,
                        roleInProject
                );

        if (result) {
            LOGGER.info(
                    "Project member added successfully."
            );
        } else {
            LOGGER.warning(
                    "Failed to add project member."
            );
        }

        return result;
    }

    @Override
    public boolean removeMember(int projectId,
                                int userId,
                                User loggedInUser) {

        checkAccess(loggedInUser);

        if (projectId <= 0) {
            throw new IllegalArgumentException("Project ID must be greater than 0");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than 0");
        }

        LOGGER.info(
                "Removing user ID: "
                        + userId
                        + " from project ID: "
                        + projectId
                        + " by user ID: "
                        + loggedInUser.getId()
        );

        boolean result =
                projectMemberDAO.removeMember(
                        projectId,
                        userId
                );

        if (result) {
            LOGGER.info(
                    "Project member removed successfully."
            );
        } else {
            LOGGER.warning(
                    "Project member removal failed."
            );
        }

        return result;
    }

    @Override
    public List<User> getProjectMembers(int projectId,
                                        User loggedInUser) {

        checkAccess(loggedInUser);

        if (projectId <= 0) {
            throw new IllegalArgumentException("Project ID must be greater than 0");
        }

        return projectMemberDAO.getProjectMembers(projectId);
    }

    @Override
    public boolean isMember(int projectId, int userId) {

        if (projectId <= 0 || userId <= 0) {
            return false;
        }

        return projectMemberDAO.isMember(projectId, userId);
    }

    private void checkAccess(User loggedInUser) {

        if (loggedInUser == null) {
            throw new SecurityException("User must be logged in");
        }

        String role = loggedInUser.getRoleName();

        if (!"ADMIN".equalsIgnoreCase(role)
                && !"PROJECT_MANAGER".equalsIgnoreCase(role)
                && !"TEAM_LEAD".equalsIgnoreCase(role)) {

            throw new SecurityException(
                    "Only Admin, Project Manager or Team Lead can manage project members"
            );
        }
    }
}