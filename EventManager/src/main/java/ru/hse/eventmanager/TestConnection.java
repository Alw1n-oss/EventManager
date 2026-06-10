package ru.hse.eventmanager;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/event_manager?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "eventmanager";
        String password = "event123";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("УСПЕХ! Подключение к MySQL работает!");
            conn.close();
        } catch (Exception e) {
            System.out.println("ОШИБКА: " + e.getMessage());
            e.printStackTrace();
        }
    }
}