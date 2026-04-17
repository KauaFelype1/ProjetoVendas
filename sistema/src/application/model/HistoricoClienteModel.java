package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import application.conexao;

public class HistoricoClienteModel {

    private int idCompra;
    private int idCliente;
    private String produto;
    private int quantidade;
    private double preco;
    private String dataCompra;

    public HistoricoClienteModel(int idCompra, int idCliente, String produto, int quantidade, double preco, String dataCompra) {
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataCompra = dataCompra;
    }

    public int getIdCompra() { return idCompra; }
    public int getIdCliente() { return idCliente; }
    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }
    public String getDataCompra() { return dataCompra; }
    
    public List<HistoricoClienteModel> listarHistorico(int idCli) {

        List<HistoricoClienteModel> lista = new ArrayList<>();

        try (Connection conn = conexao.getConnection();
             PreparedStatement consulta = conn.prepareStatement(

            		 "SELECT v.id, v.idCli, p.nome, i.quantidade, i.precoUnitario, v.dataHora\r\n"
            		 + "FROM venda v\r\n"
            		 + "JOIN itemVenda i ON v.id = i.idVenda\r\n"
            		 + "JOIN produto p ON i.idProd = p.id\r\n"
            		 + "WHERE v.idCli = ?\r\n"
            		 + "ORDER BY v.dataHora DESC\r\n"
            		 + "LIMIT 5"

             )) {

        	consulta.setInt(1, idCli);
            ResultSet resultado = consulta.executeQuery();

            while (resultado.next()) {
                lista.add(new HistoricoClienteModel(
                		resultado.getInt("id"),
                		resultado.getInt("idCli"),
                		resultado.getString("nome"),
                		resultado.getInt("quantidade"),
                		resultado.getDouble("precoUnitario"),
                		resultado.getString("dataHora")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
}