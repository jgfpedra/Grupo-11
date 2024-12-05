package partida;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

/**
 * A classe Posicao representa a posição de uma casa no tabuleiro de xadrez.
 * Ela é composta por uma linha e uma coluna, que indicam a localização da casa
 * no tabuleiro. Pode ser usada para armazenar a posição das peças ou para verificar
 * movimentos válidos de peças no tabuleiro.
 */
public class Posicao implements Cloneable{

    private int linha;
    private int coluna;

    /**
     * Construtor padrão que inicializa a posição com valores padrão (0, 0).
     */
    public Posicao(){
    }

    /**
     * Construtor que inicializa a posição com a linha e coluna especificadas.
     * 
     * @param linha A linha da posição no tabuleiro (0-7).
     * @param coluna A coluna da posição no tabuleiro (0-7).
     */
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    /**
     * Retorna a linha da posição no tabuleiro.
     * 
     * @return A linha da posição no tabuleiro (0-7).
     */
    @XmlElement
    public int getLinha() {
        return linha;
    }

    /**
     * Define a linha da posição no tabuleiro.
     * 
     * @param linha A linha da posição (0-7).
     */
    public void setLinha(int linha) {
        this.linha = linha;
    }

    /**
     * Retorna a coluna da posição no tabuleiro.
     * 
     * @return A coluna da posição no tabuleiro (0-7).
     */
    @XmlElement
    public int getColuna() {
        return coluna;
    }

    /**
     * Define a coluna da posição no tabuleiro.
     * 
     * @param coluna A coluna da posição (0-7).
     */
    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    /**
     * Compara a posição atual com outra posição.
     * A comparação é baseada nos valores de linha e coluna.
     * 
     * @param obj O objeto a ser comparado.
     * @return Verdadeiro se as posições forem iguais, falso caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicao posicao = (Posicao) obj;
        return linha == posicao.linha && coluna == posicao.coluna;
    }

    /**
     * Gera o código hash para a posição, com base nos valores de linha e coluna.
     * 
     * @return O código hash calculado a partir de linha e coluna.
     */
    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }
    
    /**
     * Cria uma cópia da posição atual.
     * 
     * @return Uma nova instância de Posicao com os mesmos valores de linha e coluna.
     */
    @Override
    public Posicao clone() {
        try {
            return (Posicao) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}