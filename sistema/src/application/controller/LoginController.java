package application.controller;

import application.model.FuncionarioModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button btnEntrar;

    @FXML
    private Label lblNewUser;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtSenha;
    
    FuncionarioModel funcionario = new FuncionarioModel(0, null, null, null, null, null, null);
    
    public void Entrar() {
    	
    	if(txtLogin.getText().isEmpty() || txtSenha.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Usuário não encontrado");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha todos os campos!");
    		mensage.showAndWait();
    	}else {
    	
    	try {
    	if(funcionario.Autenticar(txtLogin.getText(), txtSenha.getText())) {
    		
    		Alert mensage = new Alert(Alert.AlertType.CONFIRMATION);
    		mensage.setTitle("Usuário encontrado!");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Email e Senha corretos, Sistema liberado");
    		mensage.showAndWait();
    		
    		txtLogin.getScene().getWindow().hide();
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/sistema.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}else {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Usuário não encontrado");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Email ou Senha incorretos");
    		mensage.showAndWait();
    		
    	}
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    	
    }
    
    @FXML
    public void initialize() {
    	
    	txtLogin.setOnAction(e->{
    		txtSenha.requestFocus();
    	});
    	
    	txtSenha.setOnAction(e->{
    		Entrar();
    	});
    	
    	lblNewUser.setOnMouseClicked(event->{
    		
    		try {
    			
    			Parent root = FXMLLoader.load(getClass().getResource("/application/view/NewUser.fxml"));
    			Stage stage = new Stage();
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.show();
    			
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    		
    	});
    	
    }
    
}
