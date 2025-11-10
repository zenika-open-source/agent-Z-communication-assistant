package zenika.marketing.services;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class GeminiImagesServices {

    @Inject
    ConfigProperties config;

    public void generateImage(String model, ConfigProperties config, Content content) throws IOException {
        try (Client client = new Client.Builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .build()) {

            Log.info("✨ Start using Google AI API with model " + model);

            var response = client.models.generateContent(model, content, null);

            for (Part part : Objects.requireNonNull(response.parts())) {
                if (part.inlineData().isPresent()) {
                    var blob = part.inlineData().get();
                    if (blob.data().isPresent()) {
                        Files.write(Paths.get(config.getDefaultResultFilename()), blob.data().get());
                        break;
                    }
                }
            }
            Log.info("✨ Image generated: " + config.getDefaultResultFilename());
        }
    }
}
