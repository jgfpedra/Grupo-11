package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Bispo extends Peca{

    public Bispo(Cor cor){
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
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i >= 0) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna() + i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() - i, origem.getColuna() + i));
                }
            }
            // Bottom-left diagonal
            if (origem.getLinha() + i >= 0 && origem.getColuna() - i >= 0) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() - i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() - i));
                }
            }
            // Bottom-right diagonal
            if (origem.getLinha() + i >= 0 && origem.getColuna() + i >= 0) {
                if (partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() + i) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha() + i, origem.getColuna() + i));
                }
            }
        }

        return movimentosValidos;  // Retorna uma lista, mesmo que vazia
    }  
}
