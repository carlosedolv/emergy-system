package gui;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import config.DatabaseConfig;
import config.DatabaseInitializer;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.User;
import repository.UserRepository;

public class ViewController implements Initializable {

    @FXML
    private TextField txtName; // Name
    @FXML
    private TextField txtEmail; // Email
    @FXML
    private TextField txtPassword; // Password
    @FXML
    private TextField day;   // Day
    @FXML
    private TextField month;  // Month
    @FXML
    private TextField year; // Year
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnListUsers;
    @FXML
    private Button btnResetDb;

    private UserRepository userRepo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            DatabaseInitializer.initDatabase(conn);
            userRepo = new UserRepository(conn);
        } catch (Exception e) {
            Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco de dados.", "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnLogin() {
        try {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            String dayStr = day.getText().trim();
            String monthStr = month.getText().trim();
            String yearStr = year.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Alerts.showAlert("Campos obrigatórios", "Preencha Nome, Email e Senha.", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }

            LocalDate birthday = null;
            if (!dayStr.isEmpty() && !monthStr.isEmpty() && !yearStr.isEmpty()) {
                int day = Integer.parseInt(dayStr);
                int month = Integer.parseInt(monthStr);
                int year = Integer.parseInt(yearStr);
                birthday = LocalDate.of(year, month, day);
            }

            if (userRepo.findByEmail(email) != null) {
                Alerts.showAlert("Já existe", "Usuario já existe", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }

            User user = new User(name, email, password, birthday);
            user.setRegistrationDate(LocalDateTime.now());
            int id = userRepo.save(user);

            Alerts.showAlert("Sucesso", "Usuário salvo com sucesso!", "", Alert.AlertType.CONFIRMATION);
            clearFields();

        } catch (NumberFormatException e) {
            Alerts.showAlert("Data inválida", "Digite uma data de nascimento válida.", "", Alert.AlertType.ERROR);
        } catch (Exception e) {
            Alerts.showAlert("Erro ao salvar", "Erro inesperado: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onClickBtnShowUsers() {
        try {
            List<User> users = userRepo.findAll();

            if (users.isEmpty()) {
                System.out.println("Nenhum usuário encontrado.");
            } else {
                System.out.println("Usuários cadastrados:");
                for (User user : users) {
                    System.out.println(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuários: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onClickBtnClearUsers() {
        try {
            userRepo.deleteAll();
            Alerts.showAlert("Sucesso", "Todos os usuários foram apagados!", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao apagar usuários: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtPassword.clear();
        day.clear();
        month.clear();
        year.clear();
    }
}
