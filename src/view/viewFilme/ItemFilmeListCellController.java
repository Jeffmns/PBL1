package view.viewFilme;

import controller.DiarioCultural;
import model.Filme;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * Controller para o FXML de uma única célula da lista de filmes (ItemFilmeListCell.fxml).
 * Esta classe é responsável por pegar os dados de um objeto Filme e preencher os elementos
 * visuais da célula, além de lidar com as ações dos botões (Editar, Avaliar, etc.)
 * para aquele item específico.
 */
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

    /**
     * Recebe os dados de um filme e as referências necessárias para preencher a célula.
     * Este método é chamado pela classe FilmeListCell sempre que a célula precisa de ser atualizada.
     * @param filme O objeto Filme com os dados a serem exibidos.
     * @param dc A instância principal do DiarioCultural para ações como salvar.
     * @param filmeViewCtrl A referência ao controller da tela de filmes para delegar ações.
     */
    public void setDadosDoFilme(Filme filme, DiarioCultural dc, FilmeViewController filmeViewCtrl) {
        this.filme = filme;
        this.dc = dc;
        this.filmeViewCtrl = filmeViewCtrl; // Guardar a referência

        // Se o objeto filme não for nulo, preenchemos os elementos da interface com os seus dados.
        if (filme != null) {
            // Define o texto dos labels com as informações do filme.
            tituloLabel.setText(filme.getTitulo());
            detalhesLinha1Label.setText(
                    (filme.getAno_lancamento() > 0 ? filme.getAno_lancamento() : "S/A") +
                            " | " + formatarDuracao(filme.getDuracao()) +
                            (filme.getGenero() != null && !filme.getGenero().isEmpty() ? " | " + filme.getGenero() : "")
            );
            diretorLabel.setText("Direção: " + (filme.getDirecao() != null ? filme.getDirecao() : "N/A"));
            // Pede ao próprio objeto Filme para calcular a sua média de avaliações.
            double media = filme.getMediaAvaliacoes();
            notaMediaLabel.setText(media > 0 ? String.format("⭐ %.1f", media) : "⭐ Sem avaliação");

            assistidoCheck.setSelected(filme.isAssistido());
            assistidoCheck.setText(filme.isAssistido() ? "Assistido" : "Marcar como assistido");
        }
    }

    /**
     * Ação executada quando o botão "Avaliar" é clicado.
     * Delega a tarefa de abrir o diálogo de avaliação para o controller principal.
     */
    @FXML
    private void handleAvaliar() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Avaliar filme: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoAvaliacaoFilme(this.filme);
       }
   }

    /**
     * Ação executada quando o CheckBox "Assistido" é clicado.
     * Atualiza o status do filme e salva a alteração no JSON.
     */
    @FXML
    private void handleMarcarComoAssistido() {
        if (filme != null && dc != null && assistidoCheck != null) {
            filme.setAssistido(assistidoCheck.isSelected());
            assistidoCheck.setText(filme.isAssistido() ? "Assistido" : "Marcar como assistido");
            PersistenciaJson.salvar(dc);
            System.out.println("Filme '" + filme.getTitulo() + "' marcado como assistido: " + filme.isAssistido());

        }
    }

    /**
     * Ação executada quando o botão "Detalhes" é clicado.
     * Delega a tarefa de mostrar os detalhes para o controller principal.
     */
    @FXML
    private void handleInfo() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Info sobre: " + filme.getTitulo());
            filmeViewCtrl.mostrarDetalhesDoFilme(this.filme);
        }
    }

    /**
     * Ação executada quando o botão "Editar" é clicado.
     * Delega a tarefa de abrir o formulário de edição para o controller principal.
     */
    @FXML
    private void handleEditar() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Editar filme: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoEdicaoFilme(this.filme);
        }
    }

    /**
     * Ação executada quando o botão "Excluir" é clicado.
     * Delega a tarefa de pedir a confirmação e remover o filme para o controller principal.
     */
    @FXML
    private void handleExcluir() {
        // Em vez de remover diretamente, pedimos ao controller principal para fazer isso.
        // Isso mantém a lógica centralizada e evita problemas de dados desatualizados.
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Solicitando remoção do filme: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoRemocaoFilme(this.filme);
        }
    }
    /**
     * Ação executada quando o botão "Avaliações" é clicado.
     * Delega a tarefa de mostrar o histórico de avaliações para o controller principal.
     */
    @FXML
    private void handleHistorico() {
        if (filme != null && filmeViewCtrl != null) {
            System.out.println("Solicitando histórico de avaliações para: " + filme.getTitulo());
            filmeViewCtrl.abrirDialogoHistoricoAvaliacoes(filme);
        }
    }

    /**
     * Método auxiliar para formatar a duração de minutos para um formato mais legível (ex: "1h 30m").
     * @param minutos A duração total em minutos.
     * @return Uma String formatada.
     */
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
}