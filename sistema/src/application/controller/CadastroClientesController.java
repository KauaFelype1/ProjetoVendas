package application.controller;

import java.util.List;

import application.model.ClienteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastroClientesController {

    @FXML
    private Button btnSalvar;

    @FXML
    private TableColumn<ClienteModel, String> colCpf;

    @FXML
    private TableColumn<ClienteModel, String> colEmail;

    @FXML
    private TableColumn<ClienteModel, Integer> colIdCliente;

    @FXML
    private TableColumn<ClienteModel, String> colNome;

    @FXML
    private TableColumn<ClienteModel, String> colSenha;

    @FXML
    private TableColumn<ClienteModel, String> colStatus;

    @FXML
    private TableView<ClienteModel> tabCliente;
    
    private ObservableList<ClienteModel> listaCliente;

    @FXML
    private TextField txtCPF;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;
    
    ClienteModel cliente = new ClienteModel(0, null, null, null, null, null);
    
    public void salvar() {
    	
    	if(txtNome.getText().isEmpty() || txtCPF.getText().isEmpty() || txtEmail.getText().isEmpty() || txtSenha.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Erro");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha todos os campos!");
    		mensage.showAndWait();
    		
    	}else {
    		
    		
    		cliente.setNome(txtNome.getText());
    		cliente.setCpf(txtCPF.getText());
    		cliente.setEmail(txtEmail.getText());
    		cliente.setSenha(txtSenha.getText());
    		cliente.setStatus("Ativo");

    		if(cliente.Salvar()) {
    		Alert mensage = new Alert(Alert.AlertType.CONFIRMATION);
    		mensage.setTitle("Sucesso");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Cadastro realizado com sucesso");
    		mensage.showAndWait();
    		
    		LimparCampos();
    		}
    	
    		
    	}
    	
    	ListarClientes(null);
    	
    }
    
    @FXML
    public void initialize() {
    	
    	txtNome.setOnAction(e->{txtCPF.requestFocus();});
    	txtCPF.setOnAction(e->{txtEmail.requestFocus();});
    	txtEmail.setOnAction(e->{txtSenha.requestFocus();});
    	txtSenha.setOnAction(e->{salvar();});
    	
    	colIdCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
    	colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    	colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));
    	colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	ListarClientes(null);
    	
    }
    
    public void ListarClientes(String valor) {
    	
    	List <ClienteModel> clientes = cliente.ListarClientes(valor);
    	listaCliente = FXCollections.observableArrayList(clientes);
    	tabCliente.setItems(listaCliente);
    	
    }
    
    public void LimparCampos() {
    	txtNome.clear();
    	txtCPF.clear();
    	txtEmail.clear();
    	txtSenha.clear();
    }
    

}
