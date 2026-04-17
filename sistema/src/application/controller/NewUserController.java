package application.controller;

import application.model.FuncionarioModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class NewUserController {

    @FXML
    private MenuItem ItemEstoquista;

    @FXML
    private MenuItem ItemGerente;

    @FXML
    private MenuItem ItemOperario;

    @FXML
    private MenuItem ItemVendedor;

    @FXML
    private MenuButton MenuFuncao;

    @FXML
    private Button btnCadastrar;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;
    
    @FXML
    private Label lblCancelar;
    
    FuncionarioModel funcionario = new FuncionarioModel(0, null, null, null, null, null, null);
    
    public void Salvar() {
    	
    	String funcaoSelecionada = MenuFuncao.getText();
    	
    	if(txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || txtEmail.getText().isEmpty() || txtSenha.getText().isEmpty() || txtCnpj.getText().isEmpty()) {
    		
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Erro");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha todos os campos");
    		mensage.showAndWait();
    		
    		if(funcaoSelecionada.equals("Funções")) {
        		Alert mensage1 = new Alert(Alert.AlertType.ERROR);
        		mensage1.setTitle("Erro");
        		mensage1.setHeaderText(null);
        		mensage1.setContentText("Selecione uma função");
        		mensage1.showAndWait();
        	}
    		
    	}else {
    			
    		funcionario.setNome(txtNome.getText());
    		funcionario.setCpf(txtCpf.getText());
    		funcionario.setEmail(txtEmail.getText());
    		funcionario.setSenha(txtSenha.getText());
    		funcionario.setFuncao(funcaoSelecionada);
    		funcionario.setCnpj(txtCnpj.getText());
    		
    		if(funcionario.Salvar()) {
    			Alert mensage = new Alert(Alert.AlertType.CONFIRMATION);
        		mensage.setTitle("Tudo certo");
        		mensage.setHeaderText(null);
        		mensage.setContentText("Cadastro realizado com sucesso!");
        		mensage.showAndWait();
    		txtNome.getScene().getWindow().hide();
    	    }
    		
    	}
    	
    }
    
    @FXML
    public void initialize() {
    	
    	ItemOperario.setOnAction(e-> MenuFuncao.setText(ItemOperario.getText()));
    	ItemGerente.setOnAction(e-> MenuFuncao.setText(ItemGerente.getText()));
    	ItemVendedor.setOnAction(e-> MenuFuncao.setText(ItemVendedor.getText()));
    	ItemEstoquista.setOnAction(e-> MenuFuncao.setText(ItemEstoquista.getText()));
    	
    	lblCancelar.setOnMouseClicked(e->{
    		txtNome.getScene().getWindow().hide();
    	});
    	
    }

}
