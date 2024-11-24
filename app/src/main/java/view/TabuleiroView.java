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
    public static final int TILE_SIZE = 80;  // Tamanho de cada célula no tabuleiro
    private Partida partida;
    private Tabuleiro tabuleiro;
    private Map<Posicao, ImageView> pecasNoTabuleiro; // Mapeia posições para ImageViews
    private Peca pecaSelecionada;
    private Posicao posicaoSelecionada;
    
    // Imagens dos jogadores
    private Image player1Image;
    private Image player2Image;
    
    // Label para exibir o estado do jogo
    private Label estadoJogoLabel;

    private HBox capturasJogador1;  // Caixa para as capturas do jogador 1
    private HBox capturasJogador2;  // Caixa para as capturas do jogador 2


    // Construtor
    public TabuleiroView(Partida partida) {
        this.partida = partida;
        this.tabuleiro = new Tabuleiro();
        this.pecasNoTabuleiro = new HashMap<>();
        desenharTabuleiro();
        inicializarEstadoJogoLabel();
        inicializarCapturasJogadores();
    }

    private void carregarImagensJogadores() {
        // Carregar imagens dos jogadores (usando o caminho correto)
        player1Image = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png"));
        player2Image = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png"));
    }

    private void inicializarCapturasJogadores(){
        capturasJogador1 = new HBox(10);
        capturasJogador1.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogador1.setPrefHeight(100);
        capturasJogador2 = new HBox(10);
        capturasJogador2.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogador2.setPrefHeight(100);
        add(capturasJogador1, 0, 9, 8, 1);
        add(capturasJogador2, 0, 10, 8, 1);
    }

    private void inicializarEstadoJogoLabel() {
        estadoJogoLabel = new Label("Vez do Jogador 1");
        estadoJogoLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        estadoJogoLabel.setTextFill(Color.BLACK);
        add(estadoJogoLabel, 0, 10, 8, 1);
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
        atualizarCapturasJogador1(partida.getTabuleiro().getCapturadasJogador1());
        atualizarCapturasJogador2(partida.getTabuleiro().getCapturadasJogador2());
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
    private void adicionarImagensJogadores() {
        carregarImagensJogadores();
        // Coloca as imagens dos jogadores no topo do tabuleiro
        ImageView player1ImageView = new ImageView(player1Image);
        player1ImageView.setFitWidth(50);
        player1ImageView.setFitHeight(50);
        add(player1ImageView, 8, 0);

        ImageView player2ImageView = new ImageView(player2Image);
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

    public void selecionarPeca(Posicao posicao) {
        Peca peca = tabuleiro.obterPeca(posicao);
        if (peca != null && peca.getCor() == partida.getJogadorAtual().getCor()) {
            this.pecaSelecionada = peca;
            this.posicaoSelecionada = posicao;
        }
    }

    // Método para mover a peça
    public void moverPeca(Posicao destino) {
        if (pecaSelecionada != null) {  // Verifica se a peça foi selecionada
            List<Posicao> movimentosPossiveis = pecaSelecionada.proxMovimento(posicaoSelecionada);
            if (movimentosPossiveis.contains(destino)) {
                animarMovimento(posicaoSelecionada, destino);
                Movimento movimentoPeca = new Movimento(posicaoSelecionada, destino, pecaSelecionada);
                tabuleiro.aplicarMovimento(movimentoPeca);
                updateTabuleiro(tabuleiro);
                clearSelection();
            } else {
                System.out.println("Movimento inválido.");
                clearSelection();  // Limpar seleção caso o movimento não seja válido
            }
        } else {
            System.out.println("Nenhuma peça selecionada.");
        }
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
    
            // Após a animação, atualizar a posição e a UI
            transicao.setOnFinished(event -> {
                pecasNoTabuleiro.put(destino, pecaImagem);  // Adiciona a peça ao novo destino
                add(pecaImagem, destino.getColuna(), destino.getLinha());  // Coloca na nova posição na UI
                clearHighlights();  // Limpa os destaques
            });
        }
    }    

    public void updateEstadoJogo(String estadoJogo) {
        estadoJogoLabel.setText(estadoJogo);  // Atualiza o texto do Label com o novo estado do jogo
    }

    public void atualizarCapturasJogador1(List<Peca> capturadas) {
        capturasJogador1.getChildren().clear();
        for (Peca peca : capturadas) {
            ImageView imageView = new ImageView(peca.getImage());
            imageView.setFitWidth(50);  // Definindo a largura da imagem
            imageView.setPreserveRatio(true); // Mantendo a proporção original
            capturasJogador1.getChildren().add(imageView);
        }
    }
    
    public void atualizarCapturasJogador2(List<Peca> capturadas) {
        capturasJogador2.getChildren().clear();
        for (Peca peca : capturadas) {
            ImageView imageView = new ImageView(peca.getImage());
            imageView.setFitWidth(50);  // Definindo a largura da imagem
            imageView.setPreserveRatio(true); // Mantendo a proporção original
            capturasJogador2.getChildren().add(imageView);
        }
    }
}