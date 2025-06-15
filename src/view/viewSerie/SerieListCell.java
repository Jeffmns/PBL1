package view.viewSerie; // Certifique-se de que este é o pacote correto para os seus arquivos de Série

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Serie; // Alterado para importar a classe Serie

import java.io.IOException;

/**
 * Representa uma célula customizada na ListView para exibir um objeto Serie.
 * Esta classe é a "ponte" entre a ListView e o design visual de cada item,
 * carregando um FXML para a sua aparência e utilizando um controller para gerir os dados.
 */
public class SerieListCell extends ListCell<Serie> { // Alterado para estender ListCell<Serie>
    private Node graphic;
    private ItemSerieListCellController controller; // O controller da célula de Série
    private DiarioCultural dc;
    private SerieViewController serieViewCtrl; // O controller principal da tela de Séries

    /**
     * Construtor da célula de série.
     * Este método é chamado pela ListView cada vez que uma nova célula precisa de ser criada.
     * Ele carrega o ficheiro FXML que define a aparência da célula uma única vez.
     * @param dc A instância principal do DiarioCultural.
     * @param serieViewCtrl A referência ao controller da tela de séries, para callbacks.
     */
    public SerieListCell(DiarioCultural dc, SerieViewController serieViewCtrl) {
        this.dc = dc;
        this.serieViewCtrl = serieViewCtrl;
        try {
            // Carrega o FXML da célula de Série
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemSerieListCell.fxml"));
            graphic = loader.load();
            controller = loader.getController(); // Pega o controller ItemSerieListCellController
        } catch (IOException e) {
            e.printStackTrace();
            setText("Erro ao carregar célula da série.");
            graphic = null;
            controller = null;
        }
    }

    /**
     * Método chamado pelo JavaFX para atualizar o conteúdo da célula.
     * Este método é executado sempre que uma célula precisa de mostrar uma nova série
     * (por exemplo, ao rolar a lista ou ao filtrar).
     * @param serie O objeto Serie a ser exibido nesta célula.
     * @param empty true se a célula estiver vazia, false caso contrário.
     */
    @Override
    protected void updateItem(Serie serie, boolean empty) { // O item agora é do tipo Serie
        super.updateItem(serie, empty);
        if (empty || serie == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (controller != null) {
                // Passa o objeto Serie e as referências para o controller da célula
                controller.setDadosDaSerie(serie, this.dc, this.serieViewCtrl);
            }
            setGraphic(graphic);
            setText(null);
        }
    }
}
