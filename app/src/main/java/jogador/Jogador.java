package jogador;

import partida.Tabuleiro;

public interface Jogador {
    public void escolherMovimento(Tabuleiro tabuleiro);
    public void temPecas();
    public void getCor();
    public void getNome();
}
