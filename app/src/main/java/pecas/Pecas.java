package pecas;
import javafx.scene.image.Image;
import partida.*;

public abstract class Pecas {
    private Cor cor;
    private Image imagem;
    
    public Pecas(Cor cor, Image imagem) {
        this.cor = cor;
        this.imagem = imagem;
    }
    public abstract Movimento proxMovimento(Posicao origem);
    public Image getImage(){
        return imagem;
    }
    public Cor getCor(){
        return cor;
    }
}