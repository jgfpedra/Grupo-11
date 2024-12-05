package test;

/**
 * Exceção personalizada para indicar que um movimento no jogo de xadrez é inválido.
 * 
 * Esta exceção é lançada quando um jogador tenta realizar um movimento que não
 * é permitido pelas regras do xadrez. Por exemplo, quando uma peça é movida de
 * maneira inadequada ou para uma casa onde o movimento não é legal.
 */
public class MovimentoInvalidoException extends RuntimeException {

    /**
     * Construtor para a exceção MovimentoInvalidoException.
     * 
     * @param mensagem A mensagem que descreve a razão pela qual o movimento é inválido.
     */
    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
