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
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
    
        int direcao = (getCor() == Cor.BRANCO) ? -1 : 1;
        int linhaAtual = origem.getLinha();
        int colunaAtual = origem.getColuna();
        int novaLinha = linhaAtual + direcao;
        if (novaLinha >= 0 && novaLinha < 8) {
            if (tabuleiro.getCasas().get(novaLinha).get(colunaAtual).getPeca() == null) {
                movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
            }
        }
        movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
        if (getMovCount() == 0) {
            novaLinha = linhaAtual + 2 * direcao;
            int linhaIntermediaria = linhaAtual + direcao;
            if (novaLinha >= 0 && novaLinha < 8) {
                if (tabuleiro.getCasas().get(linhaIntermediaria).get(colunaAtual).getPeca() == null
                    && tabuleiro.getCasas().get(novaLinha).get(colunaAtual).getPeca() == null) {
                    movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
                }
            }
        }
        int[] direcoesColuna = {-1, 1};
        for (int i = 0; i < direcoesColuna.length; i++) {
            int novaColuna = colunaAtual + direcoesColuna[i];
            if (novaColuna >= 0 && novaColuna < 8) {
                novaLinha = linhaAtual + direcao;
                if (novaLinha >= 0 && novaLinha < 8) {
                    Peca pecaNaDiagonal = tabuleiro.getCasas().get(novaLinha).get(novaColuna).getPeca();
                    if (pecaNaDiagonal != null && pecaNaDiagonal.getCor() != this.getCor()) {
                        movimentosValidos.add(new Posicao(novaLinha, novaColuna));
                    }
                }
            }
            movimentosValidos.add(new Posicao(novaLinha, colunaAtual));
        }
    
        
        return movimentosValidos;
    }
}