package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

/**
 * Representa a peça Bispo no jogo de xadrez.
 * O Bispo pode se mover em qualquer direção diagonal,
 * mas não pode pular peças e pode capturar apenas peças adversárias.
 */
@XmlRootElement
public class Bispo extends Peca {

    /**
     * Construtor padrão do Bispo. 
     * Utilizado para a criação de instâncias sem cor definida.
     */
    public Bispo(){
    }
    
    /**
     * Construtor do Bispo com uma cor específica.
     * @param cor A cor do Bispo (BRANCO ou PRETO).
     */
    public Bispo(Cor cor) {
        super(cor, 3);
    }

    /**
     * Calcula todos os possíveis movimentos do Bispo a partir de uma posição no tabuleiro.
     * O Bispo pode se mover nas quatro direções diagonais (noroeste, nordeste, sudoeste, sudeste)
     * até encontrar o final do tabuleiro ou uma peça que bloqueie seu caminho.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça no tabuleiro.
     * @return Uma lista de posições válidas para o movimento do Bispo.
     */
    @Override
    public List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() - i >= 0 && origem.getColuna() - i >= 0) {
                Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() - i);
                Peca pecaNaCasa = tabuleiro.getCasas().get(origem.getLinha() - i).get(origem.getColuna() - i).getPeca();
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos);
                } else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
            Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() - i);
            movimentosValidos.add(pos);
        }
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() - i >= 0 && origem.getColuna() + i < 8) {
                Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() + i);
                Peca pecaNaCasa = tabuleiro.getCasas().get(origem.getLinha() - i).get(origem.getColuna() + i).getPeca();
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos);
                } else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
            Posicao pos = new Posicao(origem.getLinha() - i, origem.getColuna() + i);
            movimentosValidos.add(pos);
        }
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() + i < 8 && origem.getColuna() - i >= 0) {
                Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() - i);
                Peca pecaNaCasa = tabuleiro.getCasas().get(origem.getLinha() + i).get(origem.getColuna() - i).getPeca();
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos);
                } else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
            Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() - i);
            movimentosValidos.add(pos);
        }
        for (int i = 1; i < 8; i++) {
            if (origem.getLinha() + i < 8 && origem.getColuna() + i < 8) {
                Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() + i);
                Peca pecaNaCasa = tabuleiro.getCasas().get(origem.getLinha() + i).get(origem.getColuna() + i).getPeca();
                if (pecaNaCasa == null) {
                    movimentosValidos.add(pos);
                } else if (pecaNaCasa.getCor() != this.getCor()) {
                    movimentosValidos.add(pos);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
            Posicao pos = new Posicao(origem.getLinha() + i, origem.getColuna() + i);
            movimentosValidos.add(pos);
        }
        return movimentosValidos;
    }    
}