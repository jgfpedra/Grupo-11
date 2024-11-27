package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;

@XmlRootElement
public class Torre extends Peca {

    public Torre(){
        
    }

    public Torre(Cor cor){
        super(cor, 5);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
    
        // Direções para a Torre: Norte, Sul, Leste, Oeste
        int[][] direcoes = {
            {1, 0},  // Sul
            {-1, 0}, // Norte
            {0, 1},  // Leste
            {0, -1}  // Oeste
        };
        for (int[] dir : direcoes) {
            int linhaAtual = origem.getLinha();
            int colunaAtual = origem.getColuna();
            for (int i = 1; i < 8; i++) {
                linhaAtual += dir[0];
                colunaAtual += dir[1];
                if (linhaAtual >= 0 && linhaAtual < 8 && colunaAtual >= 0 && colunaAtual < 8) {
                    Posicao novaPosicao = new Posicao(linhaAtual, colunaAtual);
                    movimentosValidos.add(novaPosicao);
                } else {
                    break;
                }
            }
        }
    
        return movimentosValidos;
    }    
}