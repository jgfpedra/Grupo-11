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

    public boolean isReiEmCheck(Posicao posicaoRei, Cor corDoJogador) {
        // Obtém o rei da posição especificada
        Peca rei = obterPeca(posicaoRei);
        
        // Verifica se a peça é um rei e se ele é da cor do jogador
        if (rei == null || !(rei instanceof Rei) || rei.getCor() != corDoJogador) {
            return false;  // Não é o rei ou é de outra cor, então não está em check
        }
    
        // Verifica todas as peças adversárias no tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
    
                // Se houver uma peça adversária na casa
                if (peca != null && peca.getCor() != corDoJogador) {
                    // Verifica se a peça adversária pode alcançar a posição do rei
                    // Isso verifica se o movimento da peça pode atingir o rei
                    // (usando o método de validação já implementado em Movimento)
                    if (peca.proxMovimento(posicao).contains(posicaoRei)) {
                        return true;  // O rei está em check
                    }
                }
            }
        }
        
        return false;  // O rei não está em check
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

    // Verificar roque à esquerda
    private boolean verificarRoqueEsquerda() {
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

    // Verificar roque à direita
    private boolean verificarRoqueDireita() {
        // Verifique se as casas entre o rei e a torre estão livres
        return true;
    }

    public boolean temMovimentosValidosParaSairDoCheck(Cor corDoJogador) {        
        // Para todas as peças do jogador, verifique se há um movimento válido para sair do check
        for (Posicao posicaoOrigem : getPosicoesComPecas(corDoJogador)) {
            for (Posicao destino : getPossiveisDestinos(posicaoOrigem)) {
                // Verifica se o movimento é seguro (não deixa o rei em check)
                if (isMovimentoSeguro(posicaoOrigem, destino, corDoJogador)) {
                    return true;  // Se encontrar um movimento que não resulta em check, retorna true
                }
            }
        }
        return false;  // Se não encontrar nenhum movimento seguro, é checkmate
    }

    public List<Posicao> getPosicoesComPecas(Cor corDoJogador) {
        List<Posicao> posicoesComPecas = new ArrayList<>();
    
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = getCasa(new Posicao(i, j));
                Peca peca = casa.getPeca();
                
                if (peca != null && peca.getCor() == corDoJogador) {
                    posicoesComPecas.add(casa.getPosicao());
                }
            }
        }
        return posicoesComPecas;
    }

    public List<Posicao> getPossiveisDestinos(Posicao posicaoOrigem) {
        Peca peca = obterPeca(posicaoOrigem);
        
        if (peca != null) {
            // Use a lógica da peça para obter seus possíveis destinos
            return peca.proxMovimento(posicaoOrigem);
        }
        return new ArrayList<>();
    }
    
}