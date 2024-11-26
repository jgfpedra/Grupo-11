package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.net.ServerSocket;
import java.net.Socket;

import controle.TabuleiroControle;
import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;

public class PartidaOnlineView {

    public PartidaOnlineView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Online");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Botões para criar ou entrar na partida
        Button criarPartidaButton = new Button("Criar Partida");
        criarPartidaButton.setOnAction(event -> {
            // Iniciar o servidor em uma thread separada
            new Thread(() -> {
                try {
                    // Cria um ServerSocket na porta 12345
                    ServerSocket servidorSocket = new ServerSocket(12345);
                    System.out.println("Aguardando conexão...");

                    // Aceita a conexão do primeiro jogador
                    Socket cliente1 = servidorSocket.accept(); 
                    System.out.println("Jogador 1 conectado.");

                    // Aceita a conexão do segundo jogador
                    Socket cliente2 = servidorSocket.accept(); 
                    System.out.println("Jogador 2 conectado.");
                    JogadorOnline jogador1 = new JogadorOnline(Cor.PRETO, "Jogador 1", cliente1); // Criar jogador 1
                    JogadorOnline jogador2 = new JogadorOnline(Cor.BRANCO, "Jogador 2", cliente2); // Jogador 2

                    // Criar a partida e o servidor
                    Partida partida = new Partida(jogador1, jogador2, null); // Sem histórico
                    TabuleiroView tabuleiroView = new TabuleiroView(partida);

                    // Aqui seria a lógica do servidor (ServerSocket), aceitando dois jogadores
                    // Aguardando por dois jogadores se conectarem
                    // Conectar jogadores ao servidor, criar os jogadores e iniciar a partida
                    System.out.println("Criando servidor...");
                    JogadorOnline jogadorConectado = jogador1; // Jogador 1 deve ser o host
                    jogadorConectado.conectar("127.0.0.1", 12345); // Conectando à porta 12345 (pode ser configurável)

                    // Exibir a cena do tabuleiro
                    new TabuleiroControle(partida, tabuleiroView, primaryStage);

                    primaryStage.setTitle("Jogo de Xadrez");
                    primaryStage.setScene(new Scene(tabuleiroView, 4096, 4096));
                    primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
                    primaryStage.show();

                    servidorSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setOnAction(event -> {
            // Entrar em uma partida existente (cliente)
            new Thread(() -> {
                try {
                    // Criar um novo Socket para conectar ao servidor
                    Socket socket = new Socket("127.0.0.1", 12345); // Endereço do servidor e a porta que o servidor está ouvindo
        
                    // Criar o jogador com o Socket conectado
                    JogadorOnline jogador = new JogadorOnline(Cor.PRETO, "Jogador", socket);
        
                    // Tenta se conectar ao servidor
                    boolean conectado = jogador.conectar("127.0.0.1", 12345); // Endereço do servidor (localhost para testes)
        
                    if (conectado) {
                        // Se conectar, criar a partida e o tabuleiro
                        Partida partida = new Partida(jogador, null, null); // No início, só o jogador conectado
                        TabuleiroView tabuleiroView = new TabuleiroView(partida);
                        new TabuleiroControle(partida, tabuleiroView, primaryStage);
        
                        // Configura a cena do tabuleiro
                        primaryStage.setTitle("Jogo de Xadrez");
                        primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
                        primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
                        primaryStage.show();
                    } else {
                        // Caso não consiga se conectar, exibir erro
                        System.out.println("Erro ao conectar ao servidor.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });        

        // Adicionar botões no layout
        menuLayout.getChildren().addAll(
                titleLabel,
                criarPartidaButton,
                entrarPartidaButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Jogo de Xadrez - Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}