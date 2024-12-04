package jogador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import partida.Cor;

public class JogadorOnline extends Jogador {
    private Socket socket;
    private ServerSocket serverSocket;
    
    public JogadorOnline() {
    }

    public JogadorOnline(Cor cor, String nome, Image imagem) {
        super(cor, nome, imagem);
    }
    
    public Socket criarServidor(int porta) {
        try {
            System.out.println("Iniciando servidor...");
            serverSocket = new ServerSocket(porta);
            InetAddress localHost = InetAddress.getLocalHost();
            String ipServidor = localHost.getHostAddress();
            System.out.println("Servidor criado. Aguardando conexão...");
            System.out.println("IP do servidor: " + ipServidor + " Porta: " + porta);
            mostrarAlertServidor(ipServidor, porta);
            socket = serverSocket.accept();
            System.out.println("Conexão estabelecida com: " + socket.getInetAddress());
            return socket;
        } catch (IOException e) {
            System.out.println("Erro ao criar servidor: " + e.getMessage());
            e.printStackTrace();
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
        try {
            socket = new Socket(enderecoServidor, porta);
            System.out.println("Conectado ao servidor!");
            System.out.println("Socket: " + socket);
            enviarDadosParaServidor(socket);
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    private void enviarDadosParaServidor(Socket socket) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(this.getNome());
            output.writeUTF(this.getCor().toString());
            output.writeUTF(this.getImagem().getUrl());
            output.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar dados para o servidor: " + e.getMessage());
        }
    }
    
    private void mostrarAlertServidor(String ip, int porta) {
        // Criando o Alert
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informação do Servidor");
        alert.setHeaderText("Servidor Criado");
        alert.setContentText("IP do Servidor: " + ip + "\nPorta: " + porta);
        
        // Exibindo o Alert
        alert.showAndWait();
    }

}