package uk.anttheantster.anteconomy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class EconomyFileStore {
    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EconomyFileStore(Path dataDir) {
        this.file = dataDir.resolve("balances.json");
    }

    public EconomyData load() {
        if (!Files.exists(file)) return new EconomyData();
        try (Reader r = Files.newBufferedReader(file)) {
            EconomyData data = gson.fromJson(r, EconomyData.class);
            return (data != null) ? data : new EconomyData();
        } catch (Exception e) {
            e.printStackTrace();
            return new EconomyData();
        }
    }

    public void saveAtomic(EconomyData data) {
        try {
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling(file.getFileName().toString() + ".tmp");

            try (Writer w = Files.newBufferedWriter(tmp)) {
                gson.toJson(data, w);
            }

            // Atomic replace
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

