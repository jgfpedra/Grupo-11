package partida;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pecas.Peca;
import pecas.Rei;
import pecas.Torre;

@XmlRootElement
public class Movimento {
    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;

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
    }

    protected boolean validarMovimento(Tabuleiro tabuleiro) {
        // Verifica se o movimento é válido para a peça (exemplo simplificado)
        List<Posicao> destinosValidos = pecaMovida.proxMovimento(origem);
        if (!destinosValidos.contains(destino)) {
            System.out.println("Movimento inválido: O destino não é válido para esta peça.");
            return false;  // Movimento inválido
        }
    
        // Verifica se o destino contém uma peça da mesma cor
        Peca pecaDestino = tabuleiro.obterPeca(destino);
        if (pecaDestino != null && pecaDestino.getCor() == pecaMovida.getCor()) {
            System.out.println("Movimento inválido: Não pode mover para uma casa ocupada por uma peça sua.");
            return false;  // Movimento inválido
        }
    
        // Verifica se o movimento coloca o rei em check (exemplo simplificado)
        if (!tabuleiro.isMovimentoSeguro(origem, destino, pecaMovida.getCor())) {
            System.out.println("Movimento inválido: O movimento coloca o seu rei em cheque.");
            return false;  // Movimento inválido
        }
    
        // Verifica se o movimento é um roque válido
        if (pecaMovida instanceof Rei) {
            if (!verificarRoque(tabuleiro)) {
                System.out.println("Movimento inválido: O roque não é permitido.");
                return false;  // Movimento inválido
            }
        }
    
        return true;  // Movimento válido
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
