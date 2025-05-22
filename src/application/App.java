package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

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

        Menu menuView = new Menu("Telas");

        MenuItem userView = new MenuItem("Usuários");
        userView.setOnAction(e -> showUserView());

        MenuItem simView = new MenuItem("Simulações");
        simView.setOnAction(e -> showSimulationView());

        menuView.getItems().addAll(userView, simView);
        menuBar.getMenus().add(menuView);

        rootLayout.setTop(menuBar);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showUserView() {
        try {
            Parent userView = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
            rootLayout.setCenter(userView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSimulationView() {
        try {
            Parent simView = FXMLLoader.load(getClass().getResource("/gui/SimulationView.fxml"));
            rootLayout.setCenter(simView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
