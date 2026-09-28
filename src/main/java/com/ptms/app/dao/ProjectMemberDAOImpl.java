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
            throw new RuntimeException(
                    "Failed to add project member: " + e.getMessage(), e);
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
            throw new RuntimeException(
                    "Failed to remove project member: " + e.getMessage(), e);
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
                ORDER BY u.id
                """;

        List<User> members = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    User user = new User();

                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRoleName(resultSet.getString("role_name"));

                    // date_of_birth is nullable
                    Date dateOfBirth =
                            resultSet.getDate("date_of_birth");

                    if (dateOfBirth != null) {
                        user.setDateOfBirth(
                                dateOfBirth.toLocalDate()
                        );
                    } else {
                        user.setDateOfBirth(null);
                    }

                    user.setMobileNumber(
                            resultSet.getString("mobile_number")
                    );

                    user.setGender(
                            resultSet.getString("gender")
                    );

                    members.add(user);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to retrieve project members: "
                            + e.getMessage(), e);
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

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to check project membership: "
                            + e.getMessage(), e);
        }

        return false;
    }
}