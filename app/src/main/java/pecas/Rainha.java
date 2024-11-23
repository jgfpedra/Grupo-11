package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.*;

public class Rainha extends Peca {
    public Rainha(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Direções possíveis para a Rainha: norte, sul, leste, oeste, e diagonais
        int[][] direcoes = {
            {1, 0},  // Sul
            {-1, 0}, // Norte
            {0, 1},  // Leste
            {0, -1}, // Oeste
            {1, 1},  // Diagonal inferior direita
            {-1, 1}, // Diagonal superior direita
            {1, -1}, // Diagonal inferior esquerda
            {-1, -1} // Diagonal superior esquerda
        };

        // Para cada direção, move-se ao longo dessa linha até um obstáculo ou até fora do tabuleiro
        for (int[] dir : direcoes) {
            int linhaAtual = origem.getLinha();
            int colunaAtual = origem.getColuna();

            // Continuar na direção até encontrar um obstáculo
            while (true) {
                linhaAtual += dir[0];
                colunaAtual += dir[1];

                // Verificar se a nova posição está fora dos limites do tabuleiro
                if (linhaAtual < 0 || linhaAtual >= 8 || colunaAtual < 0 || colunaAtual >= 8) {
                    break;  // Se sair do tabuleiro, interrompe a movimentação nessa direção
                }

                Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(linhaAtual).get(colunaAtual).getPeca();

                // Se a casa estiver vazia ou ocupada por uma peça adversária, o movimento é válido
                if (pecaNaCasa == null || pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(novaPosicao);
                }

                // Se a casa estiver ocupada por uma peça da mesma cor, a Rainha não pode passar por ela
                if (pecaNaCasa != null && pecaNaCasa.getCor() == this.getCor()) {
                    break;  // Interrompe a movimentação nessa direção
                }
            }
        }
        return movimentosValidos;
    }
}