package view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import controle.MenuControle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import partida.Partida;

public class MenuView {

    private VBox menuLayout;
    
    public MenuView(Stage menuStage, Partida partida, MenuControle menuControle) {
        menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Botão "Salvar Jogo"
        Button saveButton = new Button("Salvar Jogo");
        saveButton.setOnAction(event -> {
            salvarJogo(partida);
        });

        Button exitButton = new Button("Sair Início");
        exitButton.setOnAction(event -> {
            // Chama o método retornarAoTabuleiro do MenuControle
            menuControle.sairAoInicio(); // Aqui
            menuStage.close();  // Fecha o menu
        });        

        // Botão "Sair"
        Button voltarButton = new Button("Voltar");
        voltarButton.setOnAction(event -> {
            menuStage.close(); // Fecha o menu (popup) quando o botão "Sair" é clicado
        });

        // Adiciona os botões ao layout
        menuLayout.getChildren().addAll(saveButton, exitButton, voltarButton);

        // Criar a cena do menu
        Scene menuScene = new Scene(menuLayout, 300, 200);
        menuStage.setTitle("Menu");
        menuStage.setScene(menuScene);
    }

    private void salvarJogo(Partida partida) {
        // Caminho absoluto para o arquivo original
        String caminhoProjeto = System.getProperty("user.dir");
        File arquivoOriginal = new File(caminhoProjeto, "/data/tabuleiro.xml");
        
        // Verifica se o arquivo original existe antes de tentar copiá-lo
        if (!arquivoOriginal.exists()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro ao Salvar Jogo");
            alert.setHeaderText(null);
            alert.setContentText("O arquivo original 'tabuleiro.xml' não foi encontrado em: " + arquivoOriginal.getAbsolutePath());
            alert.showAndWait();
            return;
        }
    
        // Chama o método para escolher o local onde salvar o jogo
        File arquivoDestino = escolherLocalDeSalvar();
        
        if (arquivoDestino != null) {
            try {
                // Copia o arquivo original para o local escolhido
                Path origem = arquivoOriginal.toPath();
                Path destino = arquivoDestino.toPath();
                Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING); // Substitui se o arquivo já existir
                
                // Exibe a mensagem de sucesso
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Salvar Jogo");
                alert.setHeaderText(null);
                alert.setContentText("O jogo foi salvo com sucesso em: " + arquivoDestino.getAbsolutePath());
                alert.showAndWait();
                
            } catch (IOException e) {
                // Exibe a mensagem de erro em caso de falha na cópia
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro ao Salvar Jogo");
                alert.setHeaderText(null);
                alert.setContentText("Houve um erro ao tentar salvar o jogo: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }    

    private File escolherLocalDeSalvar() {
        // Cria um FileChooser para o usuário selecionar o arquivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos XML", "*.xml"));
        fileChooser.setTitle("Escolha o Local para Salvar o Jogo");

        // Abrir o diálogo para salvar o arquivo
        Stage stage = new Stage();  // Crie ou obtenha o Stage da sua aplicação
        File arquivoSelecionado = fileChooser.showSaveDialog(stage);

        // Retorna o arquivo selecionado
        return arquivoSelecionado;
    }
}