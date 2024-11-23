package partida;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pecas.Bispo;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

@XmlRootElement
public class Movimento {
    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;

    public Movimento(){

    }
    public Movimento(Posicao origem, Posicao destino, Peca pecaMovida) {
        this.origem = origem;
        this.destino = destino;
        this.pecaMovida = pecaMovida;
    }
    @XmlElement
    public Posicao getOrigem() {
        return origem;
    }
    @XmlElement
    public Posicao getDestino() {
        return destino;
    }
    @XmlElement
    public Peca getPecaMovida() {
        return pecaMovida;
    }

    public void aplicar(Tabuleiro tabuleiro) {
        // Verifica se o movimento é válido
        validarMovimento(tabuleiro);

        // Aplica o movimento (troca de casas)
        Casa casaOrigem = tabuleiro.getCasa(origem);
        Casa casaDestino = tabuleiro.getCasa(destino);

        // Verifica se a casa de destino tem uma peça inimiga, e se sim, captura-a
        Peca pecaDestino = casaDestino.getPeca();
        if (pecaDestino != null && pecaDestino.getCor() != pecaMovida.getCor()) {
            tabuleiro.capturaPeca(destino);  // Captura a peça inimiga
        }

        // Move a peça da casa de origem para a casa de destino
        casaOrigem.setPeca(null);  // Remove a peça da origem
        casaDestino.setPeca(pecaMovida);  // Coloca a peça no destino
        pecaMovida.incrementarMovimento();
        System.out.println("MovCount: " + pecaMovida.getMovCount());
    }

    public List<Posicao> validarMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Posicao> movimentosValidos = new ArrayList<>();
        
        // Verifica os movimentos possíveis para a peça selecionada
        List<Posicao> destinosValidos = pecaMovida.proxMovimento(origem);
    
        // Para cada destino válido, verifica se o movimento é realmente válido
        for (Posicao destino : destinosValidos) {
            // Verifica se o movimento é válido
            if (validarMovimento(tabuleiro)) {
                // Verifica se o caminho está livre
                if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha) {
                    if (caminhoLivre(tabuleiro, origem, destino)) {
                        movimentosValidos.add(destino);
                    }
                } else if (pecaMovida instanceof Bispo) {
                    if (caminhoLivre(tabuleiro, origem, destino)) {
                        movimentosValidos.add(destino);
                    }
                } else {
                    // Para peças que não movem em linha reta ou diagonal, como o Cavalo
                    movimentosValidos.add(destino);
                }
            }
        }
        
        return movimentosValidos;
    }
    

    public boolean validarMovimento(Tabuleiro tabuleiro) {
        // Verifica se o movimento é válido para a peça (exemplo simplificado)
        List<Posicao> destinosValidos = pecaMovida.proxMovimento(origem);
        if (!destinosValidos.contains(destino)) {
            return false;  // Movimento inválido
        }
    
        // Verifica se o destino contém uma peça da mesma cor
        Peca pecaDestino = tabuleiro.obterPeca(destino);
        if (pecaDestino != null && pecaDestino.getCor() == pecaMovida.getCor()) {
            return false;  // Movimento inválido
        }
    
        // Verifica se o movimento coloca o rei em check
        if (!tabuleiro.isMovimentoSeguro(origem, destino, pecaMovida.getCor())) {
            return false;  // Movimento inválido
        }
    
        // Verifica se o movimento é um roque válido
        if (pecaMovida instanceof Rei) {
            if (!verificarRoque(tabuleiro)) {
                return false;  // Movimento inválido
            }
        }
    
        // Verifica se o caminho está livre para peças que movem em linha reta (como Torre, Bispo e Dama)
        if (pecaMovida instanceof Torre || pecaMovida instanceof Rainha) {
            if (!caminhoLivre(tabuleiro, origem, destino)) {
                System.out.println("Movimento inválido: Há peças bloqueando o caminho.");
                return false;  // Movimento inválido
            }
        }
    
        // Verifica se o caminho está livre para peças que se movem diagonalmente (como Bispo)
        if (pecaMovida instanceof Bispo) {
            if (!caminhoLivre(tabuleiro, origem, destino)) {
                System.out.println("Movimento inválido: Há peças bloqueando o caminho.");
                return false;  // Movimento inválido
            }
        }
    
        return true;  // Movimento válido
    }

    private boolean caminhoLivre(Tabuleiro tabuleiro, Posicao origem, Posicao destino) {
        int origemLinha = origem.getLinha();
        int origemColuna = origem.getColuna();
        int destinoLinha = destino.getLinha();
        int destinoColuna = destino.getColuna();

        // Movimentos horizontais ou verticais (Torre e Dama)
        if (origemLinha == destinoLinha) {  // Movimento horizontal
            int passoColuna = destinoColuna > origemColuna ? 1 : -1;
            for (int i = origemColuna + passoColuna; i != destinoColuna; i += passoColuna) {
                if (tabuleiro.obterPeca(new Posicao(origemLinha, i)) != null) {
                    return false;  // Há uma peça no caminho
                }
            }
        } else if (origemColuna == destinoColuna) {  // Movimento vertical
            int passoLinha = destinoLinha > origemLinha ? 1 : -1;
            for (int i = origemLinha + passoLinha; i != destinoLinha; i += passoLinha) {
                if (tabuleiro.obterPeca(new Posicao(i, origemColuna)) != null) {
                    return false;  // Há uma peça no caminho
                }
            }
        } 
        // Movimentos diagonais (Bispo e Dama)
        else if (Math.abs(origemLinha - destinoLinha) == Math.abs(origemColuna - destinoColuna)) {  // Movimento diagonal
            int passoLinha = destinoLinha > origemLinha ? 1 : -1;
            int passoColuna = destinoColuna > origemColuna ? 1 : -1;

            int linhaAtual = origemLinha + passoLinha;
            int colunaAtual = origemColuna + passoColuna;

            while (linhaAtual != destinoLinha && colunaAtual != destinoColuna) {
                if (tabuleiro.obterPeca(new Posicao(linhaAtual, colunaAtual)) != null) {
                    return false;  // Há uma peça no caminho
                }
                linhaAtual += passoLinha;
                colunaAtual += passoColuna;
            }
        }

        return true;  // Caminho livre
    }

    private boolean verificarRoque(Tabuleiro tabuleiro) {
        // Verificar se o movimento é do rei
        if (pecaMovida instanceof Rei) {
            // Obtém a posição do rei
            Posicao posicaoRei = origem;
    
            // Verifique se é um movimento de roque
            if (posicaoRei.getColuna() == 4) {  // O rei está na posição inicial (coluna 4)
                if (destino.getColuna() == 6) {  // Roque pequeno (movendo o rei para a direita)
                    // Verificar se a torre não se moveu e se as casas estão desocupadas
                    Peca torre = tabuleiro.obterPeca(new Posicao(0, 7));  // Torre do lado direito
                    if (torre != null && torre instanceof Torre) {
                        // Verificar se as casas entre o rei e a torre estão desocupadas
                        if (tabuleiro.obterPeca(new Posicao(0, 5)) == null && tabuleiro.obterPeca(new Posicao(0, 6)) == null) {
                            // Verificar se o rei não passa por uma casa atacada
                            if (!tabuleiro.isReiEmCheck(destino, pecaMovida.getCor())) {
                                return true;  // O movimento de roque pequeno é válido
                            }
                        }
                    }
                } else if (destino.getColuna() == 2) {  // Roque grande (movendo o rei para a esquerda)
                    // Verificar as condições para o roque grande
                    Peca torre = tabuleiro.obterPeca(new Posicao(0, 0));  // Torre do lado esquerdo
                    if (torre != null && torre instanceof Torre) {
                        // Verificar se as casas entre o rei e a torre estão desocupadas
                        if (tabuleiro.obterPeca(new Posicao(0, 1)) == null && tabuleiro.obterPeca(new Posicao(0, 2)) == null && tabuleiro.obterPeca(new Posicao(0, 3)) == null) {
                            // Verificar se o rei não passa por uma casa atacada
                            if (!tabuleiro.isReiEmCheck(destino, pecaMovida.getCor())) {
                                return true;  // O movimento de roque grande é válido
                            }
                        }
                    }
                }
            }
        }
        return false;  // O movimento não é um roque válido
    }    
}
