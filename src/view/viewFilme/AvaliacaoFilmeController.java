package view.viewFilme; // Ou o pacote correto

import controller.DiarioCultural;
import model.Filme;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date; // Para a data da avaliação

public class AvaliacaoFilmeController {

    @FXML private Label tituloDialogoLabel;
    @FXML private Label nomeFilmeLabel;
    @FXML private TextField notaField;
    @FXML private TextArea comentarioTextArea;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    private Filme filmeParaAvaliar;
    private DiarioCultural dc;

    @FXML
    public void initialize() {
        // Pode adicionar um listener ao notaField para validação em tempo real, se desejar
    }

    /**
     * Injeta o filme a ser avaliado e a instância do DiarioCultural.
     * Chamado pelo FilmeViewController antes de mostrar o diálogo.
     */
    public void setFilmeEContexto(Filme filme, DiarioCultural dc) {
        this.filmeParaAvaliar = filme;
        this.dc = dc;

        if (filme != null) {
            nomeFilmeLabel.setText(filme.getTitulo());
            // Opcional: Se você quiser carregar uma avaliação existente para edição,
            // você precisaria de uma lógica aqui para buscar a última avaliação do usuário
            // e preencher notaField e comentarioTextArea. Por agora, focaremos em nova avaliação.
        }
    }

    @FXML
    private void handleSalvarAvaliacao() {
        if (filmeParaAvaliar == null || dc == null) {
            exibirAlerta("Erro Crítico", "Dados do filme ou sistema não disponíveis.", Alert.AlertType.ERROR);
            return;
        }

        String notaStr = notaField.getText();
        String comentario = comentarioTextArea.getText();
        int notaNumerica;

        if (notaStr == null || notaStr.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "Por favor, insira uma nota (0-5).", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }

        try {
            notaNumerica = Integer.parseInt(notaStr.trim());
            if (notaNumerica < 0 || notaNumerica > 5) { // Defina seu intervalo de nota (ex: 0-5 ou 1-5)
                exibirAlerta("Nota Inválida", "A nota deve ser um número inteiro entre 0 e 5.", Alert.AlertType.WARNING);
                notaField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            exibirAlerta("Formato Inválido", "A nota deve ser um número inteiro.", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }

        // Data atual para a avaliação
        Date dataAvaliacao = new Date();

        // Chama o método do seu DiarioCultural
        // Seu método avaliarFilme(String titulo, int avaliacao, String comentario, Date data)
        // já deve chamar PersistenciaJson.salvar(this.dc) internamente.
        dc.avaliarFilme(filmeParaAvaliar.getTitulo(), notaNumerica, comentario, dataAvaliacao);

        System.out.println("Avaliação salva para '" + filmeParaAvaliar.getTitulo() + "': " + notaNumerica + " estrelas, Comentário: " + comentario);
        exibirAlerta("Sucesso", "Avaliação salva com sucesso!", Alert.AlertType.INFORMATION);

        fecharJanela();
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow(); // Pega o Stage a partir de qualquer nó
        stage.close();
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}