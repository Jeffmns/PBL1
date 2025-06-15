package view.viewLivro;

import controller.DiarioCultural;
import model.Livro;
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
public class FormularioLivroController {

    @FXML private Label tituloFormularioLabel;
    @FXML private TextField tituloField;
    @FXML private TextField anoField;
    @FXML private TextField editoraField;
    @FXML private TextField autorField;
    @FXML private TextField generoField;
    @FXML private TextField isbnField;
    @FXML private CheckBox lidoCheck;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    private DiarioCultural dc;
    private Livro livroParaEditar;
    private boolean modoEdicao = false;

    public void initialize() {

    }

    /**
     * Recebe a instância principal do DiarioCultural a partir da tela que abriu este formulário.
     * É assim que este controller ganha acesso à lista de livros para salvar os dados.
     * @param dc A instância principal do DiarioCultural.
     */
    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    /**
     * Preenche os campos do formulário com os dados de um livro existente.
     * Este método transforma o formulário do modo "Adicionar" para o modo "Editar".
     * @param livro O objeto Livro cujos dados serão editados.
     */
    public void carregarDadosParaEdicao(Livro livro) {
        this.livroParaEditar = livro;
        this.modoEdicao = true;  // Liga a "bandeira" do modo de edição.
        this.tituloFormularioLabel.setText("Editar livro");

        // Se o objeto livro não for nulo, preenchemos cada campo com os seus dados.
        if (livro != null) {
            tituloField.setText(livro.getTitulo());
            generoField.setText(livro.getGenero());
            anoField.setText(livro.getAno_lancamento() > 0 ? String.valueOf(livro.getAno_lancamento()) : "");
            autorField.setText(livro.getAutor());
            isbnField.setText(livro.getISBN());
            editoraField.setText(livro.getEditora());
            lidoCheck.setSelected(livro.isLido());
        }
    }

    /**
     * Ação executada quando o botão "Salvar" é clicado.
     * Valida os dados inseridos e, se estiverem corretos, cria um novo livro ou atualiza um existente.
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
        String editora = editoraField.getText();
        String autor = autorField.getText();
        String isbn = isbnField.getText();
        boolean lido = lidoCheck.isSelected();

        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título do livro não pode estar vazio.", Alert.AlertType.WARNING);
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

        try { // Se a "bandeira" de modo de edição estiver ligada...
            if (modoEdicao && livroParaEditar != null) {
                livroParaEditar.setTitulo(titulo);
                livroParaEditar.setGenero(genero);
                livroParaEditar.setAno_lancamento(anoLancamento);
                livroParaEditar.setAutor(autor);
                livroParaEditar.setISBN(isbn);
                livroParaEditar.setEditora(editora);
                livroParaEditar.setLido(lido);


                PersistenciaJson.salvar(dc); // Salva o DiarioCultural com o filme modificado
                System.out.println("Livro atualizado: " + livroParaEditar.getTitulo());
                exibirAlerta("Sucesso", "Livro '" + livroParaEditar.getTitulo() + "' atualizado com sucesso!", Alert.AlertType.INFORMATION);
                fecharJanela();
            } else {
                // Se não estiver em modo de edição, criamos um novo livro.
                Livro novoLivro = new Livro(titulo, autor, editora, isbn, anoLancamento, genero, lido);

                // Verifica se o livro já existe antes de cadastrar
                if (dc.getLivros().contains(novoLivro)) {
                    // Se já existe, mostra o alerta e NÃO fecha a janela
                    exibirAlerta("Livro Duplicado",
                            "Um livro com os mesmos dados já existe no seu diário.",
                            Alert.AlertType.WARNING);
                } else {
                    // Se não existe, prossegue com o cadastro
                    dc.cadastrarLivro(novoLivro); // Este método agora só adiciona e salva
                    exibirAlerta("Sucesso", "Livro '" + novoLivro.getTitulo() + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                    System.out.println("Novo livro cadastrado: " + novoLivro.getTitulo());
                    fecharJanela(); // Fecha a janela apenas se o cadastro foi bem-sucedido
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar o livro: " + e.getMessage(), Alert.AlertType.ERROR);
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