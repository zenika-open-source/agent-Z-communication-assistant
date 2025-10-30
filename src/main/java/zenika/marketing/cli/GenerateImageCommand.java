package zenika.marketing.cli;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.config.MODE_FEATURE;
import zenika.marketing.services.GeminiServices;
import zenika.marketing.services.TemplateService;

@Command(
        name = "image",
        mixinStandardHelpOptions = true,
        description = "Generate an image using Gemini AI."
)
public class GenerateImageCommand implements Runnable {

    @Inject
    GeminiServices geminiServices;

    @Inject
    ConfigProperties config;

    @Inject
    TemplateService templateService;

    @Option(
            names = {"-o", "--output"},
            description = "Output filename"
    )
    String output;

    @Option(
            names = {"--template"},
            description = "Path to template image"
    )
    String templatePath;

    @Option(
            names = {"--name"},
            description = "Speaker or writer Name"
    )
    String name;

    @Option(
            names = {"--title"},
            description = "Blog post or talk title"
    )
    String title;

    @Option(
            names = {"-m", "--model"},
            description = "Gemini model to use"
    )
    String model;

    @Option(
            names = {"--template-name"},
            description = "Name of the template to use",
            required = true
    )
    String templateName;

    @Override
    public void run() {
        try {
            var template = templateService.waitAValidTemplateByUser(templateName);

            // Check that the template is of type IMAGE
            if (!template.type().equals(MODE_FEATURE.IMAGE.toString())) {
                Log.error("❌ Error: Template '" + templateName + "' is not an IMAGE template (" + template.type() + ")");
                System.exit(1);
                return;
            }

            String templatePrompt = template.prompt();
            String finalTemplatePath = templatePath != null ? templatePath : config.getDefaultTemplatePath();
            String finalModel = model != null ? model : config.getDefaultGeminiModelImage();

            config.setDefaultName(name != null ? name : config.getDefaultName());
            config.setDefaultTitle(title != null ? title : config.getDefaultTitle());
            config.setDefaultResultFilename(output != null ? output : config.getDefaultResultFilename());

            String completedPrompt = templateService.preparePrompt(template, config);

            Log.info("\uD83D\uDCDD \uD83D\uDC49 Prompt: \n \t " + completedPrompt + "\n");

            geminiServices.generateImage(
                    finalModel,
                    completedPrompt,
                    config
            );
            System.exit(0);

        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
