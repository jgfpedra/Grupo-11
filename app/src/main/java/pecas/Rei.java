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

        // Define the 8 possible directions the king can move
        int[][] direcoes = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1},  // right
            {-1, -1}, // top-left
            {-1, 1},  // top-right
            {1, -1},  // bottom-left
            {1, 1}    // bottom-right
        };

        // Loop through each direction
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];

            // Check if the new position is within the board bounds (assuming 8x8 board)
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                Posicao novaPosicao = new Posicao(novaLinha, novaColuna);

                // Here we would check if the square is occupied by a piece of the same color
                // Assuming we have access to the current game state and can check for a piece in the target position
                // if (this.isOccupiedBySameColor(novaPosicao)) {
                //    continue;  // Skip this move if the position is occupied by a piece of the same color
                // }

                movimentosValidos.add(novaPosicao);
            }
        }

        return movimentosValidos;
    }
}