package application.controller;

import java.time.LocalDate;
import java.util.List;

import application.model.MovimentacaoEstoqueModel;
import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoricoController {

    @FXML
    private Button btnBuscar;
    
    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, String> colData;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, Integer> colID;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, Integer> colIdProd;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, String> colNome;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, Integer> colQuantidade;

    @FXML
    private TableColumn<MovimentacaoEstoqueModel, String> colTipo;

    @FXML
    private DatePicker dtFinal;

    @FXML
    private DatePicker dtInicio;

    @FXML
    private Label lblProduto;

    @FXML
    private TableView<MovimentacaoEstoqueModel> tabProduto;
    
    private ObservableList<MovimentacaoEstoqueModel> listaMovimentacao;
    
    LocalDate hoje;
    LocalDate primeiroDia;
    LocalDate ultimoDia;
    
    
    MovimentacaoEstoqueModel Movimentacao = new MovimentacaoEstoqueModel(0, 0, null, null, 0, null);
    
    @FXML
    public void initialize() {
    	
    	btnVoltar.setOnAction(e->{voltar();});
    	
    	colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colIdProd.setCellValueFactory(new PropertyValueFactory<>("idProd"));
    	colData.setCellValueFactory(new PropertyValueFactory<>("data"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nomeProd"));
    	colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    	colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    	// DATA ATUAL
    	hoje = LocalDate.now();
    	// PRIMEIRO DIA DO MÊS
    	primeiroDia = hoje.withDayOfMonth(1);
    	// ULTIMO DIA DO MÊS
    	ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());
    	
    	dtInicio.setValue(primeiroDia);
    	dtFinal.setValue(ultimoDia);
    	
    	btnBuscar.setOnAction(e->{
    		buscarHistorico(Movimentacao.getIdProd(), dtInicio.getValue(), dtFinal.getValue());
    	});
    	
    }
    
    public void buscarHistorico(int idProd, LocalDate dataInicio, LocalDate dataFinal) {
    	
    	List <MovimentacaoEstoqueModel> listaHistorico = Movimentacao.HistoricoMovimentacao(idProd, dataInicio, dataFinal);
    	listaMovimentacao = FXCollections.observableArrayList(listaHistorico);
    	tabProduto.setItems(listaMovimentacao);
    	lblProduto.setText(Movimentacao.getNomeProd());
    	dtInicio.setValue(dataInicio);
    	dtFinal.setValue(dataFinal);
    	
    }
    
    public void voltar() {
    	btnVoltar.getScene().getWindow().hide();
    }

}
