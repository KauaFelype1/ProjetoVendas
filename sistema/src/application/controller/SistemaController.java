package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import application.model.ClienteModel;
import application.model.ProdutoModel;

public class SistemaController implements Initializable {

    @FXML private Label lblPaginaTitulo;
    @FXML private Label lblPaginaSub;
    @FXML private Label lblUsuario;

    @FXML private Label lblQtdProdutos;
    @FXML private Label lblQtdClientes;
    @FXML private Label lblQtdEstoque;
    @FXML private Label lblQtdVendas;

    @FXML private HBox navInicio;
    @FXML private HBox navProdutos;
    @FXML private HBox navClientes;
    @FXML private HBox navEstoque;
    @FXML private HBox navVendas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        carregarDashboard();

        configurarHoverSidebar(navProdutos);
        configurarHoverSidebar(navClientes);
        configurarHoverSidebar(navEstoque);
        configurarHoverSidebar(navVendas);
    }

    private void configurarHoverSidebar(HBox item) {
        String baseStyle = item.getStyle();

        item.setOnMouseEntered(e ->
            item.setStyle(baseStyle + "-fx-background-color: #252b4a;")
        );

        item.setOnMouseExited(e ->
            item.setStyle(baseStyle)
        );
    }
    
    private void carregarDashboard() {

        try {
            // CLIENTES
            ClienteModel clienteModel = new ClienteModel(0, null, null, null, null, null);
            int totalClientes = clienteModel.ListarClientes(null).size();
            lblQtdClientes.setText(String.valueOf(totalClientes));

            // PRODUTOS
            ProdutoModel produtoModel = new ProdutoModel(0, null, null, null, 0, 0, null, 0);
            var listaProdutos = produtoModel.ListarProdutos(null);

            lblQtdProdutos.setText(String.valueOf(listaProdutos.size()));

            // ESTOQUE (soma das quantidades)
            int totalEstoque = 0;
            for (ProdutoModel p : listaProdutos) {
                totalEstoque += p.getQuantidade();
            }
            lblQtdEstoque.setText(String.valueOf(totalEstoque));

            lblQtdVendas.setText("—");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirJanela(String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(caminhoFXML)
            );
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Kauã");
            stage.centerOnScreen();
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void Sair() {
        System.exit(0);
    }

    @FXML
    public void AbrirCadastroProuto() {
        lblPaginaTitulo.setText("Cadastros");
        lblPaginaSub.setText("Produtos");

        abrirJanela("/application/view/CadastroProdutos.fxml");
    }

    @FXML
    public void AbrirCadastroClientes() {
        lblPaginaTitulo.setText("Cadastros");
        lblPaginaSub.setText("Clientes");

        abrirJanela("/application/view/CadastroClientes.fxml");
    }

    @FXML
    public void AbrirProcessarEstoque() {
        lblPaginaTitulo.setText("Operações");
        lblPaginaSub.setText("Estoque");

        abrirJanela("/application/view/ProcessarEstoque.fxml");
    }

    @FXML
    public void AbrirVendas() {
        lblPaginaTitulo.setText("Operações");
        lblPaginaSub.setText("Vendas");

        abrirJanela("/application/view/SelecaoCliente.fxml");
    }
}