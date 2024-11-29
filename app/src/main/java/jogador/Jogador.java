package jogador;

import javafx.scene.image.Image;
import partida.Cor;

public abstract class Jogador {
    private Cor cor;
    private String nome;
    private Image imagem;
    public Jogador(){
    }
    public Jogador(Cor cor, String nome, Image imagem){
        this.cor = cor;
        this.nome = nome;
        if(imagem == null){
            this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());
        } else {
            this.imagem = imagem;
        }
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
    private static Image carregarImagem(Cor cor, String tipoJogador) {
        String caminhoImagem = "/images/jogadores/" + tipoJogador + ".png";
        return new Image(Jogador.class.getResourceAsStream(caminhoImagem));
    }
}
