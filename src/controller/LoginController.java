package controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import application.App;
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

public class LoginController implements Initializable {

    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    private App mainApp;
    private int id;

    @FXML private Button btnSignIp;

    private UserService userService;
    
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }
    
    public int getUserId() {
    	return this.id;
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
    public void onClickBtnSignIn() {
        try {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Alerts.showAlert("Campos obrigatórios", "Preencha Email e Senha.", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }
            
            this.id = userService.login(email, password);
            if (id==0) {
                Alerts.showAlert("Inválido", "Email ou senha inválidos.", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }

            Alerts.showAlert("Sucesso", "Logado com sucesso. seja bem vindo(a)!", "", Alert.AlertType.INFORMATION);
            clearFields();
            
            
            mainApp.showProfileView(id);;

        } catch (IllegalArgumentException e) {
            Alerts.showAlert("Erro de validação", e.getMessage(), "", Alert.AlertType.WARNING);
        } catch (Exception e) {
            Alerts.showAlert("Erro ao salvar", "Erro inesperado: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
    }
}
