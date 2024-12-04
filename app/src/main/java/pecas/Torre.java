package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

@XmlRootElement
public class Torre extends Peca {

    public Torre() {
    }

    public Torre(Cor cor) {
        super(cor, 5);
    }

    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            int novaLinha = origem.getLinha() - i;
            if (novaLinha >= 0) {
                if (tabuleiro.getCasas().get(novaLinha).get(origem.getColuna()).getPeca() == null) {
                    movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                } else {
                    if (tabuleiro.getCasas().get(novaLinha).get(origem.getColuna()).getPeca().getCor() != this.getCor()) {
                        movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                    }
                    break;
                }
            } else {
                break;
            }
        }
        movimentosValidos.addAll(movimentosDeRoque(tabuleiro, origem));
        return movimentosValidos;
    }
    private List<Posicao> movimentosDeRoque(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosRoque = new ArrayList<>();
        if (this.getCor() == Cor.BRANCO) {
            if (origem.getLinha() == 0 && origem.getColuna() == 0) {
                Peca rei = tabuleiro.getCasas().get(0).get(4).getPeca();
                if (rei instanceof Rei && (rei.getMovCount() == 0)) {
                    if (tabuleiro.getCasas().get(0).get(1).getPeca() == null && tabuleiro.getCasas().get(0).get(2).getPeca() == null && tabuleiro.getCasas().get(0).get(3).getPeca() == null) {
                        movimentosRoque.add(new Posicao(0, 2));
                    }
                }
            }
            if (origem.getLinha() == 0 && origem.getColuna() == 7) {
                Peca rei = tabuleiro.getCasas().get(0).get(4).getPeca();
                if (rei instanceof Rei && (rei.getMovCount() == 0)) {
                    if (tabuleiro.getCasas().get(0).get(5).getPeca() == null && tabuleiro.getCasas().get(0).get(6).getPeca() == null) {
                        movimentosRoque.add(new Posicao(0, 6));
                    }
                }
            }
        }
        return movimentosRoque;
    }
}