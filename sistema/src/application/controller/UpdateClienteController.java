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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;

public class UpdateClienteController {

    @FXML
    private MenuButton MenuStatus;
    
    @FXML
    private MenuItem itemAtivo;

    @FXML
    private MenuItem itemInativo;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnHistorico;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<ClienteModel, String> colCpf;

    @FXML
    private TableColumn<ClienteModel, String> colEmail;

    @FXML
    private TableColumn<ClienteModel, Integer> colId;

    @FXML
    private TableColumn<ClienteModel, String> colNome;

    @FXML
    private TableColumn<ClienteModel, String> colSenha;

    @FXML
    private TableColumn<ClienteModel, String> colStatus;

    @FXML
    private TableView<ClienteModel> tabClientes;
    
    private ObservableList<ClienteModel> listaCliente;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;
    
    ClienteModel cliente = new ClienteModel(0, null, null, null, null, null);
    
    @FXML
    public void initialize() {
    	
    	txtBuscar.setOnAction(e->{buscar();});
    	
    	itemAtivo.setOnAction(e-> MenuStatus.setText(itemAtivo.getText()));
    	itemInativo.setOnAction(e-> MenuStatus.setText(itemInativo.getText()));
    	
    	colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
    	colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    	colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));
    	colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	Atualizar();
    	ListarClienteTab(null);
    	
    	
    }
    
    public void buscar() {
    	
    	if(txtBuscar.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Erro");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha o campo!");
    		mensage.showAndWait();
    		
    		txtId.clear();
    		txtNome.clear();
    		txtCpf.clear();
    		txtEmail.clear();
    		txtSenha.clear();
    		MenuStatus.setText("Status do Cliente");
    		ListarClienteTab(null);
    		
    	}else {
    		
    		cliente.Buscar(txtBuscar.getText());
    		ListarClienteTab(txtBuscar.getText());
    		
    		txtId.setText(String.valueOf(cliente.getId()));
    		txtNome.setText(cliente.getNome());
    		txtCpf.setText(cliente.getCpf());
    		txtEmail.setText(cliente.getEmail());
    		txtSenha.setText(cliente.getSenha());
    		MenuStatus.setText(cliente.getStatus());
    		
    		
    	}
    	
    }
    
    public void Modificar() {
    	
    	String status = MenuStatus.getText();
    	
    	if(txtNome.getText().isEmpty() || txtSenha.getText().isEmpty() || MenuStatus.getText().equals("Status do Cliente") || txtCpf.getText().isEmpty() || txtEmail.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
			mensage.setTitle("Erro");
			mensage.setHeaderText(null);
			mensage.setContentText("Preencha todos os campos");
			mensage.showAndWait();
			
			if(MenuStatus.getText().equals("Status do Cliente")) {
				
				Alert mensagen = new Alert(Alert.AlertType.ERROR);
				mensagen.setTitle("Erro");
				mensagen.setHeaderText(null);
				mensagen.setContentText("Selecione um status");
				mensagen.showAndWait();
				
			}
    		
    	}else {
    		
    		cliente.setNome(txtNome.getText());
    		cliente.setSenha(txtSenha.getText());
    		cliente.setStatus(MenuStatus.getText());
    		cliente.Update();
    		
    		Alert mensagen = new Alert(Alert.AlertType.CONFIRMATION);
			mensagen.setTitle("Sucesso");
			mensagen.setHeaderText(null);
			mensagen.setContentText("Modificação realizada com sucesso");
			mensagen.showAndWait();
			
			limparCampo();
			ListarClienteTab(null);
    		
    	}
    	
    	
    }
    
    public void ListarClienteTab(String valor) {
    	
    	List <ClienteModel> clientes = cliente.ListarClientes(valor);
    	listaCliente = FXCollections.observableArrayList(clientes);
    	tabClientes.setItems(listaCliente);
    	
    }
    
    public void Atualizar() {
    	
    	tabClientes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
    		
    		if(selecionado != null) {
    			
    			txtId.setText(String.valueOf(selecionado.getId()));
    			txtNome.setText(selecionado.getNome());
    			txtCpf.setText(selecionado.getCpf());
    			txtEmail.setText(selecionado.getEmail());
    			txtSenha.setText(selecionado.getSenha());
    			MenuStatus.setText(selecionado.getStatus());
    			cliente = selecionado;
    			
    		}
    		
    	});
    	
    }
    
    public void voltar() {
    	
    	txtNome.getScene().getWindow().hide();
    	
    	try {
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/CadastroClientes.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void limparCampo() {
    	
    	txtId.clear();
    	txtNome.clear();
    	txtCpf.clear();
    	txtEmail.clear();
    	txtSenha.clear();
    	txtBuscar.clear();
    	MenuStatus.setText("Status do Cliente");
    	
    }
    
    @FXML
    public void abrirHistorico() {

        try {

            if (cliente.getId() == 0) {
                new Alert(Alert.AlertType.ERROR, "Selecione um cliente!").show();
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/application/view/historicoCompra.fxml")
            );

            Parent root = loader.load();

            HistoricoCompraController controller = loader.getController();

            controller.setCliente(cliente.getId(), cliente.getNome());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void excluir() {
    	
    	cliente.Excluir();
    	
    	limparCampo();
    	ListarClienteTab(null);
    	
    }

}
