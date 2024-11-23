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
        
        // Diagonal movements (top-left, top-right, bottom-left, bottom-right)
        for (int i = 1; i < 8; i++) {
            // Top-left diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() - i >= 0) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna() - i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() - i));
                }
            }
            // Top-right diagonal
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i < 8) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna() + i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() + i));
                }
            }
            // Bottom-left diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() - i >= 0) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() - i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() - i));
                }
            }
            // Bottom-right diagonal
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() + i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() + i));
                }
            }
        }

        // Horizontal and vertical movements (north, south, east, west)
        for (int i = 1; i < 8; i++) {
            // North (vertical)
            if (origem.getLinha() - i >= 0) {  // Verifica se não sai do tabuleiro para cima
                if (partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna()) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna()));
                }
            }
            // South (vertical)
            if (origem.getLinha() + i < 8) {  // Verifica se não sai do tabuleiro para baixo
                if (partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna()) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna()));
                }
            }
            // East (horizontal)
            if (origem.getColuna() + i < 8) {  // Verifica se não sai do tabuleiro para a direita
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(origem.getColuna() + i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha(), origem.getColuna() + i));
                }
            }
            // West (horizontal)
            if (origem.getColuna() - i >= 0) {  // Verifica se não sai do tabuleiro para a esquerda
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(origem.getColuna() - i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha(), origem.getColuna() - i));
                }
            }
        }

        return movimentosValidos;
    }
}