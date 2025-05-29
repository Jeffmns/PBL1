package view;

import controller.DiarioCultural;
import model.Livro;
import persistence.PersistenciaJson;

import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.geometry.Pos;

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


    // Não se esqueça dos imports necessários no topo do seu arquivo LivroViewController.java:
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView; // Se você referenciar a tabela aqui, mas não é o caso
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.control.TableCell;
// import javafx.scene.control.Button;
// import javafx.scene.layout.HBox;
// import javafx.geometry.Pos;
// import model.Livro; // E sua classe Livro

    private void configurarColunasDaTabela() {
        // 1. Configuração das colunas que exibem dados do Livro
        // Estas chamadas DEVEM estar aqui, no corpo principal do método.
        if (colunaTitulo != null) { // Boa prática verificar se a coluna foi injetada
            colunaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        }
        if (colunaAutor != null) {
            colunaAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        }
        if (colunaGenero != null) {
            colunaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        }
        if (colunaAno != null) {
            colunaAno.setCellValueFactory(new PropertyValueFactory<>("ano_lancamento"));
        }
        if (colunaIsbn != null) {
            colunaIsbn.setCellValueFactory(new PropertyValueFactory<>("ISBN")); // Ou "isbn" dependendo do getter em Livro.java
        }
        if (colunaAvaliacao != null) {
            colunaAvaliacao.setCellValueFactory(new PropertyValueFactory<>("mediaAvaliacoes"));
        }
        // Assumindo que você tem uma TableColumn com fx:id="colunaLido" no FXML
        // e o campo @FXML private TableColumn<Livro, Boolean> colunaLido; no controller
        if (colunaLido != null) {
            colunaLido.setCellValueFactory(new PropertyValueFactory<>("lido")); // Precisa de isLido() ou getLido() em Livro.java
        }

        // 2. Configuração da coluna de "Ações" (com os botões)
        if (colunaAcoes != null) {
            colunaAcoes.setCellFactory(param -> new TableCell<Livro, Void>() { // Especificar os tipos <Entidade, TipoDaColuna>
                // Criar os botões uma vez por instância de célula para performance
                private final Button btnEditar = new Button("Editar");
                private final Button btnAvaliar = new Button("Avaliar");
                private final Button btnRemover = new Button("Remover");
                // Agrupar os botões em um HBox
                private final HBox paneBotoes = new HBox(5, btnEditar, btnAvaliar, btnRemover);

                // Bloco de inicialização da instância da célula (executado uma vez quando a célula é criada)
                {
                    paneBotoes.setAlignment(Pos.CENTER); // Centraliza os botões dentro da HBox

                    // Estilos dos botões (CSS inline)
                    btnEditar.setStyle("-fx-background-color: #FFC107; -fx-font-size: 10px; -fx-text-fill: #333; -fx-background-radius: 4; -fx-padding: 3 6 3 6;");
                    btnAvaliar.setStyle("-fx-background-color: #4CAF50; -fx-font-size: 10px; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 3 6 3 6;");
                    btnRemover.setStyle("-fx-background-color: #F44336; -fx-font-size: 10px; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 3 6 3 6;");

                    // Definir as ações dos botões
                    btnEditar.setOnAction(event -> {
                        Livro livro = (Livro) getTableRow().getItem(); // Pega o objeto Livro da linha atual
                        if (livro != null) {
                            abrirDialogoEdicaoLivro(livro); // Método que você tem no LivroViewController
                        }
                    });

                    btnAvaliar.setOnAction(event -> {
                        Livro livro = (Livro) getTableRow().getItem();
                        if (livro != null) {
                            handleAvaliarLivro(livro); // Método que você tem no LivroViewController
                        }
                    });

                    btnRemover.setOnAction(event -> {
                        Livro livro = (Livro) getTableRow().getItem();
                        if (livro != null) {
                            handleRemoverLivro(livro); // Método que você tem no LivroViewController
                        }
                    });
                }

                // Método chamado para atualizar a célula quando necessário (rolagem, mudança de dados)
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null); // Não mostrar nada se a linha estiver vazia ou o item for nulo
                        setText(null);
                    } else {
                        setGraphic(paneBotoes); // Mostrar a HBox com os botões
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

    private void handleAvaliarLivro(Livro livro) {
        System.out.println("Avaliar: " + livro.getTitulo());
        // Lógica para abrir um diálogo de avaliação
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Avaliar Livro");
        alert.setHeaderText("Avaliação para: " + livro.getTitulo());
        alert.setContentText("Funcionalidade de avaliação ainda não implementada neste diálogo.\nVocê chamaria dc.avaliarLivro(...) aqui após coletar os dados.");
        alert.showAndWait();
        // Após avaliar e salvar, chame:
        // refreshViewData();
    }
    // Dentro de LivroViewController.java
    private void handleRemoverLivro(Livro livro) {
        System.out.println("Remover: " + livro.getTitulo());

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Livro: " + livro.getTitulo());
        confirmacao.setContentText("Tem certeza que deseja remover este livro?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (dc != null && dc.getLivros() != null) {
                    boolean removido = dc.getLivros().remove(livro);
                    if (removido) {
                        PersistenciaJson.salvar(dc);
                        System.out.println("Livro removido com sucesso do DiarioCultural.");
                        refreshViewData();
                        statusLabel.setText("Livro '" + livro.getTitulo() + "' removido.");
                    } else {
                        statusLabel.setText("Erro: Livro não encontrado para remoção na lista interna.");
                        System.err.println("Erro ao remover o livro da lista do DiarioCultural.");
                    }
                } else {
                    statusLabel.setText("Erro: Sistema de dados não inicializado.");
                }
            }
        });
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