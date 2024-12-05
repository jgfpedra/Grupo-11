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
        if (origem.getColuna() == 4) {  // O Rei está na posição inicial do roque (coluna 4)
            // Verifica roque à direita
            if (verificarRoqueDireita(tabuleiro, origem)) {
                return true;
            }
            // Verifica roque à esquerda
            if (verificarRoqueEsquerda(tabuleiro, origem)) {
                return true;
            }
        }
        return false;
    }

    private boolean verificarRoqueDireita(Tabuleiro tabuleiro, Posicao origem) {
        Posicao torrePos = new Posicao(origem.getLinha(), origem.getColuna() + 3);  // Posição da Torre para roque à direita
        Peca torre = tabuleiro.obterPeca(torrePos);
        
        // Verifica se a Torre não se moveu
        if (torre instanceof Torre && torre.getMovCount() == 0) {
            // Verifica se o caminho entre o Rei e a Torre está livre de peças e não está sob ataque
            for (int i = origem.getColuna() + 1; i < torrePos.getColuna(); i++) {
                Posicao posicaoIntermediaria = new Posicao(origem.getLinha(), i);
                // Verifica se a casa intermediária está sob ataque
                if (verificarCasaSobAtaque(tabuleiro, origem, posicaoIntermediaria)) {
                    return false; // Caminho bloqueado ou sob ataque
                }
            }
            
            // Verifica se o Rei ou a Torre estão sob ataque no momento do roque
            if (tabuleiro.posicaoSobAtaque(torrePos, this.getCor()) || tabuleiro.posicaoSobAtaque(origem, this.getCor())) {
                return false; // Se a Torre ou o Rei estiverem sob ataque, o roque não é permitido
            }
            
            return true; // O roque à direita é válido
        }
        return false; // Caso contrário, não é válido
    }
    
    private boolean verificarRoqueEsquerda(Tabuleiro tabuleiro, Posicao origem) {
        Posicao torrePos = new Posicao(origem.getLinha(), origem.getColuna() - 4);  // Posição da Torre para roque à esquerda
        Peca torre = tabuleiro.obterPeca(torrePos);
        
        // Verifica se a Torre não se moveu
        if (torre instanceof Torre && torre.getMovCount() == 0) {
            // Verifica se o caminho entre o Rei e a Torre está livre de peças e não está sob ataque
            for (int i = origem.getColuna() - 1; i > torrePos.getColuna(); i--) {
                Posicao posicaoIntermediaria = new Posicao(origem.getLinha(), i);
                // Verifica se a casa intermediária está sob ataque
                if (verificarCasaSobAtaque(tabuleiro, origem, posicaoIntermediaria)) {
                    return false; // Caminho bloqueado ou sob ataque
                }
            }
            
            // Verifica se o Rei ou a Torre estão sob ataque no momento do roque
            if (tabuleiro.posicaoSobAtaque(torrePos, this.getCor()) || tabuleiro.posicaoSobAtaque(origem, this.getCor())) {
                return false; // Se a Torre ou o Rei estiverem sob ataque, o roque não é permitido
            }
            
            return true; // O roque à esquerda é válido
        }
        return false; // Caso contrário, não é válido
    }
    
    

    /**
     * Verifica se o Rei passará por uma casa sob ataque durante o movimento de roque.
     * O Rei não pode passar por casas que estão sob ataque.
     *
     * @param tabuleiro O tabuleiro atual.
     * @param origem A posição de origem do Rei.
     * @param destino A posição de destino do Rei após o roque.
     * @return {@code true} se alguma casa entre o Rei e o destino estiver sob ataque, {@code false} caso contrário.
     */
     private boolean verificarCasaSobAtaque(Tabuleiro tabuleiro, Posicao origem, Posicao destino) {
        int linha = origem.getLinha();
        int colunaOrigem = origem.getColuna();
        int colunaDestino = destino.getColuna();
        
        // Evitar checagem desnecessária da mesma casa
        if (colunaOrigem == colunaDestino) {
            return false;
        }
    
        // Verifica as casas entre a origem e o destino
        for (int i = colunaOrigem + 1; i < colunaDestino; i++) {
            Posicao posicaoIntermediaria = new Posicao(linha, i);
            // Verifica se a casa intermediária está sob ataque, mas evite repetir chamadas
            if (tabuleiro.posicaoSobAtaque(posicaoIntermediaria, this.getCor())) {
                return true; // Se alguma casa estiver sob ataque, retorna false
            }
        }
        return false; // Nenhuma casa sob ataque
    }
}