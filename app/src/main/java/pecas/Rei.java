package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import exception.RoqueInvalidoException;
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
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, 
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
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
        if (this.getMovCount() == 0) {
            if (verificarRoque(tabuleiro, origem)) {
                if (origem.getColuna() == 4) {
                    Posicao torreDireita = new Posicao(origem.getLinha(), origem.getColuna() + 3);
                    Peca torre = tabuleiro.obterPeca(torreDireita);
                    if (torre instanceof Torre) {
                        movimentosValidos.add(torreDireita);
                    }
                    Posicao torreEsquerda = new Posicao(origem.getLinha(), origem.getColuna() - 4);
                    torre = tabuleiro.obterPeca(torreEsquerda);
                    if (torre instanceof Torre) {
                        movimentosValidos.add(torreEsquerda);
                    }
                }
            }
        }   
        return movimentosValidos;
    }    
    
    /**
     * Verifica se é possível realizar um movimento de roque para a posição de destino do Rei.
     * O movimento de roque só é válido se o Rei e a Torre ainda não tiverem sido movidos
     * e se não houver peças entre eles no tabuleiro.
     * 
     * @param tabuleiro O tabuleiro onde o movimento será validado.
     * @param origem A posição atual do Rei no tabuleiro.
     * @return {@code true} se o movimento de roque for válido, {@code false} caso contrário.
     * @throws RoqueInvalidoException Se o movimento de roque não for válido, por exemplo, se a Torre já tiver se movido
     * ou se houver peças no caminho.
     */
    private boolean verificarRoque(Tabuleiro tabuleiro, Posicao origem) {
        if (origem.getColuna() == 4) {
            if (verificarRoqueDireita(tabuleiro, origem)) {
                System.out.println("c");
                return true;
            }
            if (verificarRoqueEsquerda(tabuleiro, origem)) {
                System.out.println("d");
                return true;
            }
        }
        return false;
    }

    private boolean verificarRoqueDireita(Tabuleiro tabuleiro, Posicao origem) {
        // Verifique se o roque é possível sem chamar 'posicaoSobAtaque' dentro do método de roque
        Posicao torrePos = new Posicao(origem.getLinha(), origem.getColuna() + 3);
        Peca torre = tabuleiro.obterPeca(torrePos);
        
        // Verifique se a torre está na posição e se ainda não foi movida
        if (torre instanceof Torre && torre.getMovCount() == 0) {
            // Verifique se não há peças entre o rei e a torre
            for (int i = origem.getColuna() + 1; i < torrePos.getColuna(); i++) {
                Posicao posicaoIntermediaria = new Posicao(origem.getLinha(), i);
                if (tabuleiro.obterPeca(posicaoIntermediaria) != null) {
                    return false;  // Se houver peça entre, o roque não é válido
                }
            }
            // Se a torre e o rei não estão sob ataque
            System.out.println("a");
            if (!tabuleiro.posicaoSobAtaque(origem, this.getCor()) && 
                !tabuleiro.posicaoSobAtaque(torrePos, this.getCor())) {
                System.out.println("d");
                return true;  // Roque válido
            }
        }
        return false; // Roque inválido
    }    
    
    private boolean verificarRoqueEsquerda(Tabuleiro tabuleiro, Posicao origem) {
        // Verifique se o roque à esquerda é possível sem chamar 'posicaoSobAtaque' diretamente dentro do método de roque
        Posicao torrePos = new Posicao(origem.getLinha(), origem.getColuna() - 4); // Torre está à esquerda
        Peca torre = tabuleiro.obterPeca(torrePos);
    
        // Verifique se a torre está na posição correta e se não foi movida
        if (torre instanceof Torre && torre.getMovCount() == 0) {
            // Verifique se não há peças entre o rei e a torre
            for (int i = origem.getColuna() - 1; i > torrePos.getColuna(); i--) {
                Posicao posicaoIntermediaria = new Posicao(origem.getLinha(), i);
                if (tabuleiro.obterPeca(posicaoIntermediaria) != null) {
                    return false;  // Se houver peça entre, o roque não é válido
                }
            }
            // Se a torre e o rei não estão sob ataque
            System.out.println("b");
            if (!tabuleiro.posicaoSobAtaque(origem, this.getCor()) && 
                !tabuleiro.posicaoSobAtaque(torrePos, this.getCor())) {
                System.out.println("c");
                return true;  // Roque válido
            }
        }
        return false; // Roque inválido
    }    
}