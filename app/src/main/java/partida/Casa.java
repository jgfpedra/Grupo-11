package partida;

import pecas.Peca;

public class Casa {
    private Cor cor;
    private Peca peca;
    private Posicao posicao;
    public Casa(Cor cor, Posicao posicao) {
        this.cor = cor;
        this.posicao = posicao;
        this.peca = null;
    }

    public Cor getCor() {
        return cor;
    }

    public Peca getPeca() {
        return peca;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Casa casa = (Casa) obj;
        return posicao.equals(casa.posicao) && 
            (peca != null ? peca.equals(casa.peca) : casa.peca == null);
    }

    @Override
    public int hashCode() {
        int result = posicao.hashCode();
        result = 31 * result + (peca != null ? peca.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Casa{" +
               "cor=" + cor +
               ", peca=" + peca +
               ", posicao=" + posicao +
               '}';
    }
}