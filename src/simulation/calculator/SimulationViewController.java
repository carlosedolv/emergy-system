package simulation.calculator;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import application.App;
import config.DatabaseConfig;
import config.DatabaseInitializer;
import controller.LoginController;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import model.entities.Simulation;
import services.SimulationService;

public class SimulationViewController implements Initializable {

	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtLitros;
	@FXML
	private Button btnSaveSimulation;
	@FXML
	private Button btnListAllSimulations;
	@FXML
	private RadioButton rBtnEtanol;
	@FXML
	private RadioButton rBtnBDieselB;
	@FXML
	private RadioButton rBtnBDieselM;
	@FXML
	private TextField txtResult;
	@FXML
	private ToggleGroup bioCombustivel;
	private int userId;
	private float result;
	final private float emergiaLitroEtanol = 1.837f;
	final private float emergiaLitroBiodiselB = 1.354f;
	final private float emergiaLitroBiodiselM = 1.960f;

	private SimulationService simulationService;
	private App mainApp;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
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

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@FXML
	public void onClickBtnSaveSimulation() {
		try {
			String title = txtTitle.getText().trim();
			float litros = Float.parseFloat(txtLitros.getText().trim());
			Toggle selecionado = bioCombustivel.getSelectedToggle();
			RadioButton rb = (RadioButton) selecionado;
			String tipo = rb.getText();

			if (title.isEmpty() || litros == 0 || selecionado == null) {
				Alerts.showAlert("Campos obrigatórios", "Preencha título, quantidade de litros e o tipo de combustível.",
						"", Alert.AlertType.WARNING);
				return;
			}

			switch (tipo) {

			case "Etanol":
				result = litros * emergiaLitroEtanol;
				break;
			case "BDiesel Babaçu":
				result = litros * emergiaLitroBiodiselB;
				break;
			case "BDiesel Macaúba":
				result = litros * emergiaLitroBiodiselM;
				break;
			}

			txtResult.setText(result + " x 10¹² sej");

			int simId = simulationService
					.createSimulation(new Simulation(userId, title, litros, tipo, result, LocalDateTime.now()));
			Alerts.showAlert("Sucesso", "Simulação salva com ID: " + simId, "", Alert.AlertType.INFORMATION);
			clearFields();

		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao salvar simulação: " + e.getMessage(), "", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML
	public void onClickBtnListAll() {
		try {
/*			List<Simulation> sims = simulationService.findByUserId(userId);
			if (sims.isEmpty()) {
				System.out.println("Nenhuma simulação encontrada.");
            	Alerts.showAlert("Erro", "Simulações não encontradas", "Este usuário não possui simulações", Alert.AlertType.WARNING);
			} else {
				System.out.println("Todas as simulações:");
				sims.forEach(System.out::println);
*/				mainApp.showReportsView(userId);
//			}
			
		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao listar todas as simulações: " + e.getMessage(), "",
					Alert.AlertType.ERROR);
		}
	}

	private void clearFields() {
		txtTitle.clear();
		txtLitros.clear();
	}
}
