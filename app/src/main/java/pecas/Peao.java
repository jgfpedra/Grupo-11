package pecas;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import partida.*;

public class Peao extends Peca {

    public Peao(Cor cor, Image imagem) {
        super(cor, imagem);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem){
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        if (getMovCount() == 0) { // Primeiro movimento do peão pode ser de 2 casas para frente
            int novaLinha = origem.getLinha() + 2;
            int novaColuna = origem.getColuna() + 0;
            movimentosValidos.add(new Posicao(novaLinha, novaColuna));
        }
        else { // Demais movimentos apenas 1 casa para frente
            int novaLinha = origem.getLinha() + 1;
            int novaColuna = origem.getColuna() + 0;
            movimentosValidos.add(new Posicao(novaLinha, novaColuna));
        }
        if () { // 
            
        }
    }
}
