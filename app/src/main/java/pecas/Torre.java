package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Torre extends Peca{
    public Torre(Cor cor){
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem){
        List<Posicao> movimentosValidos = new ArrayList<>();
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
        // Assuming Movimento can accept a list of valid positions
        return movimentosValidos;
    }
}
