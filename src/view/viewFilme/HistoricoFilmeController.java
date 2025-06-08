package view.viewFilme;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Review;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoricoFilmeController {

    @FXML private Label tituloMediaLabel;
    @FXML private ListView<Review> historicoListView;

    @FXML
    public void initialize() {
        // Configura como cada item (Review) será exibido na ListView
        historicoListView.setCellFactory(lv -> new ListCell<>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

                    setGraphic(textFlow); 
                    setText(null);
                    setStyle("-fx-border-color: transparent transparent #E0E0E0 transparent; -fx-padding: 5px;");
                }
            }
        });
    }

    /**
     * Recebe a lista de avaliações do controller principal e popula a ListView.
     */
    public void setAvaliacoes(String nomeDaMidia, List<Review> avaliacoes) {
        tituloMediaLabel.setText(nomeDaMidia);
        historicoListView.getItems().setAll(avaliacoes);
    }

    @FXML
    private void handleFechar() {
        Stage stage = (Stage) historicoListView.getScene().getWindow();
        stage.close();
    }
}
