package pecas;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;
import partida.*;

public abstract class Peca {
    private Cor cor;
    private Image imagem;
    private int movCount;
    
    public Peca(Cor cor) {
        this.cor = cor;
        this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());;
        this.movCount = 0;
    }
    public abstract List<Posicao> proxMovimento(Posicao origem);

    protected void somaContador(){
        movCount++;
    }

    protected void subtraiContador(){
        movCount--;
    }
    public Image getImage(){
        return imagem;
    }
    public Cor getCor(){
        return cor;
    }
    public int getMovCount() {
        return movCount;
    }
    public void incrementarMovimento(){
        this.movCount += 1;
    }
    private static Image carregarImagem(Cor cor, String tipoPeca) {
        String caminhoImagem = "/images/pecas/" + tipoPeca + "_" + (cor == Cor.BRANCO ? "branco" : "preto") + ".png";
        return new Image(Peca.class.getResourceAsStream(caminhoImagem));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Peca peca = (Peca) obj;
        return cor == peca.cor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cor);
    }
}