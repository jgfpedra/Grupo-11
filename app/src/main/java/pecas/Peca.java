package pecas;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import javafx.scene.image.Image;
import partida.*;

@XmlSeeAlso({Peao.class, Cavalo.class, Rainha.class, Torre.class, Bispo.class, Rei.class})
public abstract class Peca {
    private Cor cor;
    private Image imagem;
    private int movCount;
    
    public Peca(){
        
    }
    public Peca(Cor cor) {
        this.cor = cor;
        this.movCount = 0;
        inicializarImagem();
    }

    public void inicializarImagem() {
        if (this.cor != null) {
            this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());
        }
    }

    public abstract List<Posicao> possiveisMovimentos(Posicao origem);

    protected void somaContador(){
        movCount++;
    }

    protected void subtraiContador(){
        movCount--;
    }
    public Image getImagem(){
        return imagem;
    }
    @XmlElement
    public Cor getCor(){
        return cor;
    }
    public void setCor(Cor cor){
        this.cor = cor;
    }
    @XmlElement
    public int getMovCount() {
        return movCount;
    }
    public void setMovCount(int movCount){
        this.movCount = movCount;
    }
    public void incrementarMovimento(){
        this.movCount += 1;
    }
    public void decrementarMovimento(){
        this.movCount -= 1;
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