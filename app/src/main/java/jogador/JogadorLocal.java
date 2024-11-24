package jogador;

import partida.Casa;
import partida.Cor;
import partida.Movimento;
import partida.Tabuleiro;
import pecas.Peca;
import partida.Posicao;

public class JogadorLocal extends Jogador {
    public JogadorLocal(){

    }
    public JogadorLocal(Cor cor, String nome){
        super(cor, nome);
    }
    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
        Posicao origem = tabuleiro.getOrigemSelecionada();
        Posicao destino = tabuleiro.getDestinoSelecionada();

        if (origem != null && destino != null) {
            // Cria o objeto Movimento
            Peca pecaMovida = tabuleiro.obterPeca(origem);
            Movimento movimento = new Movimento(origem, destino, pecaMovida);

            // Valida e aplica o movimento
            try {
                tabuleiro.aplicarMovimento(movimento);
            } catch (IllegalArgumentException e) {
                System.out.println("Movimento inválido: " + e.getMessage());
            }
        }

        // Reseta as seleções
        tabuleiro.setOrigemSelecionada(null);
        tabuleiro.setDestinoSelecionada(null);

        throw new UnsupportedOperationException("Unimplemented method 'escolherMovimento'");
    }

    @Override
    public void temPecas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'temPecas'");
    }

    public void processarClique(int linha, int coluna, Tabuleiro tabuleiro) {
    Casa casaClicada = tabuleiro.getCasa(new Posicao(linha, coluna));

    if (tabuleiro.getOrigemSelecionada() == null) {
        // Seleção de origem
        Peca pecaSelecionada = casaClicada.getPeca();
        if (pecaSelecionada != null && pecaSelecionada.getCor().equals(this.getCor())) {
            tabuleiro.setOrigemSelecionada(casaClicada.getPosicao());
        }
    } else {
        // Seleção de destino
        tabuleiro.setDestinoSelecionada(casaClicada.getPosicao());
        // Tenta realizar o movimento
        this.escolherMovimento(tabuleiro);
    }
}
}