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
    private void desenharTabuleiro() {
        getChildren().clear();  // Limpa o tabuleiro existente
        pecasNoTabuleiro.clear();  // Limpa as referências das peças

        // Adicionando a régua
        adicionarRégua();

        // Loop sobre as casas e desenha as peças
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle casa = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((i + j) % 2 == 0) {
                    casa.setFill(Color.LIGHTGRAY);
                } else {
                    casa.setFill(Color.DARKGRAY);
                }

                // Adiciona a casa à grid
                add(casa, j + 1, i + 1); // +1 para respeitar a posição da régua

                // Se houver uma peça na casa, desenha a peça
                Peca peca = tabuleiro.obterPeca(new Posicao(i, j));
                if (peca != null) {
                    // Cria um ImageView para exibir a peça
                    ImageView imageView = new ImageView(peca.getImage());
                    imageView.setFitWidth(TILE_SIZE);  // Ajusta o tamanho da imagem para caber na casa
                    imageView.setFitHeight(TILE_SIZE);

                    // Adiciona a imagem da peça à casa correspondente
                    add(imageView, j + 1, i + 1); // +1 para respeitar a posição da régua
                    pecasNoTabuleiro.put(new Posicao(i, j), imageView);  // Mapeia a posição para o ImageView

                    // Adiciona o evento de clique para selecionar a peça
                    final int finalI = i;
                    final int finalJ = j;
                    imageView.setOnMouseClicked(event -> selecionarPeca(new Posicao(finalI, finalJ)));
                }
            }
        }
    }

    // Método para adicionar a régua do tabuleiro (colunas e linhas)
    private void adicionarRégua() {
        // Adicionando a régua superior (colunas)
        for (int i = 0; i < 8; i++) {
            Text colLabel = new Text(String.valueOf((char) ('A' + i))); // Letras A-H
            add(colLabel, i + 1, 0); // Adiciona no topo (linha 0), começando da coluna 1
        }

        // Adicionando a régua lateral (linhas)
        for (int i = 0; i < 8; i++) {
            Text rowLabel = new Text(String.valueOf(8 - i)); // Números 1-8
            add(rowLabel, 0, i + 1); // Adiciona na lateral esquerda (coluna 0), começando da linha 1
        }
    }

    // Método para selecionar a peça
    public void selecionarPeca(Posicao posicao) {
        // Limpa a seleção anterior se houver
        if (pecaSelecionada != null) {
            clearHighlights();  // Limpa os destaques antes de selecionar nova peça
        }

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
                highlightPossibleMoves(movimentosValidos);
            } else {
                // Se não houver movimentos válidos, limpa seleção
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

    // Método para destacar os movimentos possíveis
    public void highlightPossibleMoves(List<Posicao> possiveisMovimentos) {
        // Limpa qualquer destaque anterior
        getChildren().forEach(child -> {
            if (child instanceof Rectangle) {
                Rectangle rect = (Rectangle) child;
                // Reset the tile color to original
                if ((getRowIndexOfTile(rect) + getColumnIndexOfTile(rect)) % 2 == 0) {
                    rect.setFill(Color.LIGHTGRAY);
                } else {
                    rect.setFill(Color.DARKGRAY);
                }
            }
        });
        
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
            });
        }
    }

    // Método para atualizar o estado do jogo
    public void updateEstadoJogo(String estadoJogo) {
        Label estadoLabel = new Label("Estado do jogo: " + estadoJogo);
        estadoLabel.setText(estadoJogo);
        // Adicionar este label ao seu layout para mostrar o estado do jogo.
        System.out.println("Estado do jogo atualizado para: " + estadoJogo);
    }

    public void clearHighlights() {
        // Remove qualquer destaque anterior nas casas
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle casa = (Rectangle) node;
                // Reseta a cor da casa para o estado inicial
                if ((getRowIndexOfTile(casa) + getColumnIndexOfTile(casa)) % 2 == 0) {
                    casa.setFill(Color.LIGHTGRAY);  // Cor original
                } else {
                    casa.setFill(Color.DARKGRAY);  // Cor original
                }
            }
        }
    }
}