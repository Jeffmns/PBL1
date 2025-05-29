package view;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;

public class TelaInicialController {

    @FXML
    private AnchorPane contentAreaPane;
    @FXML
    public void initialize() {

        handleShowOutraTelaView();
    }
    @FXML
    private void handleShowOutraTelaView() {
        loadView("/view/OutraTela.fxml");
    }

    @FXML
    private void handleShowCadastroView() {
        loadView("/view/TelaCadastro.fxml");
    }

    @FXML
    private void handleShowBuscarView() {
        loadView("/view/TelaBusca.fxml");
    }

    @FXML
    private void handleShowLivroView() {
        loadView("/view/LivroView.fxml");
    }
    // Método utilitário para carregar as views no contentAreaPane
    private void loadView(String fxmlPath) {
        try {

            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Não foi possível encontrar o FXML: " + fxmlPath);

                contentAreaPane.getChildren().setAll(new javafx.scene.control.Label("Erro ao carregar a view: " + fxmlPath));
                return;
            }

            Parent view = FXMLLoader.load(fxmlUrl);
            contentAreaPane.getChildren().setAll(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

            contentAreaPane.getChildren().setAll(new javafx.scene.control.Label("Ocorreu um erro ao carregar a página."));
        }
    }
}