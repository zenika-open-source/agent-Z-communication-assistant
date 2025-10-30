package zenika.marketing.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.config.FIELDS_PROMPT;
import zenika.marketing.domain.Template;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TemplateService {

    private final List<Template> templates;

    public TemplateService() {
        Log.info("\uD83D\uDC77 Read and parse templates file ...");
        List<Template> loadedTemplates;
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/templates.json")) {
            if (is == null) {
                throw new IllegalStateException("Cannot find templates.json in classpath");
            }
            loadedTemplates = objectMapper.readValue(is, new TypeReference<>() {});

            Log.info(String.format("\uD83D\uDC77 ✅ Templates file read ... %s templates available", loadedTemplates.size()));
        } catch (Exception e) {
            Log.error("\\uD83D\uDC77 ❌ Failed to load or parse templates file ... ", e);
            loadedTemplates = Collections.emptyList();
        }
        this.templates = loadedTemplates;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public Optional<Template> findByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return templates.stream().filter(t -> name.equalsIgnoreCase(t.name())).findFirst();
    }

    public Template waitAValidTemplateByUser(String templateName) {
        Optional<Template> selectedTemplate = this.findByName(templateName);

        while (!selectedTemplate.isPresent()) {
            Log.info("\uD83D\uDC40 Please choose a template from the list below:");
            this.getTemplates().forEach(t ->
                    Log.info(String.format("  - %s: %s", t.name(), t.description()))
            );
            System.out.print("\uD83D\uDCDD Enter template name: ");
            String inputName = System.console().readLine();
            selectedTemplate = this.findByName(inputName);

            if (selectedTemplate.isEmpty()) {
                Log.info(String.format("❌ Template '%s' not found. Please use the 'list' command to see available templates.", inputName));
            }
        }

        Log.infof("✅ Template selected: %s", selectedTemplate.get().name());

        return selectedTemplate.get();

    }

    public String preparePrompt(Template temp, ConfigProperties config) {
        var finalPrompt = temp.prompt();

        // Template (outside fields)
        finalPrompt = finalPrompt.replaceFirst("%".concat(FIELDS_PROMPT.TEMPLATE.getValue()).concat("%"), temp.template());

        if (!temp.fields().isEmpty()) {
            for (String field : temp.fields()) {
                finalPrompt = finalPrompt.replaceFirst("%".concat(field).concat("%"), config.getFieldByValue(field, config));
            }
        }

        return finalPrompt;
    }
}