package com.example.repository;

import java.sql.*;

public class UserRepository {
    private final Connection con;

    public UserRepository(Connection con) {
        this.con = con;
    }

    public int insert(String name, String email) throws SQLException {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
