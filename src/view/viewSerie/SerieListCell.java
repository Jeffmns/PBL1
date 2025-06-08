package view.viewSerie; // Certifique-se de que este é o pacote correto para os seus arquivos de Série

import controller.DiarioCultural;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import model.Serie; // Alterado para importar a classe Serie

import java.io.IOException;

public class SerieListCell extends ListCell<Serie> { // Alterado para estender ListCell<Serie>
    private Node graphic;
    private ItemSerieListCellController controller; // O controller da célula de Série
    private DiarioCultural dc;
    private SerieViewController serieViewCtrl; // O controller principal da tela de Séries

    // Construtor que recebe as instâncias necessárias para Séries
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
