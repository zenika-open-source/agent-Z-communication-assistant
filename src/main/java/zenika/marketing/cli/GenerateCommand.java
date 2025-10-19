package zenika.marketing.cli;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.*;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import zenika.marketing.config.MODE_FEATURE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Command(
    name = "generate",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Generate images or videos using Gemini AI"
)
public class GenerateCommand implements Runnable {

    @ConfigProperty(name = "app.gemini.model")
    String defaultGeminiModelImage;

    @ConfigProperty(name = "app.gemini.model.veo")
    String defaultGeminiVeoModel;

    @ConfigProperty(name = "app.result.filename")
    String defaultResultFilename;

    @ConfigProperty(name = "app.prompt")
    String defaultPrompt;

    @ConfigProperty(name = "app.template.path")
    String defaultTemplatePath;

    @ConfigProperty(name = "app.file1.path")
    String defaultFile1Path;

    @ConfigProperty(name = "app.file2.path")
    String defaultFile2Path;

    @ConfigProperty(name = "app.video.ratio")
    String defaultVideoRatio;

    @ConfigProperty(name = "app.video.resolution")
    String defaultVideoResolution;

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

    @Option(
        names = {"--vertex"},
        description = "Use Vertex AI instead of Google AI API (required for video generation)",
        defaultValue = "false"
    )
    boolean useVertexAI;

    @Override
    public void run() {
        try {
            // Use CLI options or fallback to defaults
            String finalPrompt = prompt != null ? prompt : defaultPrompt;
            String finalOutput = output != null ? output : defaultResultFilename;
            String finalTemplatePath = templatePath != null ? templatePath : defaultTemplatePath;
            String finalFile1Path = file1Path != null ? file1Path : defaultFile1Path;
            String finalFile2Path = file2Path != null ? file2Path : defaultFile2Path;
            String finalVideoRatio = videoRatio != null ? videoRatio : defaultVideoRatio;
            String finalVideoResolution = videoResolution != null ? videoResolution : defaultVideoResolution;

            if (mediaType == MODE_FEATURE.IMAGE) {
                String finalModel = model != null ? model : defaultGeminiModelImage;
                generateImage(finalModel, finalPrompt, finalOutput, finalTemplatePath, finalFile1Path, finalFile2Path);
            } else if (mediaType == MODE_FEATURE.VIDEO) {
                String finalModel = model != null ? model : defaultGeminiVeoModel;
                generateVideo(finalModel, finalPrompt, finalOutput, finalTemplatePath, finalVideoRatio, finalVideoResolution);
            } else {
                Log.error("🏔️ Invalid media type: " + mediaType);
                System.exit(1);
            }
        } catch (Exception e) {
            Log.error("❌ Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> {
                Log.warn("Unknown image format: " + extension + ", defaulting to image/jpeg");
                yield "image/jpeg";
            }
        };
    }

    private void generateImage(String model, String prompt, String output, String templatePath, 
                               String file1Path, String file2Path) throws IOException {
        try (Client client = new Client.Builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .vertexAI(useVertexAI)
                .build()) {

            Log.info("✨ Start using Google AI API with model " + model);

            // Validate files exist
            Path templateFile = Path.of(templatePath);
            Path file1 = Path.of(file1Path);
            Path file2 = Path.of(file2Path);
            
            if (!Files.exists(templateFile)) {
                Log.error("❌ Template file not found: " + templatePath);
                System.exit(1);
            }
            if (!Files.exists(file1)) {
                Log.error("❌ File 1 not found: " + file1Path);
                System.exit(1);
            }
            if (!Files.exists(file2)) {
                Log.error("❌ File 2 not found: " + file2Path);
                System.exit(1);
            }

            Log.info("🎨 Using template: " + templateFile);

            var response = client.models.generateContent(
                    model,
                    Content.fromParts(
                            Part.fromBytes(Files.readAllBytes(templateFile), getMimeType(templateFile.toString())),
                            Part.fromBytes(Files.readAllBytes(file1), getMimeType(file1.toString())),
                            Part.fromBytes(Files.readAllBytes(file2), getMimeType(file2.toString())),
                            Part.fromText(prompt)
                    ),
                    null);

            for (Part part : Objects.requireNonNull(response.parts())) {
                if (part.inlineData().isPresent()) {
                    var blob = part.inlineData().get();
                    if (blob.data().isPresent()) {
                        Files.write(Paths.get(output), blob.data().get());
                        break;
                    }
                }
            }

            Log.info("✨ Image generated: " + output);
        }
    }

    private void generateVideo(String model, String prompt, String output, String templatePath,
                               String ratio, String resolution) {
        try (Client client = new Client.Builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT_ID"))
                .location(System.getenv("GOOGLE_CLOUD_LOCATION"))
                .vertexAI(true)
                .build()) {

            Log.info("✨ Start using Google AI API with model " + model);

            GenerateVideosOperation operation = client.models.generateVideos(
                    model,
                    GenerateVideosSource.builder()
                            .prompt(prompt)
                            .image(Image.fromFile(templatePath, getMimeType(Path.of(templatePath).toString())))
                            .build(),
                    GenerateVideosConfig.builder()
                            .aspectRatio(ratio)
                            .resolution(resolution)
                            .generateAudio(true)
                            .build()
            );

            while (!operation.done().filter(Boolean::booleanValue).isPresent()) {
                try {
                    Thread.sleep(10000);
                    operation = client.operations.getVideosOperation(operation, null);
                    System.out.println("⏳ Waiting for operation to complete...");
                } catch (InterruptedException e) {
                    System.out.println("Thread was interrupted while sleeping.");
                    Thread.currentThread().interrupt();
                }
            }
            
            System.out.println("Generated " + operation.response().get().generatedVideos().get().size() + " video(s).");

            Video generatedVideo = operation.response().get().generatedVideos().get().get(0).video().get();

            if (!client.vertexAI()) {
                try {
                    client.files.download(generatedVideo, output, null);
                    Log.info("✨ Video downloaded to " + output);
                } catch (GenAiIOException e) {
                    Log.error("An error occurred while downloading the video: " + e.getMessage());
                }
            }
            
            Log.info("✨ Video generated: " + output);
        }
    }
}
