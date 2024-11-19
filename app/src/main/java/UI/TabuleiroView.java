package UI;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;
import pecas.Peca;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TabuleiroView extends GridPane {
    public static final int TILE_SIZE = 80;
    private Tabuleiro tabuleiro;

    public TabuleiroView() {
        tabuleiro = new Tabuleiro();
        desenharTabuleiro();
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    // Atualiza o tabuleiro na interface gráfica
    public void updateTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        desenharTabuleiro();  // Re-desenha o tabuleiro na UI
    }

    // Método para desenhar o tabuleiro com as peças
    private void desenharTabuleiro() {
        getChildren().clear();  // Limpa o tabuleiro existente

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
                    // Aqui você deve chamar o método que desenha a peça na casa
                    // (por exemplo, desenhando uma imagem ou alterando o estilo do retângulo)
                    casa.setFill(peca.getCor() == Cor.BRANCO ? Color.WHITE : Color.BLACK);  // Exemplo de cor
                }
            }
        }
    }

    // Método para atualizar o estado do jogo (como "Check", "Checkmate")
    public void updateEstadoJogo(String estado) {
        // Exibe o estado do jogo, como "Check" ou "Checkmate", em algum rótulo da UI
        // Aqui você poderia ter um Label ou algum outro componente para exibir o estado do jogo
    }
}