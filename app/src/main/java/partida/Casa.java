package partida;

import pecas.Peca;

public class Casa {
    private Cor cor;       // Color of the tile (white, black, etc.)
    private Peca peca;     // The piece (if any) occupying the tile
    private Posicao posicao; // The position of the tile on the board

    // Constructor to initialize the Casa with its color and position
    public Casa(Cor cor, Posicao posicao) {
        this.cor = cor;
        this.posicao = posicao;
        this.peca = null; // Initially, no piece on the square
    }

    // Getters and Setters
    public Cor getCor() {
        return cor;
    }

    public Peca getPeca() {
        return peca;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    @Override
    public String toString() {
        return "Casa{" +
               "cor=" + cor +
               ", peca=" + peca +
               ", posicao=" + posicao +
               '}';
    }
}
