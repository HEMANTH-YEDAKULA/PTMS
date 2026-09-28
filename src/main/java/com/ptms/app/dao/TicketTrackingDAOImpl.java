package com.ptms.app.dao;

import com.ptms.app.model.TicketTracking;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketTrackingDAOImpl implements TicketTrackingDAO {

    @Override
    public boolean create(TicketTracking tracking) {

        String sql = """
                INSERT INTO ticket_tracking
                (ticket_id, status, progress, comment, updated_by, updated_at)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, tracking.getTicketId());
            statement.setString(2, tracking.getStatus());
            statement.setInt(3, tracking.getProgress());
            statement.setString(4, tracking.getComment());
            statement.setInt(5, tracking.getUpdatedBy());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    tracking.setId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to create ticket tracking: "
                            + e.getMessage(), e);
        }
    }

    @Override
    public List<TicketTracking> findByTicket(int ticketId) {

        String sql = """
                SELECT id,
                       ticket_id,
                       status,
                       progress,
                       comment,
                       updated_by,
                       updated_at
                FROM ticket_tracking
                WHERE ticket_id = ?
                ORDER BY updated_at DESC
                """;

        return findList(sql, ticketId);
    }

    @Override
    public List<TicketTracking> findByUser(int userId) {

        String sql = """
                SELECT id,
                       ticket_id,
                       status,
                       progress,
                       comment,
                       updated_by,
                       updated_at
                FROM ticket_tracking
                WHERE updated_by = ?
                ORDER BY updated_at DESC
                """;

        return findList(sql, userId);
    }

    @Override
    public TicketTracking findLatestByTicket(int ticketId) {

        String sql = """
                SELECT id,
                       ticket_id,
                       status,
                       progress,
                       comment,
                       updated_by,
                       updated_at
                FROM ticket_tracking
                WHERE ticket_id = ?
                ORDER BY updated_at DESC
                LIMIT 1
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ticketId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapTracking(resultSet);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to find latest ticket tracking: "
                            + e.getMessage(), e);
        }

        return null;
    }

    private List<TicketTracking> findList(
            String sql,
            int userOrTicketId) {

        List<TicketTracking> trackingList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userOrTicketId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    trackingList.add(mapTracking(resultSet));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to retrieve ticket tracking: "
                            + e.getMessage(), e);
        }

        return trackingList;
    }

    private TicketTracking mapTracking(
            ResultSet resultSet) throws SQLException {

        TicketTracking tracking = new TicketTracking();

        tracking.setId(resultSet.getInt("id"));
        tracking.setTicketId(resultSet.getInt("ticket_id"));
        tracking.setStatus(resultSet.getString("status"));
        tracking.setProgress(resultSet.getInt("progress"));
        tracking.setComment(resultSet.getString("comment"));
        tracking.setUpdatedBy(resultSet.getInt("updated_by"));

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        if (updatedAt != null) {
            tracking.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        return tracking;
    }
}