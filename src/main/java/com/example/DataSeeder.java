package com.example;

import com.example.repository.*;

import java.sql.Connection;

public class DataSeeder {
    public static void insertTestData(Connection con) throws Exception {
        UserRepository userRepo = new UserRepository(con);
        ProductRepository productRepo = new ProductRepository(con);
        OrderRepository orderRepo = new OrderRepository(con);
        OrderItemRepository orderItemRepo = new OrderItemRepository(con);

        int u1 = userRepo.insert("Иван Петров", "ivan@example.com");
        int u2 = userRepo.insert("Мария Сидорова", "maria@example.com");
        int u3 = userRepo.insert("Алексей Смирнов", "aleksey@example.com");

        int p1 = productRepo.insert("Ноутбук", 1000);
        int p2 = productRepo.insert("Смартфон", 500);
        int p3 = productRepo.insert("Клавиатура", 50);

        int order1 = orderRepo.insert(u1);
        orderItemRepo.insert(order1, p1, 2);
        orderItemRepo.insert(order1, p3, 1);

        int order2 = orderRepo.insert(u2);
        orderItemRepo.insert(order2, p2, 1);

        System.out.println("Тестовые данные добавлены!");
    }
}
