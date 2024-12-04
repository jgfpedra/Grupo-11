package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

@XmlRootElement
public class Cavalo extends Peca {

    public Cavalo(){
    }

    public Cavalo(Cor cor){
        super(cor, 3);
    }

    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2},
        };
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
                Peca pecaNaCasa = tabuleiro.obterPeca(novaPosicao);
                if (pecaNaCasa == null || pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(novaPosicao);
                }
            }
        }
        return movimentosValidos;
    }
}