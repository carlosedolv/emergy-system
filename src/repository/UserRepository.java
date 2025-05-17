package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.entities.User;

public class UserRepository {
    private Connection connection;

    public UserRepository(Connection conn) {
        this.connection = conn;
    }

    // Salva um usuário e retorna o ID gerado
    public int save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, birthday, registration_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getBirthday() != null ? user.getBirthday().toString() : null);
            stmt.setString(5, user.getRegistrationDate() != null ? user.getRegistrationDate().toString() : null);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    user.setId(id);
                    return id;
                } else {
                    throw new SQLException("Falha ao obter ID do usuário.");
                }
            }
        }
    }

    // Busca usuário pelo email
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );

                if (rs.getString("birthday") != null) {
                    user.setBirthday(LocalDate.parse(rs.getString("birthday")));
                }
                if (rs.getString("registration_date") != null) {
                    user.setRegistrationDate(LocalDateTime.parse(rs.getString("registration_date")));
                }
            }
        }

        return user;
    }

    // Busca usuário pelo ID
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );

                if (rs.getString("birthday") != null) {
                    user.setBirthday(LocalDate.parse(rs.getString("birthday")));
                }
                if (rs.getString("registration_date") != null) {
                    user.setRegistrationDate(LocalDateTime.parse(rs.getString("registration_date")));
                }
            }
        }

        return user;
    }
    
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nenhum usuário encontrado com o ID: " + id);
            } else {
                System.out.println("Usuário deletado com sucesso. ID: " + id);
            }
        }
    }
    
    
    public void resetIdCounter() throws SQLException {
        String sql = "DELETE FROM sqlite_sequence WHERE name = 'users'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int affectedRows = stmt.executeUpdate();
            System.out.println("Contador de IDs resetado para a tabela users.");
        }
    }
    
    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int affectedRows = stmt.executeUpdate();
            System.out.println("Todos os usuários foram deletados. Total: " + affectedRows);
        }
    }
    
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );

                String birthdayStr = rs.getString("birthday");
                String regDateStr = rs.getString("registration_date");

                if (birthdayStr != null) {
                    user.setBirthday(LocalDate.parse(birthdayStr));
                }

                if (regDateStr != null) {
                    user.setRegistrationDate(LocalDateTime.parse(regDateStr));
                }

                users.add(user);
            }
        }

        return users;
    }

    
    public void deleteAllAndReset() throws SQLException {
        deleteAll();        // Método que apaga todos os usuários
        resetIdCounter();   // Reseta o auto-increment
    }



}
