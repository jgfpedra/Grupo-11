package jogador;

import javafx.scene.image.Image;
import partida.Cor;

/**
 * Representa um jogador no jogo de xadrez, com atributos como cor, nome e imagem.
 * A classe é abstrata, indicando que não pode ser instanciada diretamente, sendo usada como base 
 * para tipos específicos de jogadores (ex: JogadorHumano, JogadorComputador).
 * 
 * A imagem do jogador pode ser carregada automaticamente com base em sua cor e tipo de jogador,
 * ou então uma imagem personalizada pode ser fornecida durante a criação do jogador.
 * 
 * A classe possui métodos para acessar os atributos de cor, nome e imagem do jogador.
 */
public abstract class Jogador {

    private Cor cor;
    private String nome;
    private Image imagem;

    /**
     * Construtor padrão da classe Jogador.
     * Inicializa o jogador com valores padrão (sem atribuições específicas).
     */
    public Jogador(){
    }

    /**
     * Construtor que inicializa o jogador com a cor, nome e imagem fornecidos.
     * Se a imagem for nula, uma imagem padrão será carregada com base na cor e no tipo de jogador.
     *
     * @param cor     A cor do jogador (branco ou preto).
     * @param nome    O nome do jogador.
     * @param imagem  A imagem do jogador, ou null para carregar uma imagem padrão.
     */
    public Jogador(Cor cor, String nome, Image imagem){
        this.cor = cor;
        this.nome = nome;
        if(imagem == null){
            this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());
        } else {
            this.imagem = imagem;
        }
    }
    /**
     * Retorna a cor do jogador.
     *
     * @return A cor do jogador.
     */
    public Cor getCor(){
        return this.cor;
    }

    /**
     * Retorna o nome do jogador.
     *
     * @return O nome do jogador.
     */
    public String getNome(){
        return this.nome;
    }

    /**
     * Retorna a imagem do jogador.
     *
     * @return A imagem do jogador.
     */
    public Image getImagem(){
        return this.imagem;
    }

    /**
     * Carrega a imagem do jogador com base na sua cor e tipo (classe do jogador).
     * O caminho da imagem segue o formato "/images/jogadores/{tipoJogador}.png",
     * sendo que o tipo é o nome da classe do jogador convertido para minúsculas.
     *
     * @param cor        A cor do jogador.
     * @param tipoJogador O tipo de jogador (nome da classe do jogador em minúsculas).
     * @return A imagem do jogador carregada a partir do caminho.
     */
    private static Image carregarImagem(Cor cor, String tipoJogador) {
        String caminhoImagem = "/images/jogadores/" + tipoJogador + ".png";
        return new Image(Jogador.class.getResourceAsStream(caminhoImagem));
    }
}