package controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.App;
import config.DatabaseConfig;
import config.DatabaseInitializer;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.entities.User;
import services.UserService;

public class CadastroController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtDay;
    @FXML private TextField txtMonth;
    @FXML private TextField txtYear;
    @FXML private Button btnSignUp;
    
    private App mainApp;
    private UserService userService;
    
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

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

            Alerts.showAlert("Sucesso", "Você está cadastrado! Faça o login.", "", Alert.AlertType.INFORMATION);
            clearFields();
            
            mainApp.showLoginView();
         
        } catch (IllegalArgumentException e) {
            Alerts.showAlert("Erro de validação", e.getMessage(), "", Alert.AlertType.WARNING);
        } catch (Exception e) {
            Alerts.showAlert("Erro ao salvar", "Erro inesperado: " + e.getMessage(), "", Alert.AlertType.ERROR);
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
