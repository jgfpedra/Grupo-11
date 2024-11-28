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
    private Image[] imagemJogador = new Image[1];  // Armazena a imagem selecionada

    public PartidaOnlineEntrarView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Entrar em Partida");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campos de input para o nome, IP do servidor e código da sala
        Label nomeLabel = new Label("Digite seu nome:");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Nome do jogador");

        Label ipServidorLabel = new Label("IP do servidor:");
        TextField ipServidorTextField = new TextField();
        ipServidorTextField.setPromptText("IP do servidor");

        Label codigoSalaLabel = new Label("Código da sala:");
        TextField codigoSalaTextField = new TextField();
        codigoSalaTextField.setPromptText("Código da sala");

        // Label para mostrar a imagem selecionada
        Label imagemSelecionadaLabel = new Label("Nenhuma imagem selecionada");

        // Inicializar o controlador
        partidaOnlineControle = new PartidaOnlineControle(primaryStage);

        // Botão para escolher a imagem
        Button escolherImagemButton = new Button("Escolher Imagem");
        escolherImagemButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
            File arquivoImagem = fileChooser.showOpenDialog(primaryStage);
            if (arquivoImagem != null) {
                // Armazenar a imagem selecionada
                imagemJogador[0] = new Image(arquivoImagem.toURI().toString());
                imagemSelecionadaLabel.setText("Imagem selecionada: " + arquivoImagem.getName());
            }
        });

        // Botão para entrar na partida
        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setStyle("-fx-font-size: 16px;");
        entrarPartidaButton.setOnAction(event -> {
            String nomeJogador = nomeTextField.getText();
            String ipServidor = ipServidorTextField.getText();
            String codigoSala = codigoSalaTextField.getText();

            // Verifique se todos os campos foram preenchidos corretamente
            if (!nomeJogador.isEmpty() && imagemJogador[0] != null && !ipServidor.isEmpty() && !codigoSala.isEmpty()) {
                // Entrar na partida como jogador 2, passando a imagem do jogador também
                partidaOnlineControle.entrarPartida(nomeJogador, imagemJogador[0], ipServidor, codigoSala);
            } else {
                // Mostrar alerta se algum campo estiver vazio
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha todos os campos.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-font-size: 16px;");
        voltarButton.setOnAction(e -> {
            // Volta para a tela anterior (menu principal)
            new PartidaOnlineMenuView(primaryStage);  // Supondo que você tenha uma classe MenuView para o menu principal
        });

        // Adicionar os componentes no layout
        menuLayout.getChildren().addAll(
                titleLabel,
                nomeLabel,
                nomeTextField,
                ipServidorLabel,
                ipServidorTextField,
                codigoSalaLabel,
                codigoSalaTextField,
                escolherImagemButton,
                imagemSelecionadaLabel,
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