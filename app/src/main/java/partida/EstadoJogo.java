package partida;

/**
 * Enumeração que representa os diferentes estados de uma partida de xadrez.
 * 
 * A enumeração {@code EstadoJogo} possui os seguintes valores:
 * 
 * - {@code INICIO}: Representa o início da partida.
 * - {@code EM_ANDAMENTO}: Indica que a partida está em andamento.
 * - {@code EMPATE}: Indica que a partida terminou em empate.
 * - {@code XEQUE}: Indica que o rei de um dos jogadores está em xeque.
 * - {@code XEQUE_MATE}: Indica que o jogo terminou com xeque-mate, ou seja, um jogador perdeu.
 * - {@code FIM}: Indica o fim da partida, independentemente do resultado (pode ser empate, vitória ou derrota).
 * 
 * Cada estado possui uma descrição em forma de texto associada a ele, que pode ser utilizada para exibição na interface do usuário.
 */
public enum EstadoJogo {

    INICIO("Início"), 
    EM_ANDAMENTO("Em Andamento"), 
    EMPATE("Empate"), 
    XEQUE("Xeque"), 
    XEQUE_MATE("Xeque Mate"), 
    FIM("Fim");

    private final String texto;


    /**
     * Construtor da enumeração {@code EstadoJogo}.
     * 
     * Inicializa o estado do jogo com a descrição fornecida.
     *
     * @param texto A descrição textual do estado.
     */
    private EstadoJogo(String texto) {
        this.texto = texto;
    }

    /**
     * Retorna a descrição textual do estado do jogo.
     * 
     * @return A descrição textual do estado (ex: "Início", "Em Andamento", etc.).
     */
    @Override
    public String toString() {
        return texto;
    }
}