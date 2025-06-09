package view.viewSerie; // Certifique-se de que este é o pacote correto

import controller.DiarioCultural;
import model.Serie;
import model.Temporada;
import persistence.PersistenciaJson;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioSerieController {

    // --- Campos FXML ---
    @FXML private Label tituloFormularioLabel;
    @FXML private TextField tituloField;
    @FXML private TextField generoField;
    @FXML private TextField anoField;
    @FXML private TextField elencoField;
    @FXML private TextField ondeAssistirField;
    @FXML private TextField numTemporadasField;
    // O CheckBox 'acompanhandoCheck' foi removido para seguir a lógica de status derivado
    @FXML private Button salvarButton;
    @FXML private Button cancelarButton;

    // --- Variáveis de Instância ---
    private DiarioCultural dc;
    private Serie serieParaEditar;
    private boolean modoEdicao = false;

    @FXML
    public void initialize() {
        // Nada necessário aqui por enquanto
    }

    /**
     * Injeta a instância principal do DiarioCultural, vinda do controller principal.
     */
    public void setDiarioCultural(DiarioCultural dc) {
        this.dc = dc;
    }

    /**
     * Preenche o formulário com os dados de uma série existente para edição.
     * @param serie A série a ser editada.
     */
    public void carregarDadosParaEdicao(Serie serie) {
        this.serieParaEditar = serie;
        this.modoEdicao = true;
        this.tituloFormularioLabel.setText("Editar Série");

        if (serie != null) {
            tituloField.setText(serie.getTitulo());
            generoField.setText(serie.getGenero());
            anoField.setText(String.valueOf(serie.getAnoLancamento()));
            elencoField.setText(serie.getElenco());
            ondeAssistirField.setText(serie.getOnde_assistir());
            numTemporadasField.setText(String.valueOf(serie.getTemporadas().size()));

            // Importante: Desabilitar a edição do número de temporadas para simplificar a lógica.
            // Mudar o número de temporadas em uma série já cadastrada é uma operação complexa.
            numTemporadasField.setDisable(true);
        }
    }

    /**
     * Ação do botão "Salvar". Valida os dados, cria uma nova série ou atualiza
     * uma existente e fecha o diálogo.
     */
    @FXML
    private void handleSalvar() {
        if (dc == null) {
            exibirAlerta("Erro Crítico", "O sistema de dados não foi inicializado.", Alert.AlertType.ERROR);
            return;
        }

        String titulo = tituloField.getText();
        if (titulo == null || titulo.trim().isEmpty()) {
            exibirAlerta("Campo Obrigatório", "O título não pode estar vazio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Coleta os dados dos campos
            String genero = generoField.getText();
            int ano = Integer.parseInt(anoField.getText());
            String elenco = elencoField.getText();
            String ondeAssistir = ondeAssistirField.getText();

            if (modoEdicao) {
                // Modo Edição: Atualiza o objeto existente
                serieParaEditar.setTitulo(titulo);
                serieParaEditar.setGenero(genero);
                serieParaEditar.setAno_lancamento(ano);
                serieParaEditar.setElenco(elenco);
                serieParaEditar.setOnde_assistir(ondeAssistir);

                // Salva o estado do DiarioCultural com a série modificada
                PersistenciaJson.salvar(dc);
                System.out.println("Série atualizada: " + serieParaEditar.getTitulo());

            } else {
                // Modo Adicionar: Cria uma nova Série
                int numTemporadas = Integer.parseInt(numTemporadasField.getText());
                if (numTemporadas <= 0) {
                    exibirAlerta("Dado Inválido", "O número de temporadas deve ser maior que zero.", Alert.AlertType.WARNING);
                    return;
                }

                // Certifique-se de que sua classe Serie tem um construtor que aceita estes parâmetros
                Serie novaSerie = new Serie(titulo, genero, ano, elenco, ondeAssistir);

                // CRUCIAL: Cria as temporadas vazias com base no número informado
                for (int i = 1; i <= numTemporadas; i++) {
                    // Supondo que Temporada tem um construtor como new Temporada(int numero)
                    novaSerie.getTemporadas().add(new Temporada(i));
                }

                // 2. VERIFICA SE A SÉRIE JÁ EXISTE ANTES DE TENTAR CADASTRAR
                if (dc.getSeries().contains(novaSerie)) {
                    // Se já existe, mostra o alerta e NÃO fecha a janela
                    exibirAlerta("Série Duplicada",
                            "Uma série com os mesmos dados já existe no seu diário.",
                            Alert.AlertType.WARNING);
                } else {
                    // Se não existe, prossegue com o cadastro
                    dc.cadastrarSerie(novaSerie); // Este método agora só adiciona e salva
                    System.out.println("Nova série cadastrada: " + novaSerie.getTitulo());
                    fecharJanela(); // Fecha a janela apenas se o cadastro foi bem-sucedido
                }

            }

            fecharJanela();

        } catch (NumberFormatException e) {
            exibirAlerta("Formato Inválido", "Ano e Nº de Temporadas devem ser números válidos.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro Inesperado", "Ocorreu um erro ao salvar a série.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
