package zenika.marketing.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.logging.Log;
import zenika.marketing.domain.HistoryEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class HistoryService {

    private static final String HISTORY_FILE = "generated/history.json";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private List<HistoryEntry> history = new ArrayList<>();

    @PostConstruct
    void init() {
        loadHistory();
    }

    private void loadHistory() {
        Path path = Path.of(HISTORY_FILE);
        if (Files.exists(path)) {
            try {
                history = objectMapper.readValue(path.toFile(), new TypeReference<List<HistoryEntry>>() {
                });
                Log.info("Loaded " + history.size() + " history entries.");
            } catch (IOException e) {
                Log.error("Failed to load history", e);
                history = new ArrayList<>();
            }
        }
    }

    public synchronized void addEntry(String templateName, String prompt, Path sourceImagePath) {
        try {
            Path historyDir = Path.of("generated/history");
            Files.createDirectories(historyDir);

            String fileName = System.currentTimeMillis() + "_" + sourceImagePath.getFileName().toString();
            Path targetPath = historyDir.resolve(fileName);
            Files.copy(sourceImagePath, targetPath);

            HistoryEntry entry = new HistoryEntry(templateName, prompt, targetPath.toString(),
                    java.time.LocalDateTime.now());
            history.add(0, entry);
            saveHistory();
        } catch (IOException e) {
            Log.error("Failed to add history entry", e);
        }
    }

    private void saveHistory() {
        try {
            Path path = Path.of(HISTORY_FILE);
            Files.createDirectories(path.getParent());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), history);
        } catch (IOException e) {
            Log.error("Failed to save history", e);
        }
    }

    public List<HistoryEntry> getHistory() {
        return new ArrayList<>(history);
    }
}
