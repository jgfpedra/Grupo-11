package partida;

import java.util.List;

import pecas.Peca;

public class Movimento {
    private Posicao origem;
    private Posicao destino;
    private Peca pecaMovida;

    public Movimento(Posicao origem, Posicao destino, Peca pecaMovida){
        this.origem = origem;
        this.pecaMovida = pecaMovida;

        List<Posicao> destValidos = pecaMovida.proxMovimento(origem);

        if (destValidos.contains(destino)) {
            this.destino = destino;  // If valid, set the destination
        } else {
            throw new IllegalArgumentException("Invalid move: The destination is not valid for this piece.");
        }
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
}
