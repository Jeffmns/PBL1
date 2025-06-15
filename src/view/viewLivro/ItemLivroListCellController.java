package view.viewLivro;

import controller.DiarioCultural;
import model.Livro;
import persistence.PersistenciaJson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * Controller para o ficheiro FXML de uma única célula da lista de livros (ItemLivroListCell.fxml).
 * Esta classe é responsável por pegar os dados de um objeto Livro e preencher os elementos
 * visuais da célula, além de lidar com as ações dos botões (Editar, Avaliar, etc.)
 * para aquele item específico.
 */
public class ItemLivroListCellController {

    @FXML private Label tituloLabel;
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

    /**
     * Recebe os dados de um livro e as referências necessárias para preencher a célula.
     * Este método é chamado pela classe LivroListCell sempre que a célula precisa de ser atualizada.
     * @param livro O objeto Livro com os dados a serem exibidos.
     * @param dc A instância principal do DiarioCultural para ações como salvar.
     * @param livroViewCtrl A referência ao controller da tela de livros para delegar ações.
     */
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

    /**
     * Ação executada quando o botão "Avaliar" é clicado.
     * Delega a tarefa de abrir o diálogo de avaliação para o controller principal.
     */
    @FXML
    private void handleAvaliar() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoAvaliacaoLivro(this.livro);
        }
    }

    /**
     * Ação executada quando o CheckBox "Lido" é clicado.
     * Atualiza o status do livro e salva a alteração no ficheiro JSON.
     */
    @FXML
    private void handleMarcarComoLido() {
        if (livro != null && dc != null && lidoCheck != null) {
            livro.setLido(lidoCheck.isSelected());
            lidoCheck.setText(livro.isLido() ? "Lido" : "Marcar como lido");
            PersistenciaJson.salvar(dc);
            System.out.println("Livro '" + livro.getTitulo() + "' marcado como lido: " + livro.isLido());

        }
    }

    /**
     * Ação executada quando o botão "Detalhes" é clicado.
     * Delega a tarefa de mostrar os detalhes para o controller principal.
     */
    @FXML
    private void handleInfo() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Info sobre: " + livro.getTitulo());
            livroViewCtrl.mostrarDetalhesDoLivro(this.livro);
        }
    }

    /**
     * Ação executada quando o botão "Editar" é clicado.
     * Delega a tarefa de abrir o formulário de edição para o controller principal.
     */
    @FXML
    private void handleEditar() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Editar livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoEdicaoLivro(this.livro);
        }
    }

    /**
     * Ação executada quando o botão "Excluir" é clicado.
     * Delega a tarefa de pedir a confirmação e remover o livro para o controller principal.
     */
    @FXML
    private void handleExcluir() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Solicitando remoção do livro: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoRemocaoLivro(this.livro);
        }
    }

    /**
     * Ação executada quando o botão "Avaliações" é clicado.
     * Delega a tarefa de mostrar o histórico de avaliações para o controller principal.
     */
    @FXML
    private void handleHistorico() {
        if (livro != null && livroViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + livro.getTitulo());
            livroViewCtrl.abrirDialogoHistoricoAvaliacoes(livro);
        }
    }
}