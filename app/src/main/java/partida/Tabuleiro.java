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

public class Tabuleiro implements Cloneable{
    private List<List<Casa>> casas;
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
        notificarObservadores();
    }

    public boolean isReiEmCheck(Posicao posicaoRei, Cor corDoJogador) {
        Peca rei = obterPeca(posicaoRei);
        if (rei == null || !(rei instanceof Rei) || rei.getCor() != corDoJogador) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() != corDoJogador) {
                    if (peca.possiveisMovimentos(this, posicao).contains(posicaoRei)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isMovimentoSeguro(Posicao origem, Posicao destino, Cor corDoJogador) {
        Peca pecaOrigem = obterPeca(origem);
        Peca pecaDestino = obterPeca(destino);

        aplicarMovimentoTemporario(origem, destino);

        Posicao posicaoRei = getPosicaoRei(corDoJogador);
        boolean seguro = !isReiEmCheck(posicaoRei, corDoJogador);

        desfazerMovimentoTemporario(origem, destino, pecaOrigem, pecaDestino);

        return seguro;
    }

    public void adicionarObservador(ObservadorTabuleiro observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorTabuleiro observador) {
        observadores.remove(observador);
    }

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
        return null;
    }
    public boolean temMovimentosValidosParaSairDoCheck(Cor cor) {
        Posicao posicaoRei = getPosicaoRei(cor);
        if (posicaoRei == null) {
            return false;
        }
        if (!isReiEmCheck(posicaoRei, cor)) {
            return true;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() == cor) {
                    for (Posicao destino : peca.possiveisMovimentos(this, posicao)) {
                        if (isMovimentoSeguro(posicao, destino, cor)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Peca getUltimaPecaCapturada(Cor cor) {
        if (cor == Cor.BRANCO && !pecasCapturadasBrancas.isEmpty()) {
            return pecasCapturadasBrancas.get(pecasCapturadasBrancas.size() - 1);
        } else if (cor == Cor.PRETO && !pecasCapturadasPretas.isEmpty()) {
            return pecasCapturadasPretas.get(pecasCapturadasPretas.size() - 1);
        }
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
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
        List<Movimento> movimentos = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca peca = obterPeca(posicao);
                if (peca != null && peca.getCor() == jogadorIA.getCor()) {
                    List<Posicao> movimentosPeca = peca.possiveisMovimentos(this, posicao);
                    for (Posicao destino : movimentosPeca) {
                        Movimento movimento = new Movimento(posicao, destino, peca);
                        if (isMovimentoSeguro(movimento.getOrigem(), movimento.getDestino(), jogadorIA.getCor())) {
                            movimentos.add(movimento);
                        }
                    }
                }
            }
        }
        return movimentos;
    }

    private void aplicarMovimentoTemporario(Posicao origem, Posicao destino) {
        Peca pecaMovida = obterPeca(origem);
        getCasa(origem).setPeca(null);
        getCasa(destino).setPeca(pecaMovida);
    }

    private void desfazerMovimentoTemporario(Posicao origem, Posicao destino, Peca pecaOrigem, Peca pecaDestino) {
        getCasa(origem).setPeca(pecaOrigem);
        getCasa(destino).setPeca(pecaDestino);
    }

    protected void adicionarPecaCapturada(Peca pecaCapturada) {
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

    public void setCapturadasJogadorBranco(List<Peca> pecasCapturadasBrancas){
        this.pecasCapturadasBrancas = pecasCapturadasBrancas;
    }

    public void setCapturadasJogadorPreto(List<Peca> pecasCapturadasPretas){
        this.pecasCapturadasPretas = pecasCapturadasPretas;
    }

    public List<List<Casa>> getCasas(){
        return casas;
    }

    public void limparTabuleiro() {
        for (List<Casa> linha : casas) {
            for (Casa casa : linha) {
                casa.setPeca(null);
            }
        }
    }

    @Override
    public Tabuleiro clone() {
        try {
            Tabuleiro novoTabuleiro = (Tabuleiro) super.clone();
            novoTabuleiro.casas = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                List<Casa> novaLinha = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    Casa casaOriginal = casas.get(i).get(j);
                    Casa novaCasa = new Casa(casaOriginal.getCor(), casaOriginal.getPosicao());
                    if (casaOriginal.getPeca() != null) {
                        novaCasa.setPeca(casaOriginal.getPeca().clone());
                    }
                    novaLinha.add(novaCasa);
                }
                novoTabuleiro.casas.add(novaLinha);
            }

            novoTabuleiro.pecasCapturadasBrancas = new ArrayList<>(this.pecasCapturadasBrancas);
            novoTabuleiro.pecasCapturadasPretas = new ArrayList<>(this.pecasCapturadasPretas);

            novoTabuleiro.origemSelecionada = this.origemSelecionada != null ? this.origemSelecionada.clone() : null;
            novoTabuleiro.destinoSelecionada = this.destinoSelecionada != null ? this.destinoSelecionada.clone() : null;

            novoTabuleiro.observadores = new ArrayList<>(this.observadores);

            return novoTabuleiro;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}