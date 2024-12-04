package partida;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;


/**
 * Representa um movimento realizado por uma peça no tabuleiro de xadrez.
 * 
 * A classe {@code Movimento} armazena informações sobre um movimento específico,
 * incluindo a origem e o destino das casas, a peça movida e, se houver, a peça capturada.
 * A classe fornece métodos para aplicar o movimento no tabuleiro, desfazê-lo, 
 * validar se o movimento é legal e calcular os movimentos possíveis para uma peça.
 */
@XmlRootElement
public class Movimento {

    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;
    private Peca pecaCapturada;

    /**
     * Construtor padrão para a classe Movimento.
     */
    public Movimento(){
    }
    
    /**
     * Construtor que cria um movimento com as posições de origem e destino, e a peça movida.
     * 
     * @param origem A posição de origem do movimento.
     * @param destino A posição de destino do movimento.
     * @param pecaMovida A peça que será movida.
     */
    public Movimento(Posicao origem, Posicao destino, Peca pecaMovida) {
        this.origem = origem;
        this.destino = destino;
        this.pecaMovida = pecaMovida;
    }
    
    /**
     * Obtém a posição de origem do movimento.
     * 
     * @return A posição de origem.
     */
    @XmlElement(name = "origem")
    public Posicao getOrigem() {
        return origem;
    }

    /**
     * Define a posição de origem do movimento.
     * 
     * @param origem A posição de origem da peça no tabuleiro.
     */
    public void setOrigem(Posicao origem){
        this.origem = origem;
    }

    /**
     * Obtém a posição de destino do movimento.
     * 
     * @return A posição de destino.
     */
    @XmlElement(name = "destino")
    public Posicao getDestino() {
        return destino;
    }

    /**
     * Define a posição de destino do movimento.
     * 
     * @param destino A posição de destino da peça no tabuleiro.
     */
    public void setDestino(Posicao destino){
        this.destino = destino;
    }

    /**
     * Obtém a peça que foi movida neste movimento.
     * 
     * @return A peça movida.
     */
    @XmlElements({
        @XmlElement(name = "Peao", type = Peao.class),
        @XmlElement(name = "Cavalo", type = Cavalo.class),
        @XmlElement(name = "Rei", type = Rei.class),
        @XmlElement(name = "Torre", type = Torre.class),
        @XmlElement(name = "Bispo", type = Bispo.class),
        @XmlElement(name = "Rainha", type = Rainha.class)
    })
    public Peca getPecaMovida() {
        return pecaMovida;
    }

    /**
     * Define a peça que foi movida.
     * 
     * @param pecaMovida A peça que foi movida no movimento.
     */
    public void setPecaMovida(Peca pecaMovida){
        this.pecaMovida = pecaMovida;
    }

    /**
     * Obtém a peça que foi capturada neste movimento, se houver.
     * 
     * @return A peça capturada ou {@code null} se nenhuma peça foi capturada.
     */
    public Peca getPecaCapturada() {
        return pecaCapturada;
    }

    /**
     * Aplica o movimento no tabuleiro.
     * 
     * O movimento será validado e, em seguida, executado. A peça capturada, se houver, será removida do tabuleiro.
     * 
     * @param tabuleiro O tabuleiro onde o movimento será realizado.
     */
    public void aplicar(Tabuleiro tabuleiro) {
      validarMovimento(tabuleiro);
      Casa casaOrigem = tabuleiro.getCasa(origem);
      Casa casaDestino = tabuleiro.getCasa(destino);
      Peca pecaDestino = casaDestino.getPeca();
      if (pecaDestino != null && pecaDestino.getCor() != pecaMovida.getCor()) {
        capturarPeca(tabuleiro, destino);
      }
      casaOrigem.setPeca(null);
      casaDestino.setPeca(pecaMovida);
      pecaMovida.incrementarMovimento();
    }

    /**
     * Desfaz o movimento no tabuleiro, restaurando o estado anterior.
     * 
     * Se uma peça foi capturada, ela será restaurada no tabuleiro.
     * 
     * @param tabuleiro O tabuleiro onde o movimento será desfeito.
     */
    public void voltar(Tabuleiro tabuleiro) {
        Posicao origem = this.getOrigem();
        Posicao destino = this.getDestino();
        Peca pecaMovida = this.getPecaMovida();
        Casa casaOrigem = tabuleiro.getCasa(origem);
        Casa casaDestino = tabuleiro.getCasa(destino);
        casaDestino.setPeca(null);
        casaOrigem.setPeca(pecaMovida);
        if (pecaCapturada != null) {
            casaDestino.setPeca(pecaCapturada);
        }
        pecaMovida.decrementarMovimento();
    }
    
    /**
     * Captura uma peça no tabuleiro, removendo-a e armazenando-a como capturada.
     * 
     * @param tabuleiro O tabuleiro onde a peça será capturada.
     * @param destino A posição onde a peça será capturada.
     */
    private void capturarPeca(Tabuleiro tabuleiro, Posicao destino) {
        pecaCapturada = tabuleiro.obterPeca(destino);
        tabuleiro.adicionarPecaCapturada(tabuleiro.obterPeca(destino));
        tabuleiro.removerPeca(destino);
    }

    /**
     * Valida se o movimento realizado é legal, considerando as regras do jogo.
     * 
     * @param tabuleiro O tabuleiro onde o movimento será validado.
     * @return {@code true} se o movimento for válido, {@code false} caso contrário.
     */
    public boolean validarMovimento(Tabuleiro tabuleiro) {
        if (pecaMovida instanceof Rei) {
            Peca pecaDestino = tabuleiro.obterPeca(destino);
            
            if (pecaDestino != null) {
                if (pecaDestino.getCor() == pecaMovida.getCor()) {
                    return false;
                }
                if (!tabuleiro.isMovimentoSeguro(origem, destino, pecaMovida.getCor())) {
                    return false;
                }
            }
        } else {
            List<Posicao> destinosValidos = pecaMovida.possiveisMovimentos(tabuleiro, origem);
            destinosValidos.removeIf(destino -> {
                Peca pecaDestino = tabuleiro.obterPeca(destino);
                return pecaDestino != null && pecaDestino.getCor() == pecaMovida.getCor();
            });
    
            if (destinosValidos.isEmpty() || !destinosValidos.contains(destino)) {
                return false;
            }
            if (!tabuleiro.isMovimentoSeguro(origem, destino, pecaMovida.getCor())) {
                return false;
            }
            if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha || pecaMovida instanceof Bispo) {
                if (!caminhoLivre(tabuleiro, origem, destino)) {
                    return false;
                }
            }
        }
        return true;
    }    
    
    /**
     * Calcula os movimentos válidos possíveis para a peça movida no tabuleiro.
     * 
     * @param tabuleiro O tabuleiro onde os movimentos válidos serão calculados.
     * @return Uma lista de posições de destino válidas para a peça.
     */
    public List<Posicao> validarMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        List<Posicao> destinosValidos = pecaMovida.possiveisMovimentos(tabuleiro, origem);
        for (Posicao destino : destinosValidos) {
            if (validarMovimento(tabuleiro)) {
                Peca pecaDestino = tabuleiro.obterPeca(destino);
                if (pecaDestino == null || pecaDestino.getCor() != pecaMovida.getCor()) {
                    if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha || pecaMovida instanceof Bispo) {
                        if (caminhoLivre(tabuleiro, origem, destino) || pecaDestino != null) {
                        }
                    } else {
                        movimentosValidos.add(destino);
                    }
                }
            }
        }
        
        return movimentosValidos;
    }

    /**
     * Verifica se o caminho entre a origem e o destino está livre de peças, 
     * levando em consideração as peças que se movem em linha reta ou diagonal.
     * 
     * @param tabuleiro O tabuleiro onde o caminho será verificado.
     * @param origem A posição de origem do movimento.
     * @param destino A posição de destino do movimento.
     * @return {@code true} se o caminho estiver livre, {@code false} caso contrário.
     */
    private boolean caminhoLivre(Tabuleiro tabuleiro, Posicao origem, Posicao destino) {
        int origemLinha = origem.getLinha();
        int origemColuna = origem.getColuna();
        int destinoLinha = destino.getLinha();
        int destinoColuna = destino.getColuna();
    
        if (origemLinha == destinoLinha) {
            int passoColuna = destinoColuna > origemColuna ? 1 : -1;
            for (int i = origemColuna + passoColuna; i != destinoColuna; i += passoColuna) {
                if (tabuleiro.obterPeca(new Posicao(origemLinha, i)) != null) {
                    return false;
                }
            }
        } else if (origemColuna == destinoColuna) {
            int passoLinha = destinoLinha > origemLinha ? 1 : -1;
            for (int i = origemLinha + passoLinha; i != destinoLinha; i += passoLinha) {
                if (tabuleiro.obterPeca(new Posicao(i, origemColuna)) != null) {
                    return false;
                }
            }
        } else if (Math.abs(origemLinha - destinoLinha) == Math.abs(origemColuna - destinoColuna)) {
            int passoLinha = destinoLinha > origemLinha ? 1 : -1;
            int passoColuna = destinoColuna > origemColuna ? 1 : -1;
    
            int linhaAtual = origemLinha + passoLinha;
            int colunaAtual = origemColuna + passoColuna;
    
            while (linhaAtual != destinoLinha && colunaAtual != destinoColuna) {
                if (tabuleiro.obterPeca(new Posicao(linhaAtual, colunaAtual)) != null) {
                    return false;
                }
                linhaAtual += passoLinha;
                colunaAtual += passoColuna;
            }
        }
        return true;
    }

    /**
     * Compara se dois movimentos são iguais, verificando se a posição de origem, destino
     * e a peça movida são idênticas.
     * 
     * @param obj O objeto a ser comparado com o movimento atual.
     * @return {@code true} se os movimentos são iguais, caso contrário {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movimento movimento = (Movimento) obj;
        return origem.equals(movimento.origem) && 
               destino.equals(movimento.destino) && 
               pecaMovida.equals(movimento.pecaMovida);
    }
    
    /**
     * Gera um código hash único para o movimento, baseado na posição de origem, destino
     * e a peça movida.
     * 
     * @return O código hash gerado para o movimento.
     */
    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, pecaMovida);
    }
}