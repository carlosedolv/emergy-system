package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ViewController {
	
	@FXML
	private Button btnLogin;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private Button btnSignUp;
	
	@FXML
	public void onClickBtnLogin() {
		String email = txtEmail.getText();
		String password = txtPassword.getText();
		if(email.length() == 0 || password.length() == 0) {
			Alerts.showAlert("Login", "Try Again", "Enter all fields", Alert.AlertType.WARNING);
		} else {
			Alerts.showAlert("Login", "Succefully", "Welcome " + email + " ", Alert.AlertType.INFORMATION);
		}
	}
	
	@FXML
	public void onClickBtnSignUp() {
		
	}
}

