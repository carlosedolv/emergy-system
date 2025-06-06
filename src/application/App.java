package application;

import java.io.IOException;
import java.sql.SQLException;

import controller.CadastroController;
import controller.LoginController;
import controller.ProfileController;
import controller.ReportsController;
import controller.SimulationViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.entities.User;

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    @FXML private StackPane rootPane;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Simulações");

        initRootLayout();
        showUserView();
    }

    private void initRootLayout() {
        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout, 900, 500);

        // Criar barra de menu
        MenuBar menuBar = new MenuBar();

        Menu menuView = new Menu("Menu");

        MenuItem userView = new MenuItem("Emergia");
        userView.setOnAction(e -> showUserView());
        
        MenuItem LoginView = new MenuItem("Login");
        LoginView.setOnAction(e -> showLoginView());
        
        MenuItem CadastroView = new MenuItem("Cadastro");
        CadastroView.setOnAction(e -> showCadastroView());

        menuView.getItems().addAll(userView, LoginView, CadastroView);
        menuBar.getMenus().add(menuView);

        rootLayout.setTop(menuBar);

        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public void showUserView() {
        try {
            Parent userView = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
            rootLayout.setCenter(userView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showLoginView() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        	Parent loginView = loader.load();
        	
        	LoginController controller = loader.getController();
        	controller.setMainApp(this);
        	controller.getUserId();
        	
            rootLayout.setCenter(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showProfileView(int userId) throws SQLException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProfileView.fxml"));
        	Parent profileView = loader.load();
        	
        	ProfileController controller = loader.getController();
        	
        	try {
                controller.setUserId(userId);
            } catch (SQLException e) {
                System.err.println("Erro ao definir o userId no ProfileController: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        	
        	controller.setMainApp(this);       	
            rootLayout.setCenter(profileView);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showCadastroView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cadastro.fxml"));
            Parent cadastroView = loader.load();

            CadastroController controller = loader.getController();
            controller.setMainApp(this);

            rootLayout.setCenter(cadastroView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSimulationView(int id) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SimulationView.fxml"));
            Parent simulationView = loader.load();

            SimulationViewController controller = loader.getController();
            controller.setUserId(id);
            controller.setMainApp(this);

            rootLayout.setCenter(simulationView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showReportsView(int id) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ReportsView.fxml"));
            Parent reportsView = loader.load();

            ReportsController controller = loader.getController();
            controller.setUserId(id);
            controller.setMainApp(this);

            rootLayout.setCenter(reportsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
