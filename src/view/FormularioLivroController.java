package view;
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

public class FormularioLivroController {

    @FXML private Label tituloFormularioLabel;
    @FXML private TextField tituloField;
    @FXML private TextField autorField;
    @FXML private TextField editoraField;
    @FXML private TextField isbnField;
    @FXML private TextField anoField;
    @FXML private TextField generoField;
    @FXML private CheckBox lidoCheck;
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    private DiarioCultural dc;
    private Livro livroParaEditar;
    private boolean modoEdicao = false;

    public void initialize() {

    }

    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    /**
     * Preenche o formulário com os dados de um livro existente para edição.
     * @param livro O livro a ser editado.
     */
    public void carregarDadosParaEdicao(Livro livro) {
        this.livroParaEditar = livro;
        this.modoEdicao = true;
        this.tituloFormularioLabel.setText("Editar Livro"); // Muda o título do formulário

        if (livro != null) {
            tituloField.setText(livro.getTitulo());
            autorField.setText(livro.getAutor());
            editoraField.setText(livro.getEditora());
            isbnField.setText(livro.getISBN());
            anoField.setText(livro.getAno_lancamento() > 0 ? String.valueOf(livro.getAno_lancamento()) : "");
            generoField.setText(livro.getGenero());
            lidoCheck.setSelected(livro.isLido());
        }
    }

    @FXML
    private void handleSalvar() {
        if (dc == null) {
            exibirAlerta("Erro Crítico", "O sistema de dados (DiarioCultural) não foi inicializado.", Alert.AlertType.ERROR);
            return;
        }

        String titulo = tituloField.getText();
        String autor = autorField.getText();
        String editora = editoraField.getText();
        String isbn = isbnField.getText();
        String anoStr = anoField.getText();
        String genero = generoField.getText();
        boolean lido = lidoCheck.isSelected();

        // Validação básica (exemplo)
        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título do livro não pode estar vazio.", Alert.AlertType.WARNING);
            tituloField.requestFocus();
            return;
        }

        Integer ano = null;
        if (anoStr != null && !anoStr.trim().isEmpty()) {
            try {
                ano = Integer.parseInt(anoStr.trim());
                if (ano <= 0) {
                    exibirAlerta("Dado Inválido", "O ano de lançamento deve ser um número positivo.", Alert.AlertType.WARNING);
                    anoField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                exibirAlerta("Formato Inválido", "O ano de lançamento deve ser um número (ex: 2023).", Alert.AlertType.WARNING);
                anoField.requestFocus();
                return;
            }
        }

        try {
            if (modoEdicao && livroParaEditar != null) {

                livroParaEditar.setTitulo(titulo);
                livroParaEditar.setAutor(autor);
                livroParaEditar.setEditora(editora);
                livroParaEditar.setISBN(isbn);
                livroParaEditar.setAno_lancamento(ano);
                livroParaEditar.setGenero(genero);
                livroParaEditar.setLido(lido);

                PersistenciaJson.salvar(dc);
                System.out.println("Livro atualizado: " + livroParaEditar.getTitulo());
                exibirAlerta("Sucesso", "Livro '" + livroParaEditar.getTitulo() + "' atualizado com sucesso!", Alert.AlertType.INFORMATION);

            } else {

                Livro novoLivro = new Livro(titulo, autor, editora, isbn, ano, genero, true);
                dc.cadastrarLivro(novoLivro); // Este método em DiarioCultural já deve chamar PersistenciaJson.salvar(dc)
                System.out.println("Novo livro cadastrado: " + novoLivro.getTitulo());
                exibirAlerta("Sucesso", "Livro '" + novoLivro.getTitulo() + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }

            fecharJanela();

        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar o livro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}