package view.viewLivro;

import controller.DiarioCultural;
import javafx.scene.control.*;
import model.Livro;
import persistence.PersistenciaJson;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller para a tela principal de Livros (LivroView.fxml).
 * Esta classe é responsável por gerir toda a interatividade da tela de livros,
 * como a busca, filtragem, ordenação e a abertura de diálogos para adicionar,
 * editar, avaliar ou remover livros.
 */
public class LivroViewController {

    @FXML private TextField tituloSearchField;
    @FXML private TextField autorSearchField;
    @FXML private TextField editoraSearchField;
    @FXML private TextField generoSearchField;
    @FXML private TextField isbnSearchField;
    @FXML private TextField anoSearchField;
    @FXML private ComboBox<String> ordenarLivroComboBox;
    @FXML private Button adicionarLivroButton;
    @FXML private Button buscarButton;
    @FXML private Button limparFiltrosButton;
    @FXML private Label statusLabel;
    @FXML private ListView<Livro> livrosListView;

    private DiarioCultural dc;
    private ObservableList<Livro> todosOsLivrosCache;
    private ObservableList<Livro> livrosEmExibicao;

    @FXML
    public void initialize() {
        carregarDiarioCultural();
        popularComboBoxDeOrdenacao();
        configurarListenersOuAcaoDoBotao();

        if (dc != null && dc.getLivros() != null) {
            todosOsLivrosCache = FXCollections.observableArrayList(dc.getLivros());
        } else {
            todosOsLivrosCache = FXCollections.observableArrayList();
        }
        livrosEmExibicao = FXCollections.observableArrayList(todosOsLivrosCache);

        if (livrosListView != null) {
            livrosListView.setItems(livrosEmExibicao);
            // Configura a CellFactory para usar célula customizada
            livrosListView.setCellFactory(listView -> new LivroListCell(this.dc, this));
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
            System.out.println("Novo DiarioCultural para Livro criado.");
        } else {
            System.out.println("DiarioCultural carregado com " + (dc.getLivros() != null ? dc.getLivros().size() : 0) + " livros.");
        }
    }

    /**
     * Adiciona as opções de texto (ex: "Título (A-Z)") ao ComboBox de ordenação.
     */
    private void popularComboBoxDeOrdenacao() {
        ordenarLivroComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhores Avaliados", "Piores Avaliados",
                "Mais Recentes", "Mais Antigos" // (Ano de Lançamento)
        ));
        ordenarLivroComboBox.setValue("Padrão (Entrada)");
    }
    /**
     * Adiciona "ouvintes" aos campos de filtro.
     * Isto faz com que a busca seja reativa, acontecendo à medida que o usuário digita.
     */
    private void configurarListenersOuAcaoDoBotao() {
        tituloSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        isbnSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        autorSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        generoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        editoraSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        anoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        ordenarLivroComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }
    /**
     * Este método é chamado sempre que uma busca,
     * filtro ou ordenação precisa de ser executada.
     */
    @FXML
    private void executarBuscaFiltragemOrdenacao() {
        if (dc == null) {
            if(livrosEmExibicao != null) livrosEmExibicao.clear();
            atualizarStatusLabel();
            return;
        }
        // Pega o texto de cada campo de filtro.
        String tituloQuery = tituloSearchField.getText();
        String autorQuery = autorSearchField.getText();
        String isbnQuery = isbnSearchField.getText(); // Para buscar no elenco
        String generoQuery = generoSearchField.getText();
        String editoraQuery = editoraSearchField.getText();
        String anoQueryStr = anoSearchField.getText();
        // Converte texto vazio para 'null', para que o método de busca saiba que não deve filtrar por aquele campo.
        String tituloParaBusca = (tituloQuery != null && !tituloQuery.trim().isEmpty()) ? tituloQuery.trim() : null;
        String autorParaBusca = (autorQuery != null && !autorQuery.trim().isEmpty()) ? autorQuery.trim() : null;
        String editoraParaBusca = (editoraQuery != null && !editoraQuery.trim().isEmpty()) ? editoraQuery.trim() : null;
        String generoParaBusca = (generoQuery != null && !generoQuery.trim().isEmpty()) ? generoQuery.trim() : null;
        String isbnParaBusca = (isbnQuery != null && !isbnQuery.trim().isEmpty()) ? isbnQuery.trim() : null;

        Integer anoParaBusca = null;
        if (anoQueryStr != null && !anoQueryStr.trim().isEmpty()) {
            try {
                anoParaBusca = Integer.parseInt(anoQueryStr.trim());
            } catch (NumberFormatException e) {
                statusLabel.setText("Ano inválido. Insira um número.");
            }
        }

        // Usar o método de busca de livros do DiarioCultural
        List<Livro> livrosFiltrados = dc.buscarLivros(tituloParaBusca, autorParaBusca, generoParaBusca, editoraParaBusca, anoParaBusca, isbnParaBusca);

        String tipoOrdem = ordenarLivroComboBox.getValue();
        if (tipoOrdem != null && livrosFiltrados != null) {
            switch (tipoOrdem) {
                case "Título (A-Z)":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "Título (Z-A)":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                case "Melhores Avaliados":
                    livrosFiltrados.sort(Comparator.comparingDouble(Livro::getMediaAvaliacoes).reversed());
                    break;
                case "Piores Avaliados":
                    livrosFiltrados.sort(Comparator.comparingDouble(Livro::getMediaAvaliacoes));
                    break;
                case "Mais Recentes":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getAno_lancamento, Comparator.nullsLast(Integer::compareTo)).reversed());
                    break;
                case "Mais Antigos":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getAno_lancamento, Comparator.nullsLast(Integer::compareTo)));
                    break;
            }
        }

        livrosEmExibicao.setAll(livrosFiltrados != null ? livrosFiltrados : new ArrayList<>());
        atualizarStatusLabel();
    }

    /**
     * Ação do botão "Limpar Tudo". Limpa todos os campos de filtro e reexecuta a busca
     * para mostrar todos os itens novamente.
     */
    @FXML
    private void handleLimparFiltros() {
        tituloSearchField.clear();
        autorSearchField.clear();
        isbnSearchField.clear();
        generoSearchField.clear();
        anoSearchField.clear();
        editoraSearchField.clear();
        ordenarLivroComboBox.setValue("Padrão (Entrada)");
        executarBuscaFiltragemOrdenacao();
    }

    /**
     * Atualiza o label no rodapé com a contagem de itens encontrados.
     */
    private void atualizarStatusLabel() {
        if (livrosEmExibicao == null || livrosEmExibicao.isEmpty()) {
            boolean algumFiltroAtivo = //
                    (tituloSearchField.getText() != null && !tituloSearchField.getText().trim().isEmpty()) ||
                            (autorSearchField.getText() != null && !autorSearchField.getText().trim().isEmpty()) ||
                            (isbnSearchField.getText() != null && !isbnSearchField.getText().trim().isEmpty()) ||
                            (generoSearchField.getText() != null && !generoSearchField.getText().trim().isEmpty()) ||
                            (anoSearchField.getText() != null && !anoSearchField.getText().trim().isEmpty());

            if (!algumFiltroAtivo) {
                statusLabel.setText("Nenhum livro cadastrado ou carregado.");
            } else {
                statusLabel.setText("Nenhum livro encontrado com os filtros atuais.");
            }
        } else {
            statusLabel.setText(livrosEmExibicao.size() + " livro(s) encontrado(s).");
        }
    }
    /**
     * Ação do botão "+ Adicionar Novo Livro". Chama o método que abre o diálogo do formulário.
     */
    @FXML
    private void handleAdicionarNovoLivro() {
        System.out.println("Abrindo formulário para adicionar novo livro...");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewLivro/FormularioCadastroLivro.fxml"));
            Parent root = loader.load();

            FormularioLivroController formController = loader.getController();
            formController.setDiarioCultural(this.dc);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastrar Novo Livro");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            refreshViewData();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao abrir formulário de cadastro de livro.");
        }
    }
    /**
     * Força uma recarga completa dos dados a partir do ficheiro JSON e atualiza a tela.
     * Útil para ser chamado após adicionar ou editar um item em um diálogo separado.
     */
    public void refreshViewData() {
        System.out.println("LivroViewController: Recarregando e atualizando dados.");
        carregarDiarioCultural();
        if (dc.getLivros() != null) {
            todosOsLivrosCache.setAll(dc.getLivros());
        } else {
            todosOsLivrosCache.clear();
        }
        executarBuscaFiltragemOrdenacao();
    }

    /**
     * Abre um diálogo de alerta para mostrar os detalhes completos de um livro.
     * @param livro O livro a ser detalhado.
     */
    public void mostrarDetalhesDoLivro(Livro livro) {
        if (livro == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Define o tipo de alerta
        alert.setTitle("Detalhes do livro");
        alert.setHeaderText(livro.getTitulo());

        // Monta uma string com todos os detalhes
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Ano de Lançamento: ").append(livro.getAno_lancamento()).append("\n");
        detalhes.append("Gênero: ").append(livro.getGenero() != null ? livro.getGenero() : "N/A").append("\n");
        detalhes.append("Autor: ").append(livro.getAutor() != null ? livro.getAutor() : "N/A").append("\n");
        detalhes.append("ISBN: ").append(livro.getISBN() != null ? livro.getISBN() : "N/A").append("\n");
        detalhes.append("Editora: ").append(livro.getEditora() != null ? livro.getEditora() : "N/A").append("\n");
        detalhes.append("Lido: ").append(livro.isLido() ? "Sim" : "Não").append("\n");

        double media = livro.getMediaAvaliacoes();
        detalhes.append("Nota Média: ").append(media > 0 ? String.format("%.1f ★", media) : "Sem Avaliação").append("\n");

        alert.setContentText(detalhes.toString());


        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }


    /**
     * Abre o diálogo do formulário para adicionar um novo livro ou editar um existente.
     * @param livroParaEditar O livro a ser editado, ou 'null' se for para adicionar um novo.
     */
    public void abrirDialogoEdicaoLivro(Livro livroParaEditar) {
        if (livroParaEditar == null) {
            return;
        }

        System.out.println("Abrindo formulário para editar livro: " + livroParaEditar.getTitulo());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewlivro/FormularioCadastroLivro.fxml"));
            Parent root = loader.load();

            FormularioLivroController formController = loader.getController();

            formController.setDiarioCultural(this.dc);
            formController.carregarDadosParaEdicao(livroParaEditar);


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Livro: " + livroParaEditar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);


            if (livrosListView != null && livrosListView.getScene() != null && livrosListView.getScene().getWindow() != null) {
                dialogStage.initOwner(livrosListView.getScene().getWindow());
            } else if (adicionarLivroButton != null && adicionarLivroButton.getScene() != null && adicionarLivroButton.getScene().getWindow() != null) {
                // Fallback se a listView não estiver visível ou disponível no momento
                dialogStage.initOwner(adicionarLivroButton.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            System.out.println("Formulário de edição de livro fechado. Atualizando dados...");
            refreshViewData();

        } catch (Exception e) { // Captura mais genérica para cobrir IOException, NullPointerException do controller, etc.
            e.printStackTrace();
        }
    }

    /**
     * Abre o diálogo para avaliar um livro.
     * @param livroParaAvaliar O livro a ser avaliado.
     */
    public void abrirDialogoAvaliacaoLivro(Livro livroParaAvaliar) {
        if (livroParaAvaliar == null) {
            System.err.println("Tentativa de avaliar um livro nulo.");
            exibirAlertaSimples("Seleção Inválida", null, "Nenhum livro foi selecionado para avaliação.");
            return;
        }

        System.out.println("Abrindo formulário para avaliar livro: " + livroParaAvaliar.getTitulo());
        try {
            // 1. Carrega o FXML do diálogo de avaliação
            // Certifique-se de que o caminho para o FXML está correto.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewLivro/AvaliacaoLivro.fxml"));
            Parent root = loader.load();

            // 2. Obtém o controller do diálogo
            AvaliacaoLivroController avaliacaoController = loader.getController();
            if (avaliacaoController == null) {
                System.err.println("Erro crítico: Não foi possível obter o controller do diálogo de avaliação.");
                exibirAlertaSimples("Erro Crítico", "Falha ao carregar componentes internos.", "Controller do diálogo de avaliação não encontrado.");
                return;
            }

            // 3. Passa o livro e a instância do DiarioCultural para o controller do diálogo
            avaliacaoController.setLivroEContexto(livroParaAvaliar, this.dc);

            // 4. Cria e configura o novo Stage (janela/diálogo)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Avaliar Livro: " + livroParaAvaliar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Bloqueia interação com outras janelas

            // Define a janela "pai"
            if (livrosListView != null && livrosListView.getScene() != null && livrosListView.getScene().getWindow() != null) {
                dialogStage.initOwner(livrosListView.getScene().getWindow());
            } else if (adicionarLivroButton != null && adicionarLivroButton.getScene() != null && adicionarLivroButton.getScene().getWindow() != null) {
                dialogStage.initOwner(adicionarLivroButton.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false); // Opcional

            // 5. Mostra o diálogo e espera até que ele seja fechado
            dialogStage.showAndWait();

            // 6. Após o diálogo ser fechado, atualiza a lista de livos na tela principal
            // (para refletir a nova nota média, por exemplo)
            System.out.println("Diálogo de avaliação de livro fechado. Atualizando dados...");
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
     * Abre um diálogo para exibir o histórico de todas as avaliações de um livro.
     * @param livro O livro cujo histórico será mostrado.
     */
    public void abrirDialogoHistoricoAvaliacoes(Livro livro) {
        if (livro == null || livro.getAvaliacoes().isEmpty()) {
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Este livro ainda não possui nenhuma avaliação.");
            info.setHeaderText("Histórico Vazio");
            info.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewLivro/HistoricoAvaliacoesLivro.fxml"));
            Parent root = loader.load();

            HistoricoLivroController controller = loader.getController();
            controller.setAvaliacoes(livro.getTitulo(), livro.getAvaliacoes()); // Passa o título e a lista de reviews

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Histórico de Avaliações");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(livrosListView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaSimples("Erro", "Não foi possível abrir o histórico de avaliações.", e.getMessage());
        }
    }
    /**
     * Método chamado pela célula para solicitar a remoção de um livro.
     * Ele usa a instância atualizada do DiarioCultural para garantir a consistência.
     * @param livro O objeto Livro a ser removido.
     */
    public void abrirDialogoRemocaoLivro(Livro livro) {
        if (livro == null || dc == null) return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Livro: " + livro.getTitulo());
        confirmacao.setContentText("Você tem certeza que deseja excluir este livro permanentemente?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            boolean removido = dc.removerLivro(livro);

            if (removido) {
                System.out.println("Livro removido com sucesso: " + livro.getTitulo());
                refreshViewData();
            } else {
                Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover o livro.");
                erro.showAndWait();
            }
        }
    }

    /**
     * Método Auxiliar para exibir alerta simples
     */
    private void exibirAlertaSimples(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Ou outro tipo de alerta
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
}