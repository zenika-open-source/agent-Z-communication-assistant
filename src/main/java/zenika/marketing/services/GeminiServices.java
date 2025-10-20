package zenika.marketing.services;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class GeminiServices {

    @Inject
    ConfigProperties config;

    public void generateImage(String model, String prompt, String output, String templatePath,
                              String file1Path, String file2Path) throws IOException {
        try (Client client = new Client.Builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
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
                            Part.fromBytes(Files.readAllBytes(templateFile), Utils.getMimeType(templateFile.toString())),
                            Part.fromBytes(Files.readAllBytes(file1), Utils.getMimeType(file1.toString())),
                            Part.fromBytes(Files.readAllBytes(file2), Utils.getMimeType(file2.toString())),
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

    public void generateVideo(String model, String prompt, String output, String templatePath,
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
                            .image(Image.fromFile(templatePath, Utils.getMimeType(Path.of(templatePath).toString())))
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
