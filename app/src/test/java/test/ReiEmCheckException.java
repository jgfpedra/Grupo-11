package test;

/**
 * Exceção personalizada para indicar que o Rei de um jogador está em xeque.
 * 
 * Esta exceção é lançada quando o Rei de um jogador está em uma posição onde
 * ele pode ser capturado no próximo movimento do adversário, ou seja, quando
 * o Rei está em check. O jogador não pode fazer movimentos que deixem seu
 * próprio Rei em check, e essa exceção pode ser utilizada para sinalizar esse
 * erro.
 */
public class ReiEmCheckException extends RuntimeException {
    
    /**
     * Construtor para a exceção ReiEmCheckException.
     * 
     * @param mensagem A mensagem que descreve o erro, indicando que o Rei está em xeque.
     */
    public ReiEmCheckException(String mensagem) {
        super(mensagem);
    }
}
