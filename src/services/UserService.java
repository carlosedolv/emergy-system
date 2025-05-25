package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import model.entities.User;
import repository.UserRepository;
import util.PasswordUtil;

public class UserService {

    private UserRepository userRepository;

    public UserService(Connection conn) {
        this.userRepository = new UserRepository(conn);
    }

    // Verifica se o e-mail já está cadastrado
    public boolean emailExists(String email) throws SQLException {
    	if(userRepository.findByEmail(email) == null) return false;
        return true;
    }

    // Cadastro de novo usuário com validações
    public int registerUser(User user) throws SQLException {
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email inválido.");
        }

        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Senha muito fraca (mínimo 6 caracteres).");
        }

        // Criptografa a senha antes de salvar
        String hashed = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    // Login: compara senha fornecida com hash
    public Integer login(String email, String plainPassword) throws SQLException {
        User user = userRepository.findByEmail(email);
        if (user != null && PasswordUtil.checkPassword(plainPassword, user.getPassword())) {
            return user.getId();
        } else {
        	return 0;
        }
    }

    // Busca usuário por ID
    public User findById(int id) throws SQLException {
        return userRepository.findById(id);
    }

    // Busca usuário por email
    public User findByEmail(String email) throws SQLException {
        return userRepository.findByEmail(email);
    }

    // Lista todos os usuários
    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }

    // Remove usuário por ID
    public void deleteById(int id) throws SQLException {
        userRepository.deleteById(id);
    }

    // Remove usuário por email
    public void deleteByEmail(String email) throws SQLException {
        userRepository.deleteByEmail(email);
    }

    // Remove todos os usuários e reseta contador
    public void deleteAll() throws SQLException {
        userRepository.deleteAll();
        userRepository.resetIdCounter();
    }

    // Validação de e-mail com regex
    private boolean isValidEmail(String email) {
        String regex = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
        return email != null && Pattern.matches(regex, email);
    }

    // Validação simples de senha
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}