package view.viewInicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class TelaSidebarController {

    @FXML
    private AnchorPane contentAreaPane;
    @FXML
    public void initialize() {

        handleTelaInicio();
    }
    @FXML
    private void handleTelaInicio() {
        loadView("/view/viewInicio/TelaInicio.fxml");
    }

    @FXML
    public void handleSerieView() {
        loadView("/view/viewSerie/SerieView.fxml");
    }

    @FXML
    public void handleFilmeView() {
        loadView("/view/viewFilme/FilmeView.fxml");
    }

    @FXML
    public void handleShowLivroView() {
        loadView("/view/viewLivro/LivroView.fxml");
    }

    // Método utilitário para carregar as views no contentAreaPane
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // ---- A LÓGICA ENTRA AQUI ----
            // Verifica se o FXML que está a ser carregado é o da tela de Início
            // (Usei "InicioView.fxml" como exemplo, ajuste se o seu nome for "TelaInicio.fxml")
            if (fxmlPath.contains("TelaInicio.fxml")) {
                // Obtém o controller da tela de início que acabámos de carregar
                TelaInicioController inicioCtrl = loader.getController();

                // Passa uma referência deste TelaSidebarController para ele
                // Esta é a linha que "entrega o controlo remoto"
                inicioCtrl.setControladorPrincipal(this);
            }
            // -----------------------------

            contentAreaPane.getChildren().setAll(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (Exception e) { // Use uma exceção mais genérica para apanhar todos os problemas
            e.printStackTrace();
            contentAreaPane.getChildren().setAll(new javafx.scene.control.Label("Ocorreu um erro ao carregar a página."));
        }
    }
}