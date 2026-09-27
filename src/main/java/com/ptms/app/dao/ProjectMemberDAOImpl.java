package com.ptms.app.dao;

import com.ptms.app.model.User;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDAOImpl implements ProjectMemberDAO {

    @Override
    public boolean addMember(int projectId, int userId, String roleInProject) {

        String sql = """
                INSERT INTO project_members
                (project_id, user_id, joined_at, role_in_project)
                VALUES (?, ?, CURRENT_TIMESTAMP, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);
            statement.setString(3, roleInProject);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to add project member", e);
        }
    }

    @Override
    public boolean removeMember(int projectId, int userId) {

        String sql = """
                DELETE FROM project_members
                WHERE project_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to remove project member", e);
        }
    }

    @Override
    public List<User> getProjectMembers(int projectId) {

        String sql = """
                SELECT u.id,
                       u.first_name,
                       u.last_name,
                       u.username,
                       u.email,
                       u.password,
                       u.role_name,
                       u.date_of_birth,
                       u.mobile_number,
                       u.gender
                FROM users u
                INNER JOIN project_members pm
                    ON u.id = pm.user_id
                WHERE pm.project_id = ?
                """;

        List<User> members = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    User user = new User();

                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRoleName(rs.getString("role_name"));
                    user.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                    user.setMobileNumber(rs.getString("mobile_number"));
                    user.setGender(rs.getString("gender"));

                    members.add(user);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to retrieve project members", e);
        }

        return members;
    }

    @Override
    public boolean isMember(int projectId, int userId) {

        String sql = """
                SELECT COUNT(*)
                FROM project_members
                WHERE project_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check project membership", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}