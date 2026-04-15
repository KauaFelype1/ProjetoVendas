package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class HistoricoCompraController {

    @FXML
    private TableColumn<?, ?> ColProduto;

    @FXML
    private TableColumn<?, ?> colDataCompra;

    @FXML
    private TableColumn<?, ?> colIdCliente;

    @FXML
    private TableColumn<?, ?> colIdCompra;

    @FXML
    private TableColumn<?, ?> colPreco;

    @FXML
    private TableColumn<?, ?> colQuantidade;

    @FXML
    private TableView<?> tabCompra;

    @FXML
    private TextField txtNome;

}
