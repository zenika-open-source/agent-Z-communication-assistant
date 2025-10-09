package zenika.marketing;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.*;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import zenika.marketing.config.MODE_FEATURE;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(PlayingWithGoogleAIAPI.class, args);
    }

    public static class PlayingWithGoogleAIAPI implements QuarkusApplication {

        @ConfigProperty(name = "app.gemini.model")
        String geminiModelImage;

        @ConfigProperty(name = "app.gemini.model.veo")
        String geminiVeoModel;

        @ConfigProperty(name = "app.result.filename")
        String resultFilename;

        @ConfigProperty(name = "app.prompt")
        String prompt;

        @ConfigProperty(name = "app.template.path")
        String templatePath;

        @ConfigProperty(name = "app.file1.path")
        String file1Path;

        @ConfigProperty(name = "app.file2.path")
        String file2Path;

        @ConfigProperty(name = "app.media.type", defaultValue = "image")
        String mediaType;

        @ConfigProperty(name = "app.video.ratio")
        String videoRatio;

        @ConfigProperty(name = "app.video.resolution")
        String videoResolution;

        static String contentTypePng = "image/png";

        @Override
        public int run(String... args) throws Exception {

            if (MODE_FEATURE.IMAGE.toString().equals(mediaType)) {
                return generateImage();
            } else if (MODE_FEATURE.VIDEO.toString().equals(mediaType)) {
                return generateVideo();
            } else {
                throw new Exception("\uD83C\uDFDA\uFE0F Invalid media type: " + mediaType);
            }

        }

        /**
         * Determines the MIME type based on file extension
         */
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

        private int generateImage() throws IOException {
            try (Client client = new Client.Builder()
                    .apiKey(System.getenv("GOOGLE_API_KEY"))
                    //.project(System.getenv("GOOGLE_CLOUD_PROJECT_ID"))
                    //.location(System.getenv("GOOGLE_CLOUD_LOCATION"))
                    //.vertexAI(true)
                    .build()) {

                Log.info("✨ Start using Google AI API with this model " + geminiModelImage);

                // Validate file exists
                Path templateFile = Path.of(templatePath);
                Path file1 = Path.of(file1Path);
                Path file2 = Path.of(file2Path);
                if (!Files.exists(templateFile)) {
                    Log.warn("❌ Template file not found: " + templatePath);
                }
                Log.info("\uD83C\uDFA8 Using template: " + templateFile.toString());

                var response = client.models.generateContent(
                        geminiModelImage,
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
                            try {
                                Files.write(Paths.get(resultFilename), blob.data().get());
                                break;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                Log.info("✨ Image generated - " + resultFilename);
                System.exit(0);
                return 0;
            }
        }

        private int generateVideo() {
            try (Client client = new Client.Builder()
                    .project(System.getenv("GOOGLE_CLOUD_PROJECT_ID"))
                    .location(System.getenv("GOOGLE_CLOUD_LOCATION"))
                    .vertexAI(true)
                    .build()) {

                Log.info("✨ Start using Google AI API with this model " + geminiVeoModel);

                GenerateVideosOperation operation = client.models.generateVideos(
                        geminiVeoModel,
                        GenerateVideosSource.builder()
                                .prompt(prompt)
                                .image(Image.fromFile(templatePath, getMimeType(Path.of(templatePath).toString())))
                                .build(),
                        GenerateVideosConfig.builder()
                                .aspectRatio(videoRatio)
                                .resolution(videoResolution)
                                .generateAudio(true)
                                .build()
                );

                // GenerateVideosOperation.done() is empty if the operation is not done.
                while (!operation.done().filter(Boolean::booleanValue).isPresent()) {
                    try {
                        Thread.sleep(10000); // Sleep for 10 seconds.
                        operation =
                                client.operations.getVideosOperation(operation, null);
                        System.out.println("Waiting for operation to complete...");
                    } catch (InterruptedException e) {
                        System.out.println("Thread was interrupted while sleeping.");
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(
                        "Generated "
                                + operation.response().get().generatedVideos().get().size()
                                + " video(s).");

                Video generatedVideo =
                        operation.response().get().generatedVideos().get().get(0).video().get();

                if (!client.vertexAI()) {
                    try {
                        client.files.download(generatedVideo, "video.mp4", null);
                        System.out.println("Downloaded video to video.mp4");
                    } catch (GenAiIOException e) {
                        System.out.println("An error occurred while downloading the video: " + e.getMessage());
                    }
                }
                Log.info("✨ Video generated - " + resultFilename);
                System.exit(0);
                return 0;
            }
        }
    }
}