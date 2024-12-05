package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

/**
 * Classe que representa a peça Torre no jogo de xadrez.
 * A Torre pode se mover horizontal ou verticalmente, quantas casas quiser,
 * mas não pode saltar sobre outras peças.
 */
@XmlRootElement
public class Torre extends Peca {

    /**
     * Construtor padrão da peça Torre, utilizado para criar uma instância sem cor definida.
     */
    public Torre() {
    }

    /**
     * Construtor da peça Torre com cor definida.
     * O valor da Torre é 5, pois é uma peça de alto valor no jogo.
     * 
     * @param cor A cor da peça (BRANCO ou PRETO).
     */
    public Torre(Cor cor) {
        super(cor, 5);
    }

    /**
     * Calcula os movimentos possíveis para a Torre a partir de uma posição específica no tabuleiro.
     * A Torre pode se mover horizontalmente ou verticalmente, mas não pode saltar outras peças.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da Torre.
     * @return Uma lista de posições válidas para a Torre se mover.
     */
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

    /**
     * Verifica os movimentos de roque possíveis para a Torre.
     * O roque é uma jogada especial que envolve mover a Torre e o Rei ao mesmo tempo, sob condições específicas.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição da Torre que pode realizar o roque.
     * @return Uma lista de posições válidas para o roque.
     */
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