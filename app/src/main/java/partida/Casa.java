package partida;

import pecas.Peca;

/**
 * Representa uma casa no tabuleiro de xadrez.
 * 
 * Cada casa possui uma cor, uma posição no tabuleiro e pode conter uma peça de xadrez.
 * A classe {@code Casa} é responsável por armazenar essas informações e fornecer métodos para acessar e modificar
 * as propriedades associadas à casa, como sua cor, a peça que ocupa e a posição no tabuleiro.
 */
public class Casa {

    private Cor cor;
    private Peca peca;
    private Posicao posicao;

    /**
     * Construtor que cria uma casa no tabuleiro de xadrez.
     * 
     * A casa é inicializada com uma cor e uma posição, e por padrão não contém nenhuma peça (peca = null).
     *
     * @param cor A cor da casa (preta ou branca).
     * @param posicao A posição da casa no tabuleiro.
     */
    public Casa(Cor cor, Posicao posicao) {
        this.cor = cor;
        this.posicao = posicao;
        this.peca = null;
    }

    /**
     * Retorna a cor da casa.
     * 
     * @return A cor da casa (preta ou branca).
     */
    public Cor getCor() {
        return cor;
    }

    /**
     * Retorna a peça que ocupa a casa.
     * 
     * @return A peça ocupando a casa, ou {@code null} se a casa não contiver nenhuma peça.
     */
    public Peca getPeca() {
        return peca;
    }

    /**
     * Retorna a posição da casa no tabuleiro.
     * 
     * @return A posição da casa no tabuleiro.
     */
    public Posicao getPosicao() {
        return posicao;
    }

    /**
     * Define a peça que ocupa a casa.
     * 
     * @param peca A peça a ser colocada na casa. Pode ser {@code null} caso a casa não deva conter nenhuma peça.
     */
    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    /**
     * Compara esta casa com outro objeto para verificar se são iguais.
     * 
     * Duas casas são consideradas iguais se possuem a mesma posição e a mesma peça ocupando a casa.
     * 
     * @param obj O objeto a ser comparado com esta casa.
     * @return {@code true} se as casas forem iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Casa casa = (Casa) obj;
        return posicao.equals(casa.posicao) && 
            (peca != null ? peca.equals(casa.peca) : casa.peca == null);
    }

    /**
     * Retorna o código de hash para a casa.
     * 
     * O código de hash é gerado com base na posição e na peça da casa, garantindo que duas casas iguais 
     * tenham o mesmo código de hash.
     * 
     * @return O código de hash da casa.
     */
    @Override
    public int hashCode() {
        int result = posicao.hashCode();
        result = 31 * result + (peca != null ? peca.hashCode() : 0);
        return result;
    }
}