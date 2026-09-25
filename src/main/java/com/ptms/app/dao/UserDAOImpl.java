package com.ptms.app.dao;

import com.ptms.app.model.User;
import com.ptms.app.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByEmail(String email) {

        String searchbyemail = """
                SELECT id,
                       first_name,
                       last_name,
                       username,
                       email,
                       password,
                       role_name,
                       date_of_birth,
                       mobile_number,
                       gender
                FROM users
                WHERE email = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(searchbyemail);) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("role_name"),
                            resultSet.getDate("date_of_birth") != null
                                    ? resultSet.getDate("date_of_birth").toLocalDate()
                                    : null,
                            resultSet.getString("mobile_number"),
                            resultSet.getString("gender")
                    );
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }

        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }
}