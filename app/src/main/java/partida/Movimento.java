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
        return pecaCapturada;
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
        tabuleiro.removerPeca(destino);
    }

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