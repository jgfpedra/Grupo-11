package controle;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import view.TabuleiroView;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class PartidaOnlineControle {

    private Stage primaryStage;
    private ServerSocket serverSocket;
    private JogadorOnline jogador1;  // Jogador 1 (host)
    private Partida partida;         // A partida vai ser criada após o segundo jogador entrar
    private String codigoSala;       // Código único da sala

    public PartidaOnlineControle(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Criar partida e gerenciar o servidor
    public String criarPartida(String nomeJogador1, Image imagemJogador1) {
        try {
            // Gerar código único para a sala (5 caracteres alfanuméricos)
            codigoSala = gerarCodigoSala();
            System.out.println("Código da sala: " + codigoSala);

            // Criar o servidor para escutar as conexões
            serverSocket = new ServerSocket(6969);
            System.out.println("Servidor esperando jogadores. Código da sala: " + codigoSala);

            // Criar o primeiro jogador (host)
            jogador1 = new JogadorOnline(Cor.PRETO, nomeJogador1, imagemJogador1, null);  // Ainda sem o socket
            System.out.println("Jogador 1 (host) criado: " + nomeJogador1);

            // Aguardar a conexão do segundo jogador
            Socket socketJogador1 = serverSocket.accept();
            System.out.println("Jogador 1 conectado.");

            // Atribuir o socket ao jogador 1
            jogador1.setSocket(socketJogador1);

            // Exibir a cena do tabuleiro (apenas o jogador 1)
            partida = new Partida(jogador1, null, null);  // Partida ainda sem o jogador 2
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, primaryStage);

            primaryStage.setTitle("Jogo de Xadrez - Esperando Jogador 2");
            primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
            primaryStage.show();

            // Retornar o código da sala
            return codigoSala;

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Caso ocorra algum erro, retornar null
        }
    }

    // Gerar um código aleatório de 5 caracteres alfanuméricos
    private String gerarCodigoSala() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

    // Entrar em uma partida existente (cliente)
    public void entrarPartida(String nomeJogador2, Image imagemJogador2, String ipServidor, String codigoSala) {
        try {
            System.out.println("Tentando conectar à sala: " + codigoSala);

            // Criar o socket para conectar ao servidor no IP do host e na porta 6969
            Socket socketJogador2 = new Socket(ipServidor, 6969);  // Conectar no servidor usando o IP do host
            System.out.println("Jogador 2 conectado ao servidor.");

            // Criar o segundo jogador
            JogadorOnline jogador2 = new JogadorOnline(Cor.BRANCO, nomeJogador2, imagemJogador2, socketJogador2);
            System.out.println("Jogador 2 criado: " + nomeJogador2);

            // Criar a partida com ambos os jogadores
            partida.setJogador2(jogador2);  // Completa a partida com o jogador 2
            TabuleiroView tabuleiroView = new TabuleiroView(partida);
            new TabuleiroControle(partida, tabuleiroView, primaryStage);

            primaryStage.setTitle("Jogo de Xadrez");
            primaryStage.setScene(new Scene(tabuleiroView, 800, 800));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para fechar a conexão do servidor (caso necessário)
    public void fecharServidor() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor fechado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}