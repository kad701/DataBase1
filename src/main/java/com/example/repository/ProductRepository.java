package com.example.repository;

import java.sql.*;

public class ProductRepository {
    private final Connection con;

    public ProductRepository(Connection con) {
        this.con = con;
    }

    public int insert(String name, double price) throws SQLException {
        String sql = "INSERT INTO product (name, price) VALUES (?, ?) RETURNING product_id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
