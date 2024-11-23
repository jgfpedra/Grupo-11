package pecas;

import java.util.ArrayList;
import java.util.List;

import partida.Cor;
import partida.Posicao;

public class Bispo extends Peca {

    public Bispo(Cor cor) {
        super(cor);
    }

    @Override
    public List<Posicao> proxMovimento(Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Diagonal movements (top-left, top-right, bottom-left, bottom-right)

        // Top-left diagonal
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() - i >= 0 && origem.getColuna() - i >= 0) {
                Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() - i);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna() - i).getPeca();
                
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos); // Casa vazia, pode se mover
                } 
                else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);  // Captura adversária
                    break; // Após capturar, parar a verificação dessa diagonal
                } 
                else {
                    break; // Se encontrar peça da mesma cor, parar a busca nesta direção
                }
            } else {
                break;  // Se sair dos limites do tabuleiro, parar
            }
        }

        // Top-right diagonal
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i < 8) {
                Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() + i);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(origem.getLinha() - i).get(origem.getColuna() + i).getPeca();
                
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos); // Casa vazia, pode se mover
                } 
                else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);  // Captura adversária
                    break; // Após capturar, parar a verificação dessa diagonal
                } 
                else {
                    break; // Se encontrar peça da mesma cor, parar a busca nesta direção
                }
            } else {
                break;  // Se sair dos limites do tabuleiro, parar
            }
        }

        // Bottom-left diagonal
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() + i < 8 && origem.getColuna() - i >= 0) {
                Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() - i);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() - i).getPeca();
                
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos); // Casa vazia, pode se mover
                } 
                else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);  // Captura adversária
                    break; // Após capturar, parar a verificação dessa diagonal
                } 
                else {
                    break; // Se encontrar peça da mesma cor, parar a busca nesta direção
                }
            } else {
                break;  // Se sair dos limites do tabuleiro, parar
            }
        }

        // Bottom-right diagonal
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() + i);
                Peca pecaNaCasa = partida.Tabuleiro.casas.get(origem.getLinha() + i).get(origem.getColuna() + i).getPeca();
                
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos); // Casa vazia, pode se mover
                } 
                else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);  // Captura adversária
                    break; // Após capturar, parar a verificação dessa diagonal
                } 
                else {
                    break; // Se encontrar peça da mesma cor, parar a busca nesta direção
                }
            } else {
                break;  // Se sair dos limites do tabuleiro, parar
            }
        }

        return movimentosValidos;
    }
}