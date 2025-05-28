package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import config.DatabaseConfig;
import config.DatabaseInitializer;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import model.entities.Simulation;
import services.SimulationService;

public class MethodsController implements Initializable {

    @FXML private TextField txtUserId;
    @FXML private TextField txtTitle;
    @FXML private TextField txtLitros;
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

            UnaryOperator<Change> filter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*([\\.]\\d*)?")) {
                    return change;
                }
                return null;	
            };
            txtLitros.setTextFormatter(new TextFormatter<>(filter));
            
        } catch (Exception e) {
            Alerts.showAlert("Erro de conexão", "Não foi possível conectar ao banco.", e.getMessage(), Alert.AlertType.ERROR);
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
    public void onClickBtnListByUserId() {
        try {
            int userId = Integer.parseInt(txtUserIdToList.getText().trim());
            List<Simulation> sims = simulationService.findByUserId(userId);
            if (sims.isEmpty()) {
                System.out.println("Nenhuma simulação encontrada para o usuário " + userId);
            } else {
                sims.forEach(System.out::println);
            }
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao listar simulações: " + e.getMessage(), "", Alert.AlertType.ERROR);
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
