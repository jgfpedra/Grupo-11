package partida;

import java.util.ArrayList;
import java.util.List;
import pecas.Peca;
import pecas.Rei;
import pecas.Torre;

public class Tabuleiro {
    private List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;

    public Tabuleiro() {
        casas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Casa> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                Cor cor = (i + j) % 2 == 0 ? Cor.BRANCO : Cor.PRETO;  // Cor alternada para as casas
                Posicao posicao = new Posicao(i, j);
                row.add(new Casa(cor, posicao));  // Criação da casa
            }
            casas.add(row);
        }
        observadores = new ArrayList<>();
    }

    // Aplica o movimento (muda a peça de posição e realiza capturas, se houver)
    public void aplicarMovimento(Movimento movimento) {
        Posicao origem = movimento.getOrigem();
        Posicao destino = movimento.getDestino();
        Peca pecaMovida = movimento.getPecaMovida();

        // Verifica se o movimento é válido
        movimento.validarMovimento(this);

        // Captura a peça adversária, se houver
        Peca pecaDestino = obterPeca(destino);
        if (pecaDestino != null && pecaDestino.getCor() != pecaMovida.getCor()) {
            capturaPeca(destino);  // Captura a peça adversária
        }

        // Move a peça de origem para destino
        Casa casaOrigem = getCasa(origem);
        Casa casaDestino = getCasa(destino);
        casaOrigem.setPeca(null);  // Remove a peça de origem
        casaDestino.setPeca(pecaMovida);  // Coloca a peça no destino

        // Notifica os observadores sobre o movimento
        notificarObservador();
    }

    // Verifica se o movimento deixa o rei em check
    public boolean isReiEmCheck(Posicao posicaoRei, Cor corDoJogador) {
        Peca rei = obterPeca(posicaoRei);
        if (rei == null || !(rei instanceof Rei) || rei.getCor() != corDoJogador) {
            return false;
        }

        // Verifica se algum adversário pode atacar o rei
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() != corDoJogador && peca.podeAtacar(posicaoRei)) {
                    return true;  // Rei está em check
                }
            }
        }
        return false;
    }

    // Verifica se o movimento coloca o rei em check (sem alterar o tabuleiro permanentemente)
    public boolean isMovimentoSeguro(Posicao origem, Posicao destino, Cor corDoJogador) {
        // Aplica o movimento de forma temporária
        Peca pecaOrigem = obterPeca(origem);
        Peca pecaDestino = obterPeca(destino);

        aplicarMovimentoTemporario(origem, destino);

        // Verifica se o rei está em check após o movimento
        Posicao posicaoRei = getPosicaoRei(corDoJogador);
        boolean seguro = !isReiEmCheck(posicaoRei, corDoJogador);

        // Desfaz o movimento temporário
        desfazerMovimentoTemporario(origem, destino, pecaOrigem, pecaDestino);

        return seguro;
    }

    // Aplica o movimento temporário
    private void aplicarMovimentoTemporario(Posicao origem, Posicao destino) {
        Peca pecaMovida = obterPeca(origem);
        getCasa(origem).setPeca(null);  // Remove a peça da origem
        getCasa(destino).setPeca(pecaMovida);  // Coloca a peça no destino
    }

    // Desfaz o movimento temporário
    private void desfazerMovimentoTemporario(Posicao origem, Posicao destino, Peca pecaOrigem, Peca pecaDestino) {
        getCasa(origem).setPeca(pecaOrigem);  // Restaura a peça original na origem
        getCasa(destino).setPeca(pecaDestino);  // Restaura a peça original no destino
    }

    // Verifica se o movimento do roque é válido
    public boolean verificarRoque(Posicao origem, Posicao destino, Peca pecaMovida) {
        // Verifique se o movimento é de um rei ou torre
        if (pecaMovida instanceof Rei) {
            // Verifique as condições para o roque
            if (origem.getColuna() == 4 && (destino.getColuna() == 2 || destino.getColuna() == 6)) {
                // O rei moveu-se de d1 (ou e1) para c1 ou g1 (ou d8 ou g8)
                // Verifique se o rei ou a torre já se moveram e se as casas entre eles estão desocupadas
                // Verificação simplificada do roque:
                if (origem.getLinha() == 0) {  // Para a linha 1 (branca) ou linha 8 (preta)
                    if (destino.getColuna() == 2) {
                        return verificarRoqueEsquerda();
                    } else if (destino.getColuna() == 6) {
                        return verificarRoqueDireita();
                    }
                }
            }
        }
        return false;  // Caso o movimento não seja do roque
    }

    // Verificar roque à esquerda
    private boolean verificarRoqueEsquerda() {
        // Verifique se as casas entre o rei e a torre estão livres
        return true;
    }

    // Verificar roque à direita
    private boolean verificarRoqueDireita() {
        // Verifique se as casas entre o rei e a torre estão livres
        return true;
    }

    // Obtém a peça na casa especificada
    public Peca obterPeca(Posicao posicao) {
        return casas.get(posicao.getLinha()).get(posicao.getColuna()).getPeca();
    }

    // Obtém uma casa a partir da posição
    public Casa getCasa(Posicao posicao) {
        return casas.get(posicao.getLinha()).get(posicao.getColuna());
    }

    // Captura a peça na casa de destino
    public void capturaPeca(Posicao destino) {
        Casa casaDestino = getCasa(destino);
        Peca pecaCapturada = casaDestino.getPeca();
        if (pecaCapturada != null) {
            casaDestino.setPeca(null);  // Remove a peça capturada
        }
    }

    // Obtém a posição do rei de um jogador
    public Posicao getPosicaoRei(Cor corDoJogador) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = getCasa(new Posicao(i, j));
                Peca peca = casa.getPeca();
                if (peca instanceof Rei && peca.getCor() == corDoJogador) {
                    return casa.getPosicao();
                }
            }
        }
        return null;  // Rei não encontrado (não deveria acontecer)
    }

    // Notifica os observadores sobre o movimento
    public void notificarObservador() {
        for (ObservadorTabuleiro observador : observadores) {
            observador.atualizar();
        }
    }
}