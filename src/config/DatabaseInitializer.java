package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    birthday TEXT,
                    registration_date TEXT
                );
            """;

            String createSimulations = """
                CREATE TABLE IF NOT EXISTS simulations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    liters float(6,2) NOT NULL,
                    type TEXT NOT NULL,
                    result float(8,3) NOT NULL,
                    simulation_date TEXT,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
            """;

            stmt.execute(createUsers);
            stmt.execute(createSimulations);

            System.out.println("Banco de dados inicializado.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}