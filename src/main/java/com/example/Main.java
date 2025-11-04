package com.example;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection con = DatabaseManager.getConnection()) {
            DatabaseManager.createTables(con);
            DatabaseManager.clearTables(con);

            DataSeeder.insertTestData(con);

        // Queries.runAll(con);
        //Queries.updateProductPrice(con); //  UPDATE(Ноутбук)
              Queries.deleteUser(con, 3);      //  DELETE (id=3)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
