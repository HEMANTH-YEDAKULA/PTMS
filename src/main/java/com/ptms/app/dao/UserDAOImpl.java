package com.ptms.app.dao;

import com.ptms.app.model.User;
import com.ptms.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByEmail(String email) {

        String sql = """
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
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find user by email",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    @Override
    public boolean create(User user) {

        String sql = """
                INSERT INTO users
                (name, email, password, role_id,
                 first_name, last_name, username,
                 role_name, date_of_birth, mobile_number, gender)
                SELECT ?, ?, ?, id, ?, ?, ?, role_name, ?, ?, ?
                FROM roles
                WHERE role_name = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,
                    user.getFirstName() + " " + user.getLastName());

            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getUsername());

            if (user.getDateOfBirth() != null) {
                statement.setDate(
                        7,
                        Date.valueOf(user.getDateOfBirth())
                );
            } else {
                statement.setNull(7, Types.DATE);
            }

            statement.setString(8, user.getMobileNumber());
            statement.setString(9, user.getGender());
            statement.setString(10, user.getRoleName());

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public User findById(int id) {

        String sql = """
                SELECT id, first_name, last_name, username,
                       email, password, role_name,
                       date_of_birth, mobile_number, gender
                FROM users
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find user", e);
        }

        return null;
    }



    @Override
    public List<User> findAll() {

        String sql = """
                SELECT id, first_name, last_name, username,
                       email, password, role_name,
                       date_of_birth, mobile_number, gender
                FROM users
                ORDER BY id
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to fetch users", e);
        }

        return users;
    }

    @Override
    public boolean update(User user) {

        String sql = """
                UPDATE users
                SET first_name = ?,
                    last_name = ?,
                    username = ?,
                    email = ?,
                    password = ?,
                    role_name = ?,
                    role_id = (
                        SELECT id
                        FROM roles
                        WHERE role_name = ?
                    ),
                    date_of_birth = ?,
                    mobile_number = ?,
                    gender = ?,
                    name = ?
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRoleName());
            statement.setString(7, user.getRoleName());

            if (user.getDateOfBirth() != null) {
                statement.setDate(
                        8,
                        Date.valueOf(user.getDateOfBirth())
                );
            } else {
                statement.setNull(8, Types.DATE);
            }

            statement.setString(9, user.getMobileNumber());
            statement.setString(10, user.getGender());

            statement.setString(
                    11,
                    user.getFirstName() + " " + user.getLastName()
            );

            statement.setInt(12, user.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public List<User> findByRole(String roleName) {

        String sql = """
                SELECT id, first_name, last_name, username,
                       email, password, role_name,
                       date_of_birth, mobile_number, gender
                FROM users
                WHERE role_name = ?
                ORDER BY id
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, roleName);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(mapUser(resultSet));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to find users by role", e);
        }

        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleName(rs.getString("role_name"));

        Date dateOfBirth = rs.getDate("date_of_birth");

        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        user.setMobileNumber(rs.getString("mobile_number"));
        user.setGender(rs.getString("gender"));

        return user;
    }
}