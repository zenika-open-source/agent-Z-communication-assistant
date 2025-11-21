package zenika.marketing.cli;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.domain.Template;
import zenika.marketing.services.GeminiVideoServices;
import zenika.marketing.services.TemplateService;

@Command(name = "video", mixinStandardHelpOptions = true, description = "Generate a video using Gemini AI.")
public class GenerateVideoCommand implements Runnable {

        @Inject
        GeminiVideoServices geminiServices;

        @Inject
        ConfigProperties config;

        @Inject
        TemplateService templateService;

        @Option(names = { "-o", "--output" }, description = "Output filename")
        String output;

        @Option(names = { "--photo" }, description = "Path to image to use")
        String photo;

        @Option(names = { "--template-name" }, description = "Name of the template to use", required = true)
        String templateName;

        @Option(names = { "--ratio" }, description = "Video aspect ratio (default: configured default)")
        String videoRatio;

        @Option(names = { "--resolution" }, description = "Video resolution (default: configured default)")
        String videoResolution;

        public void run() {
                try {
                        Template template = templateService.waitAValidTemplateByUser(templateName);
                        config.setDefaultPrompt(template.prompt());
                        String finalOutput = output != null ? output : config.getDefaultResultFilenameVideo();
                        config.setDefaultPhoto(photo != null ? photo : config.getDefaultPhoto());
                        config.setDefaultGeminiVeoModel(config.getDefaultGeminiVeoModel());
                        config.setDefaultVideoResolution(videoResolution != null ? videoResolution
                                        : config.getDefaultVideoResolution());
                        config.setDefaultVideoRatio(videoRatio != null ? videoRatio : config.getDefaultVideoRatio());
                        config.setDefaultPhoto(photo);
                        config.setDefaultResultFilename(output != null ? output : config.getDefaultResultFilename());

                        String completedPrompt = templateService.preparePrompt(template, config);

                        Log.infof("-> generate Video %s", template.name());
                        Log.info("\uD83D\uDCDD \uD83D\uDC49 Prompt: \n \t " + completedPrompt + "\n");

                        geminiServices.generateVideo(finalOutput, config);
                        System.exit(0);

                } catch (Exception e) {
                        Log.error("❌ Error: " + e.getMessage(), e);
                        System.exit(1);
                }
        }
}
