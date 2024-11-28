package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.File;
import controle.PartidaOnlineControle;

public class PartidaOnlineCriarView {

    private PartidaOnlineControle partidaOnlineControle;

    public PartidaOnlineCriarView(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Título do jogo
        Label titleLabel = new Label("Jogo de Xadrez - Criar Partida");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campos de input para o nome e imagem
        Label nomeLabel = new Label("Digite seu nome:");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Nome do jogador");

        Label imagemLabel = new Label("Escolha uma imagem:");
        Button escolherImagemButton = new Button("Escolher Imagem");
        Label imagemSelecionadaLabel = new Label("Nenhuma imagem selecionada");

        File[] imagemSelecionada = new File[1];
        Image[] imagemJogador = new Image[1];

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
            String nomeJogador = nomeTextField.getText();
            if (!nomeJogador.isEmpty() && imagemJogador[0] != null) {
                String codigoSala = partidaOnlineControle.criarPartida(nomeJogador, imagemJogador[0]);
                if (codigoSala != null) {
                    showCodigoSalaPopup(codigoSala);
                }
            } else {
                // Mostrar alerta se nome ou imagem não forem preenchidos
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, preencha seu nome e selecione uma imagem.", ButtonType.OK);
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
                imagemLabel,
                escolherImagemButton,
                imagemSelecionadaLabel,
                criarPartidaButton,
                voltarButton
        );

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 1200, 900);
        menuScene.getStylesheets().add(getClass().getResource("/style/menu.css").toExternalForm());

        primaryStage.setTitle("Criar Partida - Xadrez Online");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void showCodigoSalaPopup(String codigoSala) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Código da Sala");
        ButtonType buttonTypeOk = new ButtonType("Fechar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        Label label = new Label("Código da sala: " + codigoSala);
        dialog.getDialogPane().setContent(label);
        dialog.showAndWait();
    }
}