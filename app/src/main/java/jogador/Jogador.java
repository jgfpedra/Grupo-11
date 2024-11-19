package jogador;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Tabuleiro;

public abstract class Jogador {
    private Cor cor;
    private String nome;
    private Image imagem;
    public Jogador(){
    }
    public Jogador(Cor cor, String nome, Image imagem){
        this.cor = cor;
        this.nome = nome;
        this.imagem = imagem;
    }
    public Cor getCor(){
        return this.cor;
    }
    public String getNome(){
        return this.nome;
    }
    public Image getImagem(){
        return this.imagem;
    }
    public abstract void escolherMovimento(Tabuleiro tabuleiro);
    public abstract void temPecas();
}
