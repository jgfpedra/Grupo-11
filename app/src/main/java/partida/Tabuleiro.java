package partida;

import java.util.ArrayList;
import java.util.List;

import pecas.Peca;
import pecas.Rei;

public class Tabuleiro {
    private List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;  // Lista de observadores
    private List<Peca> pecasCapturadasBrancas;
    private List<Peca> pecasCapturadasPretas;

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
        notificarObservadores();
    }

    // Verifica se o rei está em check
    public boolean isReiEmCheck(Posicao posicaoRei, Cor corDoJogador) {
        Peca rei = obterPeca(posicaoRei);
        if (rei == null || !(rei instanceof Rei) || rei.getCor() != corDoJogador) {
            return false;
        }

        // Verifica se alguma peça adversária pode atacar o rei
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() != corDoJogador) {
                    if (peca.proxMovimento(posicao).contains(posicaoRei)) {
                        return true;  // O rei está em check
                    }
                }
            }
        }
        return false;  // O rei não está em check
    }

    // Verifica se o movimento coloca o rei em check
    public boolean isMovimentoSeguro(Posicao origem, Posicao destino, Cor corDoJogador) {
        Peca pecaOrigem = obterPeca(origem);
        Peca pecaDestino = obterPeca(destino);

        // Aplica o movimento temporário para verificação
        aplicarMovimentoTemporario(origem, destino);

        Posicao posicaoRei = getPosicaoRei(corDoJogador);
        boolean seguro = !isReiEmCheck(posicaoRei, corDoJogador);

        // Desfaz o movimento temporário
        desfazerMovimentoTemporario(origem, destino, pecaOrigem, pecaDestino);

        return seguro;
    }

    // Adiciona um observador à lista
    public void adicionarObservador(ObservadorTabuleiro observador) {
        observadores.add(observador);
    }

    // Remove um observador da lista
    public void removerObservador(ObservadorTabuleiro observador) {
        observadores.remove(observador);
    }

    // Notifica todos os observadores sobre o movimento
    private void notificarObservadores() {
        for (ObservadorTabuleiro observador : observadores) {
            observador.atualizar();
        }
    }

    // Métodos auxiliares (já existentes no código original)
    private void aplicarMovimentoTemporario(Posicao origem, Posicao destino) {
        Peca pecaMovida = obterPeca(origem);
        getCasa(origem).setPeca(null);  // Remove a peça da origem
        getCasa(destino).setPeca(pecaMovida);  // Coloca a peça no destino
    }

    private void desfazerMovimentoTemporario(Posicao origem, Posicao destino, Peca pecaOrigem, Peca pecaDestino) {
        getCasa(origem).setPeca(pecaOrigem);  // Restaura a peça original na origem
        getCasa(destino).setPeca(pecaDestino);  // Restaura a peça original no destino
    }

    // Obtém a peça na casa especificada
    public Peca obterPeca(Posicao posicao) {
        return casas.get(posicao.getLinha()).get(posicao.getColuna()).getPeca();
    }

    // Obtém uma casa a partir da posição
    public Casa getCasa(Posicao posicao) {
        return casas.get(posicao.getLinha()).get(posicao.getColuna());
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
    // Método para capturar uma peça
    public void capturaPeca(Posicao destino) {
        Casa casaDestino = getCasa(destino);
        Peca pecaCapturada = casaDestino.getPeca();
        if (pecaCapturada != null) {
            // Remove a peça da casa e adiciona à lista de capturadas
            casaDestino.setPeca(null);
            if (pecaCapturada.getCor() == Cor.BRANCO) {
                pecasCapturadasBrancas.add(pecaCapturada);
            } else {
                pecasCapturadasPretas.add(pecaCapturada);
            }
        }
    }

    public boolean temMovimentosValidosParaSairDoCheck(Cor cor) {
        // Obtém a posição do rei do jogador
        Posicao posicaoRei = getPosicaoRei(cor);
        
        // Se o rei não estiver na posição esperada (deve estar sempre no tabuleiro)
        if (posicaoRei == null) {
            return false; // O rei não foi encontrado, o que não deveria acontecer
        }
    
        // Verifica se o rei está em check
        if (!isReiEmCheck(posicaoRei, cor)) {
            return true;  // Se o rei não está em check, então o jogador já não precisa se preocupar com isso
        }
    
        // Para cada peça do jogador, verifica se algum movimento pode tirar o rei do check
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() == cor) {
                    // Para cada peça, verifica se ela pode fazer um movimento que saia do check
                    for (Posicao destino : peca.proxMovimento(posicao)) {
                        // Se o movimento for válido e não deixar o rei em check
                        if (isMovimentoSeguro(posicao, destino, cor)) {
                            return true;  // Existe um movimento válido para sair do check
                        }
                    }
                }
            }
        }
    
        // Se nenhum movimento for encontrado que tire o rei do check, retorna falso
        return false;
    }
    

    public void desfazerMovimento(Movimento ultimoMovimento) {
        Posicao origem = ultimoMovimento.getOrigem();
        Posicao destino = ultimoMovimento.getDestino();
        Peca pecaMovida = ultimoMovimento.getPecaMovida();
        Peca pecaCapturada = ultimoMovimento.getPecaCapturada();
    
        // Restaura a peça movida à sua posição original
        Casa casaOrigem = getCasa(origem);
        Casa casaDestino = getCasa(destino);
        casaDestino.setPeca(null);  // Remove a peça da posição de destino
        casaOrigem.setPeca(pecaMovida);  // Coloca a peça de volta na origem
    
        // Se uma peça foi capturada, restaura-a na posição de destino
        if (pecaCapturada != null) {
            casaDestino.setPeca(pecaCapturada);  // Restaura a peça capturada
        }
    
        // Notifica os observadores após desfazer o movimento
        notificarObservadores();
    }
}