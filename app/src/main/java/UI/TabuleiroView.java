package UI;

import partida.Movimento;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabuleiroView extends GridPane {
    public static final int TILE_SIZE = 80;
    private Tabuleiro tabuleiro;
    private Map<Posicao, ImageView> pecasNoTabuleiro; // Mapeia posições para ImageViews
    private Peca pecaSelecionada;
    private Posicao posicaoSelecionada;

    public TabuleiroView() {
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

    // Método para desenhar o tabuleiro com as peças
    private void desenharTabuleiro() {
        getChildren().clear();  // Limpa o tabuleiro existente
        pecasNoTabuleiro.clear();  // Limpa as referências das peças

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
                add(casa, j, i);

                // Se houver uma peça na casa, desenha a peça
                Peca peca = tabuleiro.obterPeca(new Posicao(i, j));
                if (peca != null) {
                    // Cria um ImageView para exibir a peça
                    ImageView imageView = new ImageView(peca.getImage());
                    imageView.setFitWidth(TILE_SIZE);  // Ajusta o tamanho da imagem para caber na casa
                    imageView.setFitHeight(TILE_SIZE);

                    // Adiciona a imagem da peça à casa correspondente
                    add(imageView, j, i);
                    pecasNoTabuleiro.put(new Posicao(i, j), imageView);  // Mapeia a posição para o ImageView

                    // A variável 'i' precisa ser final ou effective final, então criamos uma nova variável
                    final int finalI = i; // Definindo 'i' como final
                    final int finalJ = j;

                    // Adiciona o evento de clique para selecionar a peça
                    imageView.setOnMouseClicked(event -> selecionarPeca(new Posicao(finalI, finalJ)));
                    System.out.println("Desenhando peça na posição (" + i + ", " + j + ")");
                }
            }
        }
    }

    // Método para selecionar uma peça
    public void selecionarPeca(Posicao posicao) {
        Peca peca = tabuleiro.obterPeca(posicao);
        if (peca != null) {
            this.pecaSelecionada = peca;
            this.posicaoSelecionada = posicao;
            System.out.println("Peça selecionada na posição: " + posicao.getLinha() + ", " + posicao.getColuna());
            
            // Obter e destacar os movimentos possíveis
            List<Posicao> movimentosPossiveis = peca.proxMovimento(posicao);
            highlightPossibleMoves(movimentosPossiveis);  // Destacar as casas possíveis
        }
    }

    // Método para destacar os movimentos possíveis
    public void highlightPossibleMoves(List<Posicao> possiveisMovimentos) {
        // Limpa qualquer destaque anterior
        getChildren().forEach(child -> {
            if (child instanceof Rectangle) {
                Rectangle rect = (Rectangle) child;
                // Reset the tile color to original
                if ((getRowIndex(rect) + getColumnIndex(rect)) % 2 == 0) {
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
                    if (getRowIndex(casa) == row && getColumnIndex(casa) == col) {
                        // Alterar a cor para destacar a casa
                        casa.setFill(Color.LIGHTGREEN);  // Usando verde claro para destacar
                    }
                }
            }
        }
    }    

    public void animarMovimento(Posicao origem, Posicao destino) {
        ImageView pecaImagem = pecasNoTabuleiro.get(origem);
        if (pecaImagem != null) {
            // Calcula as coordenadas para a animação
            double origemX = getColumnIndex(pecaImagem);
            double origemY = getRowIndex(pecaImagem);
            double destinoX = destino.getColuna();
            double destinoY = destino.getLinha();
    
            // Cria a animação de transição
            TranslateTransition transicao = new TranslateTransition();
            transicao.setNode(pecaImagem);
            transicao.setFromX(origemX * TILE_SIZE);
            transicao.setFromY(origemY * TILE_SIZE);
            transicao.setToX(destinoX * TILE_SIZE);
            transicao.setToY(destinoY * TILE_SIZE);
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
    
    // Método para lidar com o clique em uma casa de destino
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
            }
    
            // Limpa a seleção após o movimento
            pecaSelecionada = null;
            posicaoSelecionada = null;
        }
    }

    public void updateEstadoJogo(String estadoJogo) {
        // Supondo que você tenha um Label ou algum componente de texto para mostrar o estado
        Label estadoLabel = new Label(estadoJogo);
        estadoLabel.setText(estadoJogo);
        // Você pode adicionar esse label a um layout, atualizar a UI, etc.
        System.out.println("Estado do jogo atualizado para: " + estadoJogo);
    }    
}