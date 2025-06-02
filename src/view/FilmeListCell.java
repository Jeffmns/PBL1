package view;

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Filme;

import java.io.IOException;

public class FilmeListCell extends ListCell<Filme> {
    private Node graphic;
    private ItemFilmeListCellController controller;
    private DiarioCultural dc;
    private FilmeViewController filmeViewCtrl; // Referência ao controller pai

    // Construtor que recebe as instâncias necessárias
    public FilmeListCell(DiarioCultural dc, FilmeViewController filmeViewCtrl) {
        this.dc = dc;
        this.filmeViewCtrl = filmeViewCtrl;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ItemFilmeListCell.fxml"));
            // O controller é definido no FXML (fx:controller="view.ItemFilmeListCellController")
            graphic = loader.load();
            controller = loader.getController(); // Pega o controller instanciado pelo FXML
        } catch (IOException e) {
            e.printStackTrace();
            setText("Erro ao carregar célula do filme.");
            graphic = null;
            controller = null;
        }
    }

    @Override
    protected void updateItem(Filme filme, boolean empty) {
        super.updateItem(filme, empty);
        if (empty || filme == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (controller != null) {
                // Passa o filme, dc, e a referência ao FilmeViewController
                controller.setDadosDoFilme(filme, this.dc, this.filmeViewCtrl /*, getIndex() se precisar do índice */);
            }
            setGraphic(graphic);
            setText(null);
        }
    }
}