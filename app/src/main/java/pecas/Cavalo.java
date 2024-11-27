package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;

@XmlRootElement
public class Cavalo extends Peca {

    public Cavalo(){
        
    }

    public Cavalo(Cor cor){
        super(cor, 3);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Todas as possíveis direções em "L" para o Cavalo
        int[][] direcoes = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},  // Dois quadrados em uma direção, um quadrado perpendicular
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}   // Um quadrado em uma direção, dois quadrados perpendicularmente
        };
    
        // Itera sobre todas as direções possíveis
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];
            
            // Apenas adiciona as novas posições sem verificação adicional
            Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
            movimentosValidos.add(novaPosicao);
        }
        
        return movimentosValidos;
    }
    
}