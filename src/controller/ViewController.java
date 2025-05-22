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
import repository.UserRepository;

public class ViewController implements Initializable {

    @FXML
    private TextField txtName; 
    @FXML
    private TextField txtEmail; 
    @FXML
    private TextField txtPassword; 
    @FXML
    private TextField txtDay;  
    @FXML
    private TextField txtMonth;  
    @FXML
    private TextField txtYear; 
    @FXML
    private TextField txtFindById;
    @FXML
    private TextField txtFindByEmail; 
    @FXML
    private TextField txtIdToRemove;
    @FXML
    private TextField txtEmailToRemove;
    
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnListUsers;
    @FXML
    private Button btnResetDb;
    @FXML
    private Button btnFindById;
    @FXML
    private Button btnFindByEmail;
    @FXML
    private Button btnRemoveById;
    @FXML
    private Button btnRemoveByEmail;

    
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

            if (userRepo.findByEmail(email) != null) {
                Alerts.showAlert("Já existe", "Usuario já existe", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }

            User user = new User(name, email, password, birthday);
            user.setRegistrationDate(LocalDateTime.now());
            int id = userRepo.save(user);

            Alerts.showAlert("Sucesso", "Usuário ID: " + id + " salvo com sucesso!", "", Alert.AlertType.CONFIRMATION);
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
    
    @FXML
    public void onClickBtnFindById() {
        try {
        	String id = txtFindById.getText().trim();
        	if(id.isEmpty()) {
        		Alerts.showAlert("Campo obrigatório!", "Preencha o ID do usúario", "Tente novamente.", Alert.AlertType.WARNING);
        	} else {
        		User user = userRepo.findById(Integer.parseInt(id));
        		if(user == null) {
        			Alerts.showAlert("Usúario não encontrado", "Usúario de ID: " + id + " não encontrado.", "", Alert.AlertType.ERROR);
        		} else {
        			System.out.println("Finded user: " + user.getName() + " " + user.getEmail() + " " + user.getBirthday() + " " + user.getSimulations().toString());
        			Alerts.showAlert("Sucesso!", "Usúario encontrado.", "", Alert.AlertType.INFORMATION);
        		}
        	}
            
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao apagar usuários: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onClickBtnFindByEmail() {
        try {
            String email = txtFindByEmail.getText().trim(); // ✅ CORRIGIDO
            if (email.isEmpty()) {
                Alerts.showAlert("Campo obrigatório!", "Preencha o Email do usuário", "Tente novamente.", Alert.AlertType.WARNING);
                return;
            }

            User user = userRepo.findByEmail(email);
            if (user == null) {
                Alerts.showAlert("Usuário não encontrado", "Usuário de email: " + email + " não encontrado.", "", Alert.AlertType.ERROR);
            } else {
                System.out.println("Usuário encontrado: " + user.getName() + " " + user.getEmail());
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
        	String id = txtIdToRemove.getText().trim();
        	if(id.isEmpty()) {
        		Alerts.showAlert("Campo obrigatório!", "Preencha o ID do usúario", "Tente novamente.", Alert.AlertType.WARNING);
        	} else {
        		User user = userRepo.findById(Integer.parseInt(id));
        		if(user == null) {
        			Alerts.showAlert("Usúario não encontrado", "Usúario de ID: " + id + " não encontrado.", "", Alert.AlertType.ERROR);
        		} else {
        			userRepo.deleteById(Integer.parseInt(id));
        			Alerts.showAlert("Sucesso!", "Usúario encontrado.", "", Alert.AlertType.INFORMATION);
        		}
        	}
            
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao apagar usuários: " + e.getMessage(), "", Alert.AlertType.ERROR);
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

            User user = userRepo.findByEmail(email);
            if (user == null) {
                Alerts.showAlert("Usuário não encontrado", "Usuário de email: " + email + " não encontrado.", "", Alert.AlertType.ERROR);
            } else {
                userRepo.deleteByEmail(email);
                Alerts.showAlert("Sucesso!", "Usuário removido com sucesso.", "", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao apagar usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
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
