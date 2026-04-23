Projeto Trading System

Descrição

Sistema desktop desenvolvido em Java utilizando JavaFX para controle de vendas, produtos e estoque.

O sistema permite:

* Cadastro de produtos
* Controle de estoque (entrada e saída)
* Histórico de movimentações
* Sugestão automática de preço de venda

---

Tecnologias utilizadas

* Java
* JavaFX
* MySQL

---

Telas do sistema

* Login
* Novo Funcionário
* Cadastro de clientes
* Cadastro de produtos
* Histórico de estoque
* Historico de vendas
* Controle de vendas

---

Como executar o projeto

1. Clone o repositório:

```
git clone https://github.com/KauaFelype1/ProjetoVendas.git
```

2. Abra no Eclipse ou IntelliJ

3. Configure o JavaFX:

* Adicione as bibliotecas do JavaFX
* Configure VM Options

4. Execute a classe principal

---

Banco de dados

Crie o banco com base no modelo:

* Banco sistema
* Tabela: produto
* Tabela: movimentacao_estoque

--
Intuito do projeto

Facilitar as vendas de um comércio, onde pode-se controlar todo o estoque de produtos, processar o historico de movimentação de entrada e saída de um produto
além de ter acesso aos usuários, podendo controlalos de forma limpa e segura

--

Sistema do banco:
create database Sistema;
use sistema;

create table produto(
id int primary key auto_increment,
nome varchar(100) not null,
descricao text,
categoria varchar(50),
preco double(10,2) not null,
quantidade int not null
);

select * from produto;

select * from produto where nome = 'Arroz';

select * from produto where categoria like '%Eletrônicos%';

alter table produto modify codigo varchar(13) unique;
SHOW INDEX FROM produto;

alter table produto add precoCusto decimal(10,2);

alter table cliente add statusCli varchar(20);

create table movimentacaoEstoque(
id int primary key auto_increment,
idProd int not null,                 -- id do produto
dataHora datetime not null,          -- data e hora do processamento
quantidade int not null,             -- quantidade movimentada
tipo int not null,                   -- 0 = entrada, 1 = saida
constraint fk_produto foreign key (idProd) references produto(id)
);

select * from movimentacaoEstoque;

create table cliente (
idCli int primary key auto_increment,
nome varchar(100) not null,
cpf varchar(11) unique not null,
email varchar(50) unique not null,
senha varchar(50) not null
);

create table funcionario(
idFun int primary key auto_increment,
nome varchar(100) not null,
cpf varchar(11) unique not null,
email varchar(50) unique not null,
senha varchar(50) not null,
funcao varchar(100) not null,
cnpj varchar(14) unique not null
);

DELIMITER //

CREATE TRIGGER validar_funcionario
BEFORE INSERT ON funcionario
FOR EACH ROW
BEGIN
    -- VALIDA CPF: deve ter exatamente 11 caracteres numéricos
    IF LENGTH(NEW.cpf) != 11 OR NEW.cpf REGEXP '[^0-9]' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CPF inválido! Deve conter exatamente 11 números.';
    END IF;

    -- VALIDA CNPJ: deve ter exatamente 14 caracteres numéricos
    IF LENGTH(NEW.cnpj) != 14 OR NEW.cnpj REGEXP '[^0-9]' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CNPJ inválido! Deve conter exatamente 14 números.';
    END IF;

    -- VALIDA EMAIL: deve conter @ e pelo menos um ponto depois do @
    IF NEW.email NOT REGEXP '^[^@]+@[^@]+\.[^@]+$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Email inválido!';
    END IF;
END //

DELIMITER ;

select * from funcionario;
select * from cliente;

DELIMITER //

CREATE TRIGGER validar_cliente
BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN
    -- VALIDA CPF: deve ter exatamente 11 caracteres numéricos
    IF LENGTH(NEW.cpf) != 11 OR NEW.cpf REGEXP '[^0-9]' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CPF inválido! Deve conter exatamente 11 números.';
    END IF;

    -- VALIDA EMAIL: deve conter @ e pelo menos um ponto depois do @
    IF NEW.email NOT REGEXP '^[^@]+@[^@]+\.[^@]+$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Email inválido!';
    END IF;
END //

DELIMITER ;


create table compra(
id_compra int primary key auto_increment,
idCli int,
idProd int,
quantidade int,
preco decimal(10,2),
dataCompra datetime default current_timestamp,

foreign key (idCli) references cliente(idCli),
foreign key (idProd) references produto(id)
);


-- ITENS DA VENDA
CREATE TABLE itemVenda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idVenda INT,
    idProd INT,
    quantidade INT,
    precoUnitario DOUBLE(10,2),
    FOREIGN KEY (idVenda) REFERENCES venda(id),
    FOREIGN KEY (idProd) REFERENCES produto(id)
);

-- PAGAMENTOS
CREATE TABLE pagamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idVenda INT,
    tipo VARCHAR(20),
    valor DOUBLE(10,2),
    FOREIGN KEY (idVenda) REFERENCES venda(id)
);

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS venda;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE venda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idCli INT,
    dataHora DATETIME NOT NULL,
    totalVenda DOUBLE(10,2),
    desconto DOUBLE(10,2),
    FOREIGN KEY (idCli) REFERENCES cliente(idCli)
);


select * from venda;
select * from itemVenda;
select * from pagamento;
select * from cliente;
select * from funcionario;
