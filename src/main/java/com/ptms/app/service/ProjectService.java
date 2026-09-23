package com.ptms.app.service;

import com.ptms.app.model.Project;

import java.util.List;

public interface ProjectService {

    boolean createProject(Project project);

    Project getProjectById(int id);

    List<Project> getAllProjects();

    boolean updateProject(Project project);

    boolean deleteProject(int id);

    List<Project> getProjectsByDomain(String domain);




}