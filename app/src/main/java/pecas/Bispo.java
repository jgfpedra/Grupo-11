package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;

@XmlRootElement
public class Bispo extends Peca {

    public Bispo(){

    }
    
    public Bispo(Cor cor) {
        super(cor, 3);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
    
        // Diagonal superior esquerda
        for (int i = 1; i < 8; i++) {
            Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() - i);
            movimentosValidos.add(pos);
        }
    
        // Diagonal superior direita
        for (int i = 1; i < 8; i++) {
            Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() + i);
            movimentosValidos.add(pos);
        }
    
        // Diagonal inferior esquerda
        for (int i = 1; i < 8; i++) {
            Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() - i);
            movimentosValidos.add(pos);
        }
    
        // Diagonal inferior direita
        for (int i = 1; i < 8; i++) {
            Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() + i);
            movimentosValidos.add(pos);
        }
    
        return movimentosValidos;
    }    
}