package view.viewFilme;

import controller.DiarioCultural;
import model.Filme;
import persistence.PersistenciaJson;

import java.util.Optional;
import javafx.scene.control.ButtonType;
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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Controller para a tela principal de Filmes (FilmeView.fxml).
 * Esta classe é responsável por gerir toda a interatividade da tela de filmes,
 * como a busca, filtragem, ordenação e a abertura de diálogos para adicionar,
 * editar ou avaliar filmes.
 */
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
    @FXML private ListView<Filme> filmesListView;

    private DiarioCultural dc;
    private ObservableList<Filme> todosOsFilmesCache;
    private ObservableList<Filme> filmesEmExibicao;


    @FXML
    public void initialize() {
        carregarDiarioCultural();
        popularComboBoxDeOrdenacao();
        configurarListenersOuAcaoDoBotao(); //Listeners para fazer a busca reativa.

        if (dc != null && dc.getFilmes() != null) {
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
    /**
     * Carrega a instância principal do DiarioCultural a partir do ficheiro JSON.
     * Se o ficheiro não existir, cria um novo objeto DiarioCultural vazio.
     */
    private void carregarDiarioCultural() {
        dc = PersistenciaJson.carregar();
        if (dc == null) {
            dc = new DiarioCultural();
            System.out.println("Novo DiarioCultural para Filmes criado.");
        } else {
            System.out.println("DiarioCultural carregado com " + (dc.getFilmes() != null ? dc.getFilmes().size() : 0) + " filmes.");
        }
    }
    /**
     * Adiciona as opções de texto (ex: "Melhores Avaliados") ao ComboBox de ordenação.
     */
    private void popularComboBoxDeOrdenacao() {
        ordenarFilmeComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhores Avaliados", "Piores Avaliados",
                "Mais Recentes", "Mais Antigos" // (Ano de Lançamento)
        ));
        ordenarFilmeComboBox.setValue("Padrão (Entrada)");
    }
    /**
     * Adiciona "ouvintes" aos campos de filtro.
     * Isto faz com que a busca seja reativa, acontecendo à medida que o usuário digita.
     */
    private void configurarListenersOuAcaoDoBotao() {
        tituloSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        diretorSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        atorSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        generoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        anoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        ordenarFilmeComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }

    /**
     * Este método é chamado sempre que uma busca,
     * filtro ou ordenação precisa de ser executada.
     */
    @FXML
    private void executarBuscaFiltragemOrdenacao() {
        if (dc == null) {
            if(filmesEmExibicao != null) filmesEmExibicao.clear();
            atualizarStatusLabel();
            return;
        }
        // Pega o texto de cada campo de filtro.
        String tituloQuery = tituloSearchField.getText();
        String diretorQuery = diretorSearchField.getText();
        String atorQuery = atorSearchField.getText(); // Para buscar no elenco
        String generoQuery = generoSearchField.getText();
        String anoQueryStr = anoSearchField.getText();
        // Converte texto vazio para 'null', para que o método de busca saiba que não deve filtrar por aquele campo.
        String tituloParaBusca = (tituloQuery != null && !tituloQuery.trim().isEmpty()) ? tituloQuery.trim() : null;
        String diretorParaBusca = (diretorQuery != null && !diretorQuery.trim().isEmpty()) ? diretorQuery.trim() : null;
        String atorParaBusca = (atorQuery != null && !atorQuery.trim().isEmpty()) ? atorQuery.trim() : null;
        String generoParaBusca = (generoQuery != null && !generoQuery.trim().isEmpty()) ? generoQuery.trim() : null;
        // Converte o texto do ano para um número, tratando possíveis erros.
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
        // Pega a opção de ordenação selecionada e aplica à lista de resultados.
        String tipoOrdem = ordenarFilmeComboBox.getValue();
        if (tipoOrdem != null && filmesFiltrados != null) {
            switch (tipoOrdem) {
                case "Título (A-Z)":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "Título (Z-A)":
                    filmesFiltrados.sort(Comparator.comparing(Filme::getTitulo, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                case "Melhores Avaliados":
                    filmesFiltrados.sort(Comparator.comparingDouble(Filme::getMediaAvaliacoes).reversed());
                    break;
                case "Piores Avaliados":
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
    /**
     * Ação do botão "Limpar Tudo". Limpa todos os campos de filtro e reexecuta a busca
     * para mostrar todos os itens novamente.
     */
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

    /**
     * Atualiza o label no rodapé com a contagem de itens encontrados.
     */
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

    /**
     * Ação do botão "+ Adicionar Novo Filme". Chama o método que abre o diálogo do formulário.
     */
    @FXML
    private void handleAdicionarNovoFilme() {
        System.out.println("Abrindo formulário para adicionar novo filme...");
        try {
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

    /**
     * Força uma atualização completa dos dados a partir do ficheiro JSON e atualiza a tela.
     * Chamado após adicionar ou editar um item em um diálogo separado.
     */
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

    /**
     * Abre um diálogo de alerta para mostrar os detalhes completos de um filme.
     * @param filme O filme a ser detalhado.
     */
    public void mostrarDetalhesDoFilme(Filme filme) {
        if (filme == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Define o tipo de alerta
        alert.setTitle("Detalhes do Filme");
        alert.setHeaderText(filme.getTitulo()); // Título do filme como cabeçalho do alerta

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Ano de Lançamento: ").append(filme.getAno_lancamento()).append("\n");
        detalhes.append("Duração: ").append(formatarDuracao(filme.getDuracao())).append("\n");
        detalhes.append("Gênero: ").append(filme.getGenero() != null ? filme.getGenero() : "N/A").append("\n");
        detalhes.append("Direção: ").append(filme.getDirecao() != null ? filme.getDirecao() : "N/A").append("\n");
        detalhes.append("Elenco: ").append(filme.getElenco() != null ? filme.getElenco() : "N/A").append("\n");
        detalhes.append("Onde Assistir: ").append(filme.getOnde_assistir() != null ? filme.getOnde_assistir() : "N/A").append("\n");
        detalhes.append("Assistido: ").append(filme.isAssistido() ? "Sim" : "Não").append("\n");

        double media = filme.getMediaAvaliacoes();
        detalhes.append("Nota Média: ").append(media > 0 ? String.format("%.1f ★", media) : "Não avaliado").append("\n");

        alert.setContentText(detalhes.toString());

        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.setResizable(true); // Permite redimensionar o diálogo
        alert.showAndWait();
    }


    /**
     * Abre o diálogo do formulário para adicionar um novo filme ou editar um existente.
     * @param filmeParaEditar O filme a ser editado, ou 'null' se for para adicionar um novo.
     */
    public void abrirDialogoEdicaoFilme(Filme filmeParaEditar) {
        if (filmeParaEditar == null) {
            return;
        }

        System.out.println("Abrindo formulário para editar filme: " + filmeParaEditar.getTitulo());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/FormularioCadastroFilme.fxml"));
            Parent root = loader.load();

            FormularioFilmeController formController = loader.getController();

            formController.setDiarioCultural(this.dc);
            formController.carregarDadosParaEdicao(filmeParaEditar);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Filme: " + filmeParaEditar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            if (filmesListView != null && filmesListView.getScene() != null && filmesListView.getScene().getWindow() != null) {
                dialogStage.initOwner(filmesListView.getScene().getWindow());
            } else if (adicionarFilmeButton != null && adicionarFilmeButton.getScene() != null && adicionarFilmeButton.getScene().getWindow() != null) {
                dialogStage.initOwner(adicionarFilmeButton.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            System.out.println("Formulário de edição de filme fechado. Atualizando dados...");
            refreshViewData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Abre o diálogo para avaliar um filme.
     * @param filmeParaAvaliar O filme a ser avaliado.
     */
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
            dialogStage.setResizable(false);

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


    /**
     * Abre um diálogo para exibir o histórico de todas as avaliações de um filme específico.
     * @param filme O filme cujo histórico de avaliações será exibido.
     */
    public void abrirDialogoHistoricoAvaliacoes(Filme filme) {
        if (filme == null || filme.getAvaliacoes().isEmpty()) {
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Este filme ainda não possui nenhuma avaliação.");
            info.setHeaderText("Histórico Vazio");
            info.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/HistoricoAvaliacoesFilme.fxml"));
            Parent root = loader.load();

            HistoricoFilmeController controller = loader.getController();
            controller.setAvaliacoes(filme.getTitulo(), filme.getAvaliacoes()); // Passa o título e a lista de reviews

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Histórico de Avaliações");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(filmesListView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaSimples("Erro", "Não foi possível abrir o histórico de avaliações.", e.getMessage());
        }
    }

    /**
     * Método chamado pela célula para solicitar a remoção de um filme.
     * Ele usa a instância atualizada do DiarioCultural para garantir a consistência.
     * @param filme O objeto Filme a ser removido.
     */
    public void abrirDialogoRemocaoFilme(Filme filme) {
        if (filme == null || dc == null) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Filme: " + filme.getTitulo());
        confirmacao.setContentText("Você tem certeza que deseja excluir este filme permanentemente?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            // Usa o método removerFilme do DiarioCultural, que já salva no JSON.
            boolean removido = dc.removerFilme(filme);

            if (removido) {
                System.out.println("Filme removido com sucesso: " + filme.getTitulo());
                refreshViewData(); // Atualiza a UI para refletir a remoção.
            } else {
                // Este alerta agora é mais preciso, pois a falha ocorreu com os dados mais recentes.
                Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover o filme. Ele pode não estar na lista atual.");
                erro.showAndWait();
            }
        }
    }

    // --- Métodos Auxiliares ---

    /**
     * Formata a duração do filme que foi informado em minutos, para horas e minutos.
     * @param minutos Duração do filme.
     */
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

    private void exibirAlertaSimples(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }


}