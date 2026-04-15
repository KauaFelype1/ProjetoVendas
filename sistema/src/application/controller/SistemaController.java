package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class SistemaController {

    @FXML
    private MenuItem ItemProduto;

    @FXML
    private MenuItem itemCliente;

    @FXML
    private MenuItem itemProcessaEstoque;

    @FXML
    private MenuItem itemSair;
    
    @FXML
    private MenuItem itemVendas;
    
    @FXML
    private void initialize() {
    	
    }
    
    public void Sair() {
    	System.exit(0); //SIGNIFICA QUE VAI FECHAR O PROGRAMA
    }
    
    public void AbrirCadastroProuto() {
    	try {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/view/CadastroProdutos.fxml"));
    	Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    	} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    public void AbrirCadastroClientes() {
    	
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/CadastroClientes.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void AbrirProcessarEstoque() {
    	
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/ProcessarEstoque.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void AbrirVendas() {
    	
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/SelecaoCliente.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

}
