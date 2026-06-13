package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    // Alamat URL untuk menyambungkan Java ke database db_perpustakaan di MySQL XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/db_perpustakaan?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Baris CRITICAL: Memaksa Java untuk memuat Driver MySQL Connector/J yang baru kamu pasang
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver tidak ditemukan! Pastikan .jar sudah ada di Dependencies IntelliJ.");
        }
        // Menghubungkan langsung ke localhost MySQL
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}