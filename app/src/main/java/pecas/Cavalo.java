package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

/**
 * Representa a peça Cavalo no jogo de xadrez.
 * O Cavalo se move em forma de "L", ou seja, duas casas em uma direção e uma casa perpendicular.
 * O Cavalo pode pular sobre outras peças no tabuleiro.
 */
@XmlRootElement
public class Cavalo extends Peca {

    /**
     * Construtor padrão do Cavalo. 
     * Utilizado para a criação de instâncias sem cor definida.
     */
    public Cavalo(){
    }

    /**
     * Construtor do Cavalo com uma cor específica.
     * @param cor A cor do Cavalo (BRANCO ou PRETO).
     */
    public Cavalo(Cor cor){
        super(cor, 3);
    }

    /**
     * Calcula todos os possíveis movimentos do Cavalo a partir de uma posição no tabuleiro.
     * O Cavalo se move em um padrão de "L", podendo avançar duas casas em uma direção e uma casa perpendicular a ela.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça no tabuleiro.
     * @return Uma lista de posições válidas para o movimento do Cavalo.
     */
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