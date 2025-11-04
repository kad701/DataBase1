package com.example;

import java.sql.*;

public class Queries {
    public static void runAll(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {


            // Вывод всех пользователей
            System.out.println("\n=== USERS ===");
            ResultSet rsUsers = stmt.executeQuery("SELECT * FROM users");
            while (rsUsers.next()) {
                System.out.println("id: " + rsUsers.getInt("id") + " " +
                        "| Name: " + rsUsers.getString("name") + " " +
                        "| Email: " + rsUsers.getString("Email"));
            }
// Вывод всех продуктов
            ResultSet rsProducts = stmt.executeQuery("SELECT * FROM product");
            System.out.println("\n=== PRODUCTS ===");
            while (rsProducts.next()) {
                System.out.println("product_id: " + rsProducts.getInt("product_id") + " | " +
                        "Name: " + rsProducts.getString("name") + " | " +
                        "Price: " + rsProducts.getBigDecimal("price"));
            }

// Вывод всех заказов
            ResultSet rsOrders = stmt.executeQuery("SELECT * FROM order_table");
            System.out.println("\n=== ORDERS ===");
            while (rsOrders.next()) {
                System.out.println("order_id: " + rsOrders.getInt("order_id") + " | " +
                        "User_id: " + rsOrders.getInt("user_id") + " | " +
                        "Order_date: " + rsOrders.getTimestamp("order_date"));
            }

// Вывод всех позиций заказов
            ResultSet rsOrderItems = stmt.executeQuery("SELECT * FROM order_item");
            System.out.println("\n=== ORDER ITEMS ===");
            while (rsOrderItems.next()) {
                System.out.println("order_item_id: " + rsOrderItems.getInt("order_item_id") + " | " +
                        "Order_id: " + rsOrderItems.getInt("order_id") + " | " +
                        "Product_id: " + rsOrderItems.getInt("product_id") + " | " +
                        "Amount: " + rsOrderItems.getInt("amount"));
            }

            // A: Продукты дороже 60
            System.out.println("\nA: Продукты дороже 60:");
            ResultSet rsA = stmt.executeQuery("SELECT name, price FROM product WHERE price > 60 ORDER BY price");
            while (rsA.next()) {
                System.out.println("Товар: " + rsA.getString("name") + " | Цена: " + rsA.getBigDecimal("price"));
            }

            // B: Количество заказов у каждого пользователя
            System.out.println("\nB: Количество заказов по пользователям:");
            ResultSet rsB = stmt.executeQuery("""
                        SELECT u.name, COUNT(o.order_id) AS total_orders
                        FROM users u
                        LEFT JOIN order_table o ON u.id = o.user_id
                        GROUP BY u.name
                        ORDER BY total_orders DESC
                    """);
            while (rsB.next()) {
                System.out.println(rsB.getString("name") + " | Заказов: " + rsB.getInt("total_orders"));
            }

            // C: Покупатели товаров дороже 100
            System.out.println("\nC: Пользователи, купившие товары > 100:");
            ResultSet rsC = stmt.executeQuery("""
                        SELECT u.name, p.name AS product, p.price
                        FROM users u
                        JOIN order_table o ON u.id = o.user_id
                        JOIN order_item oi ON o.order_id = oi.order_id
                        JOIN product p ON oi.product_id = p.product_id
                        WHERE p.price > 100
                    """);
            while (rsC.next()) {
                System.out.println(rsC.getString("name") + " купил " +
                        rsC.getString("product") + " за " + rsC.getBigDecimal("price"));
            }

        }
    }

    // UPDATE
    public static void updateProductPrice(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            System.out.println("\n=== UPDATE PRODUCT (Laptop +20%) ===");

            int updatedRows = stmt.executeUpdate("""
                        UPDATE product
                        SET price = price * 1.2
                        WHERE name = 'Ноутбук'
                    """);

            System.out.println("Обновлено строк: " + updatedRows);

            ResultSet rs = stmt.executeQuery("SELECT name, price FROM product WHERE name='Laptop'");
            while (rs.next()) {
                System.out.println("Laptop новая цена: " + rs.getBigDecimal("price"));
            }
            System.out.println("\n=== PRODUCTS AFTER UPDATE ===");
            ResultSet rsAll = stmt.executeQuery("SELECT product_id, name, price FROM product ORDER BY product_id");
            while (rsAll.next()) {
                System.out.println("product_id: " + rsAll.getInt("product_id") +
                        " | Name: " + rsAll.getString("name") +
                        " | Price: " + rsAll.getBigDecimal("price"));
            }
        }
    }

    //  DELETE
    public static void deleteUser(Connection con, int userId) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            System.out.println("\n=== DELETE USER (id = " + userId + ") ===");

            int deletedRows = stmt.executeUpdate("""
                    DELETE FROM users
                    WHERE id = """ + userId);

            System.out.println("Удалено строк: " + deletedRows);
            System.out.println("\n=== USERS AFTER DELETE ===");
            ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM users ORDER BY id");
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") +
                        " | Name: " + rs.getString("name") +
                        " | Email: " + rs.getString("email"));
            }
        }


    }
}
