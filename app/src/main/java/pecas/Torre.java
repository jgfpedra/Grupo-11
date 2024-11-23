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
                if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()).getPeca() == null) {
                    // Casa vazia, adiciona movimento
                    movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                } else {
                    // Se encontrar uma peça, verifica se é da cor adversária
                    if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()).getPeca().getCor() != this.getCor()) {
                        // Se for peça adversária, pode capturar
                        movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                    }
                    break; // Se encontrar qualquer peça (aliada ou adversária), a torre não pode mais se mover nessa direção
                }
            } else {
                break; // Se ultrapassar o limite superior do tabuleiro, pare o movimento
            }
        }

        // Movimento para o Sul (linha + i)
        for (int i = 1; i < 8; i++) {
            int novaLinha = origem.getLinha() + i;
            if (novaLinha < 8) { // Verificando se a linha não ultrapassa o limite inferior
                if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()).getPeca() == null) {
                    // Casa vazia, adiciona movimento
                    movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                } else {
                    // Se encontrar uma peça, verifica se é da cor adversária
                    if (partida.Tabuleiro.casas.get(novaLinha).get(origem.getColuna()).getPeca().getCor() != this.getCor()) {
                        // Se for peça adversária, pode capturar
                        movimentosValidos.add(new Posicao(novaLinha, origem.getColuna()));
                    }
                    break; // Se encontrar qualquer peça (aliada ou adversária), a torre não pode mais se mover nessa direção
                }
            } else {
                break; // Se ultrapassar o limite inferior do tabuleiro, pare o movimento
            }
        }

        // Movimento para o Leste (coluna + i)
        for (int i = 1; i < 8; i++) {
            int novaColuna = origem.getColuna() + i;
            if (novaColuna < 8) { // Verificando se a coluna não ultrapassa o limite direito
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna).getPeca() == null) {
                    // Casa vazia, adiciona movimento
                    movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                } else {
                    // Se encontrar uma peça, verifica se é da cor adversária
                    if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna).getPeca().getCor() != this.getCor()) {
                        // Se for peça adversária, pode capturar
                        movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                    }
                    break; // Se encontrar qualquer peça (aliada ou adversária), a torre não pode mais se mover nessa direção
                }
            } else {
                break; // Se ultrapassar o limite direito do tabuleiro, pare o movimento
            }
        }

        // Movimento para o Oeste (coluna - i)
        for (int i = 1; i < 8; i++) {
            int novaColuna = origem.getColuna() - i;
            if (novaColuna >= 0) { // Verificando se a coluna não ultrapassa o limite esquerdo
                if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna).getPeca() == null) {
                    // Casa vazia, adiciona movimento
                    movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                } else {
                    // Se encontrar uma peça, verifica se é da cor adversária
                    if (partida.Tabuleiro.casas.get(origem.getLinha()).get(novaColuna).getPeca().getCor() != this.getCor()) {
                        // Se for peça adversária, pode capturar
                        movimentosValidos.add(new Posicao(origem.getLinha(), novaColuna));
                    }
                    break; // Se encontrar qualquer peça (aliada ou adversária), a torre não pode mais se mover nessa direção
                }
            } else {
                break; // Se ultrapassar o limite esquerdo do tabuleiro, pare o movimento
            }
        }

        return movimentosValidos;
    }
}