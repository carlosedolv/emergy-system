package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import model.entities.Simulation;
import services.SimulationService;
import util.ChartGenerator;
import util.PdfExporter;



public class ReportsController implements Initializable {

	private App mainApp;
	private int userId;
	private int simId;
	private ObservableList<Simulation> observableSimulation;
	private SimulationService simulationService;
	
	@FXML private Label lblTitulo;
	@FXML private Label lblTipo;
	@FXML private Label lblLitros;
	@FXML private Label lblResultado;
	@FXML private Button btnExport;
	@FXML private Button btnLogout;
	@FXML private Button btnCalculo;
	@FXML private CheckBox chkBar;
	@FXML private CheckBox chkPie;
	@FXML private CheckBox chkLine;
	@FXML private ListView<Simulation> listViewReports;

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
			
			listViewReports.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);



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
	public void onClickBtnPerfil() throws SQLException {
		mainApp.showProfileView(userId);
	}
	
	@FXML
	public void onClickBtnExportarSimulacaoo() {
		try {
			Simulation sim = listViewReports.getSelectionModel().getSelectedItem();

			if (sim == null) {
				Alerts.showAlert("Erro", "Nenhuma simulação selecionada", "Por favor selecione uma simulação para exportar.", Alert.AlertType.WARNING);
				return;
			}

			// Gera o gráfico
			BufferedImage chartImage = ChartGenerator.generateChart(sim, 400, 400);

			// Exporta o
			String fileName = "exports/Simulacao_" + sim.getId() + "_" + sim.getTitle().replaceAll("\\s+", "_") + ".pdf";
			File fileDestination = new File(fileName);

			// Cria a pasta caso não exista
			fileDestination.getParentFile().mkdirs();

			// Exporta a simulação
			// PdfExporter.exportSimulationComparativo(sim, chartImage, fileDestination);
			
			Alerts.showAlert("Exportação concluída", "PDF exportado com sucesso!", "", Alert.AlertType.INFORMATION);

		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao exportar simulação", e.getMessage(), Alert.AlertType.ERROR);
			e.printStackTrace();
		}
		
		
	}
	

	
	@FXML
	public void onClickBtnExportarSimulacoess() {
	    try {
	        List<Simulation> selectedSims = listViewReports.getSelectionModel().getSelectedItems();

	        if (selectedSims == null || selectedSims.isEmpty()) {
	            Alerts.showAlert("Erro", "Nenhuma simulação selecionada", "Selecione uma ou mais simulações para exportar.", Alert.AlertType.WARNING);
	            return;
	        }

	        List<BufferedImage> charts = new ArrayList<>();

	        if (chkBar.isSelected()) {
	            charts.add(ChartGenerator.generateChart(selectedSims, 600, 400));
	        }

	        if (chkPie.isSelected()) {
	            charts.add(ChartGenerator.generatePieChart(selectedSims, 600, 400));
	        }

	        if (chkLine.isSelected()) {
	            charts.add(ChartGenerator.generateLineChart(selectedSims, 600, 400));
	        }

	        if (charts.isEmpty()) {
	            Alerts.showAlert("Aviso", "Nenhum gráfico selecionado", "Selecione pelo menos um tipo de gráfico.", Alert.AlertType.WARNING);
	            return;
	        }

	        // Gera nome do arquivo
	        String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
	        String fileName = "exports/Comparativo_" + selectedSims.size() + "_simulacoes_" + timestamp + ".pdf";
	        File fileDestination = new File(fileName);
	        fileDestination.getParentFile().mkdirs();

	        // Chamada corrigida (método do PdfExporter precisa aceitar múltiplas imagens)
	        PdfExporter.exportSimulationComparativo(selectedSims, charts, fileDestination);

	        Alerts.showAlert("Exportação concluída", "PDF exportado com sucesso!", "", Alert.AlertType.INFORMATION);

	    } catch (Exception e) {
	        Alerts.showAlert("Erro", "Erro ao exportar simulações", e.getMessage(), Alert.AlertType.ERROR);
	        e.printStackTrace();
	    }
	}

	@FXML
	public void onClickBtnExportarSimulacoes() {
	    try {
	        List<Simulation> selectedSims = listViewReports.getSelectionModel().getSelectedItems();

	        if (selectedSims == null || selectedSims.isEmpty()) {
	            Alerts.showAlert("Erro", "Nenhuma simulação selecionada", "Selecione uma ou mais simulações para exportar.", Alert.AlertType.WARNING);
	            return;
	        }

	        // Gera os gráficos (os que estiverem marcados)
	        List<BufferedImage> charts = new ArrayList<>();
	        if (chkPie.isSelected()) charts.add(ChartGenerator.generateChart(selectedSims, 600, 400));
	        if (chkLine.isSelected()) charts.add(ChartGenerator.generatePieChart(selectedSims, 600, 400));
	        if (chkBar.isSelected()) charts.add(ChartGenerator.generateLineChart(selectedSims, 600, 400));

	        // Abre o FileChooser para selecionar o destino
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Salvar Relatório PDF");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
	        fileChooser.setInitialFileName("Relatorio_Simulacoes.pdf");
	        File fileDestination = fileChooser.showSaveDialog(null);

	        if (fileDestination == null) {
	            return; // usuário cancelou
	        }

	        // Exporta o PDF
	        PdfExporter.exportSimulationComparativo(selectedSims, charts, fileDestination);

	        Alerts.showAlert("Exportação concluída", "PDF exportado com sucesso!", "", Alert.AlertType.INFORMATION);

	    } catch (Exception e) {
	        Alerts.showAlert("Erro", "Erro ao exportar simulações", e.getMessage(), Alert.AlertType.ERROR);
	        e.printStackTrace();
	    }
	}



}
