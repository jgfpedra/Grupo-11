package test;

public class MovimentoInvalidoException extends RuntimeException {
    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
