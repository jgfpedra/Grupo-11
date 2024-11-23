package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Rei extends Peca {

    public Rei(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();

        // Direções possíveis para o Rei: as 8 direções ao redor do Rei
        int[][] direcoes = {
            {-1, 0},  // Para cima
            {1, 0},   // Para baixo
            {0, -1},  // Para a esquerda
            {0, 1},   // Para a direita
            {-1, -1}, // Diagonal superior esquerda
            {-1, 1},  // Diagonal superior direita
            {1, -1},  // Diagonal inferior esquerda
            {1, 1}    // Diagonal inferior direita
        };

        // Verifica cada direção ao redor do Rei
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];

            // Verifica se a nova posição está dentro dos limites do tabuleiro (8x8)
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                Posicao novaPosicao = new Posicao(novaLinha, novaColuna);

                // Verifica se a casa de destino está ocupada por uma peça da mesma cor
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(novaLinha).get(novaColuna).getPeca();
                if (pecaNaCasa == null || pecaNaCasa.getCor() != this.getCor()) {
                    // Se a casa estiver vazia ou ocupada por uma peça adversária, o movimento é válido
                    movimentosValidos.add(novaPosicao);
                }
            }
        }

        return movimentosValidos;
    }
}