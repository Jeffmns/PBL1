package persistence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.DiarioCultural;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PersistenciaJson {
    private static final String CAMINHO_ARQUIVO = "src/persistence/dados.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Salva o objeto em um arquivo JSON
    public static void salvar(DiarioCultural diario) {
        try (FileWriter writer = new FileWriter(CAMINHO_ARQUIVO)) {
            gson.toJson(diario, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // Carrega o objeto a partir do arquivo JSON
    public static DiarioCultural carregar() {
        try (FileReader reader = new FileReader(CAMINHO_ARQUIVO)) {
            DiarioCultural diario = gson.fromJson(reader, DiarioCultural.class);
            return diario != null ? diario : new DiarioCultural();
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado. Criando novo diário cultural.");
            return new DiarioCultural();
        }
    }
}
