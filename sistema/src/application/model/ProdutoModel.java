package application.model;

import java.sql.Connection;

import application.conexao;
import javafx.scene.control.Alert;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoModel {
	private int id;
	private String nome;
	private String descricao;
	private String categoria;
	private double preco;
	private int quantidade;
	private String codigo;
	private double precoCusto;
	
	public ProdutoModel(int id, String nome, String descricao, String categoria, double preco, int quantidade, String codigo, double precoCusto) {
		this.id=id;
		this.nome=nome;
		this.descricao=descricao;
		this.categoria=categoria;
		this.preco=preco;
		this.quantidade=quantidade;
		this.codigo=codigo;
		this.precoCusto=precoCusto;
	}
	
	public int getId() {
		return id;
	}
	
	public double getPrecoCusto() {
		return precoCusto;
	}
	
	public void setPrecoCusto(double precoCusto) {
		this.precoCusto=precoCusto;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome=nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao=descricao;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria(String categoria) {
		this.categoria=categoria;
	}
	
	public double getPreco() {
		return preco;
	}
	
	public void setPreco(double preco) {
		this.preco=preco;
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade=quantidade;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}
	
	public void Salvar() {
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta=conn.prepareStatement("insert into produto (nome, descricao, categoria, preco, quantidade, codigo, precoCusto) values (?,?,?,?,?,?,?)");) {
			
			// VERIFICA SE EXISTE ID
			if(this.id>0) {   // SE EXISTIR ALTERA, SENÃO CADASTRA
				try{
					PreparedStatement consultaUpdate = conn.prepareStatement("update produto set nome=?,descricao=?,categoria=?,preco=?,quantidade=?,codigo=?,precoCusto=? where id=?");
					consultaUpdate.setString(1,  this.nome);
					consultaUpdate.setString(2,  this.descricao);
					consultaUpdate.setString(3,  this.categoria);
					consultaUpdate.setDouble(4,  this.preco);
					consultaUpdate.setInt(5,  this.quantidade);
					consultaUpdate.setString(6,  this.codigo);
					consultaUpdate.setDouble(7, this.precoCusto);
					consultaUpdate.setInt(8, this.id);
					consultaUpdate.executeUpdate();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			} else {
			consulta.setString(1, this.nome);
			consulta.setString(2, this.descricao);
			consulta.setString(3, this.categoria);
			consulta.setDouble(4, this.preco);      // ESSAS DUAS ULTIMAS A GENTE MUDA O SET STRING PARA SET TIPO DA VARIAVEL, JA QUE ESLAS NÃO SÃO STRING
			consulta.setInt(5, this.quantidade);
			consulta.setString(6, this.codigo);
			consulta.setDouble(7, this.precoCusto);
			
			consulta.executeUpdate();
			
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		ListarProdutos(null);
	}
	
	public void Buscar(String Valor) {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta=conn.prepareStatement("select * from produto where descricao like ? or categoria like ? or nome like ? or id like ?");) {
			
			// COLOCA INFORMAÇÕES NOS PARAMETROS DA CONSULTA SQL REPRESENTADA POR ?
			consulta.setString(1, "%"+Valor+"%");
			consulta.setString(2, "%"+Valor+"%");
			consulta.setString(3, "%"+Valor+"%");
			consulta.setString(4, "%"+Valor+"%");
			// GUARDA O RESULTADO EM UMA VARIAVEL DO TIPO RESULTSET (TIPO DE DADOS SQL)
			ResultSet resultado = consulta.executeQuery();
			// VERIFICA SE RETORNOU DADOS NA CONSULTA
			if(resultado.next()) {
				this.id = resultado.getInt("id");
				this.nome = resultado.getString("nome");
				this.descricao = resultado.getString("descricao");
				this.categoria = resultado.getString("categoria");
				this.preco = resultado.getDouble("preco");
				this.quantidade = resultado.getInt("quantidade");
				this.codigo = resultado.getString("codigo");
			}else {
				// PRODUTO NÃO ENCONTRADO
				Alert mensage = new Alert(Alert.AlertType.ERROR);
				mensage.setTitle("Erro");
				mensage.setHeaderText(null);
				mensage.setContentText("Produto não encontrado!");
				mensage.showAndWait();
				
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void Excluir() {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta=conn.prepareStatement("delete from produto where id=?");) {
			
			//VERIFICA SE O PRODUTO TEM ID
			if(this.id>0) {
				consulta.setInt(1, this.id);
				consulta.executeUpdate();
				
				Alert mensage = new Alert(Alert.AlertType.CONFIRMATION);
				mensage.setTitle("Produto excluído");
				mensage.setHeaderText(null);
				mensage.setContentText("Produto excluído com sucesso!");
				mensage.showAndWait();
				
			}else {
				Alert mensage = new Alert(Alert.AlertType.ERROR);
				mensage.setTitle("Produto excluído");
				mensage.setHeaderText(null);
				mensage.setContentText("Produto não localizado!");
				mensage.showAndWait();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean ProcessaEstoque(String operacao) {

	    if (this.id <= 0) return false;

	    try (Connection conn = conexao.getConnection()) {

	        PreparedStatement verifica = conn.prepareStatement(
	            "SELECT quantidade FROM produto WHERE id=?"
	        );
	        verifica.setInt(1, this.id);

	        ResultSet rs = verifica.executeQuery();

	        if (rs.next()) {

	            int estoqueAtual = rs.getInt("quantidade");

	            if (operacao.equals("Saída")) {

	                if (estoqueAtual <= 0) {
	                    return false;
	                }

	                if (this.quantidade > estoqueAtual) {
	                    return false;
	                }
	            }

	            String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id=?";
	            
	            if (operacao.equals("Saída")) {
	                sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id=?";
	            }

	            PreparedStatement consulta = conn.prepareStatement(sql);
	            consulta.setInt(1, this.quantidade);
	            consulta.setInt(2, this.id);
	            consulta.execute();

	            // 🔥 4. registra movimentação
	            MovimentacaoEstoqueModel movimentacao = new MovimentacaoEstoqueModel(
	                0, this.id, this.nome, null, this.quantidade, operacao
	            );
	            movimentacao.InsereMovimentacao();

	            return true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public List<ProdutoModel> ListarProdutos(String valor) {
		
		List <ProdutoModel> produtos = new ArrayList<ProdutoModel>();
			try(Connection conn = conexao.getConnection();
					PreparedStatement consulta=conn.prepareStatement("select * from produto");
					PreparedStatement consultaWhere=conn.prepareStatement("select * from produto where nome like ? or descricao like ? or categoria like ?");){
				
				ResultSet resultado=null;
				
				if(valor == null) {
					resultado = consulta.executeQuery();
				}else {
					consultaWhere.setString(1, "%"+valor+"%");
					consultaWhere.setString(2, "%"+valor+"%");
					consultaWhere.setString(3, "%"+valor+"%");
					resultado = consultaWhere.executeQuery();
				}
				
				//resultado=consulta.executeQuery();
				while(resultado.next()) {
					ProdutoModel p = new ProdutoModel(
							resultado.getInt("id"),
							resultado.getString("nome"),
							resultado.getString("descricao"),
							resultado.getString("categoria"),
							resultado.getDouble("preco"),
							resultado.getInt("quantidade"),
							resultado.getString("codigo"),
							resultado.getDouble("precoCusto")
							);
					
					produtos.add(p);
					
				}
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return produtos;
			
		}
	
	public static String gerarEAN13() {
	    // GERA OS PRIMEIROS 12 DÍGITOS ALEATÓRIOS
	    StringBuilder codigo = new StringBuilder();
	    java.util.Random random = new java.util.Random();
	    
	    for (int i = 0; i < 12; i++) {
	        codigo.append(random.nextInt(10));
	    }
	    
	    int soma = 0;
	    for (int i = 0; i < 12; i++) {
	        int digito = Character.getNumericValue(codigo.charAt(i));
	        soma += (i % 2 == 0) ? digito : digito * 3;
	    }
	    int verificador = (10 - (soma % 10)) % 10;
	    codigo.append(verificador);
	    
	    return codigo.toString();
	}

}
