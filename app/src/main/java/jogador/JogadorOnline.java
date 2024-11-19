package jogador;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Tabuleiro;

public class JogadorOnline extends Jogador{

    public JogadorOnline(){

    }
    public JogadorOnline(Cor cor, String nome, Image imagem){
        super(cor, nome, imagem);
    }
    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
    }
    @Override
    public void temPecas() {
    }
}
