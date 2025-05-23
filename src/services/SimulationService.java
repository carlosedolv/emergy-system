package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import model.entities.Simulation;
import model.entities.User;
import repository.SimulationRepository;
import repository.UserRepository;

public class SimulationService {

    private SimulationRepository simulationRepository;
    private UserRepository userRepository;

    public SimulationService(Connection conn) {
        this.simulationRepository = new SimulationRepository(conn);
        this.userRepository = new UserRepository(conn);
    }

    // Cria uma nova simulação (verifica se o usuário existe antes)
    public int createSimulation(Simulation simulation) throws SQLException {
        User user = userRepository.findById(simulation.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("Usuário com ID " + simulation.getUserId() + " não encontrado.");
        }

        simulation.setSimulationDate(LocalDateTime.now());
        return simulationRepository.save(simulation);
    }

    // Busca simulação por ID
    public Simulation findById(int id) throws SQLException {
        return simulationRepository.findById(id);
    }

    // Lista todas as simulações de um usuário
    public List<Simulation> findByUserId(int userId) throws SQLException {
        return simulationRepository.findSimulationsByUserId(userId);
    }

    // Lista todas as simulações do banco
    public List<Simulation> findAll() throws SQLException {
        return simulationRepository.findAll();
    }

    // Remove simulação por ID
    public void deleteById(int id) throws SQLException {
        simulationRepository.delete(id);
    }

    // Remove todas as simulações de um usuário
    public void deleteByUserId(int userId) throws SQLException {
        simulationRepository.deleteByUserId(userId);
    }

    // Remove todas as simulações do banco
    public void deleteAll() throws SQLException {
        simulationRepository.deleteAll();
    }
}
