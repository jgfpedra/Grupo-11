package partida;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

public class Posicao implements Cloneable{
    private int linha;
    private int coluna;

    public Posicao(){
        
    }
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    @XmlElement
    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    @XmlElement
    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "Posicao [linha=" + linha + ", coluna=" + coluna + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicao posicao = (Posicao) obj;
        return linha == posicao.linha && coluna == posicao.coluna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }
    @Override
    public Posicao clone() {
        try {
            return (Posicao) super.clone();  // Chama o clone() de Object para realizar a cópia
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // Nunca deve acontecer, pois Posicao implementa Cloneable
        }
    }
}