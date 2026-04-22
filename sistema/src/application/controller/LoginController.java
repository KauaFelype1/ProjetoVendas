package application.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import application.model.FuncionarioModel;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private Button    btnEntrar;
    @FXML private Label     lblNewUser;
    @FXML private TextField txtLogin;

    @FXML private HBox          card;
    @FXML private Pane          fundoParticulas;
    @FXML private PasswordField txtSenha;
    @FXML private TextField     txtSenhaVisivel;
    @FXML private Label         btnMostrarSenha;

    private boolean senhaVisivel = false;
    private Font    fontAwesome;

    FuncionarioModel funcionario = new FuncionarioModel(0, null, null, null, null, null, null);

    private static final int    QTD_PARTICULAS = 40;
    private static final double DIST_CONEXAO   = 100;
    private double LARGURA = 775;
    private double ALTURA  = 570;

    private final List<Particula> particulas = new ArrayList<>();
    private final List<Line>      linhas     = new ArrayList<>();
    private final Random          random     = new Random();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Rectangle clip = new Rectangle(744, 463);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        card.setClip(clip);

        fontAwesome = Font.loadFont(
            getClass().getResourceAsStream("/application/fa-solid-900.ttf"), 14
        );
        btnMostrarSenha.setFont(fontAwesome);
        btnMostrarSenha.setText("\uf070");

        fundoParticulas.widthProperty().addListener((obs, oldW, newW) -> {
            LARGURA = newW.doubleValue();
        });
        fundoParticulas.heightProperty().addListener((obs, oldH, newH) -> {
            ALTURA = newH.doubleValue();
            for (Particula p : particulas) {
                p.x = random.nextDouble() * LARGURA;
                p.y = random.nextDouble() * ALTURA;
            }
        });

        iniciarParticulas();

        txtLogin.setOnAction(e -> txtSenha.requestFocus());
        txtSenha.setOnAction(e -> Entrar());

        lblNewUser.setOnMouseClicked(event -> {
            try {
                Parent root = FXMLLoader.load(
                    getClass().getResource("/application/view/NewUser.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Kauã");
                stage.centerOnScreen();
                stage.setMaximized(true);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    public void Entrar() {

        String senhaAtual = senhaVisivel
            ? txtSenhaVisivel.getText()
            : txtSenha.getText();

        if (txtLogin.getText().isEmpty() || senhaAtual.isEmpty()) {
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Usuário não encontrado");
            msg.setHeaderText(null);
            msg.setContentText("Preencha todos os campos!");
            msg.showAndWait();
            return;
        }

        try {
            if (funcionario.Autenticar(txtLogin.getText(), senhaAtual)) {

                Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
                msg.setTitle("Usuário encontrado!");
                msg.setHeaderText(null);
                msg.setContentText("Email e Senha corretos, Sistema liberado");
                msg.showAndWait();

                txtLogin.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(
                    getClass().getResource("/application/view/sistema.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();

            } else {
                Alert msg = new Alert(Alert.AlertType.ERROR);
                msg.setTitle("Usuário não encontrado");
                msg.setHeaderText(null);
                msg.setContentText("Email ou Senha incorretos");
                msg.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void toggleSenha() {
        senhaVisivel = !senhaVisivel;

        if (senhaVisivel) {
            txtSenhaVisivel.setText(txtSenha.getText());
            txtSenha.setVisible(false);
            txtSenha.setManaged(false);
            txtSenhaVisivel.setVisible(true);
            txtSenhaVisivel.setManaged(true);
            btnMostrarSenha.setFont(fontAwesome);
            btnMostrarSenha.setText("\uf06e");
        } else {
            txtSenha.setText(txtSenhaVisivel.getText());
            txtSenhaVisivel.setVisible(false);
            txtSenhaVisivel.setManaged(false);
            txtSenha.setVisible(true);
            txtSenha.setManaged(true);
            btnMostrarSenha.setFont(fontAwesome);
            btnMostrarSenha.setText("\uf070");
        }
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
            Circle circulo = new Circle(random.nextDouble() * 2 + 1);
            circulo.setFill(Color.web("#a09cff", 0.7));
            fundoParticulas.getChildren().add(circulo);

            Particula p = new Particula();
            p.circulo = circulo;
            p.x  = random.nextDouble() * LARGURA;
            p.y  = random.nextDouble() * ALTURA;
            p.vx = (random.nextDouble() - 0.5) * 0.6;
            p.vy = (random.nextDouble() - 0.5) * 0.6;
            particulas.add(p);
        }

        new AnimationTimer() {
            @Override
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
            if (p.y < 0 || p.y > ALTURA)  p.vy *= -1;
            p.circulo.setCenterX(p.x);
            p.circulo.setCenterY(p.y);
        }

        int idxLinha = 0;
        for (int i = 0; i < particulas.size(); i++) {
            for (int j = i + 1; j < particulas.size(); j++) {
                if (idxLinha >= linhas.size()) break;
                Particula a = particulas.get(i);
                Particula b = particulas.get(j);
                double dist = Math.hypot(a.x - b.x, a.y - b.y);
                Line linha = linhas.get(idxLinha++);
                if (dist < DIST_CONEXAO) {
                    double op = 0.4 * (1 - dist / DIST_CONEXAO);
                    linha.setStartX(a.x); linha.setStartY(a.y);
                    linha.setEndX(b.x);   linha.setEndY(b.y);
                    linha.setStroke(Color.web("#6460dc", op));
                } else {
                    linha.setStroke(Color.TRANSPARENT);
                }
            }
        }
        for (int i = idxLinha; i < linhas.size(); i++) {
            linhas.get(i).setStroke(Color.TRANSPARENT);
        }
    }


    private static class Particula {
        Circle circulo;
        double x, y, vx, vy;
    }
}