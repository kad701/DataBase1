package com.example.repository;

import java.sql.*;

public class OrderRepository {
    private final Connection con;

    public OrderRepository(Connection con) {
        this.con = con;
    }

    public int insert(int userId) throws SQLException {
        String sql = "INSERT INTO order_table (user_id) VALUES (?) RETURNING order_id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
