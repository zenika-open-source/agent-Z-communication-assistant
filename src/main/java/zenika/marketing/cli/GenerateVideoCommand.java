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
        name = "video",
        mixinStandardHelpOptions = true,
        description = "Generate a video using Gemini AI."
)
public class GenerateVideoCommand implements Runnable {

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

    @Option(
            names = {"--ratio"},
            description = "Video aspect ratio (default: configured default)"
    )
    String videoRatio;

    @Option(
            names = {"--resolution"},
            description = "Video resolution (default: configured default)"
    )
    String videoResolution;

    @Override
    public void run() {
        try {
            var template = templateService.waitAValidTemplateByUser(templateName);

            // Check that the template is of type VIDEO
            if (!template.type().equals(MODE_FEATURE.VIDEO)) {
                Log.error("❌ Error: Template '" + templateName + "' is not a VIDEO template");
                System.exit(1);
                return;
            }

            String templatePrompt = template.prompt();
            String finalOutput = output != null ? output : config.getDefaultResultFilename();
            String finalTemplatePath = templatePath != null ? templatePath : config.getDefaultTemplatePath();
            String finalModel = model != null ? model : config.getDefaultGeminiVeoModel();
            String finalVideoRatio = videoRatio != null ? videoRatio : config.getDefaultVideoRatio();
            String finalVideoResolution = videoResolution != null ? videoResolution : config.getDefaultVideoResolution();

            geminiServices.generateVideo(
                    finalModel,
                    templatePrompt,
                    finalOutput,
                    finalTemplatePath,
                    finalVideoRatio,
                    finalVideoResolution
            );

        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
