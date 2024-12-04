package pecas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Posicao;
import partida.Tabuleiro;

@XmlRootElement
public class Bispo extends Peca {

    public Bispo(){
    }
    
    public Bispo(Cor cor) {
        super(cor, 3);
    }

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