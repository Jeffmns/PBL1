package view.viewFilme;

import controller.DiarioCultural;
import model.Filme;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class FormularioFilmeController {

    @FXML private Label tituloFormularioLabel;
    @FXML private TextField tituloField;
    @FXML private TextField generoField;
    @FXML private TextField anoField;
    @FXML private TextField duracaoField;
    @FXML private TextField direcaoField;
    @FXML private TextField elencoField;
    @FXML private TextField ondeAssistirField;
    @FXML private CheckBox assistidoCheck;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    private DiarioCultural dc;
    private Filme filmeParaEditar;
    private boolean modoEdicao = false;

    public void initialize() {

    }

    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    public void carregarDadosParaEdicao(Filme filme) {
        this.filmeParaEditar = filme;
        this.modoEdicao = true;
        this.tituloFormularioLabel.setText("Editar filme");

        if (filme != null) {
            tituloField.setText(filme.getTitulo());
            generoField.setText(filme.getGenero());
            anoField.setText(filme.getAno_lancamento() > 0 ? String.valueOf(filme.getAno_lancamento()) : "");
            duracaoField.setText(filme.getDuracao() > 0 ? String.valueOf(filme.getDuracao()) : "");
            direcaoField.setText(filme.getDirecao());
            elencoField.setText(filme.getElenco());
            ondeAssistirField.setText(filme.getOnde_assistir());
            assistidoCheck.setSelected(filme.isAssistido());
        }
    }

    @FXML
    private void handleSalvar() {
        if (dc == null) {
            exibirAlerta("Erro Crítico", "O sistema de dados (DiarioCultural) não foi inicializado.", Alert.AlertType.ERROR);
            return;
        }

        String titulo = tituloField.getText();
        String genero = generoField.getText();
        String anoStr = anoField.getText();
        String duracaoStr = duracaoField.getText();
        String direcao = direcaoField.getText();
        String elenco = elencoField.getText();
        String ondeAssistir = ondeAssistirField.getText();
        boolean assistido = assistidoCheck.isSelected();

        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título do filme não pode estar vazio.", Alert.AlertType.WARNING);
            tituloField.requestFocus();
            return;
        }

        int anoLancamento = 0; // Valor padrão ou indicativo de não informado
        if (anoStr != null && !anoStr.trim().isEmpty()) {
            try {
                anoLancamento = Integer.parseInt(anoStr.trim());
                if (anoLancamento <= 1800 || anoLancamento > java.time.Year.now().getValue() + 5) { // Validação simples
                    exibirAlerta("Ano Inválido", "O ano de lançamento parece inválido.", Alert.AlertType.WARNING);
                    anoField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                exibirAlerta("Formato Inválido", "O ano de lançamento deve ser um número (ex: 2023).", Alert.AlertType.WARNING);
                anoField.requestFocus();
                return;
            }
        }

        int duracaoMin = 0; // Valor padrão
        if (duracaoStr != null && !duracaoStr.trim().isEmpty()) {
            try {
                duracaoMin = Integer.parseInt(duracaoStr.trim());
                if (duracaoMin <= 0) {
                    exibirAlerta("Duração Inválida", "A duração deve ser um número positivo de minutos.", Alert.AlertType.WARNING);
                    duracaoField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                exibirAlerta("Formato Inválido", "A duração deve ser um número (em minutos).", Alert.AlertType.WARNING);
                duracaoField.requestFocus();
                return;
            }
        }

        try {
            if (modoEdicao && filmeParaEditar != null) {
                filmeParaEditar.setTitulo(titulo);
                filmeParaEditar.setGenero(genero);
                filmeParaEditar.setAno_lancamento(anoLancamento);
                filmeParaEditar.setDuracao(duracaoMin);
                filmeParaEditar.setDirecao(direcao);
                filmeParaEditar.setElenco(elenco);
                filmeParaEditar.setOnde_assistir(ondeAssistir);
                filmeParaEditar.setAssistido(assistido);
                // As avaliações são mantidas, não são editadas aqui.

                PersistenciaJson.salvar(dc); // Salva o DiarioCultural com o filme modificado
                System.out.println("Filme atualizado: " + filmeParaEditar.getTitulo());
                exibirAlerta("Sucesso", "Filme '" + filmeParaEditar.getTitulo() + "' atualizado com sucesso!", Alert.AlertType.INFORMATION);

            } else {
                // Certifique-se que sua classe Filme tem um construtor que aceita estes parâmetros
                // e inicializa a lista de avaliações internamente.
                Filme novoFilme = new Filme(titulo, genero, anoLancamento, duracaoMin, direcao, elenco, ondeAssistir);
                dc.cadastrarFilme(novoFilme); // Este método em DiarioCultural já deve chamar PersistenciaJson.salvar(dc)
                System.out.println("Novo filme cadastrado: " + novoFilme.getTitulo());
                exibirAlerta("Sucesso", "Filme '" + novoFilme.getTitulo() + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
            fecharJanela();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar o filme: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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