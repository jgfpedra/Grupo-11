package partida;

import java.util.List;

import pecas.Peca;

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

        // Notifica os observadores após o movimento
        tabuleiro.notificarObservador();
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
        // Exemplo de verificação simples para o roque, baseada na movimentação do rei e da torre
        // Verifique as condições para o roque aqui (se necessário)
        // Caso contrário, removê-lo ou lançá-lo em um erro
    }
}
