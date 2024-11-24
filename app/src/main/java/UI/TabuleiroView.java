package UI;

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
        carregarImagensJogadores();
        desenharTabuleiro();
        inicializarEstadoJogoLabel();
        
        // Inicializar as caixas de capturas
        capturasJogador1 = new HBox(10);  // Espaçamento de 10px entre as peças capturadas
        capturasJogador1.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogador1.setPrefHeight(100);
        
        capturasJogador2 = new HBox(10);
        capturasJogador2.setStyle("-fx-padding: 10px; -fx-border-color: black;");
        capturasJogador2.setPrefHeight(100);
        
        // Adicionar as caixas de capturas abaixo do tabuleiro
        add(capturasJogador1, 0, 9, 8, 1);  // Jogador 1
        add(capturasJogador2, 0, 10, 8, 1); // Jogador 2
    }

    private void carregarImagensJogadores() {
        // Carregar imagens dos jogadores (usando o caminho correto)
        player1Image = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")); // Caminho da imagem do jogador 1
        player2Image = new Image(getClass().getResourceAsStream("/images/jogadores/jogadorlocal.png")); // Caminho da imagem do jogador 2
    }

    private void inicializarEstadoJogoLabel() {
        // Inicializa o Label para mostrar o estado do jogo
        estadoJogoLabel = new Label("Vez do Jogador 1");
        estadoJogoLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        estadoJogoLabel.setTextFill(Color.BLACK);
        
        // Adiciona o estado do jogo abaixo da régua (linha 9 ou 10)
        add(estadoJogoLabel, 0, 10, 8, 1); // Linha 10 (o que será a 10ª linha visual) e ocupando todas as 8 colunas
    }
    

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        desenharTabuleiro();  // Re-desenha o tabuleiro na UI
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void updateTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        getChildren().clear();
        desenharTabuleiro();
    }

    // Método para desenhar o tabuleiro com as peças e a régua
    public void desenharTabuleiro() {
        getChildren().clear();
        
        // Desenhando o tabuleiro (8x8)
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((linha + coluna) % 2 == 0) {
                    casa.setFill(Color.LIGHTGRAY); // Cor de casas claras
                } else {
                    casa.setFill(Color.DARKGRAY); // Cor de casas escuras
                }
                add(casa, coluna, linha); // Adiciona as casas ao grid
            }
        }

        // Adiciona a régua (colunas A-H e linhas 1-8)
        adicionarRegua();
        
        // Adicionar peças
        for (Posicao posicao : tabuleiro.getPosicoesPecas()) {
            Peca peca = tabuleiro.obterPeca(posicao);
            if (peca != null) {
                adicionarPecaNoTabuleiro(posicao, peca);
            }
        }

        // Adiciona as imagens dos jogadores
        adicionarImagensJogadores();
    }

    private void adicionarImagensJogadores() {
        // Coloca as imagens dos jogadores no topo do tabuleiro
        ImageView player1ImageView = new ImageView(player1Image);
        player1ImageView.setFitWidth(50);
        player1ImageView.setFitHeight(50);
        add(player1ImageView, 8, 0); // Imagem do jogador 1 à esquerda

        ImageView player2ImageView = new ImageView(player2Image);
        player2ImageView.setFitWidth(50);
        player2ImageView.setFitHeight(50);
        add(player2ImageView, 8, 7); // Imagem do jogador 2 à direita
    }

    // Método para adicionar a régua do tabuleiro (colunas A-H e linhas 1-8)
    private void adicionarRegua() {
        // Adicionando a régua superior (colunas A-H)
        for (int i = 0; i < 8; i++) {
            Text colLabel = new Text(String.valueOf((char) ('A' + i))); // Letras A-H
            add(colLabel, i, 8); // Coloca na linha 8 (acima do tabuleiro)
        }
    
        // Adicionando a régua lateral (linhas 1-8)
        for (int i = 0; i < 8; i++) {
            Text rowLabel = new Text(String.valueOf(8 - i)); // Números 1-8
            add(rowLabel, 8, i); // Coloca na coluna 8 (à esquerda do tabuleiro)
        }
    }

    // Método para adicionar as peças no tabuleiro
    public void adicionarPecaNoTabuleiro(Posicao posicao, Peca peca) {
        ImageView imgView = new ImageView(peca.getImage());
        imgView.setFitWidth(TILE_SIZE);
        imgView.setFitHeight(TILE_SIZE);
        add(imgView, posicao.getColuna(), posicao.getLinha());
        pecasNoTabuleiro.put(posicao, imgView);
    }

    public void selecionarPeca(Posicao posicao) {
        clearSelection();
        Peca peca = tabuleiro.obterPeca(posicao);
        if (peca != null && peca.getCor() == partida.getJogadorAtual().getCor()) {
            this.pecaSelecionada = peca;
            this.posicaoSelecionada = posicao;

            Movimento movimento = new Movimento(posicaoSelecionada, null, pecaSelecionada);
            List<Posicao> movimentosValidos = movimento.validarMovimentosPossiveis(tabuleiro);

            if (!movimentosValidos.isEmpty()) {
                highlightPossibleMoves(movimentosValidos);
            } else {
                clearSelection();
            }
        } else {
            clearSelection();
        }
    }
    
    // Método para limpar a seleção de peça
    public void clearSelection() {
        pecaSelecionada = null;
        posicaoSelecionada = null;
        clearHighlights();
    }

    // Método para mover a peça
    public void moverPeca(Posicao destino) {
        if (pecaSelecionada != null) {
            List<Posicao> movimentosPossiveis = pecaSelecionada.proxMovimento(posicaoSelecionada);
            if (movimentosPossiveis.contains(destino)) {
                animarMovimento(posicaoSelecionada, destino);
                Movimento movimentoPeca = new Movimento(posicaoSelecionada, destino, pecaSelecionada);
                tabuleiro.aplicarMovimento(movimentoPeca);
                updateTabuleiro(tabuleiro);
            } else {
                clearSelection();
            }
        }
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

            TranslateTransition transicao = new TranslateTransition();
            transicao.setNode(pecaImagem);
            transicao.setFromX(origemX);
            transicao.setFromY(origemY);
            transicao.setToX(destinoX);
            transicao.setToY(destinoY);
            transicao.setCycleCount(1);
            transicao.setDuration(Duration.seconds(0.5));
            transicao.play();

            transicao.setOnFinished(event -> {
                pecasNoTabuleiro.put(destino, pecaImagem);
                pecasNoTabuleiro.remove(origem);
                clearHighlights();
            });
        }
    }

    public void updateEstadoJogo(String estadoJogo) {
        estadoJogoLabel.setText(estadoJogo);  // Atualiza o texto do Label com o novo estado do jogo
    }

    public void atualizarCapturas(List<Peca> capturadasJogador1, List<Peca> capturadasJogador2) {
        // Limpar as áreas de capturas
        capturasJogador1.getChildren().clear();
        capturasJogador2.getChildren().clear();
        
        // Adicionar as imagens das peças capturadas do Jogador 1
        for (Peca peca : capturadasJogador1) {
            ImageView pecaImageView = new ImageView(peca.getImage());
            pecaImageView.setFitWidth(50);
            pecaImageView.setFitHeight(50);
            capturasJogador1.getChildren().add(pecaImageView);
        }
        
        // Adicionar as imagens das peças capturadas do Jogador 2
        for (Peca peca : capturadasJogador2) {
            ImageView pecaImageView = new ImageView(peca.getImage());
            pecaImageView.setFitWidth(50);
            pecaImageView.setFitHeight(50);
            capturasJogador2.getChildren().add(pecaImageView);
        }
    }
}