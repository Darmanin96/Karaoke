package org.example.Conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static HikariDataSource dataSource;


    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/mysql");
            config.setUsername("root");
            config.setPassword("");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            dataSource = new HikariDataSource(config);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE 'karaoke'");
                if (!resultSet.next()) {
                    statement.executeUpdate("CREATE DATABASE karaoke");
                    System.out.println("Base de datos 'karaoke' creada.");
                } else {
                    System.out.println("La base de datos 'karaoke' ya existe.");
                }
            }
            config.setJdbcUrl("jdbc:mysql://localhost:3306/karaoke");
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            System.err.println("Error configurando la conexión: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}


