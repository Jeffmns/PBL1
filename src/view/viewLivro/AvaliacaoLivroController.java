package view.viewLivro;

import controller.DiarioCultural;
import model.Livro;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date; // Para a data da avaliação

/**
 * Controller para a janela (diálogo) de avaliação de um Livro.
 * Esta classe é responsável por obter os dados que o usuário insere (nota e comentário)
 * para um livro específico e enviá-los para serem guardados no sistema.
 */
public class AvaliacaoLivroController {

    @FXML private Label tituloDialogoLabel;
    @FXML private Label nomeLivroLabel;
    @FXML private TextField notaField;
    @FXML private TextArea comentarioTextArea;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    private Livro livroParaAvaliar;
    private DiarioCultural dc;

    @FXML
    public void initialize() {
    }

    /**
     * Recebe o objeto Livro e a instância do DiarioCultural a partir da tela principal.
     * Este é o método que "prepara" este diálogo com as informações necessárias antes de ele ser exibido.
     * @param livro O objeto Livro que será avaliado.
     * @param dc A instância principal do DiarioCultural.
     */
    public void setLivroEContexto(Livro livro, DiarioCultural dc) {
        this.livroParaAvaliar = livro;
        this.dc = dc;

        if (livro != null) {
            nomeLivroLabel.setText(livro.getTitulo());
        }
    }

    /**
     * Ação executada quando o botão "Salvar Avaliação" é clicado.
     * Ele valida os dados inseridos e, se estiverem corretos, salva a avaliação.
     */
    @FXML
    private void handleSalvarAvaliacao() {
        // Verificação de segurança: garante que temos as informações necessárias para continuar.
        if (livroParaAvaliar == null || dc == null) {
            exibirAlerta("Erro Crítico", "Dados do livro ou sistema não disponíveis.", Alert.AlertType.ERROR);
            return;
        }

        String notaStr = notaField.getText();
        String comentario = comentarioTextArea.getText();
        int notaNumerica;

        // Validação 1: O campo da nota não pode estar vazio.
        if (notaStr == null || notaStr.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "Por favor, insira uma nota (0-5).", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }
        // Validação 2: A nota precisa de ser um número válido e estar no intervalo correto.
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

        // Chama o método do seu DiarioCultural, já salva no JSON e marcar o livro como lido.
        dc.avaliarLivro(livroParaAvaliar.getTitulo(), notaNumerica, comentario, dataAvaliacao);

        System.out.println("Avaliação salva para '" + livroParaAvaliar.getTitulo() + "': " + notaNumerica + " estrelas, Comentário: " + comentario);
        exibirAlerta("Sucesso", "Avaliação salva com sucesso!", Alert.AlertType.INFORMATION);

        fecharJanela();
    }

    /**
     * Ação executada quando o botão "Cancelar" é clicado. Simplesmente fecha a janela.
     */
    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    /**
     * Método auxiliar para fechar a janela (diálogo) atual.
     */
    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow(); // Pega o Stage a partir de qualquer nó
        stage.close();
    }

    /**
     * Método auxiliar para mostrar alertas de forma padronizada.
     * @param titulo O título da janela de alerta.
     * @param mensagem A mensagem principal a ser exibida no alerta.
     * @param tipo O tipo de alerta (Erro, Aviso, Informação).
     */
    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}