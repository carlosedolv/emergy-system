package controller;

import java.net.URL;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Simulation;
import services.SimulationService;

public class SimulationViewController implements Initializable {

    @FXML private TextField txtUserId;
    @FXML private TextField txtTitle;
    @FXML private TextArea txtSimulationData;
    @FXML private TextField txtSimulationIdToDelete;
    @FXML private TextField txtUserIdToDeleteAll;
    @FXML private TextField txtUserIdToList;
    @FXML private Button btnSaveSimulation;
    @FXML private Button btnDeleteById;
    @FXML private Button btnDeleteByUserId;
    @FXML private Button btnClearAllSimulations;
    @FXML private Button btnListByUser;
    @FXML private Button btnListAllSimulations;

    private SimulationService simulationService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            simulationService = new SimulationService(DatabaseConfig.getConnection());
            DatabaseInitializer.initDatabase(DatabaseConfig.getConnection());
        } catch (Exception e) {
            Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnSaveSimulation() {
        try {
            int userId = Integer.parseInt(txtUserId.getText().trim());
            String title = txtTitle.getText().trim();
            String data = txtSimulationData.getText().trim();

            if (title.isEmpty() || data.isEmpty()) {
                Alerts.showAlert("Campos obrigatórios", "Preencha título e dados da simulação.", "", Alert.AlertType.WARNING);
                return;
            }

            int simId = simulationService.createSimulation(new Simulation(userId, title, data, LocalDateTime.now()));
            Alerts.showAlert("Sucesso", "Simulação salva com ID: " + simId, "", Alert.AlertType.INFORMATION);
            clearFields();

        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao salvar simulação: " + e.getMessage(), "", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBtnDeleteById() {
        try {
            int id = Integer.parseInt(txtSimulationIdToDelete.getText().trim());
            simulationService.deleteById(id);
            Alerts.showAlert("Sucesso", "Simulação deletada.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao deletar simulação: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnDeleteByUserId() {
        try {
            int userId = Integer.parseInt(txtUserIdToDeleteAll.getText().trim());
            simulationService.deleteByUserId(userId);
            Alerts.showAlert("Sucesso", "Simulações do usuário deletadas.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao deletar simulações do usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnClearAll() {
        try {
            simulationService.deleteAll();
            Alerts.showAlert("Sucesso", "Todas as simulações foram removidas.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao limpar banco: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnListByUserId() {
        try {
            int userId = Integer.parseInt(txtUserIdToList.getText().trim());
            List<Simulation> sims = simulationService.findByUserId(userId);
            if (sims.isEmpty()) {
                System.out.println("Nenhuma simulação encontrada para o usuário " + userId);
            } else {
                System.out.println("Simulações do usuário " + userId + ":");
                sims.forEach(System.out::println);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao listar simulações: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnListAll() {
        try {
            List<Simulation> sims = simulationService.findAll();
            if (sims.isEmpty()) {
                System.out.println("Nenhuma simulação encontrada.");
            } else {
                System.out.println("Todas as simulações:");
                sims.forEach(System.out::println);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao listar todas as simulações: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        txtUserId.clear();
        txtTitle.clear();
        txtSimulationData.clear();
    }
}
