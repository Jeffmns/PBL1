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
    @FXML private Label anoLabel; // Você precisará adicionar este fx:id ao FXML se quiser usá-lo aqui diretamente
    @FXML private Label editoraLabel;
    @FXML private Label generoLabel;  // ou combinar em detalhesLinha1Label
    @FXML private Label detalhesLinha1Label; // Para "Ano | ISBN | Gênero"
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
    private LivroViewController livroViewCtrl; // Referência ao controller principal da tela de filmes


    public void setDadosDoLivro(Livro livro, DiarioCultural dc, LivroViewController livroViewCtrl) {
        this.livro = livro;
        this.dc = dc;
        this.livroViewCtrl = livroViewCtrl; // Guardar a referência

        if (livro != null) {
            tituloLabel.setText(livro.getTitulo());
            detalhesLinha1Label.setText(
                    (livro.getAno_lancamento() > 0 ? livro.getAno_lancamento() : "S/A") +
                            " | " + livro.getEditora() +
                            (livro.getGenero() != null && !livro.getGenero().isEmpty() ? " | " + livro.getGenero() : "")
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
            livroViewCtrl.mostrarDetalhesDoLivro(this.livro); // Chama método no controller pai
        }
    }

    @FXML
    private void handleEditar() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Editar livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoEdicaoLivro(this.livro); // Chama método no controller pai
        }
    }

    @FXML
    private void handleExcluir() {
        if (livro != null && dc != null && livroViewCtrl != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Livro: " + livro.getTitulo());
            confirmacao.setContentText("Você tem certeza que deseja excluir este livro permanentemente?");

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean removido = dc.removerLivro(this.livro);  // Tenta remover o objeto filme
                if (removido) {
                    System.out.println("Livro removido: " + livro.getTitulo());
                    livroViewCtrl.refreshViewData(); // Pede para o controller principal atualizar a lista
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR, "Não foi possível remover o livro da lista.");
                    erro.showAndWait();
                }
            }
        }
    }

    @FXML
    private void handleHistorico() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + livro.getTitulo());
            // Chama um novo método no controller principal que abrirá o diálogo de histórico
            livroViewCtrl.abrirDialogoHistoricoAvaliacoes(livro);
        }
    }


}