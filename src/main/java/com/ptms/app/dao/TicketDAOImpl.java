package com.ptms.app.dao;

import com.ptms.app.model.Ticket;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOImpl implements TicketDAO {

    @Override
    public boolean create(Ticket ticket) {

        String sql = """
                INSERT INTO ticket_management
                (project_id, title, description, priority,
                 deadline, assigned_to, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, ticket.getProjectId());
            statement.setString(2, ticket.getTitle());
            statement.setString(3, ticket.getDescription());
            statement.setString(4, ticket.getPriority());

            if (ticket.getDeadline() != null) {
                statement.setDate(
                        5,
                        Date.valueOf(ticket.getDeadline())
                );
            } else {
                statement.setNull(5, Types.DATE);
            }

            if (ticket.getAssignedTo() != null) {
                statement.setInt(6, ticket.getAssignedTo());
            } else {
                statement.setNull(6, Types.INTEGER);
            }

            statement.setString(7, ticket.getStatus());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    ticket.setId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to create ticket", e);
        }
    }

    @Override
    public Ticket findById(int id) {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return mapTicket(rs);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find ticket", e);
        }

        return null;
    }

    @Override
    public List<Ticket> findAll() {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                ORDER BY id
                """;

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                tickets.add(mapTicket(rs));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to retrieve tickets", e);
        }

        return tickets;
    }

    @Override
    public boolean update(Ticket ticket) {

        String sql = """
                UPDATE ticket_management
                SET title = ?,
                    description = ?,
                    priority = ?,
                    deadline = ?,
                    assigned_to = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ticket.getTitle());
            statement.setString(2, ticket.getDescription());
            statement.setString(3, ticket.getPriority());

            if (ticket.getDeadline() != null) {
                statement.setDate(
                        4,
                        Date.valueOf(ticket.getDeadline())
                );
            } else {
                statement.setNull(4, Types.DATE);
            }

            if (ticket.getAssignedTo() != null) {
                statement.setInt(5, ticket.getAssignedTo());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setString(6, ticket.getStatus());
            statement.setInt(7, ticket.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to update ticket", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = """
                DELETE FROM ticket_management
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete ticket", e);
        }
    }

    @Override
    public boolean assignTicket(int ticketId, int userId) {

        String sql = """
                UPDATE ticket_management
                SET assigned_to = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, ticketId);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to assign ticket", e);
        }
    }

    @Override
    public List<Ticket> findByProject(int projectId) {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE project_id = ?
                ORDER BY id
                """;

        return findWithParameter(sql, projectId);
    }

    @Override
    public List<Ticket> findByStatus(String status) {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE status = ?
                ORDER BY id
                """;

        return findWithParameter(sql, status);
    }

    @Override
    public List<Ticket> findByPriority(String priority) {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE priority = ?
                ORDER BY id
                """;

        return findWithParameter(sql, priority);
    }

    @Override
    public List<Ticket> findByTitle(String title) {

        String sql = """
                SELECT id,
                       project_id,
                       title,
                       description,
                       priority,
                       deadline,
                       assigned_to,
                       created_at,
                       status
                FROM ticket_management
                WHERE title LIKE ?
                ORDER BY id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + title + "%");

            List<Ticket> tickets = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapTicket(rs));
                }
            }

            return tickets;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to search tickets", e);
        }
    }

    private List<Ticket> findWithParameter(
            String sql,
            String parameter) {

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, parameter);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    tickets.add(mapTicket(rs));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to retrieve tickets", e);
        }

        return tickets;
    }

    private List<Ticket> findWithParameter(
            String sql,
            int parameter) {

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, parameter);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    tickets.add(mapTicket(rs));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to retrieve tickets", e);
        }

        return tickets;
    }

    private Ticket mapTicket(ResultSet rs) throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setId(rs.getInt("id"));
        ticket.setProjectId(rs.getInt("project_id"));
        ticket.setTitle(rs.getString("title"));
        ticket.setDescription(rs.getString("description"));
        ticket.setPriority(rs.getString("priority"));

        Date deadline = rs.getDate("deadline");

        if (deadline != null) {
            ticket.setDeadline(deadline.toLocalDate());
        }

        int assignedTo = rs.getInt("assigned_to");

        if (rs.wasNull()) {
            ticket.setAssignedTo(null);
        } else {
            ticket.setAssignedTo(assignedTo);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");

        if (createdAt != null) {
            ticket.setCreatedAt(createdAt.toLocalDateTime());
        }

        ticket.setStatus(rs.getString("status"));

        return ticket;
    }
}