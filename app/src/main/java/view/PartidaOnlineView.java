package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;

import controle.PartidaOnlineControle;

public class PartidaOnlineView {

    private PartidaOnlineControle partidaOnlineControle;

    public PartidaOnlineView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Online");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campos de input para o nome, imagem, código da sala e IP do servidor
        Label nomeLabel = new Label("Digite seu nome:");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Nome do jogador");

        Label imagemLabel = new Label("Escolha uma imagem:");
        Button escolherImagemButton = new Button("Escolher Imagem");
        Label imagemSelecionadaLabel = new Label("Nenhuma imagem selecionada");

        TextField ipServidorTextField = new TextField();
        ipServidorTextField.setPromptText("IP do servidor");

        TextField codigoSalaTextField = new TextField();
        codigoSalaTextField.setPromptText("Código da sala");

        File[] imagemSelecionada = new File[1];  // Usando um array para manipular a imagem de forma imutável
        Image[] imagemJogador = new Image[1];  // Para armazenar a imagem como objeto Image

        // Abrir o FileChooser para escolher a imagem
        escolherImagemButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
            File arquivoImagem = fileChooser.showOpenDialog(primaryStage);
            if (arquivoImagem != null) {
                imagemSelecionada[0] = arquivoImagem;
                imagemJogador[0] = new Image(arquivoImagem.toURI().toString()); // Converte o arquivo em Image
                imagemSelecionadaLabel.setText("Imagem selecionada: " + arquivoImagem.getName());
            }
        });

        // Inicializar o controlador
        partidaOnlineControle = new PartidaOnlineControle(primaryStage);

        // Botão para criar partida
        Button criarPartidaButton = new Button("Criar Partida");
        criarPartidaButton.setOnAction(event -> {
            String nomeJogador1 = nomeTextField.getText();
            if (!nomeJogador1.isEmpty() && imagemJogador[0] != null) {
                // Criar a partida como jogador 1 (host)
                String codigoSala = partidaOnlineControle.criarPartida(nomeJogador1, imagemJogador[0]);

                // Exibir o código da sala em um popup (Dialog)
                showCodigoSalaPopup(codigoSala);
            } else {
                // Mostrar alerta se nome ou imagem não forem preenchidos
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha seu nome e selecione uma imagem.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Botão para entrar em uma partida
        Button entrarPartidaButton = new Button("Entrar em Partida");
        entrarPartidaButton.setOnAction(event -> {
            String nomeJogador2 = nomeTextField.getText();
            String ipServidor = ipServidorTextField.getText();
            String codigoSala = codigoSalaTextField.getText();
            if (!nomeJogador2.isEmpty() && imagemJogador[0] != null && !ipServidor.isEmpty() && !codigoSala.isEmpty()) {
                // Entrar na partida como jogador 2
                partidaOnlineControle.entrarPartida(nomeJogador2, imagemJogador[0], ipServidor, codigoSala);
            } else {
                // Mostrar alerta se algum campo estiver vazio
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha todos os campos.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Adicionar os componentes no layout
        menuLayout.getChildren().addAll(
                titleLabel,
                nomeLabel,
                nomeTextField,
                imagemLabel,
                escolherImagemButton,
                imagemSelecionadaLabel,
                new Label("IP do servidor:"),
                ipServidorTextField,
                new Label("Código da sala:"),
                codigoSalaTextField,
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

    // Função para exibir o código da sala em um popup (Dialog)
    private void showCodigoSalaPopup(String codigoSala) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Código da Sala");

        // Definir o botão de fechar
        ButtonType buttonTypeOk = new ButtonType("Fechar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        // Conteúdo do Dialog (exibe o código da sala)
        Label label = new Label("Código da sala: " + codigoSala);
        dialog.getDialogPane().setContent(label);

        // Mostrar o popup
        dialog.showAndWait();
    }
}