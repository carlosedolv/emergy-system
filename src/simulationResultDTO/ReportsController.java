package simulationResultDTO;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import application.App;
import config.DatabaseConfig;
import config.DatabaseInitializer;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Simulation;
import services.SimulationService;
import simulation.calculator.SimulationViewController;

public class ReportsController implements Initializable {

	private App mainApp;
	private int userId;
	private int simId;
	private ObservableList<Simulation> observableSimulation;
	@FXML
	private ListView<Simulation> listViewReports;
	private SimulationService simulationService;
	@FXML
	private Label lblTitulo;
	@FXML
	private Label lblTipo;
	@FXML
	private Label lblLitros;
	@FXML
	private Label lblResultado;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}

	public void setUserId(int userId) {
		this.userId = userId;
		loadListSimulationView();
	}

	public void setSimId(Simulation sim) {
		this.simId = sim.getId();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			simulationService = new SimulationService(DatabaseConfig.getConnection());
			DatabaseInitializer.initDatabase(DatabaseConfig.getConnection());

			listViewReports.getSelectionModel().selectedItemProperty()
					.addListener((observable, OldValue, NewValue) -> selecteItemListViewSimulation(NewValue));

		} catch (Exception e) {
			Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco.", e.getMessage(),
					Alert.AlertType.ERROR);
		}
	}

	public void loadListSimulationView() {

		try {
			System.out.println("userId: " + userId);
			List<Simulation> sims = simulationService.findByUserId(userId);
			sims.forEach(System.out::println);
			observableSimulation = FXCollections.observableArrayList(sims);
			listViewReports.setItems(observableSimulation);

			listViewReports.setCellFactory(param -> new ListCell<Simulation>() {
				@Override
				protected void updateItem(Simulation sim, boolean empty) {
					super.updateItem(sim, empty);
					if (empty || sim == null) {
						setText(null);
					} else {
						setText(sim.getTitle());
					}
				}
			});
		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao listar todas as simulações: " + e.getMessage(), "",
					Alert.AlertType.ERROR);
		}

	}

	public void selecteItemListViewSimulation(Simulation sims) {
		lblTitulo.setText(sims.getTitle());
		lblTipo.setText(sims.getTipo());
		lblLitros.setText(String.format("%.2f", sims.getLitros()));
		lblResultado.setText(String.format("%.2f", sims.getResult()) + " x 10¹² sej");
	}

	@FXML
	public void onClickBtnDeleteById() {
		try {
			Simulation sim = listViewReports.getSelectionModel().getSelectedItem();
			System.out.println(simId);
			simulationService.deleteById(sim.getId());
			loadListSimulationView();
			Alerts.showAlert("Sucesso", "Simulação deletada.", "", Alert.AlertType.INFORMATION);
		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao deletar simulação: ", "Nenhuma simulação selecionada",Alert.AlertType.ERROR);
		}
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
