package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.App;
import config.DatabaseConfig;
import config.DatabaseInitializer;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import model.entities.User;
import repository.UserRepository;
import services.SimulationService;
import services.UserService;

public class ProfileController implements Initializable {

	private App mainApp;
	private User user;
	private int userId;
	private SimulationService simulationService;
	
	@FXML private Label lblName;
	@FXML private Label lblEmail;
	@FXML private Label lblBirthDay;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}

	public void setUserId(int userId) throws SQLException {
        Connection conn = DatabaseConfig.getConnection();
        DatabaseInitializer.initDatabase(conn);
        UserRepository userRepository = new UserRepository(conn);
		
		this.userId = userId;
		this.user = userRepository.findById(userId);
		
		lblName.setText(user.getName());
		lblEmail.setText(user.getEmail());
		lblBirthDay.setText(user.getBirthday().toString());
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			simulationService = new SimulationService(DatabaseConfig.getConnection());
			DatabaseInitializer.initDatabase(DatabaseConfig.getConnection());

		} catch (Exception e) {
			Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco.", e.getMessage(),
					Alert.AlertType.ERROR);
		}
	}

	@FXML
	public void onClickBtnRelatorio() {
		mainApp.showReportsView(userId);
	}

	@FXML
	public void onClickBtnNovoCalculo() {
		mainApp.showSimulationView(userId);
	}

	@FXML
	public void onClickBtnLogout() {
		mainApp.showUserView();
	}

}
