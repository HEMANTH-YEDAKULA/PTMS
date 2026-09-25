package com.ptms.app.dao;

import com.ptms.app.model.Project;

import java.util.List;

public interface ProjectDAO {

    boolean create(Project project);

    Project findById(int id);

    List<Project> findAll();

    boolean update(Project project);

    boolean delete(int id);

    List<Project> findByDomain(String domain);
}