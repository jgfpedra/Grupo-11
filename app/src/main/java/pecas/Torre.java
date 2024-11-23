package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Torre extends Peca {
    public Torre(Cor cor){
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
    
        // Movimento para o Norte (linha - i)
        for (int i = 1; i < 8; i++) {
            int novaLinha = origem.getLinha() - i;
            if (novaLinha >= 0) { // Verificando se a linha não ultrapassa o limite superior
                // Se a casa não estiver ocupada, adicione a posição
                if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()) == null) {
                    movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                } else {
                    break; // Se encontrar uma peça, não pode ir mais
                }
            } else {
                break; // Se ultrapassar o limite superior do tabuleiro, pare o movimento
            }
        }
    
        // Movimento para o Sul (linha + i)
        for (int i = 1; i < 8; i++) {
            int novaLinha = origem.getLinha() + i;
            if (novaLinha < 8) { // Verificando se a linha não ultrapassa o limite inferior
                // Se a casa não estiver ocupada, adicione a posição
                if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()) == null) {
                    movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                } else {
                    break; // Se encontrar uma peça, não pode ir mais
                }
            } else {
                break; // Se ultrapassar o limite inferior do tabuleiro, pare o movimento
            }
        }
    
        // Movimento para o Leste (coluna + i)
        for (int i = 1; i < 8; i++) {
            int novaColuna = origem.getColuna() + i;
            if (novaColuna < 8) { // Verificando se a coluna não ultrapassa o limite direito
                // Se a casa não estiver ocupada, adicione a posição
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                } else {
                    break; // Se encontrar uma peça, não pode ir mais
                }
            } else {
                break; // Se ultrapassar o limite direito do tabuleiro, pare o movimento
            }
        }
    
        // Movimento para o Oeste (coluna - i)
        for (int i = 1; i < 8; i++) {
            int novaColuna = origem.getColuna() - i;
            if (novaColuna >= 0) { // Verificando se a coluna não ultrapassa o limite esquerdo
                // Se a casa não estiver ocupada, adicione a posição
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna) == null) {
                    movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                } else {
                    break; // Se encontrar uma peça, não pode ir mais
                }
            } else {
                break; // Se ultrapassar o limite esquerdo do tabuleiro, pare o movimento
            }
        }
    
        return movimentosValidos;
    }    
}