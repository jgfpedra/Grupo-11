package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;

@XmlRootElement
public class Rei extends Peca {

    public Rei(){
        
    }

    public Rei(Cor cor){
        super(cor, 0);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {-1, 0},  // Para cima
            {1, 0},   // Para baixo
            {0, -1},  // Para a esquerda
            {0, 1},   // Para a direita
            {-1, -1}, // Diagonal superior esquerda
            {-1, 1},  // Diagonal superior direita
            {1, -1},  // Diagonal inferior esquerda
            {1, 1}    // Diagonal inferior direita
        };
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];
    
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
                movimentosValidos.add(novaPosicao);
            }
        }
    
        return movimentosValidos;
    }    
}