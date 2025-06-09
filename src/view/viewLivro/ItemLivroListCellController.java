package view.viewLivro;

import controller.DiarioCultural;
import model.Livro;
import model.Review;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType; // Importar ButtonType
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.util.Optional; // Importar Optional

public class ItemLivroListCellController {

    @FXML private Label tituloLabel;
    @FXML private Label anoLabel;
    @FXML private Label editoraLabel;
    @FXML private Label generoLabel;  // ou combinar em detalhesLinha1Label
    @FXML private Label detalhesLinha1Label; // Para "Ano | Editora | Gênero"
    @FXML private Label autorLabel;
    @FXML private Label notaMediaLabel;

    @FXML private CheckBox lidoCheck;
    @FXML private Button infoButton;
    @FXML private Button avaliarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button historicoButton;

    private Livro livro;
    private DiarioCultural dc;
    private LivroViewController livroViewCtrl;


    public void setDadosDoLivro(Livro livro, DiarioCultural dc, LivroViewController livroViewCtrl) {
        this.livro = livro;
        this.dc = dc;
        this.livroViewCtrl = livroViewCtrl;

        if (livro != null) {
            tituloLabel.setText(livro.getTitulo());
            detalhesLinha1Label.setText(
                    (livro.getAno_lancamento() > 0 ? livro.getAno_lancamento() : "S/A") +
                            (livro.getGenero() != null && !livro.getGenero().isEmpty() ? " | " + livro.getGenero() : "") +
                            " | Editora: " + livro.getEditora()
            );
            autorLabel.setText("Autor: " + (livro.getAutor() != null ? livro.getAutor() : "N/A"));

            double media = livro.getMediaAvaliacoes();
            notaMediaLabel.setText(media > 0 ? String.format("⭐ %.1f", media) : "⭐ Sem avaliação");

            lidoCheck.setSelected(livro.isLido());
            lidoCheck.setText(livro.isLido() ? "Lido" : "Marcar como lido");
        }
    }



    @FXML
    private void handleAvaliar() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoAvaliacaoLivro(this.livro);
        }
    }

    @FXML
    private void handleMarcarComoLido() {
        if (livro != null && dc != null && lidoCheck != null) {
            livro.setLido(lidoCheck.isSelected());
            lidoCheck.setText(livro.isLido() ? "Lido" : "Marcar como lido");
            PersistenciaJson.salvar(dc);
            System.out.println("Livro '" + livro.getTitulo() + "' marcado como lido: " + livro.isLido());
            if (livroViewCtrl != null) {
                // Se a ordenação/filtragem depende do status 'assistido', pode ser necessário atualizar
                // filmeViewCtrl.refreshViewData(); Ou apenas atualizar este item se a lista for inteligente.
            }
        }
    }

    @FXML
    private void handleInfo() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Info sobre: " + livro.getTitulo());
            livroViewCtrl.mostrarDetalhesDoLivro(this.livro);
        }
    }

    @FXML
    private void handleEditar() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Editar livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoEdicaoLivro(this.livro);
        }
    }

    @FXML
    private void handleExcluir() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Solicitando remoção do livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoRemocaoLivro(this.livro);
        }
    }

    @FXML
    private void handleHistorico() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoHistoricoAvaliacoes(livro);
        }
    }


}