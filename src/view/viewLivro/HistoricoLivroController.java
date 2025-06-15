package view.viewLivro;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Review;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller para a janela (diálogo) que exibe o histórico de avaliações de um Livro.
 * Esta classe é responsável por receber uma lista de objetos Review e mostrá-la
 * de forma formatada e legível para o utilizador.
 */
public class HistoricoLivroController {

    @FXML private Label tituloMediaLabel;
    @FXML private ListView<Review> historicoListView;

    @FXML
    public void initialize() {
        // Configura como cada item (Review) será exibido na ListView
        historicoListView.setCellFactory(lv -> new ListCell<>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            /**
             * Este método é chamado para cada célula da lista, para atualizar o seu conteúdo.
             * @param review O objeto Review a ser exibido nesta célula.
             * @param empty true se a célula estiver vazia.
             */
            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);
                if (empty || review == null) {
                    setText(null);
                } else {

                    Text cabecalho = new Text("Nota " + review.getAvaliacao() + " ★\n");
                    cabecalho.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-fill: #20385e;");

                    String data = "Data: " + dateFormat.format(review.getData()) + "\n";

                    String comentarioStr = (review.getComentario().isEmpty() ? "(sem comentário)" : review.getComentario());
                    Text comentario = new Text("Comentário: " + comentarioStr);
                    comentario.setStyle("-fx-fill: #444444;");

                    TextFlow textFlow = new TextFlow(cabecalho, new Text(data), comentario);
                    textFlow.setPadding(new javafx.geometry.Insets(5, 0, 5, 0));

                    setGraphic(textFlow); // Usa o TextFlow como o visual da célula
                    setText(null);
                    setStyle("-fx-border-color: transparent transparent #E0E0E0 transparent; -fx-padding: 5px;");
                }
            }
        });
    }

    /**
     * Recebe a lista de avaliações do controller principal (LivroViewController)
     * e popula a ListView com esses dados.
     * @param nomeDaMidia O título do livro, para ser exibido no topo da janela.
     * @param avaliacoes A lista de objetos Review para mostrar.
     */
    public void setAvaliacoes(String nomeDaMidia, List<Review> avaliacoes) {
        tituloMediaLabel.setText(nomeDaMidia);
        historicoListView.getItems().setAll(avaliacoes);
    }

}
