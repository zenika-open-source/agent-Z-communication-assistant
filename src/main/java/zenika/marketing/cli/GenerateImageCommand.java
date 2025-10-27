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
            description = "Output filename (default: ${DEFAULT-VALUE})"
    )
    String output;

    @Option(
            names = {"--template"},
            description = "Path to template image"
    )
    String templatePath;

    @Option(
            names = {"--file1"},
            description = "Path to first additional image"
    )
    String file1Path;

    @Option(
            names = {"--file2"},
            description = "Path to second additional image"
    )
    String file2Path;

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
            if (template.type() != MODE_FEATURE.IMAGE) {
                Log.error("❌ Error: Template '" + templateName + "' is not an IMAGE template");
                System.exit(1);
                return;
            }

            String templatePrompt = template.prompt();
            String finalOutput = output != null ? output : config.getDefaultResultFilename();
            String finalTemplatePath = templatePath != null ? templatePath : config.getDefaultTemplatePath();
            String finalFile1Path = file1Path != null ? file1Path : config.getDefaultFile1Path();
            String finalFile2Path = file2Path != null ? file2Path : config.getDefaultFile2Path();
            String finalModel = model != null ? model : config.getDefaultGeminiModelImage();

            geminiServices.generateImage(
                    finalModel,
                    templatePrompt,
                    finalOutput,
                    finalTemplatePath,
                    finalFile1Path,
                    finalFile2Path
            );

        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
