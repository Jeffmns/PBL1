package view.viewSerie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Review;
import model.Serie;
import model.Temporada;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para a janela (diálogo) que exibe o histórico de avaliações de uma Série.
 * Esta classe é responsável por receber uma lista de objetos Review e mostrá-la
 * de forma formatada e legível para o utilizador.
 */
public class HistoricoSerieController {

    // Helper Record para juntar uma avaliação com o número da sua temporada
    private record AvaliacaoTemporada(int numeroTemporada, Review review) {}

    @FXML private Label tituloSerieLabel;
    @FXML private ListView<AvaliacaoTemporada> historicoListView;

    @FXML
    public void initialize() {
        // Configura como cada item (AvaliacaoTemporada) será exibido na ListView
        historicoListView.setCellFactory(lv -> new ListCell<>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            /**
             * Este método é chamado para cada célula da lista, para atualizar o seu conteúdo.
             * @param review O objeto Review a ser exibido nesta célula.
             * @param empty true se a célula estiver vazia.
             */
            @Override
            protected void updateItem(AvaliacaoTemporada item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null); // Limpa a célula
                } else {
                    // Cria uma formatação mais rica usando TextFlow
                    Text cabecalho = new Text("Temporada " + item.numeroTemporada() + "  |  Nota: " + item.review().getAvaliacao() + " ★\n");
                    cabecalho.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-fill: #20385e;");

                    String data = "Data: " + dateFormat.format(item.review().getData()) + "\n";

                    String comentarioStr = item.review().getComentario().isEmpty() ? "(sem comentário)" : item.review().getComentario();
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
     * Recebe o objeto Serie, extrai todas as avaliações de todas as temporadas
     * e popula a ListView.
     */
    public void setSerie(Serie serie) {
        tituloSerieLabel.setText(serie.getTitulo());

        List<AvaliacaoTemporada> todasAsAvaliacoes = new ArrayList<>();
        if (serie.getTemporadas() != null) {
            for (Temporada temporada : serie.getTemporadas()) {
                if (temporada.getAvaliacao() != null && !temporada.getAvaliacao().isEmpty()) {
                    for (Review review : temporada.getAvaliacao()) {
                        todasAsAvaliacoes.add(new AvaliacaoTemporada(temporada.getNumero(), review));
                    }
                }
            }
        }

        // Ordena as avaliações pela data mais recente (opcional, mas recomendado)
        todasAsAvaliacoes.sort((a1, a2) -> a2.review().getData().compareTo(a1.review().getData()));

        historicoListView.setItems(FXCollections.observableArrayList(todasAsAvaliacoes));
    }

    /**
     * Método auxiliar para fechar a janela (diálogo) atual.
     */
    @FXML
    private void handleFechar() {
        Stage stage = (Stage) historicoListView.getScene().getWindow();
        stage.close();
    }
}
