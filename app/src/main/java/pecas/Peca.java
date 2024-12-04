package pecas;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import javafx.scene.image.Image;
import partida.*;

/**
 * Classe abstrata que representa uma peça de xadrez.
 * Cada peça tem uma cor, um valor (por exemplo, 1 para peão, 5 para rainha), 
 * um contador de movimentos e uma imagem associada.
 * As classes específicas de peças (Peao, Cavalo, etc.) herdam desta classe.
 */
@XmlSeeAlso({Peao.class, Cavalo.class, Rainha.class, Torre.class, Bispo.class, Rei.class})
public abstract class Peca implements Cloneable{

    private Cor cor;
    private Image imagem;
    private int valor;
    private int movCount;
    
    /**
     * Construtor padrão da peça. Utilizado para a criação de instâncias sem especificar cor ou valor.
     */
    public Peca(){
    }

    /**
     * Construtor da peça com cor e valor especificados.
     * Inicializa a imagem da peça com base na cor e tipo.
     * 
     * @param cor A cor da peça (BRANCO ou PRETO).
     * @param valor O valor da peça (ex: 1 para peão, 5 para rainha).
     */
    public Peca(Cor cor, int valor) {
        this.cor = cor;
        this.movCount = 0;
        this.valor = valor;
        inicializarImagem();
    }

    /**
     * Inicializa a imagem da peça com base na sua cor e tipo (nome da classe).
     * O nome da classe determina o tipo de peça (peão, cavalo, etc.), 
     * e a cor determina se a peça é branca ou preta.
     */
    public void inicializarImagem() {
        if (this.cor != null) {
            this.imagem = carregarImagem(cor, this.getClass().getSimpleName().toLowerCase());
        }
    }

    /**
     * Incrementa o contador de movimentos da peça.
     */
    protected void somaContador(){
        movCount++;
    }

    /**
     * Decrementa o contador de movimentos da peça.
     */
    protected void subtraiContador(){
        movCount--;
    }

    /**
     * Retorna a imagem associada à peça.
     * 
     * @return A imagem da peça.
     */
    public Image getImagem(){
        return imagem;
    }

    /**
     * Retorna a cor da peça.
     * 
     * @return A cor da peça.
     */
    @XmlElement
    public Cor getCor(){
        return cor;
    }

    /**
     * Define a cor da peça.
     * 
     * @param cor A cor a ser atribuída à peça.
     */
    public void setCor(Cor cor){
        this.cor = cor;
    }

    /**
     * Retorna o valor da peça.
     * 
     * @return O valor da peça.
     */
    @XmlElement
    public int getValor(){
        return valor;
    }
    
    /**
     * Define o valor da peça.
     * 
     * @param valor O valor a ser atribuído à peça.
     */
    public void setValor(int valor){
        this.valor = valor;
    }

    /**
     * Retorna o contador de movimentos da peça.
     * 
     * @return O número de movimentos realizados pela peça.
     */
    @XmlElement
    public int getMovCount() {
        return movCount;
    }

    /**
     * Define o contador de movimentos da peça.
     * 
     * @param movCount O número de movimentos realizados pela peça.
     */
    public void setMovCount(int movCount){
        this.movCount = movCount;
    }

    /**
     * Incrementa o contador de movimentos da peça em 1.
     */
    public void incrementarMovimento(){
        this.movCount += 1;
    }

    /**
     * Decrementa o contador de movimentos da peça em 1.
     */
    public void decrementarMovimento(){
        this.movCount -= 1;
    }

    /**
     * Carrega a imagem da peça com base na cor e tipo da peça.
     * O caminho da imagem segue o padrão "/images/pecas/[tipoPeca]_[cor].png".
     * 
     * @param cor A cor da peça (BRANCO ou PRETO).
     * @param tipoPeca O tipo de peça (peao, cavalo, etc.).
     * @return A imagem da peça.
     */
    private static Image carregarImagem(Cor cor, String tipoPeca) {
        String caminhoImagem = "/images/pecas/" + tipoPeca + "_" + (cor == Cor.BRANCO ? "branco" : "preto") + ".png";
        return new Image(Peca.class.getResourceAsStream(caminhoImagem));
    }

    /**
     * Verifica se duas peças são iguais, com base em sua cor.
     * 
     * @param obj O objeto a ser comparado com a peça atual.
     * @return true se as peças forem iguais (mesma cor), false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Peca peca = (Peca) obj;
        return cor == peca.cor;
    }

    /**
     * Calcula o código hash para a peça, baseado na cor da peça.
     * 
     * @return O código hash da peça.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cor);
    }
    
    /**
     * Cria uma cópia da peça (clonagem).
     * 
     * @return Uma nova instância da peça clonada.
     */
    @Override
    public Peca clone() {
        try {
            return (Peca) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed, should not happen.");
        }
    }

    /**
     * Método abstrato que deve ser implementado por cada tipo de peça.
     * Calcula os possíveis movimentos de uma peça a partir de uma posição específica no tabuleiro.
     * 
     * @param tabuleiro O tabuleiro atual do jogo.
     * @param origem A posição de origem da peça no tabuleiro.
     * @return Uma lista de posições válidas para a peça se mover.
     */
    public abstract List<Posicao> possiveisMovimentos(Tabuleiro tabuleiro, Posicao origem);
}