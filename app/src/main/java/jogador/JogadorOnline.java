package jogador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.image.Image;
import partida.Cor;

public class JogadorOnline extends Jogador {
    private Socket socket;
    private ServerSocket serverSocket;  // Para criar o servidor
    
    public JogadorOnline() {
    }

    public JogadorOnline(Cor cor, String nome, Image imagem) {
        super(cor, nome, imagem);
    }

    public Socket criarServidor(int porta) {
        try {
            System.out.println("j");
            serverSocket = new ServerSocket(porta);  // Cria o servidor
            System.out.println("k");
            InetAddress localHost = InetAddress.getLocalHost();  // Obtém o IP local do servidor
            String ipServidor = localHost.getHostAddress();  // Extrai o IP como String
            System.out.println("Servidor criado. Aguardando conexão...");
            System.out.println("IP do servidor: " + ipServidor + " Porta: " + porta);
            socket = serverSocket.accept();  // Aguarda a conexão do Jogador 2
            return socket;
        } catch (IOException e) {
            System.out.println("Erro ao criar servidor: " + e.getMessage());
            return null;
        }
    }    

    public Socket getSocket() {
        return socket;
    }

    public void desconectar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor desconectado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao desconectar o servidor: " + e.getMessage());
        }
    }

    public boolean conectar(String enderecoServidor, int porta) {
        System.out.println("f");
        try {
            socket = new Socket(enderecoServidor, porta);  // Conectar ao servidor
            System.out.println("g");
            System.out.println("Conectado ao servidor!");
            System.out.println("Socket: " + socket);
            enviarDadosParaServidor();
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    private void enviarDadosParaServidor() {
        try {
            System.out.println("h");
            DataOutputStream output = new DataOutputStream(this.getSocket().getOutputStream());
            output.writeUTF(this.getNome());
            output.writeUTF(this.getCor().toString());
            output.writeUTF(this.getImagem().getUrl());
            output.flush();
            System.out.println("i");
        } catch (IOException e) {
            System.out.println("Erro ao enviar dados para o servidor: " + e.getMessage());
        }
    }
}