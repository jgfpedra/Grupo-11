package jogador;

import partida.Cor;

public class JogadorLocal extends Jogador {
    public JogadorLocal(){

    }
    public JogadorLocal(Cor cor, String nome){
        super(cor, nome, null);
    }
}