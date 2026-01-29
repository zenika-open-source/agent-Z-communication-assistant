package zenika.marketing.ui;

import io.javelit.core.Jt;
import io.javelit.core.JtUploadedFile;
import io.javelit.core.Server;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import zenika.marketing.services.TemplateService;
import zenika.marketing.services.GeminiImagesServices;
import zenika.marketing.services.GeminiVideoServices;
import zenika.marketing.services.GeminiTextServices;
import zenika.marketing.services.HistoryService;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.domain.Template;
import zenika.marketing.domain.HistoryEntry;
import zenika.marketing.utils.Utils;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.quarkus.logging.Log;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Unremovable
@RegisterForReflection
@Dependent
@Command(name = "ui", mixinStandardHelpOptions = true, description = "Start Javelit UI.")
public class AgentZCommunication implements Runnable {

    @Inject
    TemplateService templateService;

    @Inject
    GeminiImagesServices geminiImagesServices;

    @Inject
    GeminiVideoServices geminiVideoServices;

    @Inject
    GeminiTextServices geminiTextServices;

    @Inject
    HistoryService historyService;

    @Inject
    ConfigProperties config;

    @Override
    public void run() {
        Log.info("🚀 Starting Javelit UI on port 8501...");

        Server server = Server.builder(this::renderApp, 8501).build();
        server.start();

        Log.info("🌐 UI available at http://localhost:8501");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("Stopping Javelit UI server...");
            server.stop();
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Log.info("Javelit UI interrupted.");
            server.stop();
        }
    }

    public void renderApp() {
        var nav = Jt.navigation(
                Jt.page("gen", this::renderGenerator).title("🎨 Générateur").icon("🔥"),
                Jt.page("hist", this::renderHistory).title("📜 Historique").icon(":favorite:"))
                .use();
        nav.run();
    }

    public void renderGenerator() {
        Jt.title("🤖 Agent Z Communication Assistant").use();
        List<Template> templates = templateService.getTemplates();
        List<String> templateNames = templates.stream().map(Template::name).toList();

        String defaultTemplateName = config.getDefaultTemplatePath();
        int defaultIndex = 0;
        if (defaultTemplateName != null && !defaultTemplateName.isBlank()) {
            for (int i = 0; i < templateNames.size(); i++) {
                if (templateNames.get(i).equalsIgnoreCase(defaultTemplateName)) {
                    defaultIndex = i;
                    break;
                }
            }
        }

        String selectedName = Jt.selectbox("", templateNames).index(defaultIndex).use();

        if (selectedName == null || selectedName.isEmpty()) {
            Jt.info("Sélectionnez un template.").use();
            return;
        }

        Template selectedTemplate = templateService.findByName(selectedName).orElse(null);
        if (selectedTemplate == null) {
            Jt.error("Template non trouvé : " + selectedName).use();
            return;
        }

        var headerCols = Jt.columns(2).key("gen-header-cols").use();
        var colHeader = headerCols.col(0);
        var colButton = headerCols.col(1);

        Jt.header("🎨 " + selectedTemplate.name()).use(colHeader);
        Jt.markdown("*" + selectedTemplate.description() + "*").use(colHeader);
        boolean generateClicked = Jt.button("🚀 GÉNÉRER").use(colButton);

        Map<String, String> fieldValues = new HashMap<>();
        if (generateClicked) {
            Jt.info("⏳ Génération...").use();
            try {
                String prompt = templateService.preparePrompt(selectedTemplate, config);
                Content content = prepareContent(selectedTemplate, prompt);

                String model = config.getDefaultGeminiModelImage();

                if ("VIDEO".equals(selectedTemplate.type())) {
                    model = config.getDefaultGeminiVeoModel();
                } else if ("POST".equals(selectedTemplate.type())) {
                    model = config.getDefaultGeminiModel();
                }
                if ("IMAGE".equals(selectedTemplate.type())) {
                    geminiImagesServices.generateImage(model, config, content);
                    Path resultPath = Path.of("generated/" + config.getDefaultResultFilename());
                    if (Files.exists(resultPath)) {
                        historyService.addEntry(selectedTemplate.name(), prompt, resultPath);
                        Jt.success("✅ Généré et sauvegardé !").use();
                    }
                } else if ("VIDEO".equals(selectedTemplate.type())) {
                    geminiVideoServices.generateVideo(prompt, config);
                    Path resultPath = Path.of("generated/" + config.getDefaultResultFilenameVideo());
                    if (Files.exists(resultPath)) {
                        historyService.addEntry(selectedTemplate.name(), prompt, resultPath);
                        Jt.success("✅ Vidéo générée et sauvegardée !").use();
                    }
                } else if ("POST".equals(selectedTemplate.type())) {
                    String generatedText = geminiTextServices.generateText(model, content);
                    if (generatedText != null && !generatedText.isBlank()) {
                        Jt.sessionState().put("generatedText", generatedText);
                        Jt.success("✅ Texte généré avec succès !").use();
                    }
                }
            } catch (Exception e) {
                Jt.error("❌ " + e.getMessage()).use();
                Log.error("Generation error", e);
            }
        }

        Jt.markdown("---").key("gen-sep-header").use();

        var cols = Jt.columns(3).key("gen-main-cols").use();
        var col0 = cols.col(0);
        var col1 = cols.col(1);
        var col2 = cols.col(2);

        // --- Column 1: Model ---
        Jt.subheader("🖼️ Modèle").use(col0);
        if (selectedTemplate.template() != null && !selectedTemplate.template().isBlank()) {
            try {
                Path templatePath = Path.of(selectedTemplate.template());
                if (Files.exists(templatePath)) {
                    Jt.image(Files.readAllBytes(templatePath)).use(col0);
                }
            } catch (IOException e) {
                Jt.error("Error: " + e.getMessage()).use(col0);
            }
        }

        // --- Column 2: Inputs ---
        Jt.subheader("📝 Variables").use(col1);
        for (String field : selectedTemplate.fields()) {
            if (field.contains("PHOTO")) {
                var rawUploaded = Jt.fileUploader(field).use(col1);
                Object unwrapped = unwrap(rawUploaded);
                if (unwrapped instanceof JtUploadedFile uploaded) {
                    try {
                        Path uploadDir = Path.of("images/uploads");
                        Files.createDirectories(uploadDir);
                        byte[] fileBytes = uploaded.content();
                        Path filePath = uploadDir.resolve(uploaded.filename());
                        Files.write(filePath, fileBytes);
                        fieldValues.put(field, filePath.toString());
                        Jt.success("📷 OK").use(col1);
                        Jt.image(fileBytes).use(col1);
                    } catch (Exception e) {
                        Jt.error("Upload Error: " + e.getMessage()).use(col1);
                    }
                } else {
                    String current = config.getFieldByValue(field, config);
                    if (current != null && !current.isBlank()) {
                        try {
                            Path p = Path.of(current);
                            if (Files.exists(p)) {
                                Jt.text("Actuel: " + p.getFileName()).use(col1);
                                Jt.image(Files.readAllBytes(p)).use(col1);
                            }
                        } catch (IOException ignored) {
                        }
                    }
                }
            } else if (field.contains("PROMPT")) {
                String defaultValue = config.getFieldByValue(field, config);
                fieldValues.put(field, Jt.textArea(field).value(defaultValue).use(col1));
            } else {
                String defaultValue = config.getFieldByValue(field, config);
                fieldValues.put(field, Jt.textInput(field).value(defaultValue).use(col1));
            }
        }

        // Update config with new field values for next generation
        updateConfig(fieldValues);

        // --- Column 3: Result ---
        Jt.subheader("✨ Résultat").use(col2);

        // Display result based on template type
        if ("VIDEO".equals(selectedTemplate.type())) {
            Path videoResultPath = Path.of("generated/" + config.getDefaultResultFilenameVideo());
            if (Files.exists(videoResultPath)) {
                try {
                    Jt.success("🎬 Vidéo générée avec succès !").use(col2);
                    Jt.text("Fichier : " + config.getDefaultResultFilenameVideo()).use(col2);
                    Jt.markdown("📥 [Télécharger la vidéo](generated/" + config.getDefaultResultFilenameVideo() + ")")
                            .use(col2);
                } catch (Exception e) {
                    Jt.error("Display error: " + e.getMessage()).use(col2);
                }
            }
        } else if ("IMAGE".equals(selectedTemplate.type())) {
            Path imageResultPath = Path.of("generated/" + config.getDefaultResultFilename());
            if (Files.exists(imageResultPath)) {
                try {
                    Jt.image(Files.readAllBytes(imageResultPath)).use(col2);
                    Jt.text("Généré : " + config.getDefaultResultFilename()).use(col2);
                } catch (IOException e) {
                    Jt.error("Display error: " + e.getMessage()).use(col2);
                }
            }
        } else if ("POST".equals(selectedTemplate.type())) {
            // Display generated text from session state
            String generatedText = (String) Jt.sessionState().get("generatedText");
            if (generatedText != null && !generatedText.isBlank()) {
                Jt.markdown("### 📝 Texte généré").use(col2);
                Jt.markdown(generatedText).use(col2);
            }
        }
    }

    public void renderHistory() {
        Jt.title("📜 Historique des Générations").use();
        List<HistoryEntry> history = historyService.getHistory();

        if (history.isEmpty()) {
            Jt.info("Aucun historique pour le moment.").use();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (HistoryEntry entry : history) {
            var exp = Jt.expander(entry.templateName() + " - " + entry.timestamp().format(formatter)).use();

            var entryCols = Jt.columns(2)
                    .key("cols-" + entry.imagePath())
                    .use(exp);
            var colPrompt = entryCols.col(0);
            var colImage = entryCols.col(1);

            Jt.markdown("**Prompt:**").use(colPrompt);
            Jt.markdown(entry.prompt()).use(colPrompt);

            try {
                Path p = Path.of(entry.imagePath());
                if (Files.exists(p)) {
                    // Check if it's a video file
                    if (entry.imagePath().toLowerCase().endsWith(".mp4")) {
                        Jt.success("🎬 Vidéo").use(colImage);
                        Jt.markdown("📥 [Télécharger](" + entry.imagePath() + ")").use(colImage);
                    } else {
                        Jt.image(Files.readAllBytes(p)).use(colImage);
                    }
                } else {
                    Jt.warning("Fichier non trouvé: " + entry.imagePath()).use(colImage);
                }
            } catch (IOException e) {
                Jt.error("Error loading file").use(colImage);
            }
            Jt.markdown("---").key("hist-sep-" + entry.imagePath()).use();
        }
    }

    private Object unwrap(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof java.util.Optional<?> opt)
            return opt.isPresent() ? unwrap(opt.get()) : null;
        if (obj instanceof java.util.Collection<?> col)
            return col.isEmpty() ? null : unwrap(col.iterator().next());
        return obj;
    }

    private void updateConfig(Map<String, String> values) {
        values.forEach((field, value) -> {
            if (value == null || value.isBlank())
                return;
            switch (field) {
                case "NAME" -> config.setDefaultName(value);
                case "NAME2" -> config.setDefaultName2(value);
                case "NAME3" -> config.setDefaultName3(value);
                case "TITLE" -> config.setDefaultTitle(value);
                case "PHOTO" -> config.setDefaultPhoto(value);
                case "PHOTO2" -> config.setDefaultPhoto2(value);
                case "PHOTO3" -> config.setDefaultPhoto3(value);
                case "CONF_PHOTO" -> config.setDefaultConfPhoto(value);
                case "CONF" -> config.setDefaultConf(value);
                case "PROMPT" -> config.setDefaultPrompt(value);
            }
        });
    }

    private Content prepareContent(Template template, String prompt) throws IOException {
        List<Part> parts = new ArrayList<>();
        if (template.template() != null && !template.template().isBlank()) {
            Path p = Path.of(template.template());
            if (Files.exists(p))
                parts.add(Part.fromBytes(Files.readAllBytes(p), Utils.getMimeType(p.toString())));
        }
        for (String field : template.fields()) {
            if (field.contains("PHOTO")) {
                String path = config.getFieldByValue(field, config);
                if (path != null && !path.isBlank()) {
                    Path p = Path.of(path);
                    if (Files.exists(p))
                        parts.add(Part.fromBytes(Files.readAllBytes(p), Utils.getMimeType(p.toString())));
                }
            }
        }
        parts.add(Part.fromText(prompt));
        return Content.fromParts(parts.toArray(new Part[0]));
    }
}
