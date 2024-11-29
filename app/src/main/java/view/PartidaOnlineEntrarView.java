package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;

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

        Label codigoSalaLabel = new Label("Código da sala:");
        TextField codigoSalaTextField = new TextField();
        codigoSalaTextField.setPromptText("Código da sala");

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

        // Botão para entrar na partida
        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setStyle("-fx-font-size: 16px;");
        entrarPartidaButton.setOnAction(event -> {
            String nomeJogador2 = nomeTextField.getText();
            String ipServidor = ipServidorTextField.getText();
            String codigoSala = codigoSalaTextField.getText();
            int porta = Integer.parseInt(portaServidorTextField.getText());
        
            // Verifique se todos os campos foram preenchidos corretamente
            if (!nomeJogador2.isEmpty() && !ipServidor.isEmpty() && !codigoSala.isEmpty()) {
                try {
                    boolean sucesso = partidaOnlineControle.entrarPartida(nomeJogador2, imagemJogador2, ipServidor, porta, codigoSala);
                    if (sucesso) {
                        // Se a conexão for bem-sucedida, inicia a partida
                        System.out.println("Conexao sucedida");
                    } else {
                        // Mostrar alerta de erro caso a conexão falhe
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na conexão com o servidor.", ButtonType.OK);
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    // Caso a conversão da porta falhe (não seja um número válido)
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, insira uma porta válida.", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                // Mostrar alerta se algum campo estiver vazio
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha todos os campos.", ButtonType.OK);
                alert.showAndWait();
            }
        });        

        // Botão Voltar
        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            new PartidaOnlineMenuView(primaryStage);  // Voltar para o menu principal
        });

        // Adicionar os componentes no layout
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
                codigoSalaLabel,
                codigoSalaTextField,
                entrarPartidaButton,
                voltarButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Entrar em Partida - Xadrez Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}