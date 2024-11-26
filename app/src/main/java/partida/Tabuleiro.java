package partida;

import java.util.ArrayList;
import java.util.List;

import jogador.JogadorIA;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

public class Tabuleiro {
    public static List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;
    private List<Peca> pecasCapturadasBrancas;
    private List<Peca> pecasCapturadasPretas;
    private Posicao origemSelecionada;
    private Posicao destinoSelecionada;

    public Tabuleiro() {
        casas = new ArrayList<>();
        pecasCapturadasBrancas = new ArrayList<>();
        pecasCapturadasPretas = new ArrayList<>();

        // Inicializando as casas do tabuleiro
        for (int i = 0; i < 8; i++) {
            List<Casa> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                Cor cor = (i + j) % 2 == 0 ? Cor.BRANCO : Cor.PRETO;
                Posicao posicao = new Posicao(i, j);
                row.add(new Casa(cor, posicao));
            }
            casas.add(row);
        }

        // Inicializando as peças brancas e pretas
        for (int i = 0; i < 8; i++) {
            // Peões
            casas.get(6).get(i).setPeca(new Peao(Cor.BRANCO));
            casas.get(1).get(i).setPeca(new Peao(Cor.PRETO));
        }

        // Torres
        casas.get(7).get(0).setPeca(new Torre(Cor.BRANCO));
        casas.get(7).get(7).setPeca(new Torre(Cor.BRANCO));
        casas.get(0).get(0).setPeca(new Torre(Cor.PRETO));
        casas.get(0).get(7).setPeca(new Torre(Cor.PRETO));

        // Cavalos
        casas.get(7).get(1).setPeca(new Cavalo(Cor.BRANCO));
        casas.get(7).get(6).setPeca(new Cavalo(Cor.BRANCO));
        casas.get(0).get(1).setPeca(new Cavalo(Cor.PRETO));
        casas.get(0).get(6).setPeca(new Cavalo(Cor.PRETO));

        // Bispos
        casas.get(7).get(2).setPeca(new Bispo(Cor.BRANCO));
        casas.get(7).get(5).setPeca(new Bispo(Cor.BRANCO));
        casas.get(0).get(2).setPeca(new Bispo(Cor.PRETO));
        casas.get(0).get(5).setPeca(new Bispo(Cor.PRETO));

        // Rainhas
        casas.get(7).get(3).setPeca(new Rainha(Cor.BRANCO));
        casas.get(0).get(3).setPeca(new Rainha(Cor.PRETO));

        // Reis
        casas.get(7).get(4).setPeca(new Rei(Cor.BRANCO));
        casas.get(0).get(4).setPeca(new Rei(Cor.PRETO));
        observadores = new ArrayList<>();
    }

    public void aplicarMovimento(Movimento movimento) {
        movimento.aplicar(this);
        notificarObservadores();
    }

    public void desfazerMovimento(Movimento ultimoMovimento) {
        // Primeiro, executamos a ação de voltar o movimento (reverter a mudança de
        // posições)
        ultimoMovimento.voltar(this);

        // Se houve uma captura, restauramos a peça capturada
        Peca pecaCapturada = ultimoMovimento.getPecaCapturada();
        if (pecaCapturada != null) {
            // Restaurar a peça capturada
            Posicao posicaoCaptura = ultimoMovimento.getDestino(); // A casa onde a peça foi capturada
            getCasa(posicaoCaptura).setPeca(pecaCapturada); // Coloca a peça capturada de volta na casa

            // Agora removemos a peça da lista de capturadas
            if (pecaCapturada.getCor() == Cor.BRANCO) {
                pecasCapturadasBrancas.remove(pecaCapturada); // Remove da lista de capturadas brancas
            } else if (pecaCapturada.getCor() == Cor.PRETO) {
                pecasCapturadasPretas.remove(pecaCapturada); // Remove da lista de capturadas pretas
            }
        }

        // Atualiza os observadores do tabuleiro após a reversão
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
                        return true; // O rei está em check
                    }
                }
            }
        }
        return false; // O rei não está em check
    }

    // Verifica se o movimento coloca o rei em check
    public boolean isMovimentoSeguro(Posicao origem, Posicao destino, Cor corDoJogador) {
        Peca pecaOrigem = obterPeca(origem);
        Peca pecaDestino = obterPeca(destino);

        // Aplica o movimento temporário
        aplicarMovimentoTemporario(origem, destino);

        // Verifica se o rei está em check
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

    public void removerPeca(Posicao posicao) {
        Casa casa = getCasa(posicao);
        if (casa != null) {
            casa.setPeca(null); // Remove a peça da casa
        }
    }

    public Peca obterPeca(Posicao posicao) {
        Peca peca = casas.get(posicao.getLinha()).get(posicao.getColuna()).getPeca();
        return peca;
    }

    public Casa getCasa(Posicao posicao) {
        Casa casa = casas.get(posicao.getLinha()).get(posicao.getColuna());
        return casa;
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
        return null; // Rei não encontrado (não deveria acontecer)
    }

    // Verifica se o jogador tem movimentos válidos para sair do check
    public boolean temMovimentosValidosParaSairDoCheck(Cor cor) {
        Posicao posicaoRei = getPosicaoRei(cor);

        // Se o rei não estiver na posição esperada (deve estar sempre no tabuleiro)
        if (posicaoRei == null) {
            return false; // O rei não foi encontrado, o que não deveria acontecer
        }

        // Verifica se o rei está em check
        if (!isReiEmCheck(posicaoRei, cor)) {
            return true; // Se o rei não está em check, então o jogador já não precisa se preocupar com
                         // isso
        }

        // Para cada peça do jogador, verifica se algum movimento pode tirar o rei do
        // check
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() == cor) {
                    // Para cada peça, verifica se ela pode fazer um movimento que saia do check
                    for (Posicao destino : peca.proxMovimento(posicao)) {
                        // Se o movimento for válido e não deixar o rei em check
                        if (isMovimentoSeguro(posicao, destino, cor)) {
                            return true; // Existe um movimento válido para sair do check
                        }
                    }
                }
            }
        }

        // Se nenhum movimento for encontrado que tire o rei do check, retorna falso
        return false;
    }

    public Peca getUltimaPecaCapturada(Cor cor) {
        // Verifica qual cor foi passada como argumento
        if (cor == Cor.BRANCO && !pecasCapturadasBrancas.isEmpty()) {
            // Retorna a última peça capturada das brancas
            return pecasCapturadasBrancas.get(pecasCapturadasBrancas.size() - 1);
        } else if (cor == Cor.PRETO && !pecasCapturadasPretas.isEmpty()) {
            // Retorna a última peça capturada das pretas
            return pecasCapturadasPretas.get(pecasCapturadasPretas.size() - 1);
        }

        // Se não houver peças capturadas para a cor especificada, retorna null
        return null;
    }

    public Posicao getOrigemSelecionada() {
        return origemSelecionada;
    }

    public void setOrigemSelecionada(Posicao origemSelecionada) {
        this.origemSelecionada = origemSelecionada;
    }

    public Posicao getDestinoSelecionada() {
        return destinoSelecionada;
    }

    public List<Posicao> getPosicoesPecas() {
        List<Posicao> posicoesPecas = new ArrayList<>();

        // Itera sobre todas as casas do tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j); // Cria uma posição para a casa (i, j)
                Peca peca = obterPeca(posicao); // Obtém a peça na casa

                // Se houver uma peça na casa, adiciona a posição à lista
                if (peca != null) {
                    posicoesPecas.add(posicao);
                }
            }
        }

        return posicoesPecas;
    }

    public void setDestinoSelecionada(Posicao destinoSelecionada) {
        this.destinoSelecionada = destinoSelecionada;
    }

    public List<Movimento> getPossiveisMovimentos(JogadorIA jogadorIA) {
        List<Movimento> movimentos = new ArrayList<>(); // Lista para armazenar os movimentos válidos

        // Iterar sobre todas as casas do tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao); // Obtém a peça na posição (i, j)

                // Verifica se a peça não é nula e é do jogador IA
                if (peca != null && peca.getCor() == jogadorIA.getCor()) {
                    List<Posicao> movimentosPeca = peca.proxMovimento(posicao); // Obtém as posições possíveis para a
                                                                                // peça

                    // Para cada posição possível, criar um movimento e verificar se é seguro
                    for (Posicao destino : movimentosPeca) {
                        Movimento movimento = new Movimento(posicao, destino, peca); // Cria o movimento com a peça,
                                                                                     // origem e destino

                        // Verificar se o movimento não coloca o rei do jogador em check
                        if (isMovimentoSeguro(movimento.getOrigem(), movimento.getDestino(), jogadorIA.getCor())) {
                            movimentos.add(movimento); // Adiciona o movimento à lista de movimentos válidos
                        }
                    }
                }
            }
        }

        return movimentos; // Retorna a lista de movimentos válidos
    }

    private void aplicarMovimentoTemporario(Posicao origem, Posicao destino) {
        Peca pecaMovida = obterPeca(origem);
        getCasa(origem).setPeca(null); // Remove a peça da origem
        getCasa(destino).setPeca(pecaMovida); // Coloca a peça no destino
    }

    private void desfazerMovimentoTemporario(Posicao origem, Posicao destino, Peca pecaOrigem, Peca pecaDestino) {
        getCasa(origem).setPeca(pecaOrigem); // Restaura a peça original na origem
        getCasa(destino).setPeca(pecaDestino); // Restaura a peça original no destino
    }

    protected void adicionarPecaCapturada(Peca pecaCapturada) {
        // Verifica a cor da peça capturada
        if (pecaCapturada.getCor() == Cor.BRANCO) {
            pecasCapturadasBrancas.add(pecaCapturada);
        } else if (pecaCapturada.getCor() == Cor.PRETO) {
            pecasCapturadasPretas.add(pecaCapturada);
        }
    }

    public List<Peca> getCapturadasJogadorPreto() {
        return pecasCapturadasBrancas;
    }

    public List<Peca> getCapturadasJogadorBranco() {
        return pecasCapturadasPretas;
    }
}