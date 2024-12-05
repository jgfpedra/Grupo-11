package partida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jogador.JogadorIA;
import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Peca;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

/**
 * A classe Tabuleiro representa o tabuleiro de xadrez no qual as peças se movem.
 * O tabuleiro é composto por uma grade de 8x8 casas e gerencia os movimentos das peças,
 * incluindo a captura de peças, verificações de xeque e xeque-mate, e a interação com os observadores.
 * Além disso, a classe fornece métodos para manipulação de peças e tabuleiro.
 */
public class Tabuleiro implements Cloneable{

    private List<List<Casa>> casas;
    private ArrayList<ObservadorTabuleiro> observadores;
    private List<Peca> pecasCapturadasBrancas;
    private List<Peca> pecasCapturadasPretas;
    private Posicao origemSelecionada;
    private Posicao destinoSelecionada;
    private Map<Cor, List<Peca>> pecasAdversariasCache = new HashMap<>();
    private Map<Cor, List<Posicao>> posicoesPecasAdversariasCache = new HashMap<>();
    private Map<Cor, Set<Posicao>> posicoesAtacadasCache = new HashMap<>();
    private boolean cacheAtualizado = false;


    /**
     * Construtor padrão da classe Tabuleiro, inicializando o tabuleiro de xadrez com 8 linhas e 8 colunas.
     * As peças são posicionadas nas casas do tabuleiro conforme a configuração inicial do jogo.
     * 
     * - As peças brancas são colocadas nas linhas 6 (peões) e 7 (torres, cavalos, bispos, rainha e rei).
     * - As peças pretas são colocadas nas linhas 1 (peões) e 0 (torres, cavalos, bispos, rainha e rei).
     * 
     * Além disso, o construtor inicializa as listas de peças capturadas para ambos os jogadores e a lista de observadores.
     */
    public Tabuleiro() {
        casas = new ArrayList<>();
        pecasCapturadasBrancas = new ArrayList<>();
        pecasCapturadasPretas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<Casa> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                Cor cor = (i + j) % 2 == 0 ? Cor.BRANCO : Cor.PRETO;
                Posicao posicao = new Posicao(i, j);
                row.add(new Casa(cor, posicao));
            }
            casas.add(row);
        }
        for (int i = 0; i < 8; i++) {
            casas.get(6).get(i).setPeca(new Peao(Cor.BRANCO));
            casas.get(1).get(i).setPeca(new Peao(Cor.PRETO));
        }
        casas.get(7).get(0).setPeca(new Torre(Cor.BRANCO));
        casas.get(7).get(7).setPeca(new Torre(Cor.BRANCO));
        casas.get(0).get(0).setPeca(new Torre(Cor.PRETO));
        casas.get(0).get(7).setPeca(new Torre(Cor.PRETO));
        casas.get(7).get(1).setPeca(new Cavalo(Cor.BRANCO));
        casas.get(7).get(6).setPeca(new Cavalo(Cor.BRANCO));
        casas.get(0).get(1).setPeca(new Cavalo(Cor.PRETO));
        casas.get(0).get(6).setPeca(new Cavalo(Cor.PRETO));
        casas.get(7).get(2).setPeca(new Bispo(Cor.BRANCO));
        casas.get(7).get(5).setPeca(new Bispo(Cor.BRANCO));
        casas.get(0).get(2).setPeca(new Bispo(Cor.PRETO));
        casas.get(0).get(5).setPeca(new Bispo(Cor.PRETO));
        casas.get(7).get(3).setPeca(new Rainha(Cor.BRANCO));
        casas.get(0).get(3).setPeca(new Rainha(Cor.PRETO));
        casas.get(7).get(4).setPeca(new Rei(Cor.BRANCO));
        casas.get(0).get(4).setPeca(new Rei(Cor.PRETO));
        observadores = new ArrayList<>();
    }

    /**
     * Aplica o movimento de uma peça no tabuleiro e notifica os observadores.
     * 
     * @param movimento O movimento a ser aplicado.
     */
    public void aplicarMovimento(Movimento movimento) {
        movimento.aplicar(this);
        limparCache();
        notificarObservadores();
    }


    /**
     * Desfaz o último movimento realizado, restaurando o estado anterior do tabuleiro.
     * 
     * @param ultimoMovimento O movimento a ser desfeito.
     */
    public void desfazerMovimento(Movimento ultimoMovimento) {
        ultimoMovimento.voltar(this);
        Peca pecaCapturada = ultimoMovimento.getPecaCapturada();
        if (pecaCapturada != null) {
            Posicao posicaoCaptura = ultimoMovimento.getDestino();
            getCasa(posicaoCaptura).setPeca(pecaCapturada);
            if (pecaCapturada.getCor() == Cor.BRANCO) {
                pecasCapturadasBrancas.remove(pecaCapturada);
            } else if (pecaCapturada.getCor() == Cor.PRETO) {
                pecasCapturadasPretas.remove(pecaCapturada);
            }
        }
        notificarObservadores();
    }

    /**
     * Verifica se o rei da cor especificada está em xeque.
     * 
     * @param posicaoRei A posição do rei a ser verificado.
     * @param corDoJogador A cor do jogador que está sendo verificado.
     * @return Verdadeiro se o rei estiver em xeque, falso caso contrário.
     */
    public boolean isReiEmCheck(Posicao posicaoRei, Cor corDoJogador) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Posicao posicao = new Posicao(i, j);
                Peca pecaAdversaria = obterPeca(posicao);
                if (pecaAdversaria != null && pecaAdversaria.getCor() != corDoJogador) {
                    List<Posicao> movimentosPossiveis = pecaAdversaria.possiveisMovimentos(this, posicao);
                    if (movimentosPossiveis.contains(posicaoRei)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }    

    /**
     * Verifica se um movimento é seguro, ou seja, se não coloca o rei em xeque.
     * 
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino da peça.
     * @param corDoJogador A cor do jogador que está fazendo o movimento.
     * @return Verdadeiro se o movimento for seguro, falso caso contrário.
     */
    public boolean isMovimentoSeguro(Posicao origem, Posicao destino, Cor corDoJogador) {
        Peca pecaOrigem = obterPeca(origem);
        Peca pecaDestino = obterPeca(destino);
        aplicarMovimentoTemporario(origem, destino);
        Posicao posicaoRei = getPosicaoRei(corDoJogador);
        boolean seguro = !isReiEmCheck(posicaoRei, corDoJogador);
        desfazerMovimentoTemporario(origem, destino, pecaOrigem, pecaDestino);
        return seguro;
    }

    /**
     * Adiciona um observador para ser notificado de mudanças no tabuleiro.
     * 
     * @param observador O observador a ser adicionado.
     */
    public void adicionarObservador(ObservadorTabuleiro observador) {
        observadores.add(observador);
    }

    /**
     * Remove um observador da lista de notificações.
     * 
     * @param observador O observador a ser removido.
     */
    public void removerObservador(ObservadorTabuleiro observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica todos os observadores registrados de que o tabuleiro foi alterado.
     */
    protected void notificarObservadores() {
        for (ObservadorTabuleiro observador : observadores) {
            observador.atualizar();
        }
    }

    /**
     * Remove a peça de uma casa no tabuleiro, definindo-a como null.
     * Caso a posição fornecida seja válida e contenha uma peça, essa peça será removida.
     * 
     * @param posicao A posição da casa no tabuleiro da qual a peça será removida.
     */
    public void removerPeca(Posicao posicao) {
        Casa casa = getCasa(posicao);
        if (casa != null) {
            casa.setPeca(null);
        }
    }

    /**
     * Obtém a peça em uma posição específica do tabuleiro.
     * 
     * @param posicao A posição da casa no tabuleiro.
     * @return A peça presente na posição, ou null se não houver peça.
     */
    public Peca obterPeca(Posicao posicao) {
        Peca peca = casas.get(posicao.getLinha()).get(posicao.getColuna()).getPeca();
        return peca;
    }
    
    /**
     * Retorna a casa do tabuleiro correspondente à posição fornecida.
     * 
     * @param posicao A posição da casa que se deseja obter.
     * @return A casa correspondente à posição fornecida.
     */
    public Casa getCasa(Posicao posicao) {
        Casa casa = casas.get(posicao.getLinha()).get(posicao.getColuna());
        return casa;
    }

    /**
     * Encontra e retorna a posição do rei de um jogador no tabuleiro.
     * 
     * @param corDoJogador A cor do jogador cujos rei se deseja localizar.
     * @return A posição do rei do jogador, ou null se não encontrado.
     */
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

    /**
     * Verifica se existem movimentos válidos para sair do xeque.
     * 
     * @param cor A cor do jogador que está em xeque.
     * @return Verdadeiro se houver movimentos válidos para sair do xeque, falso caso contrário.
     */
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
                    List<Posicao> movimentosPossiveis = peca.possiveisMovimentos(this, posicao);
                    for (Posicao destino : movimentosPossiveis) {
                        if (isMovimentoSeguro(posicao, destino, cor)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }    

    /**
     * Coloca uma peça em uma posição específica do tabuleiro.
     * 
     * Este método coloca a peça fornecida na casa do tabuleiro correspondente à posição especificada.
     * Se a posição estiver fora dos limites do tabuleiro (0 a 7 para linhas e colunas), uma exceção será lançada.
     * Caso contrário, a peça será posicionada na casa indicada, substituindo qualquer peça que já estivesse lá.
     * 
     * @param peca A peça a ser colocada no tabuleiro.
     * @param posicao A posição no tabuleiro onde a peça será colocada.
     * 
     * @throws IllegalArgumentException Se a posição fornecida estiver fora dos limites do tabuleiro.
     */
    public void colocarPeca(Peca peca, Posicao posicao) {
        if (posicao.getLinha() < 0 || posicao.getLinha() >= 8 || posicao.getColuna() < 0 || posicao.getColuna() >= 8) {
            throw new IllegalArgumentException("Posição inválida no tabuleiro.");
        }
        Casa casa = getCasa(posicao);
        casa.setPeca(peca);
    }

    /**
     * Obtém a última peça capturada de uma cor específica.
     * 
     * @param cor A cor das peças capturadas a ser verificada.
     * @return A última peça capturada, ou null se nenhuma peça foi capturada.
     */
    public Peca getUltimaPecaCapturada(Cor cor) {
        if (cor == Cor.BRANCO && !pecasCapturadasBrancas.isEmpty()) {
            return pecasCapturadasBrancas.get(pecasCapturadasBrancas.size() - 1);
        } else if (cor == Cor.PRETO && !pecasCapturadasPretas.isEmpty()) {
            return pecasCapturadasPretas.get(pecasCapturadasPretas.size() - 1);
        }
        return null;
    }

    /**
     * Retorna a posição da origem selecionada para um movimento.
     * 
     * @return A posição da origem selecionada ou null se não houver nenhuma origem selecionada.
     */
    public Posicao getOrigemSelecionada() {
        return origemSelecionada;
    }

    /**
     * Define a posição da origem selecionada para um movimento.
     * 
     * @param origemSelecionada A posição da origem a ser definida.
     */
    public void setOrigemSelecionada(Posicao origemSelecionada) {
        this.origemSelecionada = origemSelecionada;
    }

    /**
     * Retorna a posição do destino selecionado para um movimento.
     * 
     * @return A posição do destino selecionado ou null se não houver nenhum destino selecionado.
     */
    public Posicao getDestinoSelecionada() {
        return destinoSelecionada;
    }

    /**
     * Retorna uma lista de posições onde as peças estão localizadas no tabuleiro.
     * 
     * @return Uma lista de posições que contêm peças no tabuleiro.
     */
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

    /**
     * Define a posição do destino selecionado para um movimento.
     * 
     * @param destinoSelecionada A posição do destino a ser definida.
     */
    public void setDestinoSelecionada(Posicao destinoSelecionada) {
        this.destinoSelecionada = destinoSelecionada;
    }

    /**
     * Retorna uma lista de movimentos válidos que o jogador da IA pode fazer, considerando o tabuleiro atual.
     * 
     * @param jogadorIA O jogador da IA para o qual os movimentos serão calculados.
     * @return Uma lista de movimentos válidos que o jogador da IA pode fazer.
     */
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

    /**
     * Aplica temporariamente um movimento no tabuleiro para verificar a segurança do rei.
     * 
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino para onde a peça será movida.
     */
    public void aplicarMovimentoTemporario(Posicao origem, Posicao destino) {
        Peca pecaMovida = obterPeca(origem);
        removerPeca(origem);
        colocarPeca(pecaMovida, destino);
    }
    

    /**
     * Desfaz temporariamente um movimento no tabuleiro, restaurando as posições anteriores.
     * 
     * @param origem A posição de origem da peça.
     * @param destino A posição de destino para onde a peça foi movida.
     * @param pecaOrigem A peça que estava na posição de origem antes do movimento.
     * @param pecaDestino A peça que estava na posição de destino antes do movimento.
     */
    public void desfazerMovimentoTemporario(Posicao origem, Posicao destino, Peca pecaOrigem, Peca pecaDestino) {
        removerPeca(destino);
        colocarPeca(pecaOrigem, origem);
        if (pecaDestino != null) {
            colocarPeca(pecaDestino, destino);
        }
    }

    /**
     * Adiciona uma peça capturada à lista de peças capturadas do jogador.
     * 
     * @param pecaCapturada A peça a ser adicionada à lista de capturadas.
     */
    protected void adicionarPecaCapturada(Peca pecaCapturada) {
        if (pecaCapturada.getCor() == Cor.BRANCO) {
            pecasCapturadasBrancas.add(pecaCapturada);
        } else if (pecaCapturada.getCor() == Cor.PRETO) {
            pecasCapturadasPretas.add(pecaCapturada);
        }
    }

    /**
     * Retorna a lista de peças capturadas pelo jogador preto.
     * 
     * @return A lista de peças capturadas pelo jogador preto.
     */
    public List<Peca> getCapturadasJogadorPreto() {
        return pecasCapturadasBrancas;
    }

    /**
    * Retorna a lista de peças capturadas pelo jogador branco.
    * 
    * @return A lista de peças capturadas pelo jogador branco.
    */
    public List<Peca> getCapturadasJogadorBranco() {
        return pecasCapturadasPretas;
    }

    /**
     * Adiciona uma peça capturada pelo jogador branco à lista de peças capturadas.
     * 
     * @param capturadaBranca A peça capturada pelo jogador branco.
     */
    public void addCapturadasJogadorBranco(Peca capturadaBranca){
        this.pecasCapturadasBrancas.add(capturadaBranca);
    }

    /**
     * Adiciona uma peça capturada pelo jogador preto à lista de peças capturadas.
     * 
     * @param capturadaPreto A peça capturada pelo jogador preto.
     */
    public void addCapturadasJogadorPreto(Peca capturadaPreto){
        this.pecasCapturadasPretas.add(capturadaPreto);
    }

    /**
     * Retorna o tabuleiro representado pelas casas.
     * 
     * @return Uma lista de listas de casas, representando o tabuleiro de xadrez.
     */
    public List<List<Casa>> getCasas(){
        return casas;
    }
    
    /**
     * Limpa todas as peças do tabuleiro, removendo-as de todas as casas.
     */
    public void limparTabuleiro() {
        for (List<Casa> linha : casas) {
            for (Casa casa : linha) {
                casa.setPeca(null);
            }
        }
    }

    /**
     * Limpa a lista de peças capturadas pelo jogador branco.
     * 
     * Este método remove todas as peças que foram capturadas pelo jogador branco
     * da lista de peças capturadas, restaurando-a a um estado vazio.
     */
    public void limparCapturadasJogadorBranco() {
        pecasCapturadasBrancas.clear();
    }

    /**
     * Limpa a lista de peças capturadas pelo jogador preto.
     * 
     * Este método remove todas as peças que foram capturadas pelo jogador preto
     * da lista de peças capturadas, restaurando-a a um estado vazio.
     */
    public void limparCapturadasJogadorPreto() {
        pecasCapturadasBrancas.clear();
    }
    
    /**
     * Verifica se uma posição está sob ataque por alguma peça adversária.
     * Este método verifica se a posição fornecida está sob ataque com base nas posições 
     * das peças adversárias e suas possíveis movimentações. O cache de posições atacadas
     * é atualizado se necessário.
     * 
     * @param posicao A posição a ser verificada se está sob ataque.
     * @param cor A cor da peça do jogador que está realizando a verificação.
     * @return {@code true} se a posição estiver sob ataque, {@code false} caso contrário.
     */
    public boolean posicaoSobAtaque(Posicao posicao, Cor cor) {
        Cor corAdversaria = cor == Cor.BRANCO ? Cor.BRANCO : Cor.PRETO;
        if (!cacheAtualizado) {
            atualizarCacheAdversario(corAdversaria);
            cacheAtualizado = true;
        }
        Set<Posicao> posicoesAtacadas = posicoesAtacadasCache.get(cor);
        if(posicoesAtacadas != null){
            boolean resultado = posicoesAtacadas.contains(posicao);
            return resultado;
        } else {
            return false;
        }
    }
    
    /**
     * Atualiza o cache com as peças adversárias e suas posições atacadas.
     * Este método percorre o tabuleiro e coleta todas as peças adversárias e as posições
     * que elas podem atacar. O cache é preenchido com essas informações para otimizar
     * verificações subsequentes. Algumas posições específicas (7, 4 e 0, 4) são ignoradas
     * durante o cálculo para evitar verificações desnecessárias.
     * 
     * @param corAdversario A cor da peça adversária cujas informações serão armazenadas no cache.
     */
    public void atualizarCacheAdversario(Cor corAdversario) {
        if (cacheAtualizado) {
            return;
        }
        List<Peca> pecasAdversarias = new ArrayList<>();
        List<Posicao> posicoesPecasAdversarias = new ArrayList<>();
        Set<Posicao> posicoesAtacadas = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 7 && j == 4) {
                    continue;
                }
                if (i == 0 && j == 4) {
                    continue;
                }
                Casa casa = this.getCasa(new Posicao(i, j));
                Peca pecaNaCasa = casa.getPeca();
                if (pecaNaCasa != null && pecaNaCasa.getCor() == corAdversario) {
                    pecasAdversarias.add(pecaNaCasa);
                    posicoesPecasAdversarias.add(new Posicao(i, j));
                    List<Posicao> movimentos = pecaNaCasa.possiveisMovimentos(this, new Posicao(i, j));
                    posicoesAtacadas.addAll(movimentos);
                }
            }
        }
        pecasAdversariasCache.put(corAdversario, pecasAdversarias);
        posicoesPecasAdversariasCache.put(corAdversario, posicoesPecasAdversarias);
        posicoesAtacadasCache.put(corAdversario, posicoesAtacadas);
        cacheAtualizado = true;
    }

    /**
     * Limpa o cache de peças adversárias e suas posições atacadas.
     * Este método remove todas as informações armazenadas nos caches de peças adversárias,
     * posições de peças adversárias e posições atacadas, e marca o cache como não atualizado.
     * 
     * Isso é útil para garantir que o cache seja recalculado durante a próxima verificação ou atualização.
     */
    public void limparCache() {
        pecasAdversariasCache.clear();
        posicoesPecasAdversariasCache.clear();
        posicoesAtacadasCache.clear();
        cacheAtualizado = false;
    }
    
    /**
     * Clona o tabuleiro, criando uma cópia independente com o mesmo estado.
     * 
     * @return Uma nova instância de Tabuleiro com o mesmo estado do original.
     */
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