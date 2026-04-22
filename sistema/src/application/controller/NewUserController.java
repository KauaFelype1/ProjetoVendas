package application.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import application.model.FuncionarioModel;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class NewUserController implements Initializable {

    @FXML private MenuItem ItemEstoquista;
    @FXML private MenuItem ItemGerente;
    @FXML private MenuItem ItemOperario;
    @FXML private MenuItem ItemVendedor;

    @FXML private MenuButton MenuFuncao;
    @FXML private Button btnCadastrar;

    @FXML private TextField txtCnpj;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNome;
    @FXML private TextField txtSenha;

    @FXML private Label lblCancelar;

    @FXML private HBox card;
    @FXML private Pane fundoParticulas;

    FuncionarioModel funcionario = new FuncionarioModel(0, null, null, null, null, null, null);

    private String funcaoSelecionada = null;

    private static final int QTD_PARTICULAS = 40;
    private static final double DIST_CONEXAO = 100;

    private double LARGURA = 860;
    private double ALTURA = 570;

    private final List<Particula> particulas = new ArrayList<>();
    private final List<Line> linhas = new ArrayList<>();
    private final Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Rectangle clip = new Rectangle(830, 500);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        card.setClip(clip);

        fundoParticulas.widthProperty().addListener((obs, o, n) -> LARGURA = n.doubleValue());
        fundoParticulas.heightProperty().addListener((obs, o, n) -> {
            ALTURA = n.doubleValue();
            for (Particula p : particulas) {
                p.x = random.nextDouble() * LARGURA;
                p.y = random.nextDouble() * ALTURA;
            }
        });

        iniciarParticulas();

        ItemOperario.setOnAction(e -> {
            funcaoSelecionada = ItemOperario.getText();
            MenuFuncao.setText(funcaoSelecionada);
        });

        ItemGerente.setOnAction(e -> {
            funcaoSelecionada = ItemGerente.getText();
            MenuFuncao.setText(funcaoSelecionada);
        });

        ItemVendedor.setOnAction(e -> {
            funcaoSelecionada = ItemVendedor.getText();
            MenuFuncao.setText(funcaoSelecionada);
        });

        ItemEstoquista.setOnAction(e -> {
            funcaoSelecionada = ItemEstoquista.getText();
            MenuFuncao.setText(funcaoSelecionada);
        });

        lblCancelar.setOnMouseClicked(e -> fecharJanela());
    }

    @FXML
    public void Salvar() {

        if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty()
                || txtEmail.getText().isEmpty() || txtSenha.getText().isEmpty()
                || txtCnpj.getText().isEmpty()) {

            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Erro");
            msg.setHeaderText(null);
            msg.setContentText("Preencha todos os campos");
            msg.showAndWait();

            if (funcaoSelecionada == null) {
                Alert msg2 = new Alert(Alert.AlertType.ERROR);
                msg2.setTitle("Erro");
                msg2.setHeaderText(null);
                msg2.setContentText("Selecione uma função");
                msg2.showAndWait();
            }

            return;
        }

        funcionario.setNome(txtNome.getText());
        funcionario.setCpf(txtCpf.getText());
        funcionario.setEmail(txtEmail.getText());
        funcionario.setSenha(txtSenha.getText());
        funcionario.setFuncao(funcaoSelecionada);
        funcionario.setCnpj(txtCnpj.getText());

        if (funcionario.Salvar()) {

            Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
            msg.setTitle("Tudo certo");
            msg.setHeaderText(null);
            msg.setContentText("Cadastro realizado com sucesso!");
            msg.showAndWait();

            fecharJanela();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) card.getScene().getWindow();
        stage.close();
    }

    private void iniciarParticulas() {

        for (int i = 0; i < 200; i++) {
            Line linha = new Line();
            linha.setStroke(Color.web("#6460dc", 0.0));
            linha.setStrokeWidth(0.5);
            linhas.add(linha);
            fundoParticulas.getChildren().add(linha);
        }

        for (int i = 0; i < QTD_PARTICULAS; i++) {
            Circle c = new Circle(random.nextDouble() * 2 + 1);
            c.setFill(Color.web("#a09cff", 0.7));
            fundoParticulas.getChildren().add(c);

            Particula p = new Particula();
            p.circulo = c;
            p.x = random.nextDouble() * LARGURA;
            p.y = random.nextDouble() * ALTURA;
            p.vx = (random.nextDouble() - 0.5) * 0.6;
            p.vy = (random.nextDouble() - 0.5) * 0.6;

            particulas.add(p);
        }

        new AnimationTimer() {
            public void handle(long now) {
                atualizarParticulas();
            }
        }.start();
    }

    private void atualizarParticulas() {

        for (Particula p : particulas) {
            p.x += p.vx;
            p.y += p.vy;

            if (p.x < 0 || p.x > LARGURA) p.vx *= -1;
            if (p.y < 0 || p.y > ALTURA) p.vy *= -1;

            p.circulo.setCenterX(p.x);
            p.circulo.setCenterY(p.y);
        }

        int idx = 0;

        for (int i = 0; i < particulas.size(); i++) {
            for (int j = i + 1; j < particulas.size(); j++) {

                if (idx >= linhas.size()) break;

                Particula a = particulas.get(i);
                Particula b = particulas.get(j);

                double dist = Math.hypot(a.x - b.x, a.y - b.y);
                Line l = linhas.get(idx++);

                if (dist < DIST_CONEXAO) {
                    double op = 0.4 * (1 - dist / DIST_CONEXAO);
                    l.setStartX(a.x);
                    l.setStartY(a.y);
                    l.setEndX(b.x);
                    l.setEndY(b.y);
                    l.setStroke(Color.web("#6460dc", op));
                } else {
                    l.setStroke(Color.TRANSPARENT);
                }
            }
        }
    }

    private static class Particula {
        Circle circulo;
        double x, y, vx, vy;
    }
}