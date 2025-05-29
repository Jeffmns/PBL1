package view;

import controller.DiarioCultural;
import model.Livro;
import persistence.PersistenciaJson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.TableCell;

public class LivroViewController {

    @FXML private TextField tituloSearchField;
    @FXML private TextField autorSearchField;
    @FXML private TextField isbnSearchField;
    @FXML private TextField generoSearchField;
    @FXML private TextField anoSearchField;



    @FXML private ComboBox<String> ordenarLivroComboBox;
    @FXML private Button adicionarLivroButton;
    @FXML private Label statusLabel;
    @FXML private TableView<Livro> tabelaLivros;

    @FXML private TableColumn<Livro, String> colunaTitulo;
    @FXML private TableColumn<Livro, String> colunaAutor;
    @FXML private TableColumn<Livro, String> colunaGenero;
    @FXML private TableColumn<Livro, Integer> colunaAno;
    @FXML private TableColumn<Livro, String> colunaIsbn;
    @FXML private TableColumn<Livro, Double> colunaAvaliacao;
    @FXML private TableColumn<Livro, Boolean> colunaLido;
    @FXML private TableColumn<Livro, Void> colunaAcoes;


    @FXML private Button buscarButton;
    @FXML private Button limparFiltrosButton;

    private DiarioCultural dc;
    private ObservableList<Livro> todosOsLivrosCache;
    private ObservableList<Livro> livrosEmExibicao;

    @FXML
    public void initialize() {
        configurarColunasDaTabela();
        carregarDiarioCultural();
        popularComboBoxDeOrdenacao();
        configurarListenersOuAcaoDoBotao();

        if (dc != null && dc.getLivros() != null) {
            todosOsLivrosCache = FXCollections.observableArrayList(dc.getLivros());
        } else {
            todosOsLivrosCache = FXCollections.observableArrayList();
        }
        livrosEmExibicao = FXCollections.observableArrayList(todosOsLivrosCache);
        tabelaLivros.setItems(livrosEmExibicao);
        atualizarStatusLabel();
    }


    public void configurarColunasDaTabela() {
        colunaTitulo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("titulo"));
        colunaAutor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("autor"));
        colunaGenero.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("genero"));
        colunaAno.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ano_lancamento"));
        colunaIsbn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ISBN"));
        colunaAvaliacao.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("mediaAvaliacoes"));
        colunaLido.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lido"));

        if (colunaAcoes != null) {
            colunaAcoes.setCellFactory(param -> new TableCell<>() {
                private final Button btnEditar = new Button("Editar");
                {
                    btnEditar.setOnAction(event -> {
                        Livro livroParaEditar = getTableView().getItems().get(getIndex());
                        if (livroParaEditar != null) {
                            abrirDialogoEdicaoLivro(livroParaEditar);
                        }
                    });
                    btnEditar.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #333; -fx-background-radius: 4; -fx-font-size: 11px;");
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnEditar);
                    }
                }
            });
        }
    }

    public void carregarDiarioCultural() {
        dc = PersistenciaJson.carregar();
        if (dc == null) {
            dc = new DiarioCultural();
            System.out.println("Novo DiarioCultural criado, nenhum dado carregado do JSON.");
        } else {
            System.out.println("DiarioCultural carregado do JSON com " + (dc.getLivros() != null ? dc.getLivros().size() : 0) + " livros.");
        }
    }


    private void popularComboBoxDeOrdenacao() { // Anteriormente popularComboBoxes()
        // Gêneros e Anos não são mais populados em ComboBoxes
        ordenarLivroComboBox.setItems(FXCollections.observableArrayList(
                "Padrão (Entrada)", "Título (A-Z)", "Título (Z-A)",
                "Melhor Avaliados", "Pior Avaliados",
                "Mais Recentes", "Mais Antigos"
        ));
        ordenarLivroComboBox.setValue("Padrão (Entrada)");
    }

    private void configurarListenersOuAcaoDoBotao() {
        // A ação do botão buscarButton já está definida no FXML para chamar executarBuscaFiltragemOrdenacao.
        // Você pode adicionar listeners aos TextFields para busca "ao vivo" se desejar,
        // mas para múltiplos campos, um botão "Buscar" explícito é geralmente melhor.
        // Exemplo de listener (opcional):
        // tituloSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        // autorSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        // isbnSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        // generoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
        // anoSearchField.textProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());

        // O ComboBox de ordenação ainda pode ter um listener para reordenar automaticamente
        ordenarLivroComboBox.valueProperty().addListener((obs, oldV, newV) -> executarBuscaFiltragemOrdenacao());
    }

    @FXML
    private void executarBuscaFiltragemOrdenacao() {
        if (dc == null) { // Não precisa verificar todosOsLivrosCache aqui, pois ele é baseado no dc.getLivros()
            if (livrosEmExibicao != null) livrosEmExibicao.clear();
            atualizarStatusLabel();
            return;
        }

        String tituloQuery = tituloSearchField.getText();
        String autorQuery = autorSearchField.getText();
        String isbnQuery = isbnSearchField.getText();
        String generoQuery = generoSearchField.getText();
        String anoQueryStr = anoSearchField.getText();

        String tituloParaBusca = (tituloQuery != null && !tituloQuery.trim().isEmpty()) ? tituloQuery.trim() : null;
        String autorParaBusca = (autorQuery != null && !autorQuery.trim().isEmpty()) ? autorQuery.trim() : null;
        String isbnParaBusca = (isbnQuery != null && !isbnQuery.trim().isEmpty()) ? isbnQuery.trim() : null;
        String generoParaBusca = (generoQuery != null && !generoQuery.trim().isEmpty()) ? generoQuery.trim() : null;

        Integer anoParaBusca = null;
        if (anoQueryStr != null && !anoQueryStr.trim().isEmpty()) {
            try {
                anoParaBusca = Integer.parseInt(anoQueryStr.trim());
            } catch (NumberFormatException e) {
                statusLabel.setText("Ano inválido. Por favor, insira um número.");

            }
        }

        List<Livro> livrosFiltrados = dc.buscarLivros(tituloParaBusca, autorParaBusca, generoParaBusca, anoParaBusca, isbnParaBusca);

        String tipoOrdem = ordenarLivroComboBox.getValue();
        if (tipoOrdem != null && livrosFiltrados != null) {
            // Lógica de ordenação (permanece a mesma)
            switch (tipoOrdem) {
                case "Título (A-Z)":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "Título (Z-A)":
                    livrosFiltrados.sort(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                case "Melhor Avaliados":
                    livrosFiltrados.sort(Comparator.comparingDouble(Livro::getMediaAvaliacoes).reversed());
                    break;
                case "Pior Avaliados":
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

    @FXML
    private void handleLimparFiltros() {
        tituloSearchField.clear();
        autorSearchField.clear();
        isbnSearchField.clear();
        generoSearchField.clear();
        anoSearchField.clear();
        ordenarLivroComboBox.setValue("Padrão (Entrada)");

        executarBuscaFiltragemOrdenacao();
    }


    private void atualizarStatusLabel() {
        if (livrosEmExibicao == null || livrosEmExibicao.isEmpty()) {
            boolean algumFiltroAtivo = (tituloSearchField.getText() != null && !tituloSearchField.getText().trim().isEmpty()) ||
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

    @FXML
    private void handleAdicionarNovoLivro() {
        System.out.println("Abrindo formulário para adicionar novo livro...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormularioCadastroLivro.fxml"));
            Parent root = loader.load();

            FormularioLivroController formController = loader.getController();
            formController.setDiarioCultural(this.dc);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastrar Novo Livro");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            System.out.println("Formulário de cadastro fechado. Atualizando dados...");
            refreshViewData();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao abrir formulário de cadastro.");
        }
    }

    private void abrirDialogoEdicaoLivro(Livro livroParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormularioCadastroLivro.fxml"));
            Parent root = loader.load();

            FormularioLivroController formController = loader.getController();
            formController.setDiarioCultural(this.dc);
            formController.carregarDadosParaEdicao(livroParaEditar);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Livro: " + livroParaEditar.getTitulo());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(this.adicionarLivroButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            System.out.println("Formulário de edição fechado. Atualizando dados...");
            refreshViewData();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao abrir formulário de edição.");
        }
    }

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
}