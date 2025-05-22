package controller;

import java.net.URL;
import java.sql.Connection;
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
import model.entities.User;
import repository.SimulationRepository;
import repository.UserRepository;

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

    private SimulationRepository simRepo;
    private UserRepository userRepo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            DatabaseInitializer.initDatabase(conn);
            simRepo = new SimulationRepository(conn);
            userRepo = new UserRepository(conn);
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

            // Verificar se o usuário existe antes de salvar a simulação
            User user = userRepo.findById(userId);
            if (user == null) {
                Alerts.showAlert("Usuário não encontrado", "ID informado não existe.", "", Alert.AlertType.WARNING);
                return;
            }

            Simulation sim = new Simulation(userId, title, data, LocalDateTime.now());
            int id = simRepo.save(sim);
            Alerts.showAlert("Sucesso", "Simulação salva com ID: " + id, "", Alert.AlertType.INFORMATION);
            clearFields();

        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao salvar simulação: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnDeleteById() {
        try {
            int id = Integer.parseInt(txtSimulationIdToDelete.getText().trim());
            simRepo.delete(id);
            Alerts.showAlert("Sucesso", "Simulação deletada.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao deletar: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnDeleteByUserId() {
        try {
            int userId = Integer.parseInt(txtUserIdToDeleteAll.getText().trim());
            simRepo.deleteByUserId(userId);
            Alerts.showAlert("Sucesso", "Todas as simulações do usuário foram deletadas.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao deletar simulações do usuário: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnClearAll() {
        try {
            simRepo.deleteAll();
            Alerts.showAlert("Sucesso", "Todas as simulações foram removidas.", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            Alerts.showAlert("Erro", "Erro ao limpar o banco: " + e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickBtnListByUserId() {
        try {
            int userId = Integer.parseInt(txtUserIdToList.getText().trim());
            List<Simulation> sims = simRepo.findSimulationsByUserId(userId);
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
            List<Simulation> sims = simRepo.findAll();
            if (sims.isEmpty()) {
                System.out.println("Nenhuma simulação encontrada.");
            } else {
                System.out.println("Simulações cadastradas:");
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
