package application.controller;

import java.util.List;

import application.model.ClienteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SelectionUserController {

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private TableColumn<ClienteModel, String> colCpf;

    @FXML
    private TableColumn<ClienteModel, String> colEmail;

    @FXML
    private TableColumn<ClienteModel, Integer> colId;

    @FXML
    private TableColumn<ClienteModel, String> colNome;

    @FXML
    private TableColumn<ClienteModel, String> colStatus;
    
    private ObservableList <ClienteModel> listaCliente;

    @FXML
    private Label lblNome;

    @FXML
    private TableView<ClienteModel> tabCliente;

    @FXML
    private TextField txtBuscar;
    
    ClienteModel cliente = new ClienteModel(0, null, null, null, null, null);
    
    @FXML
    public void initialize() {
    	
    	colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
    	colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    	colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    	AtuaizarCampo();
    	ListarClientes(null);
    	
    }
    
    public void ListarClientes(String valor) {
    	
    	List <ClienteModel> clientes = cliente.ListarClientes(valor);
    	listaCliente = FXCollections.observableArrayList(clientes);
    	tabCliente.setItems(listaCliente);
    	
    }
    
    public void AtuaizarCampo() {
    	
    	tabCliente.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado)->{
    		
    		if(selecionado != null) {
    			lblNome.setText(selecionado.getNome());
    			cliente = selecionado;
    		}
    		
    	});
    	
    }
    
    public void Buscar() {
    	
    	if(txtBuscar.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Erro");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha o campo de busca!");
    		mensage.showAndWait();
    		
    		lblNome.setText("");
    		
    	}else {
    		
    		cliente.Buscar(txtBuscar.getText());
    		ListarClientes(txtBuscar.getText());
    		
    		lblNome.setText(cliente.getNome());
    		
    	}
    	
    }
    
    public void Confirmar() {
    	
    	try {
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/caixaRegistradora.fxml"));
    		
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

}
