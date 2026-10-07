package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Bomba;
import models.Cor;
import models.Fruta;
import models.MovimentoInvalidoException;
import models.Obstaculo;
import models.Robo;
import models.RoboInteligente;
import models.Rocha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InterfaceFruitBot extends Application {
    private final GridPane tabuleiro = new GridPane();
    private final ComboBox<Integer> modo = new ComboBox<>();
    private final ComboBox<Cor> cor = new ComboBox<>();
    private final ComboBox<String> tipoObstaculo = new ComboBox<>();
    private final Spinner<Integer> x = new Spinner<>(1, 4, 1);
    private final Spinner<Integer> y = new Spinner<>(1, 4, 1);
    private final Label mensagem = new Label("Escolha um modo e clique em Novo Jogo.");
    private final Label estatisticas = new Label();
    private final List<Robo> robos = new ArrayList<>();
    private final List<Obstaculo> obstaculos = new ArrayList<>();
    private final List<Robo> achouFruta = new ArrayList<>();
    private Fruta fruta;
    private Timeline automatico;
    private int modoAtual;
    private int roboDaVez;
    private boolean fim;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Fruit Bot");
        stage.setMinWidth(900);
        stage.setMinHeight(650);

        modo.getItems().addAll(1, 2, 3, 4);
        modo.setPromptText("Modo de jogo");
        cor.getItems().addAll(Cor.values());
        cor.setValue(Cor.RED);
        tipoObstaculo.getItems().addAll("Rocha", "Bomba");
        tipoObstaculo.setValue("Rocha");

        Button novoJogo = new Button("Novo Jogo");
        Button addRobo = new Button("Adicionar Robô");
        Button addFruta = new Button("Adicionar Fruta");
        Button addObstaculo = new Button("Adicionar Obstáculo");
        Button automaticoBtn = new Button("Iniciar automático");

        novoJogo.setMaxWidth(Double.MAX_VALUE);
        addRobo.setMaxWidth(Double.MAX_VALUE);
        addFruta.setMaxWidth(Double.MAX_VALUE);
        addObstaculo.setMaxWidth(Double.MAX_VALUE);
        automaticoBtn.setMaxWidth(Double.MAX_VALUE);

        novoJogo.setOnAction(e -> novoJogo());
        addRobo.setOnAction(e -> adicionarRobo());
        addFruta.setOnAction(e -> adicionarFruta());
        addObstaculo.setOnAction(e -> adicionarObstaculo());
        automaticoBtn.setOnAction(e -> iniciarPararAutomatico(automaticoBtn));

        Button cima = new Button("↑");
        Button baixo = new Button("↓");
        Button esquerda = new Button("←");
        Button direita = new Button("→");
        cima.setOnAction(e -> moverManual(1));
        baixo.setOnAction(e -> moverManual(2));
        esquerda.setOnAction(e -> moverManual(4));
        direita.setOnAction(e -> moverManual(3));

        HBox setas = new HBox(8, esquerda, cima, baixo, direita);
        setas.setAlignment(Pos.CENTER);

        VBox lateral = new VBox(10,
                new Label("Modo:"), modo,
                new Label("Cor:"), cor,
                new Label("Posição:"), new HBox(8, x, y),
                novoJogo, addRobo, addFruta,
                new Separator(),
                new Label("Obstáculos - modo 4"), tipoObstaculo, addObstaculo,
                new Separator(),
                new Label("Controle do modo 1"), setas,
                automaticoBtn,
                new Separator(), estatisticas
        );
        lateral.setPadding(new Insets(15));
        lateral.setPrefWidth(250);

        VBox centro = new VBox(15, tabuleiro, mensagem);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setLeft(lateral);
        root.setCenter(centro);

        stage.setScene(new Scene(root, 950, 700));
        stage.show();
        desenharTabuleiro();
    }

    private void novoJogo() {
        pararAutomatico();
        if (modo.getValue() == null) {
            mensagem.setText("Escolha o modo 1, 2, 3 ou 4.");
            return;
        }
        modoAtual = modo.getValue();
        robos.clear();
        obstaculos.clear();
        achouFruta.clear();
        fruta = null;
        roboDaVez = 0;
        fim = false;
        mensagem.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");
        mensagem.setText("Modo " + modoAtual + " iniciado. Adicione os robôs e a fruta.");
        atualizar();
    }

    private void adicionarRobo() {
        if (modoAtual == 0) {
            mensagem.setText("Clique em Novo Jogo primeiro.");
            return;
        }

        int limite = modoAtual == 1 ? 1 : 2;

        if (robos.size() >= limite) {
            mensagem.setText("Quantidade máxima de robôs atingida.");
            return;
        }

        int px = x.getValue();
        int py = y.getValue();
        Cor c = cor.getValue();

        if (ocupada(px, py)) {
            mensagem.setText("A posição já está ocupada.");
            return;
        }

        for (Robo r : robos){
            if (r.getCor() == c) {
                mensagem.setText("Escolha outra cor.");
                return;
            }
        }

        Robo robo;
        if ((modoAtual == 3 || modoAtual == 4) && robos.size() == 1){
            mensagem.setText("Defina o robô inteligente: "); robo = new RoboInteligente(c);
        }else{
            mensagem.setText("Defina o robô inteligente: "); robo = new Robo(c);
        }

        robo.setPosX(px);
        robo.setPosY(py);
        robos.add(robo);
        mensagem.setText("Robô " + c.getTipoCor() + " adicionado em [" + px + "," + py + "].");
        atualizar();
    }

    private void adicionarFruta() {
        if (modoAtual == 0) { mensagem.setText("Clique em Novo Jogo primeiro."); return; }
        int px = x.getValue(), py = y.getValue();
        if (ocupada(px, py)) { mensagem.setText("A posição já está ocupada."); return; }
        if (fruta != null) { mensagem.setText("A fruta já foi adicionada."); return; }
        fruta = new Fruta(px, py);
        mensagem.setText("Fruta adicionada em [" + px + "," + py + "].");
        atualizar();
    }

    private void adicionarObstaculo() {
        if (modoAtual != 4) {
            mensagem.setText("Obstáculos só podem ser usados no modo 4.");
            return;
        }

        int px = x.getValue();
        int py = y.getValue();

        if (ocupada(px, py)) {
            mensagem.setText("A posição já está ocupada.");
            return;
        }

        Obstaculo o = "Bomba".equals(tipoObstaculo.getValue()) ? new Bomba(obstaculos.size() + 1) : new Rocha(obstaculos.size() + 1);
        o.setPosX(px);
        o.setPosY(py);
        obstaculos.add(o);
        mensagem.setText((o instanceof Bomba ? "Bomba" : "Rocha") + " adicionada em [" + px + "," + py + "].");
        atualizar();
    }

    private boolean pronto() {
        int quantidade = modoAtual == 1 ? 1 : 2;
        if (robos.size() != quantidade) { mensagem.setText("Adicione " + quantidade + " robô(s)."); return false; }
        if (fruta == null) { mensagem.setText("Adicione a fruta."); return false; }
        return true;
    }

    private void moverManual(int direcao) {
        if (modoAtual != 1) { mensagem.setText("As setas são usadas apenas no modo 1."); return; }
        if (!pronto() || fim) return;
        mover(robos.get(0), direcao);
    }

    private void iniciarPararAutomatico(Button botao) {
        if (automatico != null) {
            pararAutomatico();
            botao.setText("Iniciar automático");
            return;
        }
        if (modoAtual == 1) { mensagem.setText("No modo 1 use as setas."); return; }
        if (!pronto() || fim) return;
        automatico = new Timeline(new KeyFrame(Duration.millis(600), e -> passoAutomatico()));
        automatico.setCycleCount(Timeline.INDEFINITE);
        automatico.play();
        botao.setText("Mover robôs randomicamente");
    }

    private void pararAutomatico() {
        if (automatico != null) { automatico.stop(); automatico = null; }
    }

    private void passoAutomatico() {

        if (fim) {
            pararAutomatico();
            return;
        }

        if (modoAtual == 2) {
            mover(robos.get(roboDaVez), ThreadLocalRandom.current().nextInt(1, 5));
            roboDaVez = (roboDaVez + 1) % 2;
        } else {
            int idx = proximoRobo();
            if (idx == -1) {
                finalizar("Fim: não há robôs ativos.");
                return;
            }

            roboDaVez = idx;
            Robo robo = robos.get(idx);

            try {
                if (robo instanceof RoboInteligente inteligente) inteligente.mover();
                else robo.moverRandomico();
                verificarPosicao(robo);
            } catch (MovimentoInvalidoException e) {
                mensagem.setText("Movimento inválido. O robô permanece no mesmo lugar.");
            }
            roboDaVez = (roboDaVez + 1) % 2;
            atualizar();
        }
    }

    private void mover(Robo robo, int direcao) {
        try {
            robo.mover(direcao);
            verificarPosicao(robo);
        } catch (MovimentoInvalidoException e) {
            mensagem.setText(e.getMessage());
        }
        atualizar();
    }

    private void verificarPosicao(Robo robo) {
        if (robo.getExplodiu()) return;

        if (fruta != null && robo.encontrouAlimento(fruta)) {
            if (modoAtual == 3) {
                if (!achouFruta.contains(robo)) achouFruta.add(robo);
                if (achouFruta.size() == 2) finalizar("Os dois robôs encontraram a fruta!");
                else mensagem.setText("Um robô encontrou a fruta. Falta o outro.");
            } else {
                finalizar("Robô " + robo.getCor().getTipoCor() + " encontrou a fruta!");
            }
            return;
        }

        if (modoAtual == 4) {
            for (int i = 0; i < obstaculos.size(); i++) {
                Obstaculo o = obstaculos.get(i);
                if (o.getPosX() == robo.getPosX() && o.getPosY() == robo.getPosY()) {
                    o.bater(robo);
                    if (o instanceof Bomba) {
                        obstaculos.remove(i);
                        finalizar("BOOM! O robô " + robo.getCor().getTipoCor() + " EXPLODIU! A partida termina imediatamente.");
                        return;
                    } else {
                        mensagem.setText("O robô bateu na rocha e voltou para a posição anterior.");
                    }
                    break;
                }
            }
            if (robos.stream().allMatch(Robo::getExplodiu)) finalizar("Os dois robôs explodiram. Fim de jogo.");
        }
    }

    private int proximoRobo() {
        for (int i = 0; i < robos.size(); i++) {
            int idx = (roboDaVez + i) % robos.size();
            Robo r = robos.get(idx);
            if (r.getExplodiu()) continue;
            if (modoAtual == 3 && achouFruta.contains(r)) continue;
            return idx;
        }
        return -1;
    }

    private boolean ocupada(int px, int py) {
        if (fruta != null && fruta.getPosX() == px && fruta.getPosY() == py) return true;
        for (Robo r : robos) if (r.getPosX() == px && r.getPosY() == py) return true;
        for (Obstaculo o : obstaculos) if (o.getPosX() == px && o.getPosY() == py) return true;
        return false;
    }

    private void finalizar(String texto) {
        fim = true;
        pararAutomatico();
        mensagem.setText("FIM DE JOGO\n" + texto);
        mensagem.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        atualizar();
    }

    private void atualizar() {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < robos.size(); i++) {
            Robo r = robos.get(i);
            texto.append("Robô ").append(i + 1).append(" - ").append(r.getCor().getTipoCor())
                    .append("\nVálidos: ").append(r.getQtdMovimentosValidos())
                    .append(" | Inválidos: ").append(r.getQtdMovimentosInvalidos()).append("\n");
            if (r.getExplodiu()) texto.append("EXPLODIU\n");
        }
        estatisticas.setText(texto.toString());
        desenharTabuleiro();
    }

    private void desenharTabuleiro() {
        tabuleiro.getChildren().clear();
        tabuleiro.setHgap(3);
        tabuleiro.setVgap(3);

        for (int linha = 1; linha <= 4; linha++) {
            for (int coluna = 1; coluna <= 4; coluna++) {
                VBox casa = new VBox();
                casa.setAlignment(Pos.CENTER);
                casa.setPrefSize(120, 120);
                casa.setStyle("-fx-background-color: #eeeeee; -fx-border-color: #555555;");
                Label pos = new Label(linha + "," + coluna);
                pos.setTextFill(Color.GRAY);
                casa.getChildren().add(pos);

                if (fruta != null && fruta.getPosX() == linha && fruta.getPosY() == coluna) {
                    Label f = new Label("Fruta");
                    f.setFont(Font.font(18));
                    casa.getChildren().add(f);
                }

                for (Obstaculo o : obstaculos) {
                    if (o.getPosX() == linha && o.getPosY() == coluna) {
                        Label ob = new Label(o instanceof Bomba ? "BOMBA" : "ROCHA");
                        ob.setFont(Font.font(15));
                        casa.getChildren().add(ob);
                    }
                }

                for (Robo r : robos) {
                    if (r.getPosX() == linha && r.getPosY() == coluna && !r.getExplodiu()) {
                        Circle c = new Circle(20, corJavaFX(r.getCor()));

                        Label nome = null;

                        if(r instanceof RoboInteligente ){
                             nome = new Label("Bot inteligente " + (robos.indexOf(r) + 1));

                        }else{

                            nome = new Label("Bot normal " + (robos.indexOf(r) + 1));
                        }

                        nome.setTextFill(Color.GRAY);
                        VBox robo = new VBox(2, c, nome);
                        robo.setAlignment(Pos.CENTER);
                        casa.getChildren().add(robo);
                    }
                }
                tabuleiro.add(casa, coluna - 1, linha - 1);
            }
        }
    }

    private Color corJavaFX(Cor c) {
        return switch (c) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case WHITE -> Color.GRAY;
            case DARK -> Color.DARKGRAY;
            case YELLOW -> Color.GOLD;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
