package jogador;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.scene.image.Image;
import partida.Cor;
import partida.Tabuleiro;

@XmlRootElement
public abstract class Jogador {
    private Cor cor;
    private String nome;
    private Image imagem;
    public Jogador(){
    }
    public Jogador(Cor cor, String nome){
        this.cor = cor;
        this.nome = nome;
        this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());;;
    }
    @XmlElement
    public Cor getCor(){
        return this.cor;
    }
    @XmlElement
    public String getNome(){
        return this.nome;
    }
    @XmlElement
    public Image getImagem(){
        return this.imagem;
    }
    public abstract void escolherMovimento(Tabuleiro tabuleiro);
    public abstract void temPecas();
    private static Image carregarImagem(Cor cor, String tipoJogador) {
        System.out.println(tipoJogador);
        String caminhoImagem = "/images/jogadores/" + tipoJogador + ".png";
        return new Image(Jogador.class.getResourceAsStream(caminhoImagem));
    }
}
