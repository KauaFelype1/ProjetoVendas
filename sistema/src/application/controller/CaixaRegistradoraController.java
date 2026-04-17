package application.controller;

import java.util.List;

import application.model.ItemVendaModel;
import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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

    private int idCliente = 0;

    ProdutoModel produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);

    public void setCliente(int idCli, String nome) {
        this.idCliente = idCli;
        lblNome.setText(nome);
    }

    @FXML
    public void initialize() {
    	
    	txtProduto.setOnAction(e->{buscar();});

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        colCarNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCarQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colCarPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colCarSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tabCarrinho.setItems(carrinho);

        listaProdutos(null);
        AtualizarCampo();

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

            for (ItemVendaModel item : carrinho) {
                if (item.getIdProduto() == produto.getId()) {

                    int novaQtd = item.getQuantidade() + qtd;

                    if (novaQtd > produto.getQuantidade()) {
                        new Alert(Alert.AlertType.ERROR, "Estoque insuficiente!").show();
                        return;
                    }

                    item.setQuantidade(novaQtd);
                    item.setSubtotal(novaQtd * item.getPreco());

                    tabCarrinho.refresh();
                    calcularTotal();
                    limparCampos();
                    return;
                }
            }

            if (qtd > produto.getQuantidade()) {
                new Alert(Alert.AlertType.ERROR, "Estoque insuficiente!").show();
                return;
            }

            carrinho.add(new ItemVendaModel(
                    produto.getId(),
                    produto.getNome(),
                    qtd,
                    produto.getPreco()
            ));

            calcularTotal();
            listaProdutos(null);
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
        listaProdutos(null);
    }

    private void limparCampos() {
        txtProduto.clear();
        txtQuantidade.setText("1");
        produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);
    }

    @FXML
    private void adicionarPagamento() {
        try {
            String tipoPagamento = menuPagamento.getText();

            if (tipoPagamento.equals("Selecione")) {
                new Alert(Alert.AlertType.ERROR, "Selecione a forma de pagamento!").show();
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(txtValorPago.getText().replace(",", "."));
                if (valor <= 0) throw new Exception();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Valor inválido!").show();
                return;
            }

            double restante = getTotalComDesconto() - totalPago;
            boolean isDinheiro = tipoPagamento.equals("Dinheiro");

            if (!isDinheiro && valor > restante) {
                new Alert(Alert.AlertType.ERROR,
                    "Pix e Cartão não geram troco!\n" +
                    "Valor máximo para este pagamento: R$ " +
                    String.format("%.2f", restante).replace(".", ",")
                ).show();
                return;
            }

            totalPago += valor;

            listPagamentos.getItems().add(
                tipoPagamento + " - R$ " +
                String.format("%.2f", valor).replace(".", ",")
            );

            txtValorPago.clear();
            menuPagamento.setText("Selecione");

            if (isDinheiro) {
                double troco = totalPago - getTotalComDesconto();
                if (troco >= 0) {
                    lblTroco.setText("Troco: R$ " +
                        String.format("%.2f", troco).replace(".", ","));
                }
            } else {
                lblTroco.setText("Sem troco");
            }

            calcularTroco();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Pagamento inválido!").show();
        }
    }

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

            if (idCliente == 0) {
                new Alert(Alert.AlertType.ERROR, "Cliente não selecionado!").show();
                return;
            }

            if (carrinho.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Carrinho vazio!").show();
                return;
            }

            if (listPagamentos.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Adicione um pagamento!").show();
                return;
            }

            double desconto = 0;
            try {
                desconto = Double.parseDouble(txtDesconto.getText().replace(",", "."));
                if (desconto < 0) throw new Exception();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Desconto inválido! Use apenas números.").show();
                return;
            }

            double totalFinal = getTotalCarrinho() - (getTotalCarrinho() * desconto / 100);

            if (totalPago < totalFinal) {
                new Alert(Alert.AlertType.ERROR,
                    "Pagamento insuficiente!\n" +
                    "Total: R$ " + String.format("%.2f", totalFinal).replace(".", ",") + "\n" +
                    "Pago: R$ " + String.format("%.2f", totalPago).replace(".", ",") + "\n" +
                    "Faltam: R$ " + String.format("%.2f", totalFinal - totalPago).replace(".", ",")
                ).show();
                return;
            }

            java.sql.Connection conn = application.conexao.getConnection();

            String sqlVenda = "INSERT INTO venda (idCli, dataHora, totalVenda, desconto) VALUES (?, NOW(), ?, ?)";
            java.sql.PreparedStatement stmtVenda = conn.prepareStatement(
                    sqlVenda, java.sql.Statement.RETURN_GENERATED_KEYS);

            stmtVenda.setInt(1, idCliente);
            stmtVenda.setDouble(2, totalFinal);
            stmtVenda.setDouble(3, desconto);
            stmtVenda.executeUpdate();

            java.sql.ResultSet rs = stmtVenda.getGeneratedKeys();
            int idVenda = 0;

            if (rs.next()) idVenda = rs.getInt(1);

            String sqlItem = "INSERT INTO itemVenda (idVenda, idProd, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement stmtItem = conn.prepareStatement(sqlItem);

            for (ItemVendaModel item : carrinho) {
                stmtItem.setInt(1, idVenda);
                stmtItem.setInt(2, item.getIdProduto());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getPreco());
                stmtItem.executeUpdate();
            }

            String sqlEstoque = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";
            java.sql.PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque);

            for (ItemVendaModel item : carrinho) {
                stmtEstoque.setInt(1, item.getQuantidade());
                stmtEstoque.setInt(2, item.getIdProduto());
                stmtEstoque.executeUpdate();
            }

            String sqlPag = "INSERT INTO pagamento (idVenda, tipo, valor) VALUES (?, ?, ?)";
            java.sql.PreparedStatement stmtPag = conn.prepareStatement(sqlPag);

            for (String pag : listPagamentos.getItems()) {

                String[] partes = pag.split(" - R\\$ ");
                if (partes.length < 2) continue;

                String tipo = partes[0].trim();
                double valor = Double.parseDouble(partes[1].replace(",", ".").trim());

                stmtPag.setInt(1, idVenda);
                stmtPag.setString(2, tipo);
                stmtPag.setDouble(3, valor);
                stmtPag.executeUpdate();
            }

            conn.close();

            gerarCupom();
            limparVenda();
            listaProdutos(null);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao finalizar venda!").show();
        }
    }

    private void gerarCupom() {

        StringBuilder cupom = new StringBuilder();

        cupom.append("===== CUPOM FISCAL =====\n\n");

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
        listaProdutos(null);
    }

    @FXML
    private void aplicarDesconto() {
        String textoDesconto = txtDesconto.getText().replace(",", ".");

        if (!textoDesconto.matches("\\d+(\\.\\d+)?")) {
            new Alert(Alert.AlertType.ERROR, "Desconto inválido! Digite apenas números.").show();
            txtDesconto.setText("0");
            return;
        }

        double desconto = Double.parseDouble(textoDesconto);

        if (desconto > 5) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Autorização");
            dialog.setHeaderText("Desconto acima de 5% requer senha do Gerente:");
            dialog.setContentText("Senha:");

            String senha = dialog.showAndWait().orElse("");

            if (!senha.equals("1234")) {
                new Alert(Alert.AlertType.ERROR, "Senha incorreta! Desconto não aplicado.").show();
                txtDesconto.setText("0");
                return;
            }
        }

        calcularTotal();
    }
    
    public void buscar() {
    	
    	if(txtProduto.getText().isEmpty()) {
    		
    		Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Preencha o campo!");
            alert.showAndWait();
            listaProdutos(null);
    		
    	}else {
    		
    		produto.Buscar(txtProduto.getText());
    		listaProdutos(txtProduto.getText());
    		
    	}
    	
    }
    
    public void cancelar() {
    	
    	try {
    		
    		txtProduto.getScene().getWindow().hide();
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/application/view/SelecaoCliente.fxml"));
    		Stage stage = new Stage();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.showAndWait();
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
}