package view.viewSerie; // Certifique-se de que este é o pacote correto

import controller.DiarioCultural;
import model.Serie;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class ItemSerieListCellController {

    // --- Campos FXML (correspondem ao ItemSerieListCell.fxml) ---
    @FXML private Label tituloLabel;
    @FXML private Label detalhesLinha1Label; // Para "Ano | Gênero | Onde Assistir"
    @FXML private Label progressoLabel;      // Para "X Temporadas | Status Assistido"
    @FXML private Label notaMediaLabel;


    @FXML private Button avaliarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button infoButton;
    @FXML private Button historicoButton;


    private Serie serie;
    private DiarioCultural dc;
    private SerieViewController serieViewCtrl; // O controller principal da tela de Séries

    /**
     * Injeta os dados da série e as referências necessárias na célula.
     * É chamado pelo SerieListCell a cada atualização de item.
     * @param serie O objeto Serie a ser exibido.
     * @param dc A instância principal do DiarioCultural.
     * @param serieViewCtrl A referência ao controller principal da tela de séries.
     */
    public void setDadosDaSerie(Serie serie, DiarioCultural dc, SerieViewController serieViewCtrl) {
        this.serie = serie;
        this.dc = dc;
        this.serieViewCtrl = serieViewCtrl;

        if (serie != null) {
            tituloLabel.setText(serie.getTitulo());

            // Monta a linha de detalhes principais
            detalhesLinha1Label.setText(
                    (serie.getAnoLancamento() > 0 ? serie.getAnoLancamento() : "S/A") +
                            (serie.getGenero() != null && !serie.getGenero().isEmpty() ? " | " + serie.getGenero() : "") +
                            (serie.getOnde_assistir() != null && !serie.getOnde_assistir().isEmpty() ? " | " + serie.getOnde_assistir() : "")
            );

            // Monta a linha de progresso usando seu método getStatusAssistido()
            progressoLabel.setText(
                    serie.getTemporadas().size() + " Temporada(s) | Status: " + serie.getStatusAssistido()
            );

            // Calcula a média das avaliações das temporadas (requer getMediaAvaliacoes() na classe Serie)
            double media = serie.getMediaAvaliacoes();
            notaMediaLabel.setText(media > 0 ? String.format("⭐ %.1f (média)", media) : "⭐ Sem avaliação");

        } else {
            // Limpa os campos se a célula estiver sendo reutilizada para um item vazio
            tituloLabel.setText("");
            detalhesLinha1Label.setText("");
            progressoLabel.setText("");
            notaMediaLabel.setText("");
        }
    }

    // --- Métodos de Ação dos Botões ---

    @FXML
    private void handleAvaliar() {
        if (serie != null && serieViewCtrl != null) {
            System.out.println("Solicitando avaliação para a série: " + serie.getTitulo());
            serieViewCtrl.abrirDialogoAvaliacaoSerie(this.serie);
        }
    }

    @FXML
    private void handleInfo() {
        if (serie != null && serieViewCtrl != null) {
            System.out.println("Mostrando detalhes de: " + serie.getTitulo());
            serieViewCtrl.mostrarDetalhesDaSerie(this.serie);
        }
    }

    @FXML
    private void handleEditar() {
        if (serie != null && serieViewCtrl != null) {
            System.out.println("Solicitando edição para a série: " + serie.getTitulo());
            serieViewCtrl.abrirDialogoEdicaoSerie(this.serie);
        }
    }

    @FXML
    private void handleExcluir() {
        if (serie != null && dc != null && serieViewCtrl != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Série: " + serie.getTitulo());
            confirmacao.setContentText("Você tem certeza que deseja excluir esta série permanentemente?");

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean removido = dc.removerSerie(this.serie);
                if (removido) {
                    System.out.println("Série removida: " + serie.getTitulo());
                    serieViewCtrl.refreshViewData(); // Pede para o controller principal atualizar a lista
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover a série da lista.");
                    erro.showAndWait();
                }
            }
        }
    }

    @FXML
    private void handleHistorico() {
        if (serie != null && serieViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + serie.getTitulo());
            // Chama um novo método no controller principal que abrirá o diálogo de histórico
            serieViewCtrl.abrirDialogoHistoricoAvaliacoes(serie);
        }
    }
}
