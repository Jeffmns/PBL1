package view.viewFilme;

import controller.DiarioCultural;
import model.Filme; // Importe Filme
import persistence.PersistenciaJson;
import model.Review;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView; // Alterado para ListView
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilmeViewController {

    @FXML private TextField tituloSearchField;
    @FXML private TextField diretorSearchField;
    @FXML private TextField atorSearchField;
    @FXML private TextField generoSearchField;
    @FXML private TextField anoSearchField;
    @FXML private ComboBox<String> ordenarFilmeComboBox;
    @FXML private Button adicionarFilmeButton;
    @FXML private Button buscarButton;
    @FXML private Button limparFiltrosButton;
    @FXML private Label statusLabel;
    @FXML private ListView<Filme> filmesListView; // Alterado para ListView

    private DiarioCultural dc;
    private ObservableList<Filme> todosOsFilmesCache;
    private ObservableList<Filme> filmesEmExibicao;

    @FXML
    public void initialize() {
        carregarDiarioCultural();
        popularComboBoxDeOrdenacao();
        configurarListenersOuAcaoDoBotao();

        if (dc != null && dc.getFilmes() != null) { // Usar getFilmes()
            todosOsFilmesCache = FXCollections.observableArrayList(dc.getFilmes());
        } else {
            todosOsFilmesCache = FXCollections.observableArrayList();
        }
        filmesEmExibicao = FXCollections.observableArrayList(todosOsFilmesCache);

        if (filmesListView != null) {
            filmesListView.setItems(filmesEmExibicao);
            // Configura a CellFactory para usar sua célula customizada
            filmesListView.setCellFactory(listView -> new FilmeListCell(this.dc, this));
        }
        atualizarStatusLabel();
    }

    private void carregarDiarioCultural() {
        dc = PersistenciaJson.carregar();
        if (dc == null) {
            dc = new DiarioCultural();
            System.out.println("Novo DiarioCultural para Filmes criado.");
        } else {
            System.out.println("DiarioCultural carregado com " + (dc.getFilmes() != null ? dc.getFilmes().size() : 0) + " filmes.");
        }
    }

    private void popularComboBoxDeOrdenacao() {
        ordenarFilmeComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhores Avaliados", "Piores Avaliados",
                "Mais Recentes", "Mais Antigos" // (Ano de Lançamento)
        ));
        ordenarFilmeComboBox.setValue("Padrão (Entrada)");
    }

    private void configurarListenersOuAcaoDoBotao() {
        ordenarFilmeComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }

    @FXML
    private void executarBuscaFiltragemOrdenacao() {
        if (dc == null) {
            if(filmesEmExibicao != null) filmesEmExibicao.clear();
            atualizarStatusLabel();
            return;
        }

        String tituloQuery = tituloSearchField.getText();
        String diretorQuery = diretorSearchField.getText();
        String atorQuery = atorSearchField.getText(); // Para buscar no elenco
        String generoQuery = generoSearchField.getText();
        String anoQueryStr = anoSearchField.getText();

        String tituloParaBusca = (tituloQuery != null && !tituloQuery.trim().isEmpty()) ? tituloQuery.trim() : null;
        String diretorParaBusca = (diretorQuery != null && !diretorQuery.trim().isEmpty()) ? diretorQuery.trim() : null;
        String atorParaBusca = (atorQuery != null && !atorQuery.trim().isEmpty()) ? atorQuery.trim() : null;
        String generoParaBusca = (generoQuery != null && !generoQuery.trim().isEmpty()) ? generoQuery.trim() : null;

        Integer anoParaBusca = null;
        if (anoQueryStr != null && !anoQueryStr.trim().isEmpty()) {
            try {
                anoParaBusca = Integer.parseInt(anoQueryStr.trim());
            } catch (NumberFormatException e) {
                statusLabel.setText("Ano inválido. Insira um número.");
            }
        }

        // Usar o método de busca de filmes do DiarioCultural
        List<Filme> filmesFiltrados = dc.buscarFilmes(tituloParaBusca, diretorParaBusca, atorParaBusca, generoParaBusca, anoParaBusca);

        String tipoOrdem = ordenarFilmeComboBox.getValue();
        if (tipoOrdem != null && filmesFiltrados != null) {
            switch (tipoOrdem) {
                case "Título (A-Z)":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "Título (Z-A)":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                case "Melhor Avaliados":
                    filmesFiltrados.sort(Comparator.comparingDouble(Filme::getMediaAvaliacoes).reversed());
                    break;
                case "Pior Avaliados":
                    filmesFiltrados.sort(Comparator.comparingDouble(Filme::getMediaAvaliacoes));
                    break;
                case "Mais Recentes":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getAno_lancamento, Comparator.nullsLast(Integer::compareTo)).reversed());
                    break;
                case "Mais Antigos":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getAno_lancamento, Comparator.nullsLast(Integer::compareTo)));
                    break;
            }
        }

        filmesEmExibicao.setAll(filmesFiltrados != null ? filmesFiltrados : new ArrayList<>());
        atualizarStatusLabel();
    }

    @FXML
    private void handleLimparFiltros() {
        tituloSearchField.clear();
        diretorSearchField.clear();
        atorSearchField.clear();
        generoSearchField.clear();
        anoSearchField.clear();
        ordenarFilmeComboBox.setValue("Padrão (Entrada)");
        executarBuscaFiltragemOrdenacao();
    }

    private void atualizarStatusLabel() {
        // ... (Lógica similar ao LivroViewController, adaptada para filmes) ...
        if (filmesEmExibicao == null || filmesEmExibicao.isEmpty()) {
            boolean algumFiltroAtivo = // ... (verificar todos os campos de filtro de filme)
                    (tituloSearchField.getText() != null && !tituloSearchField.getText().trim().isEmpty()) ||
                            (diretorSearchField.getText() != null && !diretorSearchField.getText().trim().isEmpty()) ||
                            (atorSearchField.getText() != null && !atorSearchField.getText().trim().isEmpty()) ||
                            (generoSearchField.getText() != null && !generoSearchField.getText().trim().isEmpty()) ||
                            (anoSearchField.getText() != null && !anoSearchField.getText().trim().isEmpty());

            if (!algumFiltroAtivo) {
                statusLabel.setText("Nenhum filme cadastrado ou carregado.");
            } else {
                statusLabel.setText("Nenhum filme encontrado com os filtros atuais.");
            }
        } else {
            statusLabel.setText(filmesEmExibicao.size() + " filme(s) encontrado(s).");
        }
    }

    @FXML
    private void handleAdicionarNovoFilme() {
        System.out.println("Abrindo formulário para adicionar novo filme...");
        try {
            // Você precisará criar um FormularioCadastroFilme.fxml e seu controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/FormularioCadastroFilme.fxml"));
            Parent root = loader.load();

            FormularioFilmeController formController = loader.getController();
            formController.setDiarioCultural(this.dc);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastrar Novo Filme");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            refreshViewData();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao abrir formulário de cadastro de filme.");
        }
    }

    public void refreshViewData() {
        System.out.println("FilmeViewController: Recarregando e atualizando dados.");
        carregarDiarioCultural();
        if (dc.getFilmes() != null) { // Usar getFilmes()
            todosOsFilmesCache.setAll(dc.getFilmes());
        } else {
            todosOsFilmesCache.clear();
        }
        executarBuscaFiltragemOrdenacao();
    }

    public void mostrarDetalhesDoFilme(Filme filme) {
        if (filme == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Define o tipo de alerta
        alert.setTitle("Detalhes do Filme");
        alert.setHeaderText(filme.getTitulo()); // Título do filme como cabeçalho do alerta

        // Monta uma string com todos os detalhes
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Ano de Lançamento: ").append(filme.getAno_lancamento()).append("\n");
        detalhes.append("Duração: ").append(formatarDuracao(filme.getDuracao())).append("\n"); // Você precisaria de um método formatarDuracao
        detalhes.append("Gênero: ").append(filme.getGenero() != null ? filme.getGenero() : "N/A").append("\n");
        detalhes.append("Direção: ").append(filme.getDirecao() != null ? filme.getDirecao() : "N/A").append("\n");
        detalhes.append("Elenco: ").append(filme.getElenco() != null ? filme.getElenco() : "N/A").append("\n");
        detalhes.append("Onde Assistir: ").append(filme.getOnde_assistir() != null ? filme.getOnde_assistir() : "N/A").append("\n");
        detalhes.append("Assistido: ").append(filme.isAssistido() ? "Sim" : "Não").append("\n");

        double media = calcularMediaAvaliacoes(filme); // Você precisaria de um método calcularMediaAvaliacoes
        detalhes.append("Nota Média: ").append(media > 0 ? String.format("%.1f ★", media) : "Não avaliado").append("\n");

        // Se tiver uma lista de reviews, você poderia listá-las aqui também (de forma simplificada)
        // if (filme.getAvaliacoes() != null && !filme.getAvaliacoes().isEmpty()) {
        //     detalhes.append("\nAvaliações:\n");
        //     for (Review review : filme.getAvaliacoes()) {
        //         detalhes.append("  - Nota: ").append(review.getNota()).append(", Comentário: ").append(review.getComentario()).append("\n");
        //     }
        // }

        alert.setContentText(detalhes.toString());

        // Para permitir que o conteúdo seja maior e com scroll, se necessário
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.setResizable(true); // Permite redimensionar o diálogo
        alert.showAndWait();
    }
    private String formatarDuracao(int minutos) {
        if (minutos <= 0) return "N/A";
        int horas = minutos / 60;
        int mins = minutos % 60;
        if (horas > 0) {
            return String.format("%dh %02dm", horas, mins);
        } else {
            return String.format("%dm", mins);
        }
    }

    private double calcularMediaAvaliacoes(Filme filme) {
        if (filme.getAvaliacoes() == null || filme.getAvaliacoes().isEmpty()) {
            return 0.0;
        }
        double soma = 0;
        for (Review review : filme.getAvaliacoes()) {
            soma += review.getAvaliacao(); // Supondo que Review tem getNota()
        }
        return soma / filme.getAvaliacoes().size();
    }



    public void abrirDialogoEdicaoFilme(Filme filmeParaEditar) {
        if (filmeParaEditar == null) {
            return;
        }

        System.out.println("Abrindo formulário para editar filme: " + filmeParaEditar.getTitulo());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/FormularioCadastroFilme.fxml"));
            Parent root = loader.load();

            FormularioFilmeController formController = loader.getController();
            // ... (formController.setDiarioCultural e carregarDadosParaEdicao) ...
            formController.setDiarioCultural(this.dc);
            formController.carregarDadosParaEdicao(filmeParaEditar);


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Filme: " + filmeParaEditar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // CORREÇÃO AQUI: Usando filmesListView ou um botão da tela principal
            if (filmesListView != null && filmesListView.getScene() != null && filmesListView.getScene().getWindow() != null) {
                dialogStage.initOwner(filmesListView.getScene().getWindow());
            } else if (adicionarFilmeButton != null && adicionarFilmeButton.getScene() != null && adicionarFilmeButton.getScene().getWindow() != null) {
                // Fallback se a listView não estiver visível ou disponível no momento
                dialogStage.initOwner(adicionarFilmeButton.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            System.out.println("Formulário de edição de filme fechado. Atualizando dados...");
            refreshViewData();

        } catch (Exception e) { // Captura mais genérica para cobrir IOException, NullPointerException do controller, etc.
            e.printStackTrace();
        }
    }
    public void abrirDialogoAvaliacaoFilme(Filme filmeParaAvaliar) {
        if (filmeParaAvaliar == null) {
            System.err.println("Tentativa de avaliar um filme nulo.");
            exibirAlertaSimples("Seleção Inválida", null, "Nenhum filme foi selecionado para avaliação.");
            return;
        }

        System.out.println("Abrindo formulário para avaliar filme: " + filmeParaAvaliar.getTitulo());
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/AvaliacaoFilme.fxml"));
            Parent root = loader.load();

            AvaliacaoFilmeController avaliacaoController = loader.getController();
            if (avaliacaoController == null) {
                System.err.println("Erro crítico: Não foi possível obter o controller do diálogo de avaliação.");
                exibirAlertaSimples("Erro Crítico", "Falha ao carregar componentes internos.", "Controller do diálogo de avaliação não encontrado.");
                return;
            }

            avaliacaoController.setFilmeEContexto(filmeParaAvaliar, this.dc);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Avaliar Filme: " + filmeParaAvaliar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Bloqueia interação com outras janelas

            if (filmesListView != null && filmesListView.getScene() != null && filmesListView.getScene().getWindow() != null) {
                dialogStage.initOwner(filmesListView.getScene().getWindow());
            } else if (adicionarFilmeButton != null && adicionarFilmeButton.getScene() != null && adicionarFilmeButton.getScene().getWindow() != null) {
                dialogStage.initOwner(adicionarFilmeButton.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false); // Opcional

            dialogStage.showAndWait();

            System.out.println("Diálogo de avaliação de filme fechado. Atualizando dados...");
            refreshViewData();

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlertaSimples("Erro de Carregamento", "Não foi possível abrir o diálogo de avaliação.", "Ocorreu um erro ao carregar o arquivo FXML: " + e.getMessage());
        } catch (Exception e) { // Captura outras exceções inesperadas
            e.printStackTrace();
            exibirAlertaSimples("Erro Inesperado", "Ocorreu um erro inesperado.", "Detalhes: " + e.getMessage());
        }
    }

    private void exibirAlertaSimples(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Ou outro tipo de alerta
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

}