package view.viewSerie; // Certifique-se de que este é o pacote correto

import controller.DiarioCultural;
import model.Serie;  // Importa a classe Serie
import persistence.PersistenciaJson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private void carregarDiarioCultural() {
        dc = PersistenciaJson.carregar();
        if (dc == null) {
            dc = new DiarioCultural();
            System.out.println("Novo DiarioCultural para Séries criado.");
        } else {
            System.out.println("DiarioCultural carregado com " + (dc.getSeries() != null ? dc.getSeries().size() : 0) + " séries.");
        }
    }

    private void popularComboBoxDeOrdenacao() {
        ordenarSerieComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhores Avaliadas", "Piores Avaliadas",
                "Mais Recentes", "Mais Antigas"
        ));
        ordenarSerieComboBox.setValue("Padrão (Entrada)");
    }

    private void configurarListenersOuAcaoDoBotao() {
        ordenarSerieComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }

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
                case "Mais Antigos":
                    seriesFiltradas.sort(Comparator.comparing(Serie::getAnoLancamento, Comparator.nullsLast(Integer::compareTo)));
                    break;
            }
        }

        seriesEmExibicao.setAll(seriesFiltradas != null ? seriesFiltradas : new ArrayList<>());
        atualizarStatusLabel();
    }

    @FXML
    private void handleLimparFiltros() {
        tituloSearchField.clear();
        generoSearchField.clear();
        anoSearchField.clear();
        ordenarSerieComboBox.setValue("Padrão (Entrada)");
        executarBuscaFiltragemOrdenacao();
    }

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

    @FXML
    private void handleAdicionarNovaSerie() {
        abrirDialogoEdicaoSerie(null); // Chama o método de edição com null para indicar "novo"
    }

    public void refreshViewData() {
        carregarDiarioCultural();
        if (dc.getSeries() != null) {
            todosOsSeriesCache.setAll(dc.getSeries());
        } else {
            todosOsSeriesCache.clear();
        }
        executarBuscaFiltragemOrdenacao();
    }

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

    private void exibirAlertaSimples(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}
