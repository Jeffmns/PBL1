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

/**
 * Controller para o formulário de Adicionar ou Editar um Filme.
 * Esta classe é o "controller" da janela que abre para preencher os dados de um filme.
 * Ela lida com a validação dos dados e com o salvamento das informações.
 */
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
    /**
     * Recebe a instância principal do DiarioCultural a partir da tela que abriu este formulário.
     * É assim que este controller ganha acesso à lista de filmes para salvar os dados.
     * @param dc A instância principal do DiarioCultural.
     */
    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    /**
     * Preenche os campos do formulário com os dados de um filme existente.
     * Este método transforma o formulário do modo "Adicionar" para o modo "Editar".
     * @param filme O objeto Filme cujos dados serão editados.
     */
    public void carregarDadosParaEdicao(Filme filme) {
        this.filmeParaEditar = filme;
        this.modoEdicao = true;
        this.tituloFormularioLabel.setText("Editar filme");

        // Se o objeto filme não for nulo, preenchemos cada campo com os seus dados.
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

    /**
     * Ação executada quando o botão "Salvar" é clicado.
     * Valida os dados inseridos e, se estiverem corretos, cria um novo filme ou atualiza um existente.
     */
    @FXML
    private void handleSalvar() {
        if (dc == null) {
            exibirAlerta("Erro Crítico", "O sistema de dados (DiarioCultural) não foi inicializado.", Alert.AlertType.ERROR);
            return;
        }
        // Pega o que o usuário digitou em cada campo.
        String titulo = tituloField.getText();
        String genero = generoField.getText();
        String anoStr = anoField.getText();
        String duracaoStr = duracaoField.getText();
        String direcao = direcaoField.getText();
        String elenco = elencoField.getText();
        String ondeAssistir = ondeAssistirField.getText();
        boolean assistido = assistidoCheck.isSelected();

        // Validação 1: O campo do título não pode estar vazio.
        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título do filme não pode estar vazio.", Alert.AlertType.WARNING);
            tituloField.requestFocus();
            return;
        }
        // Validação 2: Converte e valida o ano.
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
        // Validação 3: Converte e valida a duração.
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
            if (modoEdicao && filmeParaEditar != null) { // Se a "bandeira" de modo de edição estiver ligada
                filmeParaEditar.setTitulo(titulo);
                filmeParaEditar.setGenero(genero);
                filmeParaEditar.setAno_lancamento(anoLancamento);
                filmeParaEditar.setDuracao(duracaoMin);
                filmeParaEditar.setDirecao(direcao);
                filmeParaEditar.setElenco(elenco);
                filmeParaEditar.setOnde_assistir(ondeAssistir);
                filmeParaEditar.setAssistido(assistido);


                PersistenciaJson.salvar(dc); // Salva o DiarioCultural com o filme modificado
                System.out.println("Filme atualizado: " + filmeParaEditar.getTitulo());
                exibirAlerta("Sucesso", "Filme '" + filmeParaEditar.getTitulo() + "' atualizado com sucesso!", Alert.AlertType.INFORMATION);
                fecharJanela();
            } else {
                // Se não estiver em modo de edição, criamos um novo filme.
                Filme novoFilme = new Filme(titulo, genero, anoLancamento, duracaoMin, direcao, elenco, ondeAssistir);

                // Verifica se o filme já existe antes de cadastrar
                if (dc.getFilmes().contains(novoFilme)) {
                    // Se já existe, mostramos um aviso e não fechamos a janela.
                    exibirAlerta("Filme Duplicado",
                            "Um filme com os mesmos dados já existe no seu diário.",
                            Alert.AlertType.WARNING);
                } else {
                    // Se não existe, prossegue com o cadastro
                    dc.cadastrarFilme(novoFilme); // Este método agora só adiciona e salva
                    exibirAlerta("Sucesso", "Filme '" + novoFilme.getTitulo() + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                    System.out.println("Novo filme cadastrado: " + novoFilme.getTitulo());
                    fecharJanela(); // Fecha a janela apenas se o cadastro foi bem-sucedido
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar o filme: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
        alert.showAndWait();
    }
}