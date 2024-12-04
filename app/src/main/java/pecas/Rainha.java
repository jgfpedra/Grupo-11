package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.*;

/**
 * Classe que representa a peça Rainha no jogo de xadrez.
 * A Rainha é uma peça muito poderosa, pois pode se mover qualquer número de casas 
 * em linhas, colunas ou diagonais, desde que não haja peças bloqueando o caminho.
 */
@XmlRootElement
public class Rainha extends Peca {

    /**
     * Construtor padrão da Rainha, utilizado para criar uma instância sem cor definida.
     */
    public Rainha(){
    }

    /**
     * Construtor da Rainha com cor definida.
     * A Rainha recebe um valor de 9, pois é a peça mais valiosa após o Rei.
     * 
     * @param cor A cor da peça (BRANCO ou PRETO).
     */
    public Rainha(Cor cor){
        super(cor, 9);
    }

    /**
     * Calcula os movimentos possíveis para a Rainha a partir de uma posição específica no tabuleiro.
     * A Rainha pode mover-se em qualquer direção: horizontal, vertical e diagonal.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da Rainha.
     * @return Uma lista de posições válidas para a Rainha se mover.
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
            int linhaAtual = origem.getLinha();
            int colunaAtual = origem.getColuna();
            while (true) {
                linhaAtual += dir[0];
                colunaAtual += dir[1];
                if (linhaAtual < 0 || linhaAtual >= 8 || colunaAtual < 0 || colunaAtual >= 8) {
                    break;
                }
                Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                Peca pecaNaCasa = tabuleiro.obterPeca(novaPosicao);
                if (pecaNaCasa == null) {
                    movimentosValidos.add(novaPosicao);
                } else {
                    if (pecaNaCasa.getCor() != this.getCor()) {
                        movimentosValidos.add(novaPosicao);
                    }
                    break;
                }
            }
        }
        return movimentosValidos;
    }    
}