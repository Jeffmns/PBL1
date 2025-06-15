package view.viewFilme;

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Filme;

import java.io.IOException;

/**
 * Representa uma célula customizada na ListView para exibir um objeto Filme.
 * Esta classe é a "ponte" entre a ListView e o design visual de cada item,
 * carregando um FXML para a sua aparência e utilizando um controller para gerenciar os dados.
 */
public class FilmeListCell extends ListCell<Filme> {
    // Variável para guardar o layout visual da célula (o HBox do FXML).
    private Node graphic;
    private ItemFilmeListCellController controller;
    private DiarioCultural dc;
    private FilmeViewController filmeViewCtrl;

    /**
     * Construtor da célula de filme.
     * Este método é chamado pela ListView cada vez que uma nova célula precisa de ser criada.
     * @param dc A instância principal do DiarioCultural.
     * @param filmeViewCtrl A referência ao controller da tela de filmes.
     */
    public FilmeListCell(DiarioCultural dc, FilmeViewController filmeViewCtrl) {
        this.dc = dc;
        this.filmeViewCtrl = filmeViewCtrl;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewFilme/ItemFilmeListCell.fxml"));
            graphic = loader.load();
            controller = loader.getController();

        } catch (IOException e) {
            // Se houver um erro ao carregar o FXML (ex: ficheiro não encontrado), imprime o erro.
            e.printStackTrace();
            setText("Erro ao carregar célula do filme.");
            graphic = null;
            controller = null;
        }
    }

    /**
     * Método chamado pelo JavaFX para atualizar o conteúdo da célula.
     * Este método é executado sempre que uma célula precisa de mostrar um novo filme
     * (por exemplo, ao rolar a lista ou ao filtrar).
     * @param filme O objeto Filme a ser exibido nesta célula.
     * @param empty true se a célula estiver vazia, false caso contrário.
     */
    @Override
    protected void updateItem(Filme filme, boolean empty) {
        super.updateItem(filme, empty);
        // Se a célula estiver vazia ou o objeto filme for nulo, não mostra nada.
        if (empty || filme == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (controller != null) {
                // Passa o filme e as referências para o controller da célula,
                // para que ele possa preencher os Labels, CheckBox, etc.
                controller.setDadosDoFilme(filme, this.dc, this.filmeViewCtrl);
            }

            setGraphic(graphic); // Define a estrutura visual que carregamos (o 'graphic') como o conteúdo desta célula.
            setText(null);// Garante que nenhum texto padrão seja mostrado.
        }
    }
}
