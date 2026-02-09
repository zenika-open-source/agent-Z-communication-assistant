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
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class TemplateService {

    private final List<Template> templates;

    public TemplateService() {
        Log.info("👷 Loading templates from templates.json ...");
        List<Template> loadedTemplates = Collections.emptyList();
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = TemplateService.class.getResourceAsStream("/templates.json")) {
            if (is == null) {
                Log.error("❌ Cannot find templates.json in classpath");
            } else {
                loadedTemplates = objectMapper.readValue(is, new TypeReference<>() {
                });
                Log.infof("✅ Loaded %d templates", loadedTemplates.size());
            }
        } catch (Exception e) {
            Log.error("❌ Failed to load or parse templates.json", e);
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
            this.getTemplates().forEach(t -> Log.info(String.format("  - %s: %s", t.name(), t.description())));
            System.out.print("\uD83D\uDCDD Enter template name: ");
            String inputName = System.console().readLine();
            selectedTemplate = this.findByName(inputName);

            if (selectedTemplate.isEmpty()) {
                Log.info(String.format(
                        "❌ Template '%s' not found. Please use the 'list' command to see available templates.",
                        inputName));
            }
        }

        Log.infof("✅ Template selected: %s", selectedTemplate.get().name());

        return selectedTemplate.get();

    }

    public String preparePrompt(Template temp, ConfigProperties config) {
        return preparePrompt(temp, config, Collections.emptyMap());
    }

    public String preparePrompt(Template temp, ConfigProperties config, Map<String, String> sessionOverrides) {
        var finalPrompt = temp.prompt();
        var templateRegex = "%".concat(FIELDS_PROMPT.TEMPLATE.getValue()).concat("%");

        if (temp.template() != null && finalPrompt.indexOf(templateRegex) != -1) {
            finalPrompt = finalPrompt.replaceFirst(templateRegex, temp.template());
        }

        if (!temp.fields().isEmpty()) {
            for (String field : temp.fields()) {
                if (finalPrompt.contains("%" + field + "%")) {
                    // Check session overrides first, then fall back to config
                    String value = sessionOverrides.getOrDefault(field, config.getFieldByValue(field, config));
                    finalPrompt = finalPrompt.replace("%" + field + "%", value != null ? value : "");
                }
            }
        }

        return finalPrompt;
    }
}