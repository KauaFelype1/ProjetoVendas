package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import application.conexao;
import javafx.scene.control.Alert;

public class ClienteModel {
	
	private int idCli;
	private String nome;
	private String cpf;
	private String email;
	private String senha;
	private String statusCli;
	
	public ClienteModel(int idCli, String nome, String cpf, String email, String senha, String statusCli) {
		this.idCli=idCli;
		this.nome=nome;
		this.cpf=cpf;
		this.email=email;
		this.senha=senha;
		this.statusCli=statusCli;
	}
	
	public int getId() {return idCli;}
	public String getNome() {return nome;}
	public String getCpf() {return cpf;}
	public String getEmail() {return email;}
	public String getSenha() {return senha;}
	public String getStatus() {return statusCli;}
	
	public void setId(int idCli) {this.idCli=idCli;}
	public void setNome(String nome) {this.nome=nome;}
	public void setCpf(String cpf) {this.cpf=cpf;}
	public void setEmail(String email) {this.email=email;}
	public void setSenha(String senha) {this.senha=senha;}
	public void setStatus(String statusCli) {this.statusCli=statusCli;}
	
	public boolean Salvar() {
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("insert into cliente (nome, cpf, email, senha, statusCli) value (?,?,?,?,?)");) {
			
			consulta.setString(1, nome);
			consulta.setString(2, cpf);
			consulta.setString(3, email);
			consulta.setString(4, senha);
			consulta.setString(5, statusCli);
			consulta.executeUpdate();
			return true;
			
		}catch(Exception e) {

			String msg = e.getMessage();
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Erro");
			alerta.setHeaderText(null);
			
			if(msg.contains("CPF inválido")) {
	            alerta.setContentText("CPF inválido! Digite apenas os 11 números.");
	        } else if(msg.contains("Email inválido")) {
	            alerta.setContentText("Email inválido! Ex: exemplo@email.com");
	        } else if(msg.contains("Duplicate entry")) {
	            alerta.setContentText("CPF ou email já cadastrado!");
	        } else {
	            alerta.setContentText("Erro ao cadastrar: " + msg);
	        }
			alerta.show();
			return false;
			
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
	
	public List<ClienteModel> ListarClientes(String valor){
		
		List <ClienteModel> clientes = new ArrayList<ClienteModel>();
		
		try(Connection conn = conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("select * from cliente");
				PreparedStatement consultaWhere = conn.prepareStatement("select * from cliente where nome like ? or cpf like ? or email like ?");) {
			
			ResultSet resultado=null;
			
			if(valor == null) {
				resultado = consulta.executeQuery();
			}else {
				consultaWhere.setString(1, "%"+valor+"%");
				consultaWhere.setString(2, "%"+valor+"%");
				consultaWhere.setString(3, "%"+valor+"%");
				resultado = consultaWhere.executeQuery();
			}
			
			while(resultado.next()) {
				ClienteModel c = new ClienteModel(
						resultado.getInt("idCli"),
						resultado.getString("nome"),
						resultado.getString("cpf"),
						resultado.getString("email"),
						resultado.getString("senha"),
						resultado.getString("statusCli")
						);
				clientes.add(c);
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return clientes;
		
	}
	
}
