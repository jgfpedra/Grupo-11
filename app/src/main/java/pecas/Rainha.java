package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.*;

public class Rainha extends Peca {
    public Rainha(Cor cor){
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem){
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Diagonal movements (top-left, top-right, bottom-left, bottom-right)
        for (int i = 1; i < 8; i++) {
            // Top-left diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() - i));
            }
            // Top-right diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i < 8) {
                movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() + i));
            }
            // Bottom-left diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() - i));
            }
            // Bottom-right diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() + i));
            }
        }

        // Horizontal and vertical movements (norte, sul, leste e oeste)
        for (int i = 1; i < 8; i++) {
            // Norte
            if (origem.getLinha() + i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna()));
            }
            // Sul
            if (origem.getLinha() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna()));
            }
            // Leste
            if (origem.getColuna() - i >= 0) {
                movimentosValidos.add(new Posicao(origem.getLinha(), origem.getColuna() - i));
            }
            // Oeste
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                movimentosValidos.add(new Posicao(origem.getLinha(), origem.getColuna() + i));
            }
        }

        return movimentosValidos;
    }
}

