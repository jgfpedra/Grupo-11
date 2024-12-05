package partida;

/**
 * Interface que define o comportamento de um observador do tabuleiro de xadrez.
 * Qualquer classe que implemente esta interface será notificada para atualizar seu estado
 * sempre que houver uma alteração no tabuleiro.
 */
public interface ObservadorTabuleiro {
    /**
     * Método chamado para atualizar o estado do observador quando houver uma mudança no tabuleiro.
     */
    public void atualizar();
}