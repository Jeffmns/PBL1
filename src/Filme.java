import java.util.Date;

public class Filme implements Comparable<Filme>{
    private String titulo;
    private String genero;
    private int ano_lancamento;
    private int duracao;
    private String direcao;
    private String roteiro;
    private String elenco;
    private String titulo_original;
    private String onde_assistir;
    private boolean assistido;

    public Filme(String titulo, String genero, int ano_lancamento, int duracao, String direcao, String roteiro, String elenco, String titulo_original, String onde_assistir, boolean assistido) {
        this.titulo = titulo;
        this.genero = genero;
        this.ano_lancamento = ano_lancamento;
        this.duracao = duracao;
        this.direcao = direcao;
        this.roteiro = roteiro;
        this.elenco = elenco;
        this.titulo_original = titulo_original;
        this.onde_assistir = onde_assistir;
        this.assistido = assistido;
    }

    @Override
    public int compareTo(Filme filme) {
        return this.getTitulo().compareTo(filme.getTitulo());
    }

    public void avaliarFilme (Date data_review, String review, int avaliacao){
        System.out.println("Teste");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAssistido() {
        return assistido;
    }

    public void setAssistido(boolean assistido) {
        this.assistido = assistido;
    }

    public String getOnde_assistir() {
        return onde_assistir;
    }

    public void setOnde_assistir(String onde_assistir) {
        this.onde_assistir = onde_assistir;
    }

    public String getTitulo_original() {
        return titulo_original;
    }

    public void setTitulo_original(String titulo_original) {
        this.titulo_original = titulo_original;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getRoteiro() {
        return roteiro;
    }

    public void setRoteiro(String roteiro) {
        this.roteiro = roteiro;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAno_lancamento() {
        return ano_lancamento;
    }

    public void setAno_lancamento(int ano_lancamento) {
        this.ano_lancamento = ano_lancamento;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }


}

