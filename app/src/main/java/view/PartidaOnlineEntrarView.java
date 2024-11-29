package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;
import partida.Cor;

import java.io.File;

import controle.PartidaOnlineControle;

public class PartidaOnlineEntrarView {

    private PartidaOnlineControle partidaOnlineControle;
    private Image imagemJogador2;

    public PartidaOnlineEntrarView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Entrar em Partida");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campos de input para o nome, IP do servidor, porta e código da sala
        Label nomeLabel = new Label("Digite seu nome:");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Nome do jogador");

        Label ipServidorLabel = new Label("IP do servidor:");
        TextField ipServidorTextField = new TextField();
        ipServidorTextField.setPromptText("IP do servidor");

        Label portaServidorLabel = new Label("Porta do servidor:");
        TextField portaServidorTextField = new TextField();
        portaServidorTextField.setPromptText("Porta do servidor");

        Label imagemLabel = new Label("Escolha a imagem do jogador:");
        Button escolherImagemButton = new Button("Escolher Imagem");
        escolherImagemButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File arquivo = fileChooser.showOpenDialog(primaryStage);
            if (arquivo != null) {
                imagemJogador2 = new Image(arquivo.toURI().toString());
            }
        });
        partidaOnlineControle = new PartidaOnlineControle(primaryStage);
        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setStyle("-fx-font-size: 16px;");
        entrarPartidaButton.setOnAction(event -> {
            String nomeJogador2 = nomeTextField.getText();
            String ipServidor = ipServidorTextField.getText();
            int porta = Integer.parseInt(portaServidorTextField.getText());
            if (!nomeJogador2.isEmpty() && !ipServidor.isEmpty()) {
                try {
                    System.out.println("a");
                    boolean sucesso = partidaOnlineControle.entrarPartida(Cor.PRETO, nomeJogador2, imagemJogador2, ipServidor, porta);
                    if (sucesso) {
                        System.out.println("Conexao sucedida");
                    } else {
                        showCustomPopup("Falha na conexão com o servidor.");
                    }
                } catch (NumberFormatException e) {
                    showCustomPopup("Por favor, insira uma porta válida.");
                }
            } else {
                showCustomPopup("Por favor, preencha todos os campos.");
            }
        });
        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new PartidaOnlineMenuView(primaryStage);
        });
        menuLayout.getChildren().addAll(
                titleLabel,
                nomeLabel,
                nomeTextField,
                imagemLabel,
                escolherImagemButton,
                ipServidorLabel,
                ipServidorTextField,
                portaServidorLabel,
                portaServidorTextField,
                entrarPartidaButton,
                voltarButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 800, 800);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Entrar em Partida - Xadrez Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void showCustomPopup(String message) {
        // Criar um novo Stage para o popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Impede interação com a janela principal enquanto o popup está aberto
        popupStage.setTitle("Mensagem");
    
        // Layout do popup
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4; -fx-border-color: #000; -fx-border-radius: 5px;");
        
        // Adicionar a mensagem
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px;");
        
        // Botão de fechamento
        Button closeButton = new Button("Fechar");
        closeButton.setStyle("-fx-font-size: 14px;");
        closeButton.setOnAction(event -> popupStage.close());
    
        vbox.getChildren().addAll(messageLabel, closeButton);
        
        Scene scene = new Scene(vbox);
        
        // Ajustar o tamanho do popup para ser pequeno e controlado
        popupStage.setScene(scene);
        popupStage.setMinWidth(200);  // Definir a largura mínima
        popupStage.setMinHeight(100); // Definir a altura mínima
        popupStage.setWidth(250);     // Largura inicial
        popupStage.setHeight(150);    // Altura inicial
    
        // Mostrar o popup
        popupStage.showAndWait();
    }    
}