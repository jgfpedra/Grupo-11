package pecas;
import partida.*;

public abstract class Pecas {
    protected Posicao posicao;
    private Tabuleiro tabuleiro;

    public Pecas(Posicao posicao, Tabuleiro tabuleiro) {
        this.posicao = posicao;
        this.tabuleiro = tabuleiro;
    }

    protected Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public abstract boolean[][] movPossiveis();

    public boolean movPossivel(Posicao posicao){
        return movPossiveis()[posicao.getLinha()][posicao.getColuna()];
    }

    public boolean ePossivelMover(){
        boolean[][] move = movPossiveis();
        for (int i = 0; i < move.length; i++) {
			for (int j = 0; j < move.length; j++) {
				if (move[i][j]) {
					return true;
				}
			}
		}
		return false;
    }
}