package pecas;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Movimento;
import partida.Posicao;

public class Bispo extends Pecas{

    public Bispo(Cor cor, Image imagem){
        super(cor, imagem);
    }
    @Override
    public Movimento proxMovimento(Posicao origem) {
        // TODO: a partir de um objeto Movimento, a gente verifica as casas
        throw new UnsupportedOperationException("Unimplemented method 'proxMovimento'");
    }

}
