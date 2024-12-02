package UI.controle;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jogador.*;
import partida.*;
import UI.view.TabuleiroView;

public class PartidaLocalControle {

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