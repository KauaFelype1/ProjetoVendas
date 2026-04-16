package application.model;

public class ItemVendaModel {

    private int idProduto;
    private String nome;
    private int quantidade;
    private double preco;
    private double subtotal;

    public ItemVendaModel(int idProduto, String nome, int quantidade, double preco) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.subtotal = quantidade * preco;
    }

    public int getIdProduto() { return idProduto; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }
    public double getSubtotal() { return subtotal; }
    
    public void setIdProduto(int idProduto) {this.idProduto=idProduto;}
    public void setNome(String nome) {this.nome=nome;}
    public void setQuantidade(int quantidade) {this.quantidade=quantidade;}
    public void setPreco(double preco) {this.preco=preco;}
    public void setSubtotal(double subtotal) {this.subtotal=subtotal;}
    
}
