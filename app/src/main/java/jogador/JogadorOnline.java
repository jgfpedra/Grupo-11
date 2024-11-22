package jogador;

import java.net.Socket;
import java.io.IOException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import partida.Cor;
import partida.Tabuleiro;

@XmlRootElement
public class JogadorOnline extends Jogador {
    private Socket socket;

    public JogadorOnline() {
    }

    public JogadorOnline(Cor cor, String nome, Socket socket) {
        super(cor, nome);
        this.socket = socket;
    }

    @Override
    public void escolherMovimento(Tabuleiro tabuleiro) {
        // Handle movement logic for online player, probably listen to the network for moves
    }

    @Override
    public void temPecas() {
        // Handle pieces for online player
    }

    @XmlElement
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
}