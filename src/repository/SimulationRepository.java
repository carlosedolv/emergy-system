package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.entities.Simulation;

public class SimulationRepository {
    private Connection connection;

    public SimulationRepository(Connection conn) {
        this.connection = conn;
    }

    // Salva uma simulação e retorna o ID gerado
    public int save(Simulation simulation) throws SQLException {
        String sql = "INSERT INTO simulations (user_id, title, liters, type, result, simulation_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, simulation.getUserId());
            stmt.setString(2, simulation.getTitle());
            stmt.setFloat(3, simulation.getLitros());
            stmt.setString(4, simulation.getTipo());
            stmt.setFloat(5, simulation.getResult());
            stmt.setString(6, simulation.getSimulationDate().toString());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir simulação, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    simulation.setId(id);
                    return id;
                } else {
                    throw new SQLException("Falha ao obter ID da simulação.");
                }
            }
        }
    }

    // Busca simulação pelo ID
    public Simulation findById(int id) throws SQLException {
        String sql = "SELECT * FROM simulations WHERE id = ?";
        Simulation sim = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sim = new Simulation(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getFloat("liters"),
                    rs.getString("type"),
                    rs.getFloat("result"),
                    LocalDateTime.parse(rs.getString("simulation_date"))
                );
            }
        }
        return sim;
    }

    // Lista todas as simulações de um usuário
    public List<Simulation> findSimulationsByUserId(int userId) throws SQLException {
        List<Simulation> simulations = new ArrayList<>();
        String sql = "SELECT * FROM simulations WHERE user_id = ? ORDER BY simulation_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Simulation sim = new Simulation(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getFloat("liters"),
                    rs.getString("type"),
                    rs.getFloat("result"),
                    LocalDateTime.parse(rs.getString("simulation_date"))
                );
                simulations.add(sim);
            }
        }

        return simulations;
    }
    
 // Lista todas as simulações do banco
    public List<Simulation> findAll() throws SQLException {
        List<Simulation> simulations = new ArrayList<>();
        String sql = "SELECT * FROM simulations ORDER BY simulation_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Simulation sim = new Simulation(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getFloat("liters"),
                    rs.getString("type"),
                    rs.getFloat("result"),
                    LocalDateTime.parse(rs.getString("simulation_date"))
                );
                simulations.add(sim);
            }
        }

        return simulations;
    }


    // Remove simulação por ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM simulations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nenhuma simulação encontrada com o ID: " + id);
            } else {
                System.out.println("Simulação removida com sucesso. ID: " + id);
            }
        }
    }

    // Remove todas as simulações de um usuário
    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM simulations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    // Deleta todas as simulações do banco (útil para testes ou reset)
    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM simulations";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int affectedRows = stmt.executeUpdate();
            System.out.println("Simulações apagadas: " + affectedRows);
        }
    }
}
