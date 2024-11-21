package jogador;

import partida.Cor;
import partida.Tabuleiro;

public class JogadorOnline extends Jogador{

    public JogadorOnline(){

    }
    public JogadorOnline(Cor cor, String nome){
        super(cor, nome);
    }
    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
    }
    @Override
    public void temPecas() {
    }
}
