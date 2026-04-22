package application.controller;

import java.util.List;

import application.model.HistoricoClienteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoricoCompraController {

    @FXML
    private TableColumn<HistoricoClienteModel, Integer> colIdCompra;

    @FXML
    private TableColumn<HistoricoClienteModel, Integer> colIdCliente;

    @FXML
    private TableColumn<HistoricoClienteModel, String> ColProduto;

    @FXML
    private TableColumn<HistoricoClienteModel, Integer> colQuantidade;

    @FXML
    private TableColumn<HistoricoClienteModel, Double> colPreco;

    @FXML
    private TableColumn<HistoricoClienteModel, String> colDataCompra;

    @FXML
    private TableView<HistoricoClienteModel> tabCompra;

    @FXML
    private TextField txtNome;
    
    @FXML
    private Label lblTotalRegistros;
    
    @FXML
    private Button btnVoltar;

    private ObservableList<HistoricoClienteModel> lista;

    HistoricoClienteModel historico = new HistoricoClienteModel(0, 0, null, 0, 0, null);

    private int idCliente;

    public void setCliente(int idCli, String nome) {
        this.idCliente = idCli;
        txtNome.setText(nome);
        listarHistorico();
        // adicione essa linha:
        lblTotalRegistros.setText(String.valueOf(tabCompra.getItems().size()));
    }

    @FXML
    public void initialize() {

        colIdCompra.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        ColProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colDataCompra.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));

    }

    public void listarHistorico() {

        List<HistoricoClienteModel> dados = historico.listarHistorico(idCliente);
        lista = FXCollections.observableArrayList(dados);
        tabCompra.setItems(lista);

    }
    
    @FXML
    public void voltar() {
        txtNome.getScene().getWindow().hide();
    }

}