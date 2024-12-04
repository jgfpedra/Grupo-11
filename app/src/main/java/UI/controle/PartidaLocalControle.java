package UI.controle;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jogador.*;
import partida.*;
import UI.view.TabuleiroView;


/**
 * Controlador responsável por iniciar e carregar partidas locais de xadrez.
 * 
 * Esta classe gerencia a lógica de inicialização de uma nova partida ou o carregamento
 * de uma partida salva, permitindo que o jogo seja iniciado com jogadores humanos
 * ou com jogadores controlados por Inteligência Artificial (IA). 
 * 
 * A classe também configura a interface gráfica, incluindo a exibição do tabuleiro de xadrez
 * e a definição do estilo visual do jogo.
 */
public class PartidaLocalControle {

    /**
     * Inicia uma nova partida de xadrez com os parâmetros fornecidos.
     * 
     * Este método cria dois jogadores (que podem ser IA ou jogadores humanos) e inicializa
     * a partida. Em seguida, configura a interface gráfica com o tabuleiro de xadrez
     * e exibe a janela principal do jogo.
     * 
     * @param player1IsAI Booleano indicando se o jogador 1 será controlado por IA.
     * @param player2IsAI Booleano indicando se o jogador 2 será controlado por IA.
     * @param player1AISelectedLevel Nível de dificuldade selecionado para a IA do jogador 1.
     * @param player2AISelectedLevel Nível de dificuldade selecionado para a IA do jogador 2.
     * @param corJogador1 Cor da peça do jogador 1 (branco ou preto).
     * @param corJogador2 Cor da peça do jogador 2 (branco ou preto).
     * @param primaryStage A janela principal da aplicação.
     */
    public void iniciarJogo(boolean player1IsAI, boolean player2IsAI, String player1AISelectedLevel, String player2AISelectedLevel, Cor corJogador1, Cor corJogador2, Stage primaryStage) {
        Jogador player1 = player1IsAI ? new JogadorIA(corJogador1, "IA Preto", getAILevel(player1AISelectedLevel)) : new JogadorLocal(corJogador1, "Jogador 1");
        Jogador player2 = player2IsAI ? new JogadorIA(corJogador2, "IA Branco", getAILevel(player2AISelectedLevel)) : new JogadorLocal(corJogador2, "Jogador 2");
        Partida partida = new Partida(player1, player2, null);
        TabuleiroView tabuleiroView = new TabuleiroView(partida, false);
        new TabuleiroControle(partida, tabuleiroView, primaryStage);
        primaryStage.setTitle("Jogo de Xadrez");
        primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
        primaryStage.show(); 
    }

    /**
     * Carrega uma partida salva a partir de um arquivo e a reinicia com os parâmetros fornecidos.
     * 
     * Este método permite ao usuário carregar um jogo de uma sessão anterior, restaurando o estado
     * da partida a partir de um arquivo de histórico. Depois de carregar o estado da partida,
     * o método configura a interface gráfica e exibe o tabuleiro com o estado restaurado.
     * 
     * @param player1IsAI Booleano indicando se o jogador 1 será controlado por IA.
     * @param player2IsAI Booleano indicando se o jogador 2 será controlado por IA.
     * @param player1AISelectedLevel Nível de dificuldade selecionado para a IA do jogador 1.
     * @param player2AISelectedLevel Nível de dificuldade selecionado para a IA do jogador 2.
     * @param corJogador1 Cor da peça do jogador 1 (branco ou preto).
     * @param corJogador2 Cor da peça do jogador 2 (branco ou preto).
     * @param arquivoHistorico Arquivo que contém o histórico da partida salva.
     * @param primaryStage A janela principal da aplicação.
     */
    public void carregarJogo(boolean player1IsAI, boolean player2IsAI, String player1AISelectedLevel, String player2AISelectedLevel, Cor corJogador1, Cor corJogador2, File arquivoHistorico, Stage primaryStage) {
        HistoricoMovimentos historicoMovimentos = new HistoricoMovimentos();
        historicoMovimentos.carregarEstadoDeArquivo(arquivoHistorico);
        Jogador jogador1 = player1IsAI ? new JogadorIA(corJogador1, "IA Preto", getAILevel(player1AISelectedLevel)) : new JogadorLocal(corJogador1, "Jogador 1");
        Jogador jogador2 = player2IsAI ? new JogadorIA(corJogador2, "IA Branco", getAILevel(player2AISelectedLevel)) : new JogadorLocal(corJogador2, "Jogador 2");
        Partida partida = new Partida(jogador1, jogador2, historicoMovimentos);
        TabuleiroView tabuleiroView = new TabuleiroView(partida, false);
        new TabuleiroControle(partida, tabuleiroView, primaryStage);
        primaryStage.setTitle("Jogo de Xadrez - Partida Carregada");
        primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * Converte o nível de dificuldade selecionado para um valor numérico.
     * 
     * Este método converte o nível de dificuldade da IA (como "Fácil", "Médio" ou "Difícil")
     * em um valor numérico que será usado para configurar a IA. 
     * 
     * @param level O nível de dificuldade selecionado.
     * @return O valor numérico correspondente ao nível de dificuldade.
     */
    private int getAILevel(String level) {
        switch (level) {
            case "Fácil":
                return 1;
            case "Médio":
                return 2;
            case "Difícil":
                return 3;
            default:
                return 2;
        }
    }
}