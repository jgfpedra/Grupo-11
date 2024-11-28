package jogador;

import partida.Casa;
import partida.Cor;
import partida.Movimento;
import partida.Partida;
import pecas.Peca;
import partida.Posicao;

public class JogadorLocal extends Jogador {
    public JogadorLocal(){

    }
    public JogadorLocal(Cor cor, String nome){
        super(cor, nome, null);
    }
    
    @Override
    public void escolherMovimento(Partida partida) {
        Posicao origem = partida.getTabuleiro().getOrigemSelecionada();
        Posicao destino = partida.getTabuleiro().getDestinoSelecionada();

        if (origem != null && destino != null) {
            // Cria o objeto Movimento
            Peca pecaMovida = partida.getTabuleiro().obterPeca(origem);
            Movimento movimento = new Movimento(origem, destino, pecaMovida);

            // Valida e aplica o movimento
            try {
                partida.jogar(movimento);
            } catch (IllegalArgumentException e) {
                System.out.println("Movimento inválido: " + e.getMessage());
            }
        }

        // Reseta as seleções
        partida.getTabuleiro().setOrigemSelecionada(null);
        partida.getTabuleiro().setDestinoSelecionada(null);

        throw new UnsupportedOperationException("Unimplemented method 'escolherMovimento'");
    }

    @Override
    public void temPecas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPecas'");
    }

    public void processarClique(int linha, int coluna, Partida partida) {
    Casa casaClicada = partida.getTabuleiro().getCasa(new Posicao(linha, coluna));

    if (partida.getTabuleiro().getOrigemSelecionada() == null) {
        // Seleção de origem
        Peca pecaSelecionada = casaClicada.getPeca();
        if (pecaSelecionada != null && pecaSelecionada.getCor().equals(this.getCor())) {
            partida.getTabuleiro().setOrigemSelecionada(casaClicada.getPosicao());
        }
    } else {
        // Seleção de destino
        partida.getTabuleiro().setDestinoSelecionada(casaClicada.getPosicao());
        // Tenta realizar o movimento
        this.escolherMovimento(partida);
    }
}
}