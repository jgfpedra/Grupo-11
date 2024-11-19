package pecas;

import java.util.List;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Posicao;

public class Rei extends Peca{

    public Rei(Cor cor, Image imagem){
        super(cor, imagem);
    }
    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'proxMovimento'");
    }

}
