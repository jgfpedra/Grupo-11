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
import test.CaminhoBloqueadoException;
import test.MovimentoInvalidoException;
import test.ReiEmCheckException;
import test.RoqueInvalidoException;

@XmlRootElement
public class Movimento {
    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;
    private Peca pecaCapturada;

    public Movimento(){

    }
    
    public Movimento(Posicao origem, Posicao destino, Peca pecaMovida) {
        this.origem = origem;
        this.destino = destino;
        this.pecaMovida = pecaMovida;
    }
    
    @XmlElement(name = "origem")
    public Posicao getOrigem() {
        return origem;
    }

    public void setOrigem(Posicao origem){
        this.origem = origem;
    }

    @XmlElement(name = "destino")
    public Posicao getDestino() {
        return destino;
    }

    public void setDestino(Posicao destino){
        this.destino = destino;
    }

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

    public void setPecaMovida(Peca pecaMovida){
        this.pecaMovida = pecaMovida;
    }
    
    public Peca getPecaCapturada() {
        return pecaCapturada;  // Retorna a peça capturada, se houver
    }

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
    

    private void capturarPeca(Tabuleiro tabuleiro, Posicao destino) {
        pecaCapturada = tabuleiro.obterPeca(destino);  
        tabuleiro.adicionarPecaCapturada(tabuleiro.obterPeca(destino));
        tabuleiro.removerPeca(destino);  // O método `removerPeca` deve ser implementado no Tabuleiro
    }

    public boolean validarMovimento(Tabuleiro tabuleiro) {
        List<Posicao> destinosValidos = pecaMovida.possiveisMovimentos(tabuleiro, origem);
        destinosValidos.removeIf(destino -> {
            Peca pecaDestino = tabuleiro.obterPeca(destino);
            return pecaDestino != null && pecaDestino.getCor() == pecaMovida.getCor(); 
        });
        if (destinosValidos.isEmpty() || !destinosValidos.contains(destino)) {
            throw new MovimentoInvalidoException("Movimento inválido para a peça.");
        }
        if (!tabuleiro.isMovimentoSeguro(origem, destino, pecaMovida.getCor())) {
            throw new ReiEmCheckException("O movimento coloca o rei em check.");
        }
        if (pecaMovida instanceof Rei) {
            if (!verificarRoque(tabuleiro)) {
                throw new RoqueInvalidoException("Movimento de roque inválido.");
            }
        }
        if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha || pecaMovida instanceof Bispo) {
            if (!caminhoLivre(tabuleiro, origem, destino)) {
                throw new CaminhoBloqueadoException("O caminho da peça está bloqueado.");
            }
        }
        return true;
    }     

    public List<Posicao> validarMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Verifica os movimentos possíveis para a peça selecionada
        List<Posicao> destinosValidos = pecaMovida.possiveisMovimentos(tabuleiro, origem);
        
        // Para cada destino válido, verifica se o movimento é realmente válido
        for (Posicao destino : destinosValidos) {
            // Verifica se o movimento é válido
            if (validarMovimento(tabuleiro)) {
                Peca pecaDestino = tabuleiro.obterPeca(destino);
                
                // Se a casa de destino estiver vazia ou contiver uma peça inimiga, é um movimento válido
                if (pecaDestino == null || pecaDestino.getCor() != pecaMovida.getCor()) {
                    // Verifica se o caminho está livre ou a captura é possível
                    if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha || pecaMovida instanceof Bispo) {
                        if (caminhoLivre(tabuleiro, origem, destino) || pecaDestino != null) {
                            movimentosValidos.add(destino);  // Permite a captura como um movimento válido
                        }
                    } else {
                        movimentosValidos.add(destino);
                    }
                }
            }
        }
        
        return movimentosValidos;
    }    

    private boolean caminhoLivre(Tabuleiro tabuleiro, Posicao origem, Posicao destino) {
        int origemLinha = origem.getLinha();
        int origemColuna = origem.getColuna();
        int destinoLinha = destino.getLinha();
        int destinoColuna = destino.getColuna();
    
        // Movimentos horizontais ou verticais (Torre e Dama)
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
    
    private boolean verificarRoque(Tabuleiro tabuleiro) {
        if (pecaMovida instanceof Rei) {
            Posicao posicaoRei = origem;
    
            // Verifique se é um movimento de roque
            if (posicaoRei.getColuna() == 4) {  // O rei está na posição inicial (coluna 4)
                if (destino.getColuna() == 6) {  // Roque pequeno (movendo o rei para a direita)
                    // Verificar se a torre não se moveu e se as casas estão desocupadas
                    Peca torre = tabuleiro.obterPeca(new Posicao(0, 7));  // Torre do lado direito
                    if (torre != null && torre instanceof Torre && torre.getMovCount() == 0) {
                        // Verificar se as casas entre o rei e a torre estão desocupadas
                        if (tabuleiro.obterPeca(new Posicao(0, 5)) == null && tabuleiro.obterPeca(new Posicao(0, 6)) == null) {
                            // Verificar se o rei não passa por uma casa atacada e se não está em check
                            if (!tabuleiro.isReiEmCheck(new Posicao(0, 4), pecaMovida.getCor()) && 
                                !tabuleiro.isReiEmCheck(new Posicao(0, 5), pecaMovida.getCor()) && 
                                !tabuleiro.isReiEmCheck(destino, pecaMovida.getCor())) {
                                return true;  // O movimento de roque pequeno é válido
                            }
                        }
                    }
                } else if (destino.getColuna() == 2) {  // Roque grande (movendo o rei para a esquerda)
                    // Verificar as condições para o roque grande
                    Peca torre = tabuleiro.obterPeca(new Posicao(0, 0));  // Torre do lado esquerdo
                    if (torre != null && torre instanceof Torre && torre.getMovCount() == 0) {
                        // Verificar se as casas entre o rei e a torre estão desocupadas
                        if (tabuleiro.obterPeca(new Posicao(0, 1)) == null && 
                            tabuleiro.obterPeca(new Posicao(0, 2)) == null && 
                            tabuleiro.obterPeca(new Posicao(0, 3)) == null) {
                            // Verificar se o rei não passa por uma casa atacada e se não está em check
                            if (!tabuleiro.isReiEmCheck(new Posicao(0, 4), pecaMovida.getCor()) &&
                                !tabuleiro.isReiEmCheck(new Posicao(0, 3), pecaMovida.getCor()) && 
                                !tabuleiro.isReiEmCheck(destino, pecaMovida.getCor())) {
                                return true;  // O movimento de roque grande é válido
                            }
                        }
                    }
                }
            }
        }
        return false;  // O movimento não é um roque válido
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movimento movimento = (Movimento) obj;
        return origem.equals(movimento.origem) && 
               destino.equals(movimento.destino) && 
               pecaMovida.equals(movimento.pecaMovida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, pecaMovida);
    }
}
