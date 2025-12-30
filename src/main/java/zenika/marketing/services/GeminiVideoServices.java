package zenika.marketing.services;

import java.nio.file.Path;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.GenerateVideosConfig;
import com.google.genai.types.GenerateVideosOperation;
import com.google.genai.types.GenerateVideosSource;
import com.google.genai.types.Image;
import com.google.genai.types.Video;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;
import zenika.marketing.utils.Utils;

@ApplicationScoped
public class GeminiVideoServices {

    @Inject
    ConfigProperties config;

    public void generateVideo(String prompt, ConfigProperties config) {
        try (Client client = new Client.Builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT_ID"))
                .location(System.getenv("GOOGLE_CLOUD_LOCATION"))
                .vertexAI(true)
                .build()) {

            Log.info("✨ Start using Google AI API with model " + config.getDefaultGeminiVeoModel());

            GenerateVideosOperation operation = client.models.generateVideos(
                    config.getDefaultGeminiVeoModel(),
                    GenerateVideosSource.builder()
                            .prompt(prompt)
                            .image(Image.fromFile(config.getDefaultPhoto(),
                                    Utils.getMimeType(Path.of(config.getDefaultPhoto()).toString())))
                            .build(),
                    GenerateVideosConfig.builder()
                            .aspectRatio(config.getDefaultVideoRatio())
                            .resolution(config.getDefaultVideoResolution())
                            .generateAudio(true)
                            .build());

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
                client.files.download(generatedVideo, "generated/" + config.getDefaultResultFilenameVideo(), null);
                Log.info("✨ Video downloaded to " + config.getDefaultResultFilenameVideo());
            } catch (GenAiIOException e) {
                Log.error("An error occurred while downloading the video: " + e.getMessage());
            }

            Log.info("✨ Video generated: " + config.getDefaultResultFilenameVideo());
        }
    }
}
