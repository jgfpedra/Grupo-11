package jogador;

import java.net.Socket;

import javafx.scene.image.Image;

import java.io.IOException;

import partida.Cor;
import partida.Partida;

public class JogadorOnline extends Jogador {
    private Socket socket;

    public JogadorOnline() {
    }

    public JogadorOnline(Cor cor, String nome, Image imagem, Socket socket) {
        super(cor, nome, imagem);
        this.socket = socket;
    }

    @Override
    public void escolherMovimento(Partida partida) {
    }

    @Override
    public void temPecas() {
    }

    public Socket getSocket() {
        return socket;
    }

    // Create connection method
    public boolean conectar(String enderecoServidor, int porta) {
        try {
            socket = new Socket(enderecoServidor, porta);
            System.out.println("Conectado ao servidor!");
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    public void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Desconectado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public void setSocket(Socket socketJogador) {
        this.socket = socketJogador;
    }
}