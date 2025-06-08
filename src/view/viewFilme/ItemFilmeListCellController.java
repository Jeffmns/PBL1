package view.viewFilme;

import controller.DiarioCultural;
import model.Filme;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType; // Importar ButtonType
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.util.Optional; // Importar Optional

public class ItemFilmeListCellController {

    @FXML private Label tituloLabel;
    @FXML private Label detalhesLinha1Label; // Para "Ano | Duração | Gênero"
    @FXML private Label diretorLabel;
    @FXML private Label notaMediaLabel;

    @FXML private CheckBox assistidoCheck;
    @FXML private Button infoButton;
    @FXML private Button avaliarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button historicoButton;

    private Filme filme;
    private DiarioCultural dc;
    private FilmeViewController filmeViewCtrl;


    public void setDadosDoFilme(Filme filme, DiarioCultural dc, FilmeViewController filmeViewCtrl) {
        this.filme = filme;
        this.dc = dc;
        this.filmeViewCtrl = filmeViewCtrl; // Guardar a referência

        if (filme != null) {
            tituloLabel.setText(filme.getTitulo());
            detalhesLinha1Label.setText(
                    (filme.getAno_lancamento() > 0 ? filme.getAno_lancamento() : "S/A") +
                            " | " + formatarDuracao(filme.getDuracao()) +
                            (filme.getGenero() != null && !filme.getGenero().isEmpty() ? " | " + filme.getGenero() : "")
            );
            diretorLabel.setText("Direção: " + (filme.getDirecao() != null ? filme.getDirecao() : "N/A"));

            double media = filme.getMediaAvaliacoes();
            notaMediaLabel.setText(media > 0 ? String.format("⭐ %.1f", media) : "⭐ Sem avaliação");

            assistidoCheck.setSelected(filme.isAssistido());
            assistidoCheck.setText(filme.isAssistido() ? "Assistido" : "Marcar como assistido");
        }
    }

    private String formatarDuracao(int minutos) {
        if (minutos <= 0) return "N/D";
        int horas = minutos / 60;
        int mins = minutos % 60;
        if (horas > 0) {
            return String.format("%dh %02dm", horas, mins);
        } else {
            return String.format("%dm", mins);
        }
    }

    @FXML
    private void handleAvaliar() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Avaliar filme: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoAvaliacaoFilme(this.filme);
       }
   }

    @FXML
    private void handleMarcarComoAssistido() {
        if (filme != null && dc != null && assistidoCheck != null) {
            filme.setAssistido(assistidoCheck.isSelected());
            assistidoCheck.setText(filme.isAssistido() ? "Assistido" : "Marcar como assistido");
            PersistenciaJson.salvar(dc);
            System.out.println("Filme '" + filme.getTitulo() + "' marcado como assistido: " + filme.isAssistido());

        }
    }

    @FXML
    private void handleInfo() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Info sobre: " + filme.getTitulo());
            filmeViewCtrl.mostrarDetalhesDoFilme(this.filme); // Chama método no controller pai
        }
    }

    @FXML
    private void handleEditar() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Editar filme: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoEdicaoFilme(this.filme); // Chama método no controller pai
        }
    }

    @FXML
    private void handleExcluir() {
        if (filme != null && dc != null && filmeViewCtrl != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Filme: " + filme.getTitulo());
            confirmacao.setContentText("Você tem certeza que deseja excluir este filme permanentemente?");

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean removido = dc.removerFilme(this.filme); // Tenta remover o objeto filme
                if (removido) {
                    System.out.println("Filme removido: " + filme.getTitulo());
                    filmeViewCtrl.refreshViewData(); // Pede para o controller principal atualizar a lista
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover o filme da lista.");
                    erro.showAndWait();
                }
            }
        }
    }

    @FXML
    private void handleHistorico() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + filme.getTitulo());
            // Chama um novo método no controller principal que abrirá o diálogo de histórico
            filmeViewCtrl.abrirDialogoHistoricoAvaliacoes(filme);
        }
    }

}