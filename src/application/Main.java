package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import config.DatabaseConfig;
import config.DatabaseInitializer;
import model.entities.User;
import repository.UserRepository;

public class Main {
	
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            DatabaseInitializer.initDatabase(conn);

            UserRepository userRepo = new UserRepository(conn);

            // 1. Cria novo usuário
            User user = new User("Carlos", "carlos@email.com", "senha123", LocalDate.of(2000, 5, 20));
            user.setRegistrationDate(LocalDateTime.now());
            
            int generatedId = userRepo.save(user);
            System.out.println("Usuário salvo com sucesso! ID: " + generatedId);

            // 2. Busca o usuário pelo ID
            User encontrado = userRepo.findById(generatedId);

            // 3. Mostra resultado
            if (encontrado != null) {
                System.out.println("Usuário encontrado: " + encontrado.getName() +
                        " | Email: " + encontrado.getEmail() +
                        " | Nascimento: " + encontrado.getBirthday() +
                        " | Registro: " + encontrado.getRegistrationDate());
            } else {
                System.out.println("Usuário não encontrado pelo ID.");
            }
            
                       
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
