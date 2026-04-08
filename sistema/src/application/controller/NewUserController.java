package application.controller;

import application.model.FuncionarioModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    
    FuncionarioModel funcionario = new FuncionarioModel(0, null, null, null, null, null, null);
    
    public void Salvar() {
    	
    	if(txtNome.getText().isEmpty() || txtCpf.getText().isEmpty()) {
    		
    	}
    	
    }

}
