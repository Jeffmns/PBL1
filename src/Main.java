//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DiarioCultural dc = new DiarioCultural();
        Livro l1 = new Livro("Cristine", "Stephen King", "Objetiva",
                "978-85-60280-87-2", 2013, "Terror", true, true);

        dc.cadastrarLivro(l1);

        Filme f1 = new Filme("Inrestelar", "ficção cientifíca", 2013,
                150, "Cristopher Nolan", "xxx",
                "Anne Hateway", "Interestelar", "Netflix", false);

        Serie s1 = new Serie("Stranger Things", "Ficção",
                2016, 2025, "Sadie Sink", "Stranger Things", "Netflix");

        dc.cadastrarFilme(f1);
        dc.cadastrarSerie(s1);
    }
}