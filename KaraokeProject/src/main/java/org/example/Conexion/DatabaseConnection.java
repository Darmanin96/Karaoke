package org.example.Conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
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

            // Verificar y crear la base de datos si no existe
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS karaoke");
                System.out.println("Base de datos 'karaoke' asegurada.");
            }

            // Reconfigurar HikariCP para usar la base de datos 'karaoke'
            config.setJdbcUrl("jdbc:mysql://localhost:3306/karaoke");
            dataSource = new HikariDataSource(config);

            // Ejecutar el script SQL desde un archivo
            executeSqlFromFile("src/main/resources/SQL/Karaoke.sql");
        } catch (Exception e) {
            System.err.println("Error configurando la conexión: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void executeSqlFromFile(String filePath) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            // Leer el archivo línea por línea
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("--")) { // Ignorar líneas vacías y comentarios
                    sqlBuilder.append(line).append(" ");
                    if (line.endsWith(";")) { // Ejecutar la sentencia cuando termina con ';'
                        String sql = sqlBuilder.toString();
                        System.out.println("Ejecutando SQL: " + sql); // Imprime la sentencia SQL
                        statement.execute(sql);
                        sqlBuilder.setLength(0); // Limpiar el acumulador
                    }
                }
            }
            System.out.println("Script SQL ejecutado con éxito desde: " + filePath);

        } catch (IOException e) {
            System.err.println("Error leyendo el archivo SQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error ejecutando el script SQL: " + e.getMessage());
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
