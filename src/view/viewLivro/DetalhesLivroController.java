package view.viewLivro;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Livro;

public class DetalhesLivroController {

    @FXML private Label tituloPrincipalLabel;
    @FXML private Label tituloDetalheLabel;
    @FXML private Label autorDetalheLabel;
    @FXML private Label editoraDetalheLabel;
    @FXML private Label isbnDetalheLabel;
    @FXML private Label anoDetalheLabel;
    @FXML private Label generoDetalheLabel;
    @FXML private Label lidoDetalheLabel;
    @FXML private Label notaMediaDetalheLabel;
    // Adicione @FXML para outros Labels se tiver

    public void carregarLivro(Livro livro) {
        if (livro != null) {
            tituloPrincipalLabel.setText("Detalhes: " + livro.getTitulo()); // Exemplo
            tituloDetalheLabel.setText(livro.getTitulo());
            autorDetalheLabel.setText(livro.getAutor() != null ? livro.getAutor() : "N/A");
            editoraDetalheLabel.setText(livro.getEditora() != null ? livro.getEditora() : "N/A");
            isbnDetalheLabel.setText(livro.getISBN() != null ? livro.getISBN() : "N/A");
            anoDetalheLabel.setText(livro.getAno_lancamento() > 0 ? String.valueOf(livro.getAno_lancamento()) : "N/A");
            generoDetalheLabel.setText(livro.getGenero() != null ? livro.getGenero() : "N/A");
            lidoDetalheLabel.setText(livro.isLido() ? "Sim" : "Não");
            notaMediaDetalheLabel.setText(livro.getMediaAvaliacoes() > 0 ? String.format("%.1f", livro.getMediaAvaliacoes()) : "Não avaliado");
        }
    }

    @FXML
    private void handleFecharDialog() {
        Stage stage = (Stage) tituloPrincipalLabel.getScene().getWindow(); // Pega o Stage atual
        stage.close();
    }
}

