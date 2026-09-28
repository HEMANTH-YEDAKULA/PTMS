package com.ptms.app.service;

import com.ptms.app.model.User;

import java.util.List;

public interface ProjectMemberService {

    boolean addMember(int projectId, int userId, String roleInProject, User loggedInUser);

    boolean removeMember(int projectId, int userId, User loggedInUser);

    List<User> getProjectMembers(int projectId, User loggedInUser);

    boolean isMember(int projectId, int userId);
}