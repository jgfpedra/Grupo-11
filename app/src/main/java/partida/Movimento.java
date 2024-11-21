package partida;

import java.util.List;

import pecas.Peca;
import pecas.Rei;
import pecas.Torre;

public class Movimento {
    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;

    public Movimento(Posicao origem, Posicao destino, Peca pecaMovida) {
        this.origem = origem;
        this.destino = destino;
        this.pecaMovida = pecaMovida;
    }

    public Posicao getOrigem() {
        return origem;
    }

    public Posicao getDestino() {
        return destino;
    }

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

    protected void validarMovimento(Tabuleiro tabuleiro) {
        // Verifica se o movimento é válido para a peça
        List<Posicao> destinosValidos = pecaMovida.proxMovimento(origem);  // Obtém os destinos válidos da peça
        if (!destinosValidos.contains(destino)) {
            throw new IllegalArgumentException("Movimento inválido: O destino não é válido para esta peça.");
        }

        // Verifica se o destino contém uma peça da mesma cor
        Peca pecaDestino = tabuleiro.obterPeca(destino);
        if (pecaDestino != null && pecaDestino.getCor() == pecaMovida.getCor()) {
            throw new IllegalArgumentException("Movimento inválido: Não pode mover para uma casa ocupada por uma peça sua.");
        }

        // Verifica se o movimento do roque é válido (se o movimento for do rei ou torre)
        if (pecaMovida instanceof Rei) {
            // Implementar verificação de roque, se necessário
            // Exemplo: Se for roque, verificar as condições do roque (torre e rei não se moveram, casas desocupadas, etc.)
            verificarRoque(tabuleiro);
        }
    }

    private void verificarRoque(Tabuleiro tabuleiro) {
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
                                // O movimento de roque pequeno é válido
                                return;
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
                                // O movimento de roque grande é válido
                                return;
                            }
                        }
                    }
                }
            }
        }
        
        // Caso contrário, se não for roque, lança um erro
        throw new IllegalArgumentException("Movimento inválido: As condições do roque não foram atendidas.");
    }
}
