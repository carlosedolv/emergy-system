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
	@FXML
	private Label lblName;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblBirthDay;

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

	/*
	 * @FXML public void onClickBtnSaveSimulation() { try { int userId =
	 * Integer.parseInt(txtUserId.getText().trim()); String title =
	 * txtTitle.getText().trim(); String data = txtSimulationData.getText().trim();
	 * 
	 * if (title.isEmpty() || data.isEmpty()) {
	 * Alerts.showAlert("Campos obrigatórios",
	 * "Preencha título e dados da simulação.", "", Alert.AlertType.WARNING);
	 * return; }
	 * 
	 * int simId = simulationService.createSimulation(new Simulation(userId, title,
	 * data, LocalDateTime.now())); Alerts.showAlert("Sucesso",
	 * "Simulação salva com ID: " + simId, "", Alert.AlertType.INFORMATION);
	 * clearFields();
	 * 
	 * } catch (Exception e) { Alerts.showAlert("Erro", "Erro ao salvar simulação: "
	 * + e.getMessage(), "", Alert.AlertType.ERROR); e.printStackTrace(); } }
	 */

	/*
	 * @FXML public void onClickBtnDeleteByUserId() { try { int userId =
	 * Integer.parseInt(txtUserIdToDeleteAll.getText().trim());
	 * simulationService.deleteByUserId(userId); Alerts.showAlert("Sucesso",
	 * "Simulações do usuário deletadas.", "", Alert.AlertType.INFORMATION); } catch
	 * (Exception e) { Alerts.showAlert("Erro",
	 * "Erro ao deletar simulações do usuário: " + e.getMessage(), "",
	 * Alert.AlertType.ERROR); } }
	 * 
	 * @FXML public void onClickBtnClearAll() { try { simulationService.deleteAll();
	 * Alerts.showAlert("Sucesso", "Todas as simulações foram removidas.", "",
	 * Alert.AlertType.INFORMATION); } catch (Exception e) {
	 * Alerts.showAlert("Erro", "Erro ao limpar banco: " + e.getMessage(), "",
	 * Alert.AlertType.ERROR); } }
	 * 
	 * @FXML public void onClickBtnListAll() { try { List<Simulation> sims =
	 * simulationService.findAll(); if (sims.isEmpty()) {
	 * System.out.println("Nenhuma simulação encontrada."); } else {
	 * System.out.println("Todas as simulações:");
	 * sims.forEach(System.out::println); } } catch (Exception e) {
	 * Alerts.showAlert("Erro", "Erro ao listar todas as simulações: " +
	 * e.getMessage(), "", Alert.AlertType.ERROR); } }
	 * 
	 * private void clearFields() { txtUserId.clear(); txtTitle.clear();
	 * txtSimulationData.clear(); }
	 */
}
