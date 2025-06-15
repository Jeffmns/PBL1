package view.viewSerie; // Certifique-se de que este é o pacote correto

import controller.DiarioCultural;
import model.Serie;
import model.Temporada;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para o formulário de Adicionar ou Editar uma Série.
 * Esta classe é o "controller" da janela que abre para preencher os dados de uma série.
 * Ela lida com a validação dos dados e com o salvamento das informações.
 */
public class FormularioSerieController {

    // --- Campos FXML ---
    @FXML private Label tituloFormularioLabel;
    @FXML private TextField tituloField;
    @FXML private TextField generoField;
    @FXML private TextField anoField;
    @FXML private TextField elencoField;
    @FXML private TextField ondeAssistirField;
    @FXML private TextField numTemporadasField;
    // O CheckBox 'acompanhandoCheck' foi removido para seguir a lógica de status derivado
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    // --- Variáveis de Instância ---
    private DiarioCultural dc;
    private Serie serieParaEditar;
    private boolean modoEdicao = false;

    @FXML
    public void initialize() {
    }

    /**
     * Recebe a instância principal do DiarioCultural a partir da tela que abriu este formulário.
     * É assim que este controller ganha acesso à lista de séries para salvar os dados.
     * @param dc A instância principal do DiarioCultural.
     */
    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    /**
     * Preenche os campos do formulário com os dados de uma série existente.
     * Este método transforma o formulário do modo "Adicionar" para o modo "Editar".
     * @param serie O objeto Serie cujos dados serão editados.
     */
    public void carregarDadosParaEdicao(Serie serie) {
        this.serieParaEditar = serie;
        this.modoEdicao = true;
        this.tituloFormularioLabel.setText("Editar Série");

        if (serie != null) {
            tituloField.setText(serie.getTitulo());
            generoField.setText(serie.getGenero());
            anoField.setText(String.valueOf(serie.getAnoLancamento()));
            elencoField.setText(serie.getElenco());
            ondeAssistirField.setText(serie.getOnde_assistir());
            numTemporadasField.setText(String.valueOf(serie.getTemporadas().size()));

            // Importante: Desabilitar a edição do número de temporadas para simplificar a lógica.
            // Mudar o número de temporadas em uma série já cadastrada é uma operação complexa.
            numTemporadasField.setDisable(true);
        }
    }

    /**
     * Ação executada quando o botão "Salvar" é clicado.
     * Valida os dados inseridos e, se estiverem corretos, cria um novo livro ou atualiza um existente.
     */
    @FXML
    private void handleSalvar() {
        if (dc == null) {
            exibirAlerta("Erro Crítico", "O sistema de dados não foi inicializado.", Alert.AlertType.ERROR);
            return;
        }

        String titulo = tituloField.getText();
        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título não pode estar vazio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Coleta os dados dos campos
            String genero = generoField.getText();
            int ano = Integer.parseInt(anoField.getText());
            String elenco = elencoField.getText();
            String ondeAssistir = ondeAssistirField.getText();

            if (modoEdicao) {
                // Modo Edição: Atualiza o objeto existente
                serieParaEditar.setTitulo(titulo);
                serieParaEditar.setGenero(genero);
                serieParaEditar.setAno_lancamento(ano);
                serieParaEditar.setElenco(elenco);
                serieParaEditar.setOnde_assistir(ondeAssistir);

                // Salva o estado do DiarioCultural com a série modificada
                PersistenciaJson.salvar(dc);
                System.out.println("Série atualizada: " + serieParaEditar.getTitulo());
                fecharJanela();
            } else {
                // Modo Adicionar: Cria uma nova Série
                int numTemporadas = Integer.parseInt(numTemporadasField.getText());
                if (numTemporadas <= 0) {
                    exibirAlerta("Dado Inválido", "O número de temporadas deve ser maior que zero.", Alert.AlertType.WARNING);
                    return;
                }

                // Certifique-se de que sua classe Serie tem um construtor que aceita estes parâmetros
                Serie novaSerie = new Serie(titulo, genero, ano, elenco, ondeAssistir);

                // CRUCIAL: Cria as temporadas vazias com base no número informado
                for (int i = 1; i <= numTemporadas; i++) {
                    // Supondo que Temporada tem um construtor como new Temporada(int numero)
                    novaSerie.getTemporadas().add(new Temporada(i));
                }

                // 2. VERIFICA SE A SÉRIE JÁ EXISTE ANTES DE TENTAR CADASTRAR
                if (dc.getSeries().contains(novaSerie)) {
                    // Se já existe, mostra o alerta e NÃO fecha a janela
                    exibirAlerta("Série Duplicada",
                            "Uma série com os mesmos dados já existe no seu diário.",
                            Alert.AlertType.WARNING);
                } else {
                    // Se não existe, prossegue com o cadastro
                    dc.cadastrarSerie(novaSerie); // Este método agora só adiciona e salva
                    System.out.println("Nova série cadastrada: " + novaSerie.getTitulo());
                    fecharJanela(); // Fecha a janela apenas se o cadastro foi bem-sucedido
                }

            }

            fecharJanela();

        } catch (NumberFormatException e) {
            exibirAlerta("Formato Inválido", "Ano e Nº de Temporadas devem ser números válidos.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar a série.", Alert.AlertType.ERROR);
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
