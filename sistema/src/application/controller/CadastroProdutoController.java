package application.controller;

import java.util.List;

import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastroProdutoController {

    @FXML
    private Button btnSalvar;
    
    @FXML
    private Button btnSugestao;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtQuantidade;
    
    @FXML
    private TextField txtBuscar;
    
    @FXML
    private TextField txtCode;
    
    @FXML
    private TextField txtPrecoComprado;
    
    @FXML
    private Button btnBuscar;
    
    @FXML
    private Button btnExcluir;
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TableColumn<ProdutoModel, String> colCategoria;

    @FXML
    private TableColumn<ProdutoModel, String> colDescricao;

    @FXML
    private TableColumn<ProdutoModel, Integer> colID;

    @FXML
    private TableColumn<ProdutoModel, String> colNome;

    @FXML
    private TableColumn<ProdutoModel, Double> colPreco;

    @FXML
    private TableColumn<ProdutoModel, Integer> colQuantidade;
    
    @FXML
    private TableColumn<ProdutoModel, String> colCodigo;
    
    @FXML
    private TableColumn<ProdutoModel, Double> colPrecoCusto;

    @FXML
    private TableView<ProdutoModel> tabProduto;
    
    @FXML
    private Button btnVoltar;
    
    private ObservableList<ProdutoModel> listaProdutos;
    
    ProdutoModel produto = new ProdutoModel(0, null, null, null, 0, 0, null, 0);
    
    // MÉTODO PARA SALVAR O CADASTRO
    public void Salvar() {
        Alert mensage;

        if(txtNome.getText().isEmpty() || txtDescricao.getText().isEmpty() || 
           txtCategoria.getText().isEmpty() || txtPreco.getText().isEmpty() || 
           txtCode.getText().isEmpty() || txtPrecoComprado.getText().isEmpty()) {

        	String erro="";
        	if(txtNome.getText().isEmpty()) {erro=erro+"\nCampo Nome em Branco!";}
        	if(txtDescricao.getText().isEmpty()) {erro=erro+"\nCampo Descrição em Branco!";}
        	if(txtCategoria.getText().isEmpty()) {erro=erro+"\nCampo Categoria em Branco!";}
        	if(txtPreco.getText().isEmpty()) {erro=erro+"\nCampo Preço em Branco!";}
        	if(txtCode.getText().isEmpty()) {erro=erro+"\nCampo Código de barra em Branco!";}
        	if(txtPrecoComprado.getText().isEmpty()) {erro=erro+"\nCampo Preço Comprado em Branco!";}
        	
            mensage = new Alert(Alert.AlertType.ERROR);
            mensage.setTitle("ERRO");
            mensage.setHeaderText(null);
            mensage.setContentText("Preencha os campos: "+erro);
            mensage.show();

        } else {
      
            produto.setNome(txtNome.getText());
            produto.setDescricao(txtDescricao.getText());
            produto.setCategoria(txtCategoria.getText());
            produto.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
            produto.setQuantidade(0);
            produto.setCodigo(txtCode.getText());
            produto.setPrecoCusto(Double.parseDouble(txtPrecoComprado.getText()));

            produto.Salvar();

            mensage = new Alert(Alert.AlertType.CONFIRMATION);
            mensage.setTitle("Confirmação");
            mensage.setHeaderText(null);
            mensage.setContentText("Produto cadastrado com sucesso");
            mensage.show();

            txtNome.setText("");
            txtDescricao.setText("");
            txtCategoria.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("");
            txtId.setText("");
            txtCode.setText(ProdutoModel.gerarEAN13());
            txtPrecoComprado.clear();
            ListarProdutosTab(null);
        }
    }
    
    // MÉTODO PARA BUSCAR O CADASTRO DO PODUTO
    public void Pesquisar() {
    	
    	if(!txtBuscar.getText().isEmpty()) {
    		
    		
    		// EXECUTA O MÉTODO DE BUSCAR
    		produto.Buscar(txtBuscar.getText());
    		ListarProdutosTab(txtBuscar.getText());
    		// INFORMA OS VALORES NOS CAMPOS DO FORMULARIO
    		txtNome.setText(produto.getNome());
    		txtDescricao.setText(produto.getDescricao());
    		txtCategoria.setText(produto.getCategoria());
    		txtPreco.setText(String.valueOf(produto.getPreco()).replace(".", ","));
    		txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
    		txtId.setText(String.format("%06d",produto.getId()));
    		txtCode.setText(produto.getCodigo());
    		txtPrecoComprado.setText(String.valueOf(produto.getPrecoCusto()));
    		
    		
    	}else {
    		// MENSAGEM PARA DIGITAR TEXTO EM CAMPO BUSCAR
    		Alert mensage = new Alert(Alert.AlertType.ERROR);
    		mensage.setTitle("Erro");
    		mensage.setHeaderText(null);
    		mensage.setContentText("Preencha o campo de buscar");
    		mensage.showAndWait();
    		ListarProdutosTab(null);
    		
    		txtNome.clear();
            txtDescricao.clear();
            txtCategoria.clear();    // O CLEAR TBM LIMPA OS CAMPOS
            txtPreco.clear();
            txtQuantidade.clear();
            txtId.clear();
            txtCode.setText(ProdutoModel.gerarEAN13());
            txtPrecoComprado.clear();
    		
    	}
    	
    }
    
    // MÉTODO PARA EXCLUIR PRODUTO
    public void Excluir() {
    	
    	produto.Excluir();
    	
    	txtNome.clear();
        txtDescricao.clear();
        txtCategoria.clear();    // O CLEAR TBM LIMPA OS CAMPOS
        txtPreco.clear();
        txtQuantidade.clear();
        txtId.clear();
        txtCode.clear();
        txtPrecoComprado.setText(ProdutoModel.gerarEAN13());
        ListarProdutosTab(null);
    	
    }
    
    // O MÉTODO INITIALIZE VINCULA DIRETAMENTE O CONTROLE COM O FXML
    // A PALAVRA FXML É RESERVADA DO JAVAFX PARA REALIZAR A INTERAÇÃO
    @FXML
    public void initialize() {
    	
    	btnVoltar.setOnAction(e -> txtNome.getScene().getWindow().hide());
    	
    	txtCode.setText(ProdutoModel.gerarEAN13());
    	
    	txtBuscar.setOnAction(e->{Pesquisar();});
    	
    	// ATRIBUI O TIPO DE INFORMAÇÃO  DOS GETTERS DA MODEL EX: return this.id;
    	colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    	colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    	colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
    	colPreco.setCellFactory(col -> new javafx.scene.control.TableCell<ProdutoModel, Double>() {
    	    @Override
    	    protected void updateItem(Double preco, boolean empty) {
    	        super.updateItem(preco, empty);
    	        if (empty || preco == null) {
    	            setText(null);
    	        } else {
    	            setText(String.format("%.2f", preco).replace(".", ","));
    	        }
    	    }
    	});
    	colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    	colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
    	colPrecoCusto.setCellValueFactory(new PropertyValueFactory<>("precoCusto"));
    	
    	AtualizarCampo();
    	
    	ListarProdutosTab(null);
    	
    }
    
    public void ListarProdutosTab(String valor) {
    	
    	List <ProdutoModel> produtos = produto.ListarProdutos(valor);
    	listaProdutos = FXCollections.observableArrayList(produtos);
    	tabProduto.setItems(listaProdutos);
    	
    }
    
    public void AtualizarCampo() {
    	
    	tabProduto.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
    		
    		if (selecionado != null) {
                txtId.setText(String.format("%06d",selecionado.getId()));
                txtNome.setText(selecionado.getNome());
                txtDescricao.setText(selecionado.getDescricao());
                txtCategoria.setText(selecionado.getCategoria());
                txtPreco.setText(String.valueOf(selecionado.getPreco()).replace(".", ","));
                txtQuantidade.setText(String.valueOf(selecionado.getQuantidade()));
                txtCode.setText(selecionado.getCodigo());
                txtPrecoComprado.setText(String.valueOf(selecionado.getPrecoCusto()));
                
                produto = selecionado;
            }
    		
    	});
    	
    }
    
    public void CalcularPreco() {
    	
    	try {
    		
    		double precoCusto = Double.parseDouble(txtPrecoComprado.getText().replace(",", "."));
    		
    		if(precoCusto <= 0){
                throw new Exception();
            }
    		
    		double margem = 0;
    		
    		if(precoCusto > 0 && precoCusto <= 10) {
    			margem = 5;
    		} else if(precoCusto <= 40 && precoCusto > 10) {
    			margem = 10;
    		}else if(precoCusto <= 100 && precoCusto > 40) {
    			margem = 25;
    		}else if(precoCusto > 100 && precoCusto <= 200) {
    			margem = 33;
    		}else if(precoCusto > 200) {
    			margem = 45;
    		}
    		
    		double precoVenda = precoCusto + (precoCusto * margem / 100);
    		double lucro = precoVenda - precoCusto;
    		double porcentagem = (lucro / precoCusto) * 100;
    		
    		String mensagem = "Sugestão de Venda\n\n"
                    + "Preço de Custo: R$ " + String.format("%.2f", precoCusto).replace(".", ",") + "\n"
                    + "Margem aplicada: " + margem + "%\n\n"
                    + "Preço sugerido: R$ " + String.format("%.2f", precoVenda).replace(".", ",") + "\n"
                    + "Lucro: R$ " + String.format("%.2f", lucro).replace(".", ",") + "\n"
                    + "Porcentagem de ganho: " + String.format("%.2f", porcentagem) + "%";
    		
    		Alert mensage = new Alert(Alert.AlertType.INFORMATION);
    		mensage.setTitle("Sugestão de venda");
    		mensage.setHeaderText(null);
    		mensage.setContentText(mensagem);
    		mensage.showAndWait();
    		
    	}catch(Exception e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setContentText("Preencha o preço de custo");
    		alert.showAndWait();
    	}
    	
    }

}
