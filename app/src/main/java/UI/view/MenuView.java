package UI.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import UI.controle.MenuControle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import partida.Partida;

/**
 * Classe responsável pela criação do menu de opções durante uma partida de xadrez.
 * 
 * O menu permite que o jogador salve o estado do jogo, saia para o menu inicial ou apenas volte para a partida.
 * 
 * As funcionalidades de salvar o jogo incluem a escolha de um local para salvar um arquivo de estado do jogo.
 */
public class MenuView {

    private VBox menuLayout;

    /**
     * Constrói a interface do menu e adiciona os botões de interação.
     * 
     * @param menuStage A janela do menu.
     * @param partida O objeto da partida atual.
     * @param menuControle Controle que gerencia as ações do menu.
     */
    public MenuView(Stage menuStage, Partida partida, MenuControle menuControle) {
        menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Button saveButton = new Button("Salvar Jogo");
        saveButton.setOnAction(event -> {
            salvarJogo(partida);
        });
        Button exitButton = new Button("Sair Início");
        exitButton.setOnAction(event -> {
            menuControle.sairAoInicio();
            menuStage.close();
        });        
        Button voltarButton = new Button("Voltar");
        voltarButton.setOnAction(event -> {
            menuStage.close();
        });
        menuLayout.getChildren().addAll(saveButton, exitButton, voltarButton);
        Scene menuScene = new Scene(menuLayout, 300, 200);
        menuStage.setTitle("Menu");
        menuStage.setScene(menuScene);
    }

    /**
     * Método que permite salvar o estado atual do jogo em um arquivo XML.
     * 
     * O arquivo de estado do jogo é copiado do arquivo original para o local escolhido pelo usuário.
     * Se ocorrer algum erro ao tentar salvar o jogo, uma mensagem de erro será exibida.
     * 
     * @param partida O objeto da partida a ser salva.
     */
    private void salvarJogo(Partida partida) {
        String caminhoProjeto = System.getProperty("user.dir");
        File arquivoOriginal = new File(caminhoProjeto, "/data/tabuleiro.xml");
        if (!arquivoOriginal.exists()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro ao Salvar Jogo");
            alert.setHeaderText(null);
            alert.setContentText("O arquivo original 'tabuleiro.xml' não foi encontrado em: " + arquivoOriginal.getAbsolutePath());
            alert.showAndWait();
            return;
        }
        File arquivoDestino = escolherLocalDeSalvar();
        if (arquivoDestino != null) {
            try {
                if (!arquivoDestino.getName().endsWith(".xml")) {
                    arquivoDestino = new File(arquivoDestino.getParent(), arquivoDestino.getName() + ".xml");
                }
                Path origem = arquivoOriginal.toPath();
                Path destino = arquivoDestino.toPath();
                Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Salvar Jogo");
                alert.setHeaderText(null);
                alert.setContentText("O jogo foi salvo com sucesso em: " + arquivoDestino.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro ao Salvar Jogo");
                alert.setHeaderText(null);
                alert.setContentText("Houve um erro ao tentar salvar o jogo: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Abre um diálogo para o usuário escolher o local onde deseja salvar o arquivo do jogo.
     * 
     * Utiliza um filtro de arquivos XML para garantir que o jogo seja salvo com a extensão adequada.
     * 
     * @return O arquivo escolhido pelo usuário ou null se não houver escolha.
     */
    private File escolherLocalDeSalvar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos XML", "*.xml"));
        fileChooser.setTitle("Escolha o Local para Salvar o Jogo");
        Stage stage = new Stage();
        File arquivoSelecionado = fileChooser.showSaveDialog(stage);
        return arquivoSelecionado;
    }
}