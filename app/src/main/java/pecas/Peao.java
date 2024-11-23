package pecas;

import java.util.ArrayList;
import java.util.List;
import partida.*;

public class Peao extends Peca {

    public Peao(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        int direcao = (getCor() == Cor.BRANCO) ? 1 : -1;  // Determina a direção do movimento (1 para branco, -1 para preto)
        int linhaAtual = origem.getLinha();
        int colunaAtual = origem.getColuna();

        // 1. Movimento normal: O peão pode se mover uma casa para frente
        int novaLinha = linhaAtual + direcao;
        if (novaLinha >= 0 && novaLinha < 8) {  // Garante que a linha não saia do tabuleiro
            if (partida.Tabuleiro.casas.get(novaLinha).get(colunaAtual).getPeca() == null) {
                movimentosValidos.add(new Posicao(novaLinha, colunaAtual));  // Movimenta-se uma casa para frente
            }
        }

        // 2. Primeiro movimento: O peão pode mover duas casas para frente
        if (getMovCount() == 0) {
            novaLinha = linhaAtual + 2 * direcao;
            int linhaIntermediaria = linhaAtual + direcao;  // Linha entre a origem e o destino
            if (novaLinha >= 0 && novaLinha < 8) {
                if (partida.Tabuleiro.casas.get(linhaIntermediaria).get(colunaAtual).getPeca() == null
                    && partida.Tabuleiro.casas.get(novaLinha).get(colunaAtual).getPeca() == null){
                    movimentosValidos.add(new Posicao(novaLinha, colunaAtual));  // Movimenta-se duas casas para frente
                }
            }
        }

        // 3. Captura na diagonal (peças adversárias)
        int[] direcoesColuna = {-1, 1};  // As duas direções possíveis para captura na diagonal (esquerda e direita)
        for (int i = 0; i < direcoesColuna.length; i++) {
            int novaColuna = colunaAtual + direcoesColuna[i];
            if (novaColuna >= 0 && novaColuna < 8) {  // Garante que a coluna não saia do tabuleiro
                novaLinha = linhaAtual + direcao;
                if (novaLinha >= 0 && novaLinha < 8) {  // Garante que novaLinha está dentro dos limites
                    if(partida.Tabuleiro.casas.get(novaLinha).get(novaColuna).getPeca() != null
                        && partida.Tabuleiro.casas.get(novaLinha).get(novaColuna).getCor() != partida.Tabuleiro.casas.get(linhaAtual).get(colunaAtual).getCor()){
                        movimentosValidos.add(new Posicao(novaLinha, novaColuna));  // Adiciona o movimento de captura na diagonal
                    }
                }
            }
        }

        return movimentosValidos;
    }
}