package com.example.repository;

import java.sql.*;

public class OrderItemRepository {
    private final Connection con;

    public OrderItemRepository(Connection con) {
        this.con = con;
    }

    public void insert(int orderId, int productId, int amount) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, product_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, amount);
            ps.executeUpdate();
        }
    }
}
