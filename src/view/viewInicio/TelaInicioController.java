package view.viewInicio;

import controller.DiarioCultural;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TelaInicioController {


    @FXML private Label boasVindasLabel;
    @FXML private Label totalLivrosLabel;
    @FXML private Label totalFilmesLabel;
    @FXML private Label totalSeriesLabel;

    private DiarioCultural dc;
    private TelaSidebarController telaSidebarCtrl; // Referência ao controller principal para navegação

    /**
     * Este método é chamado para injetar o controller principal, permitindo a navegação.
     */
    public void setControladorPrincipal(TelaSidebarController telaSidebarCtrl) {
        this.telaSidebarCtrl = telaSidebarCtrl;
    }

    @FXML
    public void initialize() {
        definirMensagemDeBoasVindas();
        carregarEstatisticas();
    }

    private void definirMensagemDeBoasVindas() {
        boasVindasLabel.setText("Seja bem-vindo(a) ao Diário Cultural!");
    }

    private void carregarEstatisticas() {
        this.dc = PersistenciaJson.carregar();
        if (this.dc == null) {
            this.dc = new DiarioCultural();
        }

        totalLivrosLabel.setText(String.valueOf(dc.getLivros() != null ? dc.getLivros().size() : 0));
        totalFilmesLabel.setText(String.valueOf(dc.getFilmes() != null ? dc.getFilmes().size() : 0));
        totalSeriesLabel.setText(String.valueOf(dc.getSeries() != null ? dc.getSeries().size() : 0));
    }


    @FXML
    private void handleIrParaLivros() {
        if (telaSidebarCtrl != null) {
            telaSidebarCtrl.handleShowLivroView();
        }
    }

    @FXML
    private void handleIrParaFilmes() {
        if (telaSidebarCtrl != null) {
            telaSidebarCtrl.handleFilmeView();
        }
    }

    @FXML
    private void handleIrParaSeries() {
        if (telaSidebarCtrl != null) {
            telaSidebarCtrl.handleSerieView();
        }
    }
}
