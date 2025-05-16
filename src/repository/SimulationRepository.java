package repository;

import java.sql.Connection;
import java.sql.SQLException;

import model.entities.Simulation;
import model.entities.User;

public class SimulationRepository {
	private Connection connection;
	
	public SimulationRepository(Connection conn) {
		this.connection = conn;
	}
	
	public void save(Simulation simulation) throws SQLException {
		
	}
	
	public void delete(Integer id) {
		
	}

	public /*List<Simulation>*/ void findSimulationsByUser(User user) {
		
	}
	
	public /*Simulation*/ void findSimulationById(Integer id) {
		
	}

}
