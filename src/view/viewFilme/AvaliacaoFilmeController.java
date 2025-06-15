package view.viewFilme;

import controller.DiarioCultural;
import model.Filme;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;

/**
 * Controller para o diálogo (tela) de avaliação de um filme.
 * Esta classe é responsável por pegar os dados que o usuário insere (nota e comentário)
 * e enviá-los para serem salvos no sistema.
 */
public class AvaliacaoFilmeController {


    @FXML private Label nomeFilmeLabel;       // Label que mostra o nome do filme a ser avaliado.
    @FXML private TextField notaField;        // Campo para o usuário digitar a nota.
    @FXML private TextArea comentarioTextArea; // Campo para o usuário escrever um comentário.
    @FXML private Button salvarButton;         // O botão "Salvar".
    @FXML private Button cancelarButton;       // O botão "Cancelar".

    private Filme filmeParaAvaliar;
    private DiarioCultural dc;

    /**
     * Método de inicialização do JavaFX.
     */
    @FXML
    public void initialize() {

    }

    /**
     * Recebe o objeto Filme e a instância do DiarioCultural a partir da tela principal.
     * Este é o método que "prepara" este diálogo com as informações necessárias antes de ele ser exibido.
     * @param filme O objeto Filme que será avaliado.
     * @param dc A instância principal do DiarioCultural.
     */
    public void setFilmeEContexto(Filme filme, DiarioCultural dc) {
        this.filmeParaAvaliar = filme;
        this.dc = dc;

        // Se o objeto filme não for nulo, atualiza o label com o título do filme.
        if (filme != null) {
            nomeFilmeLabel.setText(filme.getTitulo());
        }
    }

    /**
     * Ação executada quando o botão "Salvar Avaliação" é clicado.
     * Ele valida os dados inseridos e, se estiverem corretos, salva a avaliação.
     */
    @FXML
    private void handleSalvarAvaliacao() {
        // Verificação de segurança: garante que temos as informações necessárias para continuar.
        if (filmeParaAvaliar == null || dc == null) {
            exibirAlerta("Erro Crítico", "Dados do filme ou sistema não disponíveis.", Alert.AlertType.ERROR);
            return; // Interrompe a execução do método se houver um erro crítico.
        }

        // Pega o que o usuário digitou nos campos.
        String notaStr = notaField.getText();
        String comentario = comentarioTextArea.getText();
        int notaNumerica;

        // Validação 1: O campo da nota não pode estar vazio.
        if (notaStr == null || notaStr.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "Por favor, insira uma nota (0-5).", Alert.AlertType.WARNING);
            notaField.requestFocus(); // Coloca o cursor piscando no campo da nota para o usuário corrigir.
            return;
        }

        // Validação 2: A nota precisa de ser um número válido e estar no intervalo correto.
        try {
            notaNumerica = Integer.parseInt(notaStr.trim());
            if (notaNumerica < 0 || notaNumerica > 5) { // Verifica se a nota está entre 0 e 5.
                exibirAlerta("Nota Inválida", "A nota deve ser um número inteiro entre 0 e 5.", Alert.AlertType.WARNING);
                notaField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            // Se o usuário digitou algo que não é um número, este erro acontece.
            exibirAlerta("Formato Inválido", "A nota deve ser um número inteiro.", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }

        // Pega a data e hora atuais para registar quando a avaliação foi feita.
        Date dataAvaliacao = new Date();

        // Chama o método no controller do sistema (DiarioCultural) para de fato salvar a avaliação.
        dc.avaliarFilme(filmeParaAvaliar.getTitulo(), notaNumerica, comentario, dataAvaliacao);

        System.out.println("Avaliação salva para '" + filmeParaAvaliar.getTitulo() + "': " + notaNumerica + " estrelas, Comentário: " + comentario);
        exibirAlerta("Sucesso", "Avaliação salva com sucesso!", Alert.AlertType.INFORMATION);
        fecharJanela();
    }

    /**
     * Ação executada quando o botão "Cancelar" é clicado.
     */
    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    /**
     * Método auxiliar para fechar a tela (diálogo) atual.
     */
    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
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
        alert.showAndWait(); // Mostra o alerta e espera o usuário clicar em "OK".
    }
}
