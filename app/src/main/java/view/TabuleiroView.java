package view;

import partida.Movimento;
import partida.Partida;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabuleiroView extends GridPane {
    public static final int TILE_SIZE = 80;
    private Partida partida;
    private Tabuleiro tabuleiro;
    private Map<Posicao, ImageView> pecasNoTabuleiro;
    private Peca pecaSelecionada;
    private Posicao posicaoSelecionada;
    private Image imagemJogadorPreto;
    private Image imagemJogadorBranco;
    private Label estadoJogoLabel;
    private HBox capturasJogadorPreto;
    private HBox capturasJogadorBranco;


    // Construtor
    public TabuleiroView(Partida partida) {
        this.partida = partida;
        this.tabuleiro = new Tabuleiro();
        this.pecasNoTabuleiro = new HashMap<>();
        desenharTabuleiro();
        inicializarEstadoJogoLabel();
        inicializarCapturasJogadores();
    }

    public void updateTabuleiro(Tabuleiro tabuleiro) {
        for (Posicao posicao : tabuleiro.getPosicoesPecas()) {
            Peca peca = tabuleiro.obterPeca(posicao);
            if (peca != null) {
                if (!pecasNoTabuleiro.containsKey(posicao)) {
                    adicionarPecaNoTabuleiro(posicao, peca);
                } else {
                    pecasNoTabuleiro.get(posicao).setImage(peca.getImage());
                }
            } else {
                if (pecasNoTabuleiro.containsKey(posicao)) {
                    getChildren().remove(pecasNoTabuleiro.get(posicao));
                    pecasNoTabuleiro.remove(posicao);
                }
            }
        }
        updateEstadoJogo(partida.getEstadoJogo().toString());
        atualizarCapturasJogador(capturasJogadorPreto, partida.getTabuleiro().getCapturadasJogadorPreto());
        atualizarCapturasJogador(capturasJogadorBranco, partida.getTabuleiro().getCapturadasJogadorBranco());
    }

    public void desenharTabuleiro() {
        getChildren().clear();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((linha + coluna) % 2 == 0) {
                    casa.setFill(Color.LIGHTGRAY);
                } else {
                    casa.setFill(Color.DARKGRAY);
                }
                add(casa, coluna, linha);
            }
        }
        adicionarImagensJogadores();
        adicionarRegua();
        for (Posicao posicao : tabuleiro.getPosicoesPecas()) {
            Peca peca = tabuleiro.obterPeca(posicao);
            if (peca != null) {
                adicionarPecaNoTabuleiro(posicao, peca);
            }
        }
    }

    public void selecionarPeca(Posicao posicao) {
        Peca peca = tabuleiro.obterPeca(posicao);
        if (peca != null && peca.getCor() == partida.getJogadorAtual().getCor()) {
            this.pecaSelecionada = peca;
            this.posicaoSelecionada = posicao;
        }
    }

    public void moverPeca(Posicao destino) {
        if (pecaSelecionada != null) {
            List<Posicao> movimentosPossiveis = pecaSelecionada.proxMovimento(posicaoSelecionada);
            if (movimentosPossiveis.contains(destino)) {
                animarMovimento(posicaoSelecionada, destino);   
                Movimento movimentoAtual = new Movimento(posicaoSelecionada, destino, pecaSelecionada);  // Usando a pecaSelecionada
                partida.jogar(movimentoAtual);
                clearSelection();
            } else {
                clearSelection();
            }
        }
    }

    private void carregarImagensJogadores() {
        // Carregar imagens dos jogadores (usando o caminho correto)
        imagemJogadorPreto = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png"));
        imagemJogadorBranco = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png"));
    }

    private void inicializarEstadoJogoLabel() {
        estadoJogoLabel = new Label("Vez do Jogador 1");
        estadoJogoLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        estadoJogoLabel.setTextFill(Color.BLACK);
        add(estadoJogoLabel, 0, 10, 8, 1);
    }

    private void inicializarCapturasJogadores(){
        capturasJogadorPreto = new HBox(10);
        capturasJogadorPreto.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogadorPreto.setPrefHeight(100);
        capturasJogadorBranco = new HBox(10);
        capturasJogadorBranco.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogadorBranco.setPrefHeight(100);
        add(capturasJogadorPreto, 0, 9, 8, 1);
        add(capturasJogadorBranco, 0, 10, 8, 1);
    }

    private void adicionarImagensJogadores() {
        carregarImagensJogadores();
        // Coloca as imagens dos jogadores no topo do tabuleiro
        ImageView player1ImageView = new ImageView(imagemJogadorPreto);
        player1ImageView.setFitWidth(50);
        player1ImageView.setFitHeight(50);
        add(player1ImageView, 8, 0);

        ImageView player2ImageView = new ImageView(imagemJogadorBranco);
        player2ImageView.setFitWidth(50);
        player2ImageView.setFitHeight(50);
        add(player2ImageView, 8, 7);
    }

    private void adicionarRegua() {
        for (int i = 0; i < 8; i++) {
            Text colLabel = new Text(String.valueOf(8 - i));
            add(colLabel, i, 8);
        }
        for (int i = 0; i < 8; i++) {
            Text rowLabel = new Text(String.valueOf(8 - i));
            add(rowLabel, 8, i);
        }
    }

    public void adicionarPecaNoTabuleiro(Posicao posicao, Peca peca) {
        ImageView imgView = new ImageView(peca.getImage());
        imgView.setFitWidth(TILE_SIZE);
        imgView.setFitHeight(TILE_SIZE);
        add(imgView, posicao.getColuna(), posicao.getLinha());
        pecasNoTabuleiro.put(posicao, imgView);
    }

    public void clearSelection() {
        pecaSelecionada = null;
        posicaoSelecionada = null;
        clearHighlights();
    }    

    // Destaca os movimentos possíveis
    public void highlightPossibleMoves(List<Posicao> possiveisMovimentos) {
        clearHighlights();
        for (Posicao posicao : possiveisMovimentos) {
            for (javafx.scene.Node node : getChildren()) {
                if (node instanceof Rectangle) {
                    Rectangle casa = (Rectangle) node;
                    if (getRowIndex(casa) == posicao.getLinha() && getColumnIndex(casa) == posicao.getColuna()) {
                        casa.setFill(Color.LIGHTGREEN);
                    }
                }
            }
        }
    }

    // Limpa os destaques de possíveis movimentos
    public void clearHighlights() {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle casa = (Rectangle) node;
                int row = getRowIndex(node);
                int col = getColumnIndex(node);
                casa.setFill((row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.DARKGRAY);
            }
        }
    }

    // Método para animar o movimento de uma peça
    public void animarMovimento(Posicao origem, Posicao destino) {
        ImageView pecaImagem = pecasNoTabuleiro.get(origem);
        if (pecaImagem != null) {
            double origemX = origem.getColuna() * TILE_SIZE;
            double origemY = origem.getLinha() * TILE_SIZE;
            double destinoX = destino.getColuna() * TILE_SIZE;
            double destinoY = destino.getLinha() * TILE_SIZE;
    
            // Remover a peça da UI e do mapa de peças no tabuleiro
            getChildren().remove(pecaImagem);  // Remove a peça da UI
            pecasNoTabuleiro.remove(origem);   // Remove a referência da peça do mapa
    
            // Definir animação
            TranslateTransition transicao = new TranslateTransition();
            transicao.setNode(pecaImagem);
            transicao.setFromX(origemX);
            transicao.setFromY(origemY);
            transicao.setToX(destinoX);
            transicao.setToY(destinoY);
            transicao.setCycleCount(1);
            transicao.setDuration(Duration.seconds(0.5));
            transicao.play();
        }
    }    

    public void updateEstadoJogo(String estadoJogo) {
        estadoJogoLabel.setText(estadoJogo);  // Atualiza o texto do Label com o novo estado do jogo
    }

    public void atualizarCapturasJogador(HBox capturadasJogador, List<Peca> capturadas) {
        capturadasJogador.getChildren().clear();  // Limpa a área de capturas do jogador 1
        System.out.println(capturadas);
        for (Peca peca : capturadas) {
            System.out.println("vrnc");
            ImageView imageView = new ImageView(peca.getImage());
            imageView.setFitWidth(50);  // Define a largura da imagem
            imageView.setPreserveRatio(true); // Mantém a proporção da imagem
            capturadasJogador.getChildren().add(imageView);  // Adiciona a imagem à área de capturas
        }
    }    
}