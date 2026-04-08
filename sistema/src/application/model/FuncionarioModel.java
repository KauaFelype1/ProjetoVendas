package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import application.conexao;
import javafx.scene.control.Alert;

public class FuncionarioModel{

	private int idFun;
	private String nome;
	private String cpf;
	private String email;
	private String senha;
	private String funcao;
	private String cnpj;
	
	public FuncionarioModel(int idFun, String nome, String cpf, String email, String senha, String funcao, String cnpj) {
		this.idFun=idFun;
		this.nome=nome;
		this.cpf=cpf;
		this.email=email;
		this.senha=senha;
		this.funcao=funcao;
		this.cnpj=cnpj;
	}
	
	public int getId() {return idFun;}
	public String getNome() {return nome;}
	public String getCpf() {return cpf;}
	public String getEmail() {return email;}
	public String getSenha() {return senha;}
	public String getFuncao() {return funcao;}
	public String getCnpj() {return cnpj;}
	
	public void setId(int idFun) {this.idFun=idFun;}
	public void setNome(String nome) {this.nome=nome;}
	public void setCpf(String cpf) {this.cpf=cpf;}
	public void setEmail(String email) {this.email=email;}
	public void setSenha(String senha) {this.senha=senha;}
	public void setFuncao(String funcao) {this.funcao=funcao;}
	public void setCnpj(String cnpj) {this.cnpj=cnpj;}
	
	public void Salvar() {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("insert into funcionario(nome, cpf, email, senha, funcao, cnpj) value(?,?,?,?,?,?)")) {
			
			consulta.setString(1, nome);
			consulta.setString(2, cpf);
			consulta.setString(3, email);
			consulta.setString(4, senha);
			consulta.setString(5, funcao);
			consulta.setString(6, cnpj);
			consulta.executeUpdate();
			
		}catch(Exception e) {
			
			String msg = e.getMessage();
		    Alert alerta = new Alert(Alert.AlertType.ERROR);
		    alerta.setTitle("Erro");
		    alerta.setHeaderText(null);

		    if(msg.contains("CPF inválido")) {
		        alerta.setContentText("CPF inválido! Digite apenas os 11 números.");
		    } else if(msg.contains("CNPJ inválido")) {
		        alerta.setContentText("CNPJ inválido! Digite apenas os 14 números.");
		    } else if(msg.contains("Email inválido")) {
		        alerta.setContentText("Email inválido! Ex: exemplo@email.com");
		    } else if(msg.contains("Duplicate entry")) {
		        alerta.setContentText("CPF, email ou CNPJ já cadastrado!");
		    } else {
		        alerta.setContentText("Erro ao cadastrar: " + msg);
		    }

		    alerta.show();
			
		}
		
	}
	
	public boolean Autenticar(String email, String senha) {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("select * from funcionario where BINARY email=? and BINARY senha=?");) {
			
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
