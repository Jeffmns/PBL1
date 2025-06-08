package view.viewLivro;

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Livro;

import java.io.IOException;

public class LivroListCell extends ListCell<Livro> {
    private Node graphic;
    private ItemLivroListCellController controller;
    private DiarioCultural dc;
    private LivroViewController livroViewCtrl; // Referência ao controller pai

    // Construtor que recebe as instâncias necessárias
    public LivroListCell(DiarioCultural dc, LivroViewController livroViewCtrl) {
        this.dc = dc;
        this.livroViewCtrl = livroViewCtrl;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewLivro/ItemLivroListCell.fxml"));
            graphic = loader.load();
            controller = loader.getController(); // Pega o controller instanciado pelo FXML
        } catch (IOException e) {
            e.printStackTrace();
            setText("Erro ao carregar célula do livro.");
            graphic = null;
            controller = null;
        }
    }

    @Override
    protected void updateItem(Livro livro, boolean empty) {
        super.updateItem(livro, empty);
        if (empty || livro == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (controller != null) {
                controller.setDadosDoLivro(livro, this.dc, this.livroViewCtrl);
            }
            setGraphic(graphic);
            setText(null);
        }
    }
}