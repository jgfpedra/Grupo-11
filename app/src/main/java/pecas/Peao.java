package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.*;

/**
 * Representa a peça Peão no jogo de xadrez.
 * O Peão move-se uma casa para frente, mas captura peças adversárias na diagonal.
 * Na sua primeira jogada, o Peão pode mover-se duas casas para frente.
 */
@XmlRootElement
public class Peao extends Peca {

    /**
     * Construtor padrão do Peão.
     * Utilizado para a criação de instâncias sem cor definida.
     */
    public Peao(){
    }
    
    /**
     * Construtor do Peão com uma cor específica.
     * @param cor A cor do Peão (BRANCO ou PRETO).
     */
    public Peao(Cor cor){
        super(cor, 1);
    }

    /**
     * Calcula todos os possíveis movimentos do Peão a partir de uma posição no tabuleiro.
     * O Peão pode mover-se uma casa para frente ou duas casas no seu primeiro movimento.
     * O Peão também pode capturar peças adversárias na diagonal.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça no tabuleiro.
     * @return Uma lista de posições válidas para o movimento do Peão.
     */
    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int direcao = (getCor() == Cor.BRANCO) ? -1 : 1;
        int linhaAtual = origem.getLinha();
        int colunaAtual = origem.getColuna();
        int novaLinha = linhaAtual + direcao;
        if (novaLinha >= 0 && novaLinha < 8) {
            if (tabuleiro.getCasas().get(novaLinha).get(colunaAtual).getPeca() == null) {
                movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
            }
        }
        if (getMovCount() == 0) {
            novaLinha = linhaAtual + 2 * direcao;
            int linhaIntermediaria = linhaAtual + direcao;
            if (novaLinha >= 0 && novaLinha < 8) {
                if (tabuleiro.getCasas().get(linhaIntermediaria).get(colunaAtual).getPeca() == null
                    && tabuleiro.getCasas().get(novaLinha).get(colunaAtual).getPeca() == null) {
                    movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
                }
            }
        }
        int[] direcoesColuna = {-1, 1};
        for (int i = 0; i < direcoesColuna.length; i++) {
            int novaColuna = colunaAtual + direcoesColuna[i];
            if (novaColuna >= 0 && novaColuna < 8) {
                novaLinha = linhaAtual + direcao;
                if (novaLinha >= 0 && novaLinha < 8) {
                    Peca pecaNaDiagonal = tabuleiro.getCasas().get(novaLinha).get(novaColuna).getPeca();
                    if (pecaNaDiagonal != null && pecaNaDiagonal.getCor() != this.getCor()) {
                        movimentosValidos.add(new Posicao(novaLinha, novaColuna));
                    }
                }
            }
        }
        return movimentosValidos;
    }    
}