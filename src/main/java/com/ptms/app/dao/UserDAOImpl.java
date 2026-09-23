package com.ptms.app.dao;

import com.ptms.app.model.User;
import com.ptms.app.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findUserByEmail(String email) {

        String searchbymail = """
                SELECT
                    u.id,
                    u.name,
                    u.email,
                    u.password_hash,
                    u.role_id,
                    r.role_name
                FROM users u
                JOIN roles r
                    ON u.role_id = r.id
                WHERE u.email = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(searchbymail)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getInt("role_id"),
                            resultSet.getString("role_name")
                    );
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to find user by email", e
            );
        }

        return null;
    }
}