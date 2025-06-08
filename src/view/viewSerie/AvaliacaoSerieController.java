package view.viewSerie; // Certifique-se de que o pacote está correto

import controller.DiarioCultural;
import model.Serie;
import model.Temporada;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Date;

public class AvaliacaoSerieController {

    // --- Campos FXML ---
    @FXML private Label nomeSerieLabel;
    @FXML private ComboBox<Temporada> temporadaComboBox; // O ComboBox conterá objetos Temporada
    @FXML private TextField notaField;
    @FXML private TextArea comentarioTextArea;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    // --- Variáveis de Instância ---
    private Serie serieParaAvaliar;
    private DiarioCultural dc;

    @FXML
    public void initialize() {
        // Configura como o ComboBox deve exibir os objetos Temporada
        temporadaComboBox.setConverter(new StringConverter<Temporada>() {
            @Override
            public String toString(Temporada temporada) {
                if (temporada == null) {
                    return null;
                } else {
                    // Exibe "Temporada X" e se já foi assistida
                    return "Temporada " + temporada.getNumero() + (temporada.isAssistido() ? " (Assistida)" : "");
                }
            }

            @Override
            public Temporada fromString(String string) {
                // Não precisamos implementar isso para um ComboBox não editável
                return null;
            }
        });
    }

    /**
     * Injeta a série a ser avaliada e a instância do DiarioCultural.
     * Chamado pelo SerieViewController antes de mostrar o diálogo.
     */
    public void setSerieEContexto(Serie serie, DiarioCultural dc) {
        this.serieParaAvaliar = serie;
        this.dc = dc;

        if (serie != null) {
            nomeSerieLabel.setText(serie.getTitulo());
            // Popula o ComboBox com as temporadas da série
            temporadaComboBox.setItems(FXCollections.observableArrayList(serie.getTemporadas()));
        }
    }

    @FXML
    private void handleSalvarAvaliacao() {
        // 1. Obter a temporada selecionada
        Temporada temporadaSelecionada = temporadaComboBox.getValue();
        if (temporadaSelecionada == null) {
            exibirAlerta("Campo Obrigatório", "Por favor, selecione uma temporada para avaliar.", Alert.AlertType.WARNING);
            return;
        }

        // 2. Validar a nota
        String notaStr = notaField.getText();
        int notaNumerica;
        if (notaStr == null || notaStr.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "Por favor, insira uma nota (0-5).", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }
        try {
            notaNumerica = Integer.parseInt(notaStr.trim());
            if (notaNumerica < 0 || notaNumerica > 5) { // Defina seu intervalo
                exibirAlerta("Nota Inválida", "A nota deve ser um número inteiro entre 0 e 5.", Alert.AlertType.WARNING);
                notaField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            exibirAlerta("Formato Inválido", "A nota deve ser um número inteiro.", Alert.AlertType.WARNING);
            notaField.requestFocus();
            return;
        }

        // 3. Obter os outros dados
        String comentario = comentarioTextArea.getText();
        Date data = new Date(); // Data atual

        // 4. Chamar o método do seu DiarioCultural para avaliar a temporada específica
        // Este método já deve marcar a temporada como 'assistido = true' e salvar no JSON.
        dc.avaliarTemporadaSerie(serieParaAvaliar.getTitulo(), temporadaSelecionada.getNumero(), notaNumerica, comentario, data);

        System.out.println("Avaliação salva para " + serieParaAvaliar.getTitulo() + " - Temporada " + temporadaSelecionada.getNumero());
        exibirAlerta("Sucesso", "Avaliação para a Temporada " + temporadaSelecionada.getNumero() + " salva!", Alert.AlertType.INFORMATION);

        fecharJanela();
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
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
