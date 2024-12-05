package exception;

/**
 * Exceção personalizada para indicar que o movimento de roque é inválido.
 * 
 * Esta exceção é lançada quando um jogador tenta realizar um movimento de
 * roque que não atende às condições necessárias, como o Rei ou a Torre já
 * terem se movido, ou se houver peças no caminho do roque.
 */
public class RoqueInvalidoException extends RuntimeException {
    
    /**
     * Construtor para a exceção RoqueInvalidoException.
     * 
     * @param mensagem A mensagem que descreve o erro, indicando que o movimento
     *                 de roque é inválido.
     */
    public RoqueInvalidoException(String mensagem) {
        super(mensagem);
    }
}
