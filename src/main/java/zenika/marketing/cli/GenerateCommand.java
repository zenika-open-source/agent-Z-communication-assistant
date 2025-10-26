package zenika.marketing.cli;

import io.quarkus.logging.Log;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.config.MODE_FEATURE;
import zenika.marketing.services.GeminiServices;
import zenika.marketing.services.TemplateService;

@Command(
        name = "generate",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "Generate images or videos using Gemini AI."
)
public class GenerateCommand implements Runnable {


    private final GeminiServices geminiServices;

    private final ConfigProperties config;

    private final TemplateService templateService;

    @Option(
            names = {"-t", "--type"},
            description = "Media type to generate: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
            defaultValue = "IMAGE"
    )
    MODE_FEATURE mediaType;

    @Option(
            names = {"-p", "--prompt"},
            description = "Prompt for generation"
    )
    String prompt;

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
            description = "Name of the template to use"
    )
    String templateName;

    public GenerateCommand(GeminiServices geminiServices, ConfigProperties config, TemplateService templateService) {
        this.geminiServices = geminiServices;
        this.config = config;
        this.templateService = templateService;
    }

    @Override
    public void run() {
        try {
            var template = templateService.waitAValidTemplateByUser(templateName);

            String templatePrompt = template.prompt();
            String finalOutput = output != null ? output : config.getDefaultResultFilename();
            String finalTemplatePath = templatePath != null ? templatePath : config.getDefaultTemplatePath();
            String finalFile1Path = file1Path != null ? file1Path : config.getDefaultFile1Path();
            String finalFile2Path = file2Path != null ? file2Path : config.getDefaultFile2Path();
            String finalVideoRatio = config.getDefaultVideoRatio();
            String finalVideoResolution = config.getDefaultVideoResolution();

            if (template.type() == MODE_FEATURE.IMAGE) {
                String finalModel = model != null ? model : config.getDefaultGeminiModelImage();
                geminiServices.generateImage(finalModel, templatePrompt, finalOutput, finalTemplatePath, finalFile1Path, finalFile2Path);
            } else if (template.type() == MODE_FEATURE.VIDEO) {
                String finalModel = model != null ? model : config.getDefaultGeminiVeoModel();
                geminiServices.generateVideo(finalModel, templatePrompt, finalOutput, finalTemplatePath, finalVideoRatio, finalVideoResolution);
            }

        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}