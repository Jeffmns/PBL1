package view.viewLivro;

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Livro;

import java.io.IOException;

/**
 * Representa uma célula customizada na ListView para exibir um objeto Livro.
 * Esta classe é a "ponte" entre a ListView e o design visual de cada item,
 * carregando um FXML para a sua aparência e utilizando um controller para gerir os dados.
 */
public class LivroListCell extends ListCell<Livro> {
    private Node graphic;
    private ItemLivroListCellController controller;
    private DiarioCultural dc;
    private LivroViewController livroViewCtrl;

    /**
     * Construtor da célula de filme.
     * Este método é chamado pela ListView cada vez que uma nova célula precisa de ser criada.
     * Ele carrega o ficheiro FXML que define a aparência da célula uma única vez.
     * @param dc A instância principal do DiarioCultural.
     * @param livroViewCtrl A referência ao controller da tela de livros, para callbacks.
     */
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
    /**
     * Método chamado pelo JavaFX para atualizar o conteúdo da célula.
     * Este método é executado sempre que uma célula precisa de mostrar um novo livro
     * (por exemplo, ao rolar a lista ou ao filtrar).
     * @param livro O objeto Livro a ser exibido nesta célula.
     * @param empty true se a célula estiver vazia, false caso contrário.
     */
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