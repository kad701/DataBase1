

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        // Подключение к БД
        String url = "jdbc:postgresql://localhost:5434/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {

            //  Создание таблиц
            // Таблица Users
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "email VARCHAR(100) UNIQUE" +
                    ")");
            // Таблица Product
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS product (" +
                    "product_id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "price NUMERIC(10,2) NOT NULL" +
                    ")");
            // Таблица Order_table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_table (" +
                    "order_id SERIAL PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "order_date TIMESTAMP DEFAULT NOW(), " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ")");
            // Таблица Order_Item
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_item (" +
                    "order_item_id SERIAL PRIMARY KEY, " +
                    "order_id INT NOT NULL, " +
                    "product_id INT NOT NULL, " +
                    "amount INT NOT NULL, " +
                    "FOREIGN KEY (order_id) REFERENCES order_table(order_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE" +
                    ")");

            System.out.println("Таблицы созданы успешно!");

            //  Очистка таблиц , убедимся что таблицы пустые
            stmt.executeUpdate("TRUNCATE TABLE order_item, order_table, product, users RESTART IDENTITY CASCADE");

            //  Вставка пользователей в таблицу Users

            int userId1 = insertUser(con, "Иван Петров", "ivan@example.com");
            int userId2 = insertUser(con, "Мария Сидорова", "maria@example.com");
            int userId3 = insertUser(con, "Алексей Смирнов", "aleksey@example.com");
            int userId4 = insertUser(con, "Ольга Кузнецова", "olga@example.com");
            int userId5 = insertUser(con, "Сергей Лебедев", "sergey@example.com");
            int userId6 = insertUser(con, "Елена Новикова", "elena@example.com");
            int userId7 = insertUser(con, "Дмитрий Орлов", "dmitry@example.com");
            int userId8 = insertUser(con, "Анна Фролова", "anna@example.com");



            // Вставка продуктов в таблицу Product
            int productId1 = insertProduct(con, "Ноутбук", 1000.00);
            int productId2 = insertProduct(con, "Смартфон", 500.00);
            int productId3 = insertProduct(con, "Клавиатура", 50.00);
            int productId4 = insertProduct(con, "Мышь", 30.00);
            int productId5 = insertProduct(con, "Монитор", 300.00);
            int productId6 = insertProduct(con, "Наушники", 80.00);
            int productId7 = insertProduct(con, "Вебкамера", 60.00);
            int productId8 = insertProduct(con, "USB-хаб", 20.00);


// Вставка в таблицу Order_table
            // Заказ 1 для пользователя userId1
            int orderId1 = insertOrder(con, userId1);
            insertOrderItem(con, orderId1, productId1, 2); // 2 ноутбука
            insertOrderItem(con, orderId1, productId3, 3); // 3 клавиатуры
            insertOrderItem(con, orderId1, productId5, 1); // 1 монитор

// Заказ 2 для пользователя userId2
            int orderId2 = insertOrder(con, userId2);
            insertOrderItem(con, orderId2, productId2, 1); // 1 смартфон
            insertOrderItem(con, orderId2, productId6, 2); // 2 наушника
            insertOrderItem(con, orderId2, productId7, 1); // 1 вебкамера

// Заказ 3 для пользователя userId3
            int orderId3 = insertOrder(con, userId3);
            insertOrderItem(con, orderId3, productId3, 2); // 2 клавиатуры
            insertOrderItem(con, orderId3, productId4, 2); // 2 мыши
            insertOrderItem(con, orderId3, productId8, 3); // 3 USB-хаба

// Заказ 4 для пользователя userId5
            int orderId4 = insertOrder(con, userId5);
            insertOrderItem(con, orderId4, productId1, 1); // 1 ноутбук
            insertOrderItem(con, orderId4, productId2, 1); // 1 смартфон
            insertOrderItem(con, orderId4, productId5, 1); // 1 монитор
            insertOrderItem(con, orderId4, productId6, 1); // 1 наушники

            System.out.println("Тестовые данные добавлены успешно!");

            //  Вывод таблицы Users
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
                System.out.println("\nПользователи:");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " | " +
                            rs.getString("name") + " | " +
                            rs.getString("email"));
                }
            }

            //  Вывод таблицы Product
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM product")) {
                System.out.println("\nПродукты:");
                while (rs.next()) {
                    System.out.println(rs.getInt("product_id") + " | " +
                            rs.getString("name") + " | " +
                            rs.getBigDecimal("price"));
                }
            }

            // Вывод таблицы Order
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT o.order_id, u.name AS user_name, p.name AS product_name, oi.amount " +
                            "FROM order_item oi " +
                            "JOIN order_table o ON oi.order_id = o.order_id " +
                            "JOIN users u ON o.user_id = u.id " +
                            "JOIN product p ON oi.product_id = p.product_id")) {
                System.out.println("\nЗаказы:");
                while (rs.next()) {
                    System.out.println("Заказ #" + rs.getInt("order_id") +
                            " | Пользователь: " + rs.getString("user_name") +
                            " | Товар: " + rs.getString("product_name") +
                            " | Кол-во: " + rs.getInt("amount"));
                }
            }


            // A Выбрать продукт из таблицы с ценой  больше 60, отсортировать результат
            try (ResultSet rs = stmt.executeQuery(
                    "Select name,price From Product Where price>60 Order By price" )) {
                System.out.println("\n Задание А:");
                while (rs.next()) {
                    System.out.println("  Товар: " + rs.getString("name") +
                            " | Цена: " + rs.getInt("price"));
                }
            }

            //B Подсчитать общее число заказов у каждого пользователя
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT u.name, COUNT(o.order_id) AS total_orders " +
                            "FROM users u " +
                            "LEFT JOIN order_table o ON u.id = o.user_id " +
                            "GROUP BY u.id, u.name, u.email " +
                            "ORDER BY total_orders DESC")) {
                System.out.println("\n Задание B:");
                while (rs.next()) {
                    System.out.println("  Пользователь: " + rs.getString("name") +
                            " | Количество заказов: " + rs.getInt("total_orders"));
                }
            }

            // С Вывести название товара и имя клиента, который купил товар стоимостью больше 100
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT u.name AS user_name, p.name AS product_name, p.price " +
                            "FROM users u " +
                            "JOIN order_table o ON u.id = o.user_id " +
                            "JOIN order_item oi ON o.order_id = oi.order_id " +
                            "JOIN product p ON oi.product_id = p.product_id " +
                            "WHERE p.price > 100"
            )) {
                System.out.println("\nЗадание C: Список пользователей и товаров с ценой >100");
                while (rs.next()) {
                    System.out.println("Пользователь: " + rs.getString("user_name") +
                            " | Товар: " + rs.getString("product_name") +
                            " | Цена: " + rs.getBigDecimal("price"));
                }
            }
            //D Изменить цену тлвра наушники на 150, вывести таблицу товары
            String sql = "UPDATE product SET price = 150 WHERE name = 'Наушники'";

            // Первый Statement для UPDATE
            try (Statement stmtUpdate = con.createStatement()) {
                int rowsUpdated = stmtUpdate.executeUpdate(sql);
                System.out.println("Обновлено строк: " + rowsUpdated);
            }

            // Второй Statement для SELECT
            try (Statement stmtSelect = con.createStatement();
                 ResultSet rs = stmtSelect.executeQuery("SELECT product_id, name AS product_name, price FROM product")) {
                System.out.println("\nЗадание D: изменить цену товара наушники на 150 ");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("product_id") +
                            " | Товар: " + rs.getString("product_name") +
                            " | Цена: " + rs.getBigDecimal("price"));
                }
            }

            //e Удалить первую записиь в таблице Product (DELETE): удалите 1–2 записи, чтобы проверить целостность
            //(особенно, если есть FOREIGN KEY).

            int productIdToDelete = 1; // ID продукта, который хотим удалить

            String sqlDelete = "DELETE FROM product WHERE product_id = ?";

            try (PreparedStatement pstmt = con.prepareStatement(sqlDelete)) {
                pstmt.setInt(1, productIdToDelete);
                int rowsDeleted = pstmt.executeUpdate();
                System.out.println("Удалено строк: " + rowsDeleted);
            }
            try (Statement stmt2 = con.createStatement();
                 ResultSet rs = stmt2.executeQuery("SELECT product_id, name, price FROM product")) {

                System.out.println("\nТаблица продуктов после удаления:");
                System.out.println("ID | Название | Цена");
                System.out.println("------------------------");
                System.out.println("\nЗадание E:Удалить первую строчку из таблицы  продукты ");
                while (rs.next()) {
                    System.out.println(rs.getInt("product_id") +
                            " | " + rs.getString("name") +
                            " | " + rs.getBigDecimal("price"));
                }
            }






        } catch (SQLException e) {
            e.printStackTrace();
        }






    }

    // Метод вставки пользователя
    private static int insertUser(Connection con, String name, String email) throws SQLException {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?) RETURNING id";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }



    }

    // Метод вставки продукта
    private static int insertProduct(Connection con, String name, double price) throws SQLException {
        String sql = "INSERT INTO product (name, price) VALUES (?, ?) RETURNING product_id";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // Метод вставки заказа
    private static int insertOrder(Connection con, int userId) throws SQLException {
        String sql = "INSERT INTO order_table (user_id) VALUES (?) RETURNING order_id";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // Метод вставки товаров в заказ
    private static void insertOrderItem(Connection con, int orderId, int productId, int amount) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, product_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, amount);
            pstmt.executeUpdate();
        }
    }
}
