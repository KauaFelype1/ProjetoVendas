package application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.conexao;
import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProcessarEstoqueController {

    @FXML
    private Button btnBuscar;
    
    @FXML
    private Button btnHistorico;

    @FXML
    private Button btnProcessor;
    
    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<ProdutoModel, String> colCategoria;

    @FXML
    private TableColumn<ProdutoModel, String> colCode;

    @FXML
    private TableColumn<ProdutoModel, String> colDescricao;

    @FXML
    private TableColumn<ProdutoModel, Integer> colID;

    @FXML
    private TableColumn<ProdutoModel, String> colNome;

    @FXML
    private TableColumn<ProdutoModel, Integer> colQuantidade;

    @FXML
    private TextField txtCodeBarras;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private TextField txtProduto;

    @FXML
    private TextField txtQuantidade;
    
    @FXML
    private TableView<ProdutoModel> tabProduto;
    
    @FXML
    private ToggleGroup rdTipo;
    private ObservableList <ProdutoModel> listaProdutos;
    
    ProdutoModel produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);
    
    @FXML
    public void initialize() {
    	
    	colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCode.setCellValueFactory(new PropertyValueFactory<>("codigo"));
    	colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    	colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    	colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    	
    	ListarProdutosTab(null);
    	
    	tabProduto.getSelectionModel().selectedItemProperty().addListener(
    			(obs, selecao, novaSelecao) -> {
    				if(novaSelecao != null) {
    					// ATUALIZAR MODEL
    					produto=novaSelecao;
    					// ATUALIZAR CAMPOS
    					txtId.setText(String.format("%06d", produto.getId()));
    					txtProduto.setText(produto.getNome());
    					txtCodeBarras.setText(produto.getCodigo());
    					txtQuantidade.setText("0");
    				}
    			});
    	
    	btnProcessor.setOnAction(e->{
    		produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
    		// PEGA A INFORMAÇÃO SELECIONADA SE ENTRADA OU SAIDA
    		RadioButton operacao = (RadioButton) rdTipo.getSelectedToggle();
    		boolean sucesso = produto.ProcessaEstoque(operacao.getText());

    	    if (!sucesso) {
    	        new Alert(Alert.AlertType.ERROR, "Estoque insuficiente!").show();
    	        return;
    	    }
    		txtPesquisar.clear();
    		txtId.clear();
    		txtCodeBarras.clear();
    		txtProduto.clear();
    		txtQuantidade.setText("0");
    		ListarProdutosTab(null);
    	});
    	
    	btnBuscar.setOnAction(e -> {Pesquisar();});
    	
    }
    
    public void Pesquisar() {
    	// VERIFICA SE TEM UM TEXTO NO CAMPO DE BUSCA
    	if(!txtPesquisar.getText().isEmpty()) {
    		produto.Buscar(txtPesquisar.getText());
    		ListarProdutosTab(txtPesquisar.getText());
    		
    		txtId.setText(String.format("%06d", produto.getId()));
			txtProduto.setText(produto.getNome());
			txtCodeBarras.setText(produto.getCodigo());
			txtQuantidade.setText("0");
    	}else {   // SENÃO BUSCA TODOS OS PRODUTOS
    		//BUSCA TODOS OS PRODUTOS
    		ListarProdutosTab(null);
    	}
    	
    }
    
    public List<ProdutoModel> ListarProdutos(String Valor){
    	
    	List <ProdutoModel> produtos = new ArrayList<ProdutoModel>();
    	try(Connection conn = conexao.getConnection();
    			PreparedStatement consulta = conn.prepareStatement("select * from produto");
    			PreparedStatement consultaWhere = conn.prepareStatement("select * from produto where nome like ? or descricao like ? or categoria like ?")){
    		
    		ResultSet resultado = null;
    		if(Valor == null) {
    			resultado=consulta.executeQuery();
    		}else {
    			consultaWhere.setString(1, "%"+Valor+"%");
    			consultaWhere.setString(2, "%"+Valor+"%");
    			consultaWhere.setString(3, "%"+Valor+"%");
    			resultado=consultaWhere.executeQuery();
    		}
    		
    		while(resultado.next()) {
    			ProdutoModel p = new ProdutoModel(
    					resultado.getInt("id"),
    					resultado.getString("nome"),
    					resultado.getString("descricao"),
    					resultado.getString("categoria"),
    					resultado.getDouble("preco"),
    					resultado.getInt("quantidade"),
    					resultado.getString("codigo"),
    					resultado.getDouble("precoCusto")
    					);
    			produtos.add(p);
    			
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return produtos;
    	
    }
    
    public void ListarProdutosTab(String Valor) {
    	List<ProdutoModel> produtos = produto.ListarProdutos(Valor);
    	listaProdutos=FXCollections.observableArrayList(produtos);
    	tabProduto.setItems(listaProdutos);
    }
    
    public void AbrirHistorico() {
    	
    	LocalDate hoje = LocalDate.now();
    	LocalDate primeiroDia = hoje.withDayOfMonth(1);
    	LocalDate ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/HistoricoProcessamento.fxml"));
    		Parent root = loader.load();
    		HistoricoController controller = loader.getController();
    		controller.buscarHistorico(produto.getId(), primeiroDia, ultimoDia);
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
            stage.setTitle("Kauã");
            stage.centerOnScreen();
            stage.setMaximized(true);
            stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void Voltar(){
    	
    	btnVoltar.getScene().getWindow().hide();
    	
    }
    

}
