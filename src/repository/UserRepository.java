package repository;

import java.sql.Connection;

import model.entities.User;

public class UserRepository {
	private Connection connection;
	
	public UserRepository(Connection conn) {
		this.connection = conn;
	}
	
	public void save(User user) {
		
	}
	
	public /*User*/ void findByEmail(String email) {
		
	}
	
	public /*User*/ void findById(Integer id) {
		
	}
		
}
