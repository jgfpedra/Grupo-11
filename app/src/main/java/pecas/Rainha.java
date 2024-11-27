package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.*;

@XmlRootElement
public class Rainha extends Peca {

    public Rainha(){
        
    }

    public Rainha(Cor cor){
        super(cor, 9);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
            {1, 1},
            {-1, 1},
            {1, -1},
            {-1, -1}
        };
        for (int[] dir : direcoes) {
            int linhaAtual = origem.getLinha();
            int colunaAtual = origem.getColuna();
            while (true) {
                linhaAtual += dir[0];
                colunaAtual += dir[1];
                if (linhaAtual < 0 || linhaAtual >= 8 || colunaAtual < 0 || colunaAtual >= 8) {
                    break;
                }
        
                Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                movimentosValidos.add(novaPosicao);
            }
        }
        return movimentosValidos;
    }
}