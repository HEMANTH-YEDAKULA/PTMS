package com.ptms.app.dao;

import com.ptms.app.model.Project;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    @Override
    public boolean create(Project project) {

        String sql = """
                INSERT INTO projects
                (name, requirements, manager_id, team_lead_id, client_id,
                 domain, cost, team_size, start_date, deadline, priority, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getRequirements());
            statement.setInt(3, project.getManagerId());

            if (project.getTeamLeadId() != null) {
                statement.setInt(4, project.getTeamLeadId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            if (project.getClientId() != null) {
                statement.setInt(5, project.getClientId());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());
            statement.setInt(8, project.getTeamSize());
            statement.setObject(9, project.getStartDate());
            statement.setObject(10, project.getDeadline());
            statement.setString(11, project.getPriority());
            statement.setString(12, project.getStatus());

            int rows = statement.executeUpdate();

            if (rows == 1) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        project.setId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to create project", e);
        }

        return false;
    }

    @Override
    public Project findById(int id) {

        String sql = "SELECT * FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProject(resultSet);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find project", e);
        }

        return null;
    }

    @Override
    public List<Project> findAll() {

        String sql = "SELECT * FROM projects ORDER BY id";

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                projects.add(mapProject(resultSet));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to retrieve projects", e);
        }

        return projects;
    }

    @Override
    public boolean update(Project project) {

        String sql = """
                UPDATE projects
                SET name = ?,
                    requirements = ?,
                    manager_id = ?,
                    team_lead_id = ?,
                    client_id = ?,
                    domain = ?,
                    cost = ?,
                    team_size = ?,
                    start_date = ?,
                    deadline = ?,
                    priority = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getRequirements());
            statement.setInt(3, project.getManagerId());

            if (project.getTeamLeadId() != null) {
                statement.setInt(4, project.getTeamLeadId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            if (project.getClientId() != null) {
                statement.setInt(5, project.getClientId());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());
            statement.setInt(8, project.getTeamSize());
            statement.setObject(9, project.getStartDate());
            statement.setObject(10, project.getDeadline());
            statement.setString(11, project.getPriority());
            statement.setString(12, project.getStatus());
            statement.setInt(13, project.getId());

            return statement.executeUpdate() == 1;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to update project", e);
        }
    }

    @Override
    public boolean deleteById(int id) {

        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete project", e);
        }
    }

    @Override
    public boolean existsById(int id) {

        String sql = "SELECT 1 FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to check project", e);
        }
    }

    @Override
    public List<Project> findByDomain(String domain) {

        String sql = """
                SELECT * FROM projects
                WHERE domain = ?
                ORDER BY id
                """;

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, domain);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    projects.add(mapProject(resultSet));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find projects by domain", e);
        }

        return projects;
    }

    @Override
    public List<Project> findByManager(int managerId) {

        String sql = """
                SELECT * FROM projects
                WHERE manager_id = ?
                ORDER BY id
                """;

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, managerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    projects.add(mapProject(resultSet));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find projects by manager", e);
        }

        return projects;
    }

    private Project mapProject(ResultSet resultSet) throws SQLException {

        Project project = new Project();

        project.setId(resultSet.getInt("id"));
        project.setName(resultSet.getString("name"));
        project.setRequirements(resultSet.getString("requirements"));
        project.setManagerId(resultSet.getInt("manager_id"));

        int teamLeadId = resultSet.getInt("team_lead_id");
        project.setTeamLeadId(
                resultSet.wasNull() ? null : teamLeadId
        );

        int clientId = resultSet.getInt("client_id");
        project.setClientId(
                resultSet.wasNull() ? null : clientId
        );

        project.setDomain(resultSet.getString("domain"));
        project.setCost(resultSet.getBigDecimal("cost"));
        project.setTeamSize(resultSet.getInt("team_size"));

        Date startDate = resultSet.getDate("start_date");
        project.setStartDate(
                startDate != null ? startDate.toLocalDate() : null
        );

        Date deadline = resultSet.getDate("deadline");
        project.setDeadline(
                deadline != null ? deadline.toLocalDate() : null
        );

        project.setPriority(resultSet.getString("priority"));
        project.setStatus(resultSet.getString("status"));

        return project;
    }
}