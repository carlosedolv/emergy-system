package config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String DB_NAME = "emergy_system.db";

    // Caminho seguro na pasta Documentos do usuário
    private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "emergy-system";
    private static final String DB_PATH = DB_FOLDER + File.separator + DB_NAME;
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        ensureDatabaseLocation();

        Connection conn = DriverManager.getConnection(URL);
        enableForeignKeys(conn);
        return conn;
    }

    private static void enableForeignKeys(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    private static void ensureDatabaseLocation() {
        try {
            File dbDir = new File(DB_FOLDER);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                // Opcional: copiar um banco modelo da pasta onde o app foi instalado
                File appDir = new File(System.getProperty("user.dir"));
                File defaultDb = new File(appDir, DB_NAME);

                if (defaultDb.exists()) {
                    Files.copy(defaultDb.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    // SQLite vai criar um novo arquivo vazio no primeiro acesso, então não precisa criar manualmente
                    System.out.println("Nenhum banco modelo encontrado. Um novo será criado automaticamente.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao preparar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
