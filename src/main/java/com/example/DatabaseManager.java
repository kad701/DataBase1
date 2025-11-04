package com.example;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5434/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50),
                    email VARCHAR(100) UNIQUE
                )
            """);
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS product (
                    product_id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    price NUMERIC(10,2) NOT NULL
                )
            """);
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS order_table (
                    order_id SERIAL PRIMARY KEY,
                    user_id INT NOT NULL,
                    order_date TIMESTAMP DEFAULT NOW(),
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS order_item (
                    order_item_id SERIAL PRIMARY KEY,
                    order_id INT NOT NULL,
                    product_id INT NOT NULL,
                    amount INT NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES order_table(order_id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
                )
            """);
            System.out.println(" Таблицы созданы успешно!");
        }
    }

    public static void clearTables(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE order_item, order_table, product, users RESTART IDENTITY CASCADE");
            System.out.println(" Таблицы очищены!");
        }
    }


}
