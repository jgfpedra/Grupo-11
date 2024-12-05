package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;


/**
 * Classe que representa a peça Rei no jogo de xadrez.
 * O Rei pode mover-se uma casa em qualquer direção, horizontal, vertical ou diagonal,
 * mas nunca pode se mover para uma casa onde estaria em cheque.
 */
@XmlRootElement
public class Rei extends Peca {

    /**
     * Construtor padrão da peça Rei, utilizado para criar uma instância sem cor definida.
     */
    public Rei(){    
    }

    /**
     * Construtor da peça Rei com cor definida.
     * O valor do Rei é 0, pois ele não é capturável diretamente, mas é a peça mais importante do jogo.
     * 
     * @param cor A cor da peça (BRANCO ou PRETO).
     */
    public Rei(Cor cor){
        super(cor, 0);
    }

    /**
     * Calcula os movimentos possíveis para o Rei a partir de uma posição específica no tabuleiro.
     * O Rei pode mover-se uma casa em qualquer direção: horizontal, vertical ou diagonal.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem do Rei.
     * @return Uma lista de posições válidas para o Rei se mover.
     */
    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
            {1, 1},
            {-1, 1},
            {1, -1},
            {-1, -1}
        };
        for (int[] dir : direcoes) {
            int linhaAtual = origem.getLinha() + dir[0];
            int colunaAtual = origem.getColuna() + dir[1];
            if (linhaAtual >= 0 && linhaAtual < 8 && colunaAtual >= 0 && colunaAtual < 8) {
                Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                Peca pecaNaCasa = tabuleiro.obterPeca(novaPosicao);
                if (pecaNaCasa == null || pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(novaPosicao);
                }
            }
        }

        return movimentosValidos;
    }
}