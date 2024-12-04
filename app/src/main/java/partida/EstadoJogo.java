package partida;

public enum EstadoJogo {
    INICIO("Início"), 
    EM_ANDAMENTO("Em Andamento"), 
    EMPATE("Empate"), 
    XEQUE("Xeque"), 
    XEQUE_MATE("Xeque Mate"), 
    FIM("Fim");

    private final String texto;

    private EstadoJogo(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}