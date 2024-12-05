package test;

/**
 * Exceção personalizada para representar o caso onde um caminho no tabuleiro de xadrez
 * está bloqueado, ou seja, uma peça não pode se mover para uma posição porque outra peça
 * está bloqueando o caminho.
 * 
 * Esta exceção é lançada quando uma peça tenta se mover para uma casa onde o caminho
 * está obstruído, impedindo o movimento.
 */
public class CaminhoBloqueadoException extends RuntimeException {
    
    /**
     * Construtor para a exceção CaminhoBloqueadoException.
     * 
     * @param mensagem A mensagem que descreve o motivo da exceção ser lançada.
     */
    public CaminhoBloqueadoException(String mensagem) {
        super(mensagem);
    }
}