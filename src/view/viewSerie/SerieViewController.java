package view.viewSerie; // Certifique-se de que este é o pacote correto

import controller.DiarioCultural;
import javafx.scene.control.*;
import model.Filme;
import model.Serie;  // Importa a classe Serie
import persistence.PersistenciaJson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller para a tela principal de Séries (SerieView.fxml).
 * Esta classe é responsável por gerir toda a interatividade da tela de séries,
 * como a busca, filtragem, ordenação e a abertura de diálogos para adicionar,
 * editar, avaliar ou remover séries.
 */
public class SerieViewController {

    // --- Campos FXML (adaptados para os filtros de Série) ---
    @FXML private TextField tituloSearchField;
    @FXML private TextField generoSearchField;
    @FXML private TextField elencoSearchField;
    @FXML private TextField anoSearchField;

    @FXML private ComboBox<String> ordenarSerieComboBox;
    @FXML private Button adicionarSerieButton;
    @FXML private Button buscarButton;
    @FXML private Button limparFiltrosButton;
    @FXML private Label statusLabel;
    @FXML private ListView<Serie> seriesListView; // Agora é uma ListView de Séries

    // --- Variáveis de Instância ---
    private DiarioCultural dc;
    private ObservableList<Serie> todosOsSeriesCache;
    private ObservableList<Serie> seriesEmExibicao;

    @FXML
    public void initialize() {
        carregarDiarioCultural();
        popularComboBoxDeOrdenacao();
        configurarListenersOuAcaoDoBotao();

        if (dc != null && dc.getSeries() != null) { // Usa dc.getSeries()
            todosOsSeriesCache = FXCollections.observableArrayList(dc.getSeries());
        } else {
            todosOsSeriesCache = FXCollections.observableArrayList();
        }
        seriesEmExibicao = FXCollections.observableArrayList(todosOsSeriesCache);

        if (seriesListView != null) {
            seriesListView.setItems(seriesEmExibicao);
            // Configura a CellFactory para usar sua célula customizada para Séries
            seriesListView.setCellFactory(listView -> new SerieListCell(this.dc, this));
        }
        atualizarStatusLabel();
    }

    /**
     * Carrega a instância principal do DiarioCultural a partir do ficheiro JSON.
     * Se o ficheiro não existir, cria um objeto DiarioCultural vazio.
     */
    private void carregarDiarioCultural() {
        dc = PersistenciaJson.carregar();
        if (dc == null) {
            dc = new DiarioCultural();
            System.out.println("Novo DiarioCultural para Séries criado.");
        } else {
            System.out.println("DiarioCultural carregado com " + (dc.getSeries() != null ? dc.getSeries().size() : 0) + " séries.");
        }
    }

    /**
     * Adiciona as opções de texto (ex: "Título (A-Z)") ao ComboBox de ordenação.
     */
    private void popularComboBoxDeOrdenacao() {
        ordenarSerieComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhores Avaliadas", "Piores Avaliadas",
                "Mais Recentes", "Mais Antigas"
        ));
        ordenarSerieComboBox.setValue("Padrão (Entrada)");
    }

    /**
     * Adiciona "ouvintes" aos campos de filtro.
     * Isto faz com que a busca seja reativa, acontecendo à medida que o usuário digita.
     */
    private void configurarListenersOuAcaoDoBotao() {
        tituloSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        elencoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        generoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        anoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        ordenarSerieComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }

    /**
     * Este método é chamado sempre que uma busca,
     * filtro ou ordenação precisa de ser executada.
     */
    @FXML
    private void executarBuscaFiltragemOrdenacao() {
        if (dc == null) {
            if (seriesEmExibicao != null) seriesEmExibicao.clear();
            atualizarStatusLabel();
            return;
        }

        String tituloQuery = tituloSearchField.getText();
        String generoQuery = generoSearchField.getText();
        String elencoQuery = elencoSearchField.getText();
        String anoQueryStr = anoSearchField.getText();

        String tituloParaBusca = (tituloQuery != null && !tituloQuery.trim().isEmpty()) ? tituloQuery.trim() : null;
        String generoParaBusca = (generoQuery != null && !generoQuery.trim().isEmpty()) ? generoQuery.trim() : null;
        String elencoParaBusca = (elencoQuery != null && !elencoQuery.trim().isEmpty()) ? elencoQuery.trim() : null;

        Integer anoParaBusca = null;
        if (anoQueryStr != null && !anoQueryStr.trim().isEmpty()) {
            try {
                anoParaBusca = Integer.parseInt(anoQueryStr.trim());
            } catch (NumberFormatException e) {
                statusLabel.setText("Ano inválido. Insira um número.");
                // Em caso de ano inválido, a busca prosseguirá sem o critério de ano
            }
        }

        List<Serie> seriesFiltradas = dc.buscarSeries(tituloParaBusca, generoParaBusca, elencoParaBusca, anoParaBusca);

        String tipoOrdem = ordenarSerieComboBox.getValue();
        if (tipoOrdem != null && seriesFiltradas != null) {
            switch (tipoOrdem) {
                case "Título (A-Z)":
                    seriesFiltradas.sort(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "Título (Z-A)":
                    seriesFiltradas.sort(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                case "Melhores Avaliadas":
                    seriesFiltradas.sort(Comparator.comparingDouble(Serie::getMediaAvaliacoes).reversed());
                    break;
                case "Piores Avaliadas":
                    seriesFiltradas.sort(Comparator.comparingDouble(Serie::getMediaAvaliacoes));
                    break;
                case "Mais Recentes":
                    seriesFiltradas.sort(Comparator.comparing(Serie::getAnoLancamento, Comparator.nullsLast(Integer::compareTo)).reversed());
                    break;
                case "Mais Antigas":
                    seriesFiltradas.sort(Comparator.comparing(Serie::getAnoLancamento, Comparator.nullsLast(Integer::compareTo)));
                    break;
            }
        }

        seriesEmExibicao.setAll(seriesFiltradas != null ? seriesFiltradas : new ArrayList<>());
        atualizarStatusLabel();
    }

    /**
     * Ação do botão "Limpar Tudo". Limpa todos os campos de filtro e reexecuta a busca
     * para mostrar todos os itens novamente.
     */
    @FXML
    private void handleLimparFiltros() {
        tituloSearchField.clear();
        generoSearchField.clear();
        anoSearchField.clear();
        elencoSearchField.clear();
        ordenarSerieComboBox.setValue("Padrão (Entrada)");
        executarBuscaFiltragemOrdenacao();
    }

    /**
     * Atualiza o label no rodapé com a contagem de itens encontrados.
     */
    private void atualizarStatusLabel() {
        if (seriesEmExibicao == null || seriesEmExibicao.isEmpty()) {
            boolean algumFiltroAtivo = (tituloSearchField.getText() != null && !tituloSearchField.getText().trim().isEmpty()) ||
                    (generoSearchField.getText() != null && !generoSearchField.getText().trim().isEmpty()) ||
                    (anoSearchField.getText() != null && !anoSearchField.getText().trim().isEmpty()) ||
                    (elencoSearchField.getText() != null && !elencoSearchField.getText().trim().isEmpty());
            if (!algumFiltroAtivo) {
                statusLabel.setText("Nenhuma série cadastrada ou carregada.");
            } else {
                statusLabel.setText("Nenhuma série encontrada com os filtros atuais.");
            }
        } else {
            statusLabel.setText(seriesEmExibicao.size() + " série(s) encontrada(s).");
        }
    }

    /**
     * Ação do botão "+ Adicionar Nova Série". Chama o método que abre o diálogo do formulário.
     */
    @FXML
    private void handleAdicionarNovaSerie() {
        abrirDialogoEdicaoSerie(null); // Chama o método de edição com null para indicar "novo"
    }

    /**
     * Força uma recarga completa dos dados a partir do ficheiro JSON e atualiza a tela.
     * Útil para ser chamado após adicionar ou editar um item em um diálogo separado.
     */
    public void refreshViewData() {
        carregarDiarioCultural();
        if (dc.getSeries() != null) {
            todosOsSeriesCache.setAll(dc.getSeries());
        } else {
            todosOsSeriesCache.clear();
        }
        executarBuscaFiltragemOrdenacao();
    }

    /**
     * Abre um diálogo de alerta para mostrar os detalhes completos de uma série.
     * @param serie A série a ser detalhada.
     */
    public void mostrarDetalhesDaSerie(Serie serie) {
        if (serie == null) return;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes da Série");
        alert.setHeaderText(serie.getTitulo());

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Ano de Lançamento: ").append(serie.getAnoLancamento()).append("\n");
        detalhes.append("Temporadas: ").append(serie.getTemporadas().size()).append("\n");
        detalhes.append("Gênero: ").append(serie.getGenero() != null ? serie.getGenero() : "N/A").append("\n");
        detalhes.append("Elenco: ").append(serie.getElenco() != null ? serie.getElenco() : "N/A").append("\n");
        detalhes.append("Onde Assistir: ").append(serie.getOnde_assistir() != null ? serie.getOnde_assistir() : "N/A").append("\n");

        detalhes.append("Status: ").append(serie.getStatusAssistido()).append("\n"); // Usa seu método de status


        double media = serie.getMediaAvaliacoes();
        detalhes.append("Nota Média Geral: ").append(media > 0 ? String.format("%.1f ★", media) : "Não avaliada").append("\n");

        alert.setContentText(detalhes.toString());
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Abre o diálogo do formulário para adicionar uma nova série ou editar uma existente.
     * @param serieParaEditar A série a ser editada, ou 'null' se for para adicionar uma nova.
     */
    public void abrirDialogoEdicaoSerie(Serie serieParaEditar) {
        String fxmlPath = "/view/viewSerie/FormularioCadastroSerie.fxml";
        String tituloJanela = serieParaEditar == null ? "Adicionar Nova Série" : "Editar Série";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            FormularioSerieController formController = loader.getController();
            formController.setDiarioCultural(this.dc);

            if (serieParaEditar != null) {
                formController.carregarDadosParaEdicao(serieParaEditar);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(tituloJanela);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            if (adicionarSerieButton.getScene() != null) dialogStage.initOwner(adicionarSerieButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            refreshViewData();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaSimples("Erro", "Não foi possível abrir o formulário.", e.getMessage());
        }
    }

    /**
     * Abre o diálogo para avaliar uma série.
     * @param serieParaAvaliar A série a ser avaliada.
     */
    public void abrirDialogoAvaliacaoSerie(Serie serieParaAvaliar) {
        String fxmlPath = "/view/viewSerie/AvaliacaoSerie.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            AvaliacaoSerieController avaliacaoController = loader.getController();
            avaliacaoController.setSerieEContexto(serieParaAvaliar, this.dc);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Avaliar Temporada de: " + serieParaAvaliar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            if (seriesListView.getScene() != null) dialogStage.initOwner(seriesListView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            refreshViewData();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaSimples("Erro", "Não foi possível abrir o diálogo de avaliação.", e.getMessage());
        }
    }

    /**
     * Abre um diálogo para exibir o histórico de todas as avaliações de uma série.
     * @param serie A série cujo histórico será mostrado.
     */
    public void abrirDialogoHistoricoAvaliacoes(Serie serie) {
        if (serie == null) return;

        // Verifica se a série tem alguma temporada com avaliação
        boolean temAvaliacoes = serie.getTemporadas().stream()
                .anyMatch(temporada -> temporada.getAvaliacao() != null && !temporada.getAvaliacao().isEmpty());

        if (!temAvaliacoes) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Histórico de Avaliações");
            info.setHeaderText("Nenhuma avaliação para '" + serie.getTitulo() + "'");
            info.setContentText("Nenhuma temporada desta série foi avaliada ainda.");
            info.showAndWait();
            return;
        }

        try {
            // Carrega o FXML do novo diálogo de histórico para séries
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewSerie/HistoricoAvaliacoesSerie.fxml"));
            Parent root = loader.load();

            // Obtém o controller do diálogo e passa o objeto Serie completo
            HistoricoSerieController controller = loader.getController();
            controller.setSerie(serie);

            // Cria e configura o novo Stage (janela/diálogo)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Histórico de Avaliações: " + serie.getTitulo());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (seriesListView.getScene() != null) {
                dialogStage.initOwner(seriesListView.getScene().getWindow());
            }
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            // Lidar com erros de carregamento do FXML
            exibirAlertaSimples("Erro", "Não foi possível abrir o histórico de avaliações.", e.getMessage());
        }
    }

    /**
     * Método chamado pela célula para solicitar a remoção de uma série.
     * Ele usa a instância atualizada do DiarioCultural para garantir a consistência.
     * @param serie O objeto Serie a ser removido.
     */
    public void abrirDialogoRemocaoSerie(Serie serie) {
        if (serie == null || dc == null) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Série: " + serie.getTitulo());
        confirmacao.setContentText("Você tem certeza que deseja excluir esta série permanentemente?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            // Usa o método removerFilme do DiarioCultural, que já salva no JSON.
            boolean removido = dc.removerSerie(serie);

            if (removido) {
                System.out.println("Série removida com sucesso: " + serie.getTitulo());
                refreshViewData();
            } else {

                Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover o filme.");
                erro.showAndWait();
            }
        }
    }

    /**
     * Método Auxiliar para exibir alerta simples
     */
    private void exibirAlertaSimples(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
