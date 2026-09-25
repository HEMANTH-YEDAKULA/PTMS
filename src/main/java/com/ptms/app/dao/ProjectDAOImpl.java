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
                 domain, cost, start_date, deadline, priority, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getRequirements());
            statement.setInt(3, project.getManagerId());

            if (project.getTeamLeadId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, project.getTeamLeadId());
            }

            if (project.getClientId() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, project.getClientId());
            }

            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());

            if (project.getStartDate() == null) {
                statement.setNull(8, Types.DATE);
            } else {
                statement.setDate(8, Date.valueOf(project.getStartDate()));
            }

            if (project.getDeadline() == null) {
                statement.setNull(9, Types.DATE);
            } else {
                statement.setDate(9, Date.valueOf(project.getDeadline()));
            }

            statement.setString(10, project.getPriority());
            statement.setString(11, project.getStatus());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create project", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project findById(int id) {

        String sql = """
                SELECT id, name, requirements, manager_id, team_lead_id,
                       client_id, domain, cost, start_date, deadline,
                       priority, status
                FROM projects
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return mapProject(rs);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find project", e);
        }

        return null;
    }

    @Override
    public List<Project> findAll() {

        String sql = """
                SELECT id, name, requirements, manager_id, team_lead_id,
                       client_id, domain, cost, start_date, deadline,
                       priority, status
                FROM projects
                ORDER BY id
                """;

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                projects.add(mapProject(rs));
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

            if (project.getTeamLeadId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, project.getTeamLeadId());
            }

            if (project.getClientId() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, project.getClientId());
            }

            statement.setString(6, project.getDomain());
            statement.setBigDecimal(7, project.getCost());

            if (project.getStartDate() == null) {
                statement.setNull(8, Types.DATE);
            } else {
                statement.setDate(8, Date.valueOf(project.getStartDate()));
            }

            if (project.getDeadline() == null) {
                statement.setNull(9, Types.DATE);
            } else {
                statement.setDate(9, Date.valueOf(project.getDeadline()));
            }

            statement.setString(10, project.getPriority());
            statement.setString(11, project.getStatus());
            statement.setInt(12, project.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to update project", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete project", e);
        }
    }

    @Override
    public List<Project> findByDomain(String domain) {

        String sql = """
                SELECT id, name, requirements, manager_id, team_lead_id,
                       client_id, domain, cost, start_date, deadline,
                       priority, status
                FROM projects
                WHERE domain = ?
                ORDER BY id
                """;

        List<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, domain);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    projects.add(mapProject(rs));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find projects by domain", e);
        }

        return projects;
    }

    private Project mapProject(ResultSet rs) throws SQLException {

        Date startDate = rs.getDate("start_date");
        Date deadline = rs.getDate("deadline");

        int teamLeadId = rs.getInt("team_lead_id");
        Integer teamLead = rs.wasNull() ? null : teamLeadId;

        int clientId = rs.getInt("client_id");
        Integer client = rs.wasNull() ? null : clientId;

        return new Project(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("requirements"),
                rs.getInt("manager_id"),
                teamLead,
                client,
                rs.getString("domain"),
                rs.getBigDecimal("cost"),
                startDate != null ? startDate.toLocalDate() : null,
                deadline != null ? deadline.toLocalDate() : null,
                rs.getString("priority"),
                rs.getString("status")
        );
    }
}