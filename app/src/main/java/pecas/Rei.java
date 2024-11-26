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

    public Rei(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> possiveisMovimentos(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        int[][] direcoes = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1},
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
        };
    
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];
            if (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
                Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(novaLinha).get(novaColuna).getPeca();
                if (pecaNaCasa == null || pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(novaPosicao);
                }
            }
        }
    
        List<Posicao> movimentosSeguros = new ArrayList<>();
        for (Posicao movimento : movimentosValidos) {
            boolean posicaoSegura = true;
            for (int linha = 0; linha < 8; linha++) {
                for (int coluna = 0; coluna < 8; coluna++) {
                    Peca peca = partida.Tabuleiro.casas.get(linha).get(coluna).getPeca();
                    if (peca != null && peca.getCor() != this.getCor()) {
                        List<Posicao> movimentosAdversarios = peca.possiveisMovimentos(new Posicao(linha, coluna));
                        if (movimentosAdversarios.contains(movimento)) {
                            posicaoSegura = false;
                            break;
                        }
                    }
                }
                if (!posicaoSegura) {
                    break;
                }
            }
            if (posicaoSegura) {
                movimentosSeguros.add(movimento);
            }
        }
        return movimentosSeguros;
    }    
}