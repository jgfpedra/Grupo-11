package partida;

public enum EstadoJogo {
    INICIO("Início"), 
    EM_ANDAMENTO("Em Andamento"), 
    EMPATE("Empate"), 
    XEQUE("Xeque"), 
    XEQUE_MATE("Xeque Mate"), 
    FIM("Fim");

    private final String texto;

    // Construtor que recebe o nome legível
    private EstadoJogo(String texto) {
        this.texto = texto;
    }

    // Método que retorna o nome legível
    @Override
    public String toString() {
        return texto;
    }
}