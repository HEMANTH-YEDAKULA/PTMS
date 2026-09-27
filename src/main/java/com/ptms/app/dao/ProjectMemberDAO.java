package com.ptms.app.dao;

import com.ptms.app.model.User;

import java.util.List;

public interface ProjectMemberDAO {

    boolean addMember(int projectId, int userId, String roleInProject);

    boolean removeMember(int projectId, int userId);

    List<User> getProjectMembers(int projectId);

    boolean isMember(int projectId, int userId);
}