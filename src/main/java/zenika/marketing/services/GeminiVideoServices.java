package zenika.marketing.services;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class GeminiVideoServices {

    @Inject
    ConfigProperties config;

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

            try {
                client.files.download(generatedVideo, output, null);
                Log.info("✨ Video downloaded to " + output);
            } catch (GenAiIOException e) {
                Log.error("An error occurred while downloading the video: " + e.getMessage());
            }

            Log.info("✨ Video generated: " + output);
        }
    }

}
