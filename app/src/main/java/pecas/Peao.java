package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.*;

@XmlRootElement
public class Peao extends Peca {

    public Peao(){

    }
    public Peao(Cor cor){
        super(cor, 1);
    }

    @Override
    public List<Posicao> proximoMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int direcao = (getCor() == Cor.BRANCO) ? -1 : 1;
        int linhaAtual = origem.getLinha();
        int colunaAtual = origem.getColuna();
        
        int novaLinha = linhaAtual + direcao;
        movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
        
        if (getMovCount() == 0) {
            novaLinha = linhaAtual + 2 * direcao;
            movimentosValidos.add(new Posicao(novaLinha, colunaAtual));  // Movimento duas casas à frente
        }
        
        return movimentosValidos;
    }    
}