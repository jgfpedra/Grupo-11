package pecas;
import java.util.List;

import javafx.scene.image.Image;
import partida.*;

public abstract class Peca {
    private Cor cor;
    private Image imagem;
    
    public Peca(Cor cor, Image imagem) {
        this.cor = cor;
        this.imagem = imagem;
    }
    public abstract List<Posicao> proxMovimento(Posicao origem);
    public Image getImage(){
        return imagem;
    }
    public Cor getCor(){
        return cor;
    }
}