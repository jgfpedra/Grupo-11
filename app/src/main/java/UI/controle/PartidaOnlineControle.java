package UI.controle;

import jogador.JogadorOnline;
import partida.Cor;
import partida.Partida;
import UI.view.TabuleiroView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PartidaOnlineControle {

    private JogadorOnline jogador1;
    private JogadorOnline jogador2;
    private Partida partida;
    private Stage stage;

    public PartidaOnlineControle(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public boolean criarPartida(String nomeJogador1, Image imagemJogador1, Cor corJogador1, int porta) {
        jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
        Socket socket = jogador1.criarServidor(porta);
        if (socket != null) {
            new Thread(() -> {
                aceitarConexaoJogador2(socket);
            }).start();
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(jogador1.getNome());
                output.writeUTF(jogador1.getCor().name());
                output.writeUTF(jogador1.getImagem().getUrl());
            } catch (IOException e) {
                System.out.println("Erro ao enviar dados do Jogador 1: " + e.getMessage());
            }
            
            return true;
        }
        return false;
    }   

    public void aceitarConexaoJogador2(Socket socket) {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String nomeJogador2 = input.readUTF();
            Cor corJogador2 = Cor.valueOf(input.readUTF());
            Image imagemJogador2 = new Image(input.readUTF());
            System.out.println("Jogador 2: " + nomeJogador2 + " (Cor: " + corJogador2 + ")");
            
            jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
            partida = new Partida(jogador1, jogador2, null);
            iniciarPartida(socket, false);
        } catch (IOException e) {
            System.out.println("Erro ao receber dados do Jogador 2: " + e.getMessage());
        }
    }      

    public boolean entrarPartida(Cor corJogador2, String nomeJogador2, Image imagemJogador2, String ipServidor, int portaServidor) {
        jogador2 = new JogadorOnline(corJogador2, nomeJogador2, imagemJogador2);
        if (jogador2.conectar(ipServidor, portaServidor)) {
            try {
                DataInputStream input = new DataInputStream(jogador2.getSocket().getInputStream());
                String nomeJogador1 = input.readUTF();
                Cor corJogador1 = Cor.valueOf(input.readUTF());
                Image imagemJogador1 = new Image(input.readUTF());

                System.out.println("Nome jogador1: " + nomeJogador1);
    
                jogador1 = new JogadorOnline(corJogador1, nomeJogador1, imagemJogador1);
                
                Platform.runLater(() -> {
                    partida = new Partida(jogador1, jogador2, null);
                    iniciarPartida(jogador2.getSocket(), true);
                });
                return true;
            } catch (IOException e) {
                System.out.println("Erro ao receber dados do Jogador 1: " + e.getMessage());
            }
        }
        return false;
    }
    public void iniciarPartida(Socket socket, boolean isJogador2) {
        if (partida != null) {
            TabuleiroView tabuleiroView = new TabuleiroView(partida, isJogador2);
            new TabuleiroControle(partida, tabuleiroView, stage, socket);
            Platform.runLater(() -> {
                stage.setTitle("Jogo de Xadrez Online");
                stage.setScene(new Scene(tabuleiroView, 800, 800));
                stage.getScene().getStylesheets().add(getClass().getResource("/style/tabuleiro.css").toExternalForm());
                stage.show();
            });
        }
    }
}