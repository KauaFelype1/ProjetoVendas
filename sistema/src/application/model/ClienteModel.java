package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import application.conexao;

public class ClienteModel {
	
	private int idCli;
	private String nome;
	private String cpf;
	private String email;
	private String senha;
	
	public ClienteModel(int idCli, String nome, String cpf, String email, String senha) {
		this.idCli=idCli;
		this.nome=nome;
		this.cpf=cpf;
		this.email=email;
		this.senha=senha;
	}
	
	public int getId() {return idCli;}
	public String getNome() {return nome;}
	public String getCpf() {return cpf;}
	public String getEmail() {return email;}
	public String getSenha() {return senha;}
	
	public void setId(int idCli) {this.idCli=idCli;}
	public void setNome(String nome) {this.nome=nome;}
	public void setCpf(String cpf) {this.cpf=cpf;}
	public void setEmail(String email) {this.email=email;}
	public void setSenha(String senha) {this.senha=senha;}
	
	public void Salvar() {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("insert into cliente (nome, cpf, email, senha) value (?,?,?,?)");) {
			
			consulta.setString(1, nome);
			consulta.setString(2, cpf);
			consulta.setString(3, email);
			consulta.setString(4, senha);
			consulta.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean Autenticar(String email, String senha) {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("select * from cliente where BINARY email=? and BINARY senha=?");) {
			
			consulta.setString(1, email);
			consulta.setString(2, senha);
			
			ResultSet resultado = consulta.executeQuery();
			return resultado.next();
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}
