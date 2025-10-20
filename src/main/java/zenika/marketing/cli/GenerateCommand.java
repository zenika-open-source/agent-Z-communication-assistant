package zenika.marketing.cli;

import io.quarkus.logging.Log;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.config.MODE_FEATURE;
import zenika.marketing.services.GeminiServices;

@Command(
    name = "generate",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Generate images or videos using Gemini AI"
)
public class GenerateCommand implements Runnable {


    private final GeminiServices geminiServices;

    private final ConfigProperties config;

    @Option(
        names = {"-t", "--type"},
        description = "Media type to generate: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
        defaultValue = "image"
    )
    MODE_FEATURE mediaType;

    @Option(
        names = {"-p", "--prompt"},
        description = "Prompt for generation (default: from application.properties)"
    )
    String prompt;

    @Option(
        names = {"-o", "--output"},
        description = "Output filename (default: ${DEFAULT-VALUE})"
    )
    String output;

    @Option(
        names = {"--template"},
        description = "Path to template image (default: from application.properties)"
    )
    String templatePath;

    @Option(
        names = {"--file1"},
        description = "Path to first additional image (default: from application.properties)"
    )
    String file1Path;

    @Option(
        names = {"--file2"},
        description = "Path to second additional image (default: from application.properties)"
    )
    String file2Path;

    @Option(
        names = {"-m", "--model"},
        description = "Gemini model to use (default: from application.properties)"
    )
    String model;

    @Option(
        names = {"--ratio"},
        description = "Video aspect ratio (for video generation, default: from application.properties)"
    )
    String videoRatio;

    @Option(
        names = {"--resolution"},
        description = "Video resolution (for video generation, default: from application.properties)"
    )
    String videoResolution;

    public GenerateCommand(GeminiServices geminiServices, ConfigProperties config){
        this.geminiServices = geminiServices;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            // Use CLI options or fallback to defaults
            String finalPrompt = prompt != null ? prompt : config.getDefaultPrompt();
            String finalOutput = output != null ? output : config.getDefaultResultFilename();
            String finalTemplatePath = templatePath != null ? templatePath : config.getDefaultTemplatePath();
            String finalFile1Path = file1Path != null ? file1Path : config.getDefaultFile1Path();
            String finalFile2Path = file2Path != null ? file2Path : config.getDefaultFile2Path();
            String finalVideoRatio = videoRatio != null ? videoRatio : config.getDefaultVideoRatio();
            String finalVideoResolution = videoResolution != null ? videoResolution : config.getDefaultVideoResolution();

            if (mediaType == MODE_FEATURE.IMAGE) {
                String finalModel = model != null ? model : config.getDefaultGeminiModelImage();
                geminiServices.generateImage(finalModel, finalPrompt, finalOutput, finalTemplatePath, finalFile1Path, finalFile2Path);
            } else if (mediaType == MODE_FEATURE.VIDEO) {
                String finalModel = model != null ? model : config.getDefaultGeminiVeoModel();
                geminiServices.generateVideo(finalModel, finalPrompt, finalOutput, finalTemplatePath, finalVideoRatio, finalVideoResolution);
            } else {
                Log.error("🏔️ Invalid media type: " + mediaType);
                System.exit(1);
            }
        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
