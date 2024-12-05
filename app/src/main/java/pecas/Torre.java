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
        return movimentosValidos;
    }
}