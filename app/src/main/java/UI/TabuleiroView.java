package UI;

import partida.Movimento;
import partida.Partida;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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

    // Construtor
    public TabuleiroView(Partida partida) {
        this.partida = partida;
        tabuleiro = new Tabuleiro();
        pecasNoTabuleiro = new HashMap<>();
        desenharTabuleiro();
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
                add(casa, coluna, linha); // Adiciona as casas ao grid sem deslocamento extra
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
    }

    // Método para adicionar a régua do tabuleiro (colunas A-H e linhas 1-8)
    private void adicionarRegua() {
        // Adicionando a régua superior (colunas A-H)
        for (int i = 0; i < 8; i++) {
            Text colLabel = new Text(String.valueOf((char) ('A' + i))); // Letras A-H
            add(colLabel, i, 8); // Coloca na linha 0 (acima do tabuleiro)
        }
    
        // Adicionando a régua lateral (linhas 1-8)
        for (int i = 0; i < 8; i++) {
            Text rowLabel = new Text(String.valueOf(8 - i)); // Números 1-8
            add(rowLabel, 8, i); // Coloca na coluna 0 (à esquerda do tabuleiro)
        }
    }

    // Método para adicionar as peças no tabuleiro
    public void adicionarPecaNoTabuleiro(Posicao posicao, Peca peca) {
        ImageView imgView = new ImageView(peca.getImage());
        imgView.setFitWidth(TILE_SIZE);
        imgView.setFitHeight(TILE_SIZE);
        add(imgView, posicao.getColuna(), posicao.getLinha()); // Posições sem deslocamento extra
        pecasNoTabuleiro.put(posicao, imgView);
    }

    public void selecionarPeca(Posicao posicao) {
        // Limpa qualquer destaque de peça anterior
        clearSelection();
    
        Peca peca = tabuleiro.obterPeca(posicao);
        if (peca != null && peca.getCor() == partida.getJogadorAtual().getCor()) {
            this.pecaSelecionada = peca;
            this.posicaoSelecionada = posicao;
    
            // Criar movimento
            Movimento movimento = new Movimento(posicaoSelecionada, null, pecaSelecionada);
    
            // Obter movimentos válidos
            List<Posicao> movimentosValidos = movimento.validarMovimentosPossiveis(tabuleiro);
    
            // Se houver movimentos válidos, destaca as casas válidas
            if (!movimentosValidos.isEmpty()) {
                highlightPossibleMoves(movimentosValidos);  // Atualiza o highlight de movimentos válidos
            } else {
                // Se não houver movimentos válidos, reseta a seleção
                System.out.println("Sem movimentos válidos para esta peça!");
                clearSelection();  // Reseta a seleção de peça
            }
        } else {
            // Caso não tenha peça ou a peça não seja sua, resetar seleção
            System.out.println("Seleção inválida ou peça do oponente!");
            clearSelection();  // Reseta a seleção
        }
    }
    
    // Método para limpar a seleção de peça
    public void clearSelection() {
        pecaSelecionada = null;
        posicaoSelecionada = null;
        clearHighlights();  // Limpa os destaques de possíveis movimentos
    }

    // Método para mover a peça
    public void moverPeca(Posicao destino) {
        if (pecaSelecionada != null) {
            // Obter os movimentos possíveis para a peça selecionada
            List<Posicao> movimentosPossiveis = pecaSelecionada.proxMovimento(posicaoSelecionada);
    
            if (movimentosPossiveis.contains(destino)) {
                // Anima o movimento da peça
                animarMovimento(posicaoSelecionada, destino);
    
                // Cria um movimento e aplica no tabuleiro
                Movimento movimentoPeca = new Movimento(posicaoSelecionada, destino, pecaSelecionada);
                tabuleiro.aplicarMovimento(movimentoPeca);  // Aplica o movimento no tabuleiro
    
                // Atualiza a interface gráfica
                updateTabuleiro(tabuleiro);
    
                System.out.println("Movimento realizado para: " + destino.getLinha() + ", " + destino.getColuna());
            } else {
                System.out.println("Movimento inválido!");
                clearSelection();  // Limpa a seleção em caso de movimento inválido
            }
        }
    }    
    
    public void highlightPossibleMoves(List<Posicao> possiveisMovimentos) {
        // Limpa qualquer destaque anterior
        clearHighlights();
    
        // Destaca as casas possíveis
        for (Posicao posicao : possiveisMovimentos) {
            int row = posicao.getLinha();
            int col = posicao.getColuna();
    
            // Encontre o Rectangle que corresponde à posição
            for (javafx.scene.Node node : getChildren()) {
                if (node instanceof Rectangle) {
                    Rectangle casa = (Rectangle) node;
                    if (getRowIndexOfTile(casa) == row && getColumnIndexOfTile(casa) == col) {
                        // Alterar a cor para destacar a casa
                        casa.setFill(Color.LIGHTGREEN);  // Usando verde claro para destacar
                    }
                }
            }
        }
    }
    

    // Métodos para calcular os índices de linha e coluna manualmente
    private int getRowIndexOfTile(Rectangle rect) {
        return (int) rect.getLayoutY() / TILE_SIZE;
    }

    private int getColumnIndexOfTile(Rectangle rect) {
        return (int) rect.getLayoutX() / TILE_SIZE;
    }

    // Método para animar o movimento de uma peça
    public void animarMovimento(Posicao origem, Posicao destino) {
        ImageView pecaImagem = pecasNoTabuleiro.get(origem);
        if (pecaImagem != null) {
            // Calcula as coordenadas para a animação
            double origemX = origem.getColuna() * TILE_SIZE;
            double origemY = origem.getLinha() * TILE_SIZE;
            double destinoX = destino.getColuna() * TILE_SIZE;
            double destinoY = destino.getLinha() * TILE_SIZE;
    
            // Cria a animação de transição
            TranslateTransition transicao = new TranslateTransition();
            transicao.setNode(pecaImagem);
            transicao.setFromX(origemX);
            transicao.setFromY(origemY);
            transicao.setToX(destinoX);
            transicao.setToY(destinoY);
            transicao.setCycleCount(1);
            transicao.setDuration(Duration.seconds(0.5));
    
            // Inicia a animação
            transicao.play();
    
            // Atualiza a posição do ImageView depois da animação
            transicao.setOnFinished(event -> {
                // Atualiza a posição da peça no mapa
                pecasNoTabuleiro.put(destino, pecaImagem);
                pecasNoTabuleiro.remove(origem);  // Remove a peça da posição original
                System.out.println("Movimento finalizado!");
    
                // Limpa os destaques após o movimento
                clearHighlights();
            });
        }
    }    

    // Método para limpar os destaques de casas válidas
    public void clearHighlights() {
        // Remove qualquer destaque anterior nas casas
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle casa = (Rectangle) node;
                // Reseta a cor da casa para o estado inicial
                int row = getRowIndexOfTile(casa);
                int col = getColumnIndexOfTile(casa);
    
                if ((row + col) % 2 == 0) {
                    casa.setFill(Color.LIGHTGRAY);  // Casa clara
                } else {
                    casa.setFill(Color.DARKGRAY);  // Casa escura
                }
            }
        }
    }    

    // Método que você solicitou que foi removido
    public void updateEstadoJogo(String estadoJogo) {
        // Atualizar o estado do jogo (pode ser utilizado para mostrar mensagens de "xeque", "fim de jogo", etc.)
        Label estadoLabel = new Label(estadoJogo);
        estadoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        add(estadoLabel, 8, 0, 1, 8); // Posiciona o estado na parte superior do tabuleiro
    }
}