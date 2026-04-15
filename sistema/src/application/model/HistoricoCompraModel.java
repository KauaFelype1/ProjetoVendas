package application.model;

public class HistoricoCompraModel {
	
	private int idCompra;
	private int idCli;
	private int idProd;
	private int quantidade;
	private double preco;
	private String dataCompra;
	private String nomeProduto;
	
	public HistoricoCompraModel(int idCompra, int idCli, int idProd, int quantidade, double preco, String dataCompra, String nomeProduto) {
		this.idCompra=idCompra;
		this.idCli=idCli;
		this.idProd=idProd;
		this.quantidade=quantidade;
		this.preco=preco;
		this.dataCompra=dataCompra;
		this.nomeProduto=nomeProduto;
	}
	
	public int getIdCompra() {return idCompra;}
	public int getIdCli() {return idCli;}
	public int getIdProd() {return idProd;}
	public int getQuantidade() {return quantidade;}
	public double getPreco() {return preco;}
	public String getDataCompra() {return dataCompra;}
	public String getNomeProduto() {return nomeProduto;}
	
	public void setIdCompra(int idCompra) {this.idCompra=idCompra;}
	public void setIdCli(int idCli) {this.idCli=idCli;}
	public void setIdProd(int idProd) {this.idProd=idProd;}
	public void setQuantidade(int quantidade) {this.quantidade=quantidade;}
	public void setPreco(double preco) {this.preco=preco;}
	public void setDataCompra(String dataCompra) {this.dataCompra=dataCompra;}
	public void setNomeProduto(String nomeProduto) {this.nomeProduto=nomeProduto;}

}
