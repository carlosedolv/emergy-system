package controller;

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
import services.UserService;

public class ViewController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private TextField txtDay;
    @FXML private TextField txtMonth;
    @FXML private TextField txtYear;
    @FXML private TextField txtFindById;
    @FXML private TextField txtFindByEmail;
    @FXML private TextField txtIdToRemove;
    @FXML private TextField txtEmailToRemove;

    @FXML private Button btnSignUp;
    @FXML private Button btnListUsers;
    @FXML private Button btnResetDb;
    @FXML private Button btnFindById;
    @FXML private Button btnFindByEmail;
    @FXML private Button btnRemoveById;
    @FXML private Button btnRemoveByEmail;

    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            DatabaseInitializer.initDatabase(conn);
            userService = new UserService(conn);
        } catch (Exception e) {
            Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco de dados.", "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnSignUp() {
        try {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            String dayStr = txtDay.getText().trim();
            String monthStr = txtMonth.getText().trim();
            String yearStr = txtYear.getText().trim();

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

            User user = new User(name, email, password, birthday);
            user.setRegistrationDate(LocalDateTime.now());
            int id = userService.registerUser(user);

            Alerts.showAlert("Sucesso", "Cadastrado com sucesso, faça o login.", "", Alert.AlertType.INFORMATION);
            clearFields();

        } catch (IllegalArgumentException e) {
            Alerts.showAlert("Erro de validação", e.getMessage(), "", Alert.AlertType.WARNING);
        } catch (Exception e) {
            Alerts.showAlert("Erro ao salvar", "Erro inesperado: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnShowUsers() {
        try {
            List<User> users = userService.findAll();
            if (users.isEmpty()) {
                System.out.println("Nenhum usuário encontrado.");
            } else {
                System.out.println("Usuários cadastrados:");
                users.forEach(System.out::println);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao buscar usuários: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnClearUsers() {
        try {
            userService.deleteAll();
            Alerts.showAlert("Sucesso", "Todos os usuários foram apagados!", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao apagar usuários: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnFindById() {
        try {
            String idStr = txtFindById.getText().trim();
            if (idStr.isEmpty()) {
                Alerts.showAlert("Campo obrigatório!", "Preencha o ID do usuário", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }
            int id = Integer.parseInt(idStr);
            User user = userService.findById(id);
            if (user == null) {
                Alerts.showAlert("Usuário não encontrado", "Usuário de ID: " + id + " não encontrado.", "", Alert.AlertType.ERROR);
            } else {
                System.out.println("Usuário encontrado: " + user);
                Alerts.showAlert("Sucesso!", "Usuário encontrado.", "", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao buscar usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnFindByEmail() {
        try {
            String email = txtFindByEmail.getText().trim();
            if (email.isEmpty()) {
                Alerts.showAlert("Campo obrigatório!", "Preencha o Email do usuário", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }
            User user = userService.findByEmail(email);
            if (user == null) {
                Alerts.showAlert("Usuário não encontrado", "Usuário de email: " + email + " não encontrado.", "", Alert.AlertType.ERROR);
            } else {
                System.out.println("Usuário encontrado: " + user);
                Alerts.showAlert("Sucesso!", "Usuário encontrado.", "", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao buscar usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnRemoveById() {
        try {
            String idStr = txtIdToRemove.getText().trim();
            if (idStr.isEmpty()) {
                Alerts.showAlert("Campo obrigatório!", "Preencha o ID do usuário", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }
            int id = Integer.parseInt(idStr);
            userService.deleteById(id);
            Alerts.showAlert("Sucesso!", "Usuário removido.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao remover usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnRemoveByEmail() {
        try {
            String email = txtEmailToRemove.getText().trim();
            if (email.isEmpty()) {
                Alerts.showAlert("Campo obrigatório!", "Preencha o email do usuário", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }
            userService.deleteByEmail(email);
            Alerts.showAlert("Sucesso!", "Usuário removido.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao remover usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtDay.clear();
        txtMonth.clear();
        txtYear.clear();
    }
}
