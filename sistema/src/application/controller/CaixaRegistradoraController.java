package application.controller;

import java.util.List;

import application.model.ItemVendaModel;
import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CaixaRegistradoraController {

    @FXML private TableColumn<ProdutoModel, String> colCategoria;
    @FXML private TableColumn<ProdutoModel, Integer> colId;
    @FXML private TableColumn<ProdutoModel, String> colNome;
    @FXML private TableColumn<ProdutoModel, Double> colPreco;
    @FXML private TableColumn<ProdutoModel, Integer> colQuantidade;

    @FXML private TableView<ItemVendaModel> tabCarrinho;
    @FXML private TableColumn<ItemVendaModel, String> colCarNome;
    @FXML private TableColumn<ItemVendaModel, Integer> colCarQtd;
    @FXML private TableColumn<ItemVendaModel, Double> colCarPreco;
    @FXML private TableColumn<ItemVendaModel, Double> colCarSubtotal;

    @FXML private Label lblNome;
    @FXML private Label lblTotal;
    @FXML private Label lblTotalComDesconto;
    @FXML private Label lblTroco;

    @FXML private TextField txtProduto;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtDesconto;
    @FXML private TextField txtValorPago;

    @FXML private MenuButton menuPagamento;
    @FXML private MenuItem itemDinheiro;
    @FXML private MenuItem itemCartaoDebito;
    @FXML private MenuItem itemCartaoCredito;
    @FXML private MenuItem itemPix;

    @FXML private ListView<String> listPagamentos;

    @FXML private TableView<ProdutoModel> tabProduto;

    private ObservableList<ProdutoModel> listaProduto;
    private ObservableList<ItemVendaModel> carrinho = FXCollections.observableArrayList();

    private double totalPago = 0;

    ProdutoModel produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);

    @FXML
    public void initialize() {

        // PRODUTOS
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // CARRINHO
        colCarNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colCarPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colCarSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tabCarrinho.setItems(carrinho);

        listaProdutos(null);
        AtualizarCampo();

        // MENU PAGAMENTO
        itemDinheiro.setOnAction(e -> menuPagamento.setText("Dinheiro"));
        itemCartaoDebito.setOnAction(e -> menuPagamento.setText("Cartão Débito"));
        itemCartaoCredito.setOnAction(e -> menuPagamento.setText("Cartão Crédito"));
        itemPix.setOnAction(e -> menuPagamento.setText("Pix"));
    }

    public void listaProdutos(String valor) {
        List<ProdutoModel> produtos = produto.ListarProdutos(valor);
        listaProduto = FXCollections.observableArrayList(produtos);
        tabProduto.setItems(listaProduto);
    }

    public void AtualizarCampo() {
        tabProduto.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtProduto.setText(selecionado.getNome());
                produto = selecionado;
            }
        });
    }

    @FXML
    private void adicionarCarrinho() {
        try {

            if (produto.getId() == 0) throw new Exception();

            int qtd = Integer.parseInt(txtQuantidade.getText());
            if (qtd <= 0) throw new Exception();

            if (qtd > produto.getQuantidade()) {
                new Alert(Alert.AlertType.ERROR, "Estoque insuficiente!").show();
                return;
            }

            for (ItemVendaModel item : carrinho) {
                if (item.getIdProduto() == produto.getId()) {
                    item.setQuantidade(item.getQuantidade() + qtd);
                    item.setSubtotal(item.getQuantidade() * item.getPreco());
                    tabCarrinho.refresh();
                    calcularTotal();
                    limparCampos();
                    return;
                }
            }

            carrinho.add(new ItemVendaModel(
                    produto.getId(),
                    produto.getNome(),
                    qtd,
                    produto.getPreco()
            ));

            calcularTotal();
            limparCampos();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao adicionar produto!").show();
        }
    }

    private double getTotalCarrinho() {
        double total = 0;
        for (ItemVendaModel item : carrinho) {
            total += item.getSubtotal();
        }
        return total;
    }

    private double getTotalComDesconto() {
        double total = getTotalCarrinho();
        double desconto = 0;

        try {
            desconto = Double.parseDouble(txtDesconto.getText().replace(",", "."));
        } catch (Exception e) {}

        return total - (total * desconto / 100);
    }

    private void calcularTotal() {
        lblTotal.setText("R$ " + String.format("%.2f", getTotalCarrinho()).replace(".", ","));
        lblTotalComDesconto.setText("Total com desconto: R$ " +
                String.format("%.2f", getTotalComDesconto()).replace(".", ","));
        calcularTroco();
    }

    @FXML
    private void removerItem() {
        ItemVendaModel selecionado = tabCarrinho.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            carrinho.remove(selecionado);
            calcularTotal();
        }
    }

    private void limparCampos() {
        txtProduto.clear();
        txtQuantidade.setText("1");
        produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);
    }

    // 🔥 DESCONTO
    @FXML
    private void aplicarDesconto() {
        try {

            double desconto = Double.parseDouble(txtDesconto.getText().replace(",", "."));

            if (desconto > 5) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setHeaderText("Senha do gerente:");
                String senha = dialog.showAndWait().orElse("");

                if (!senha.equals("1234")) {
                    new Alert(Alert.AlertType.ERROR, "Senha incorreta!").show();
                    return;
                }
            }

            calcularTotal();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Desconto inválido!").show();
        }
    }

    // 💳 PAGAMENTO
    @FXML
    private void adicionarPagamento() {
        try {

            if (menuPagamento.getText().equals("Selecione")) throw new Exception();

            double valor = Double.parseDouble(txtValorPago.getText().replace(",", "."));
            if (valor <= 0) throw new Exception();

            totalPago += valor;

            listPagamentos.getItems().add(
                    menuPagamento.getText() + " - R$ " +
                    String.format("%.2f", valor).replace(".", ",")
            );

            txtValorPago.clear();
            menuPagamento.setText("Selecione");

            calcularTroco();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Pagamento inválido!").show();
        }
    }

    // 💰 TROCO
    private void calcularTroco() {

        double totalFinal = getTotalComDesconto();
        double troco = totalPago - totalFinal;

        if (troco < 0) {
            lblTroco.setText("Falta: R$ " +
                    String.format("%.2f", Math.abs(troco)).replace(".", ","));
        } else {
            lblTroco.setText("Troco: R$ " +
                    String.format("%.2f", troco).replace(".", ","));
        }
    }
    
    @FXML
    private void finalizarVenda() {

        try {

            if (carrinho.isEmpty()) {
                throw new Exception("Carrinho vazio!");
            }

            double totalFinal = getTotalComDesconto();

            if (totalPago < totalFinal) {
                throw new Exception("Pagamento insuficiente!");
            }

            // 🔥 AQUI futuramente vai salvar no banco

            gerarCupom();

            new Alert(Alert.AlertType.INFORMATION, "Venda finalizada com sucesso!").show();

            limparVenda();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    
    private void gerarCupom() {

        StringBuilder cupom = new StringBuilder();

        cupom.append("===== CUPOM NÃO FISCAL =====\n\n");

        for (ItemVendaModel item : carrinho) {
            cupom.append(item.getNome())
                 .append(" x").append(item.getQuantidade())
                 .append(" - R$ ")
                 .append(String.format("%.2f", item.getSubtotal()).replace(".", ","))
                 .append("\n");
        }

        cupom.append("\n--------------------------\n");

        cupom.append("TOTAL: R$ ")
             .append(String.format("%.2f", getTotalCarrinho()).replace(".", ","))
             .append("\n");

        cupom.append("DESCONTO: ")
             .append(txtDesconto.getText())
             .append("%\n");

        cupom.append("TOTAL FINAL: R$ ")
             .append(String.format("%.2f", getTotalComDesconto()).replace(".", ","))
             .append("\n");

        cupom.append("PAGO: R$ ")
             .append(String.format("%.2f", totalPago).replace(".", ","))
             .append("\n");

        double troco = totalPago - getTotalComDesconto();

        cupom.append("TROCO: R$ ")
             .append(String.format("%.2f", troco).replace(".", ","))
             .append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cupom");
        alert.setHeaderText("Resumo da Venda");
        alert.setContentText(cupom.toString());
        alert.showAndWait();
    }
    
    private void limparVenda() {

        carrinho.clear();
        listPagamentos.getItems().clear();

        totalPago = 0;

        txtDesconto.setText("0");
        txtValorPago.clear();

        lblTotal.setText("R$ 0,00");
        lblTotalComDesconto.setText("Total com desconto: R$ 0,00");
        lblTroco.setText("R$ 0,00");
    }
    
}