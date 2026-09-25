package com.ptms.app.service;

import com.ptms.app.model.Project;
import com.ptms.app.model.User;

import java.util.List;

public interface ProjectService {

    boolean createProject(Project project, User loggedInUser);

    Project getProjectById(int id);

    List<Project> getAllProjects();

    boolean updateProject(Project project, User loggedInUser);

    boolean deleteProject(int id, User loggedInUser);

    List<Project> getProjectsByDomain(String domain);
}