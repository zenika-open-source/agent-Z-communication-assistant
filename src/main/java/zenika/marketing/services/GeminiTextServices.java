package zenika.marketing.services;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.marketing.config.ConfigProperties;

@ApplicationScoped
public class GeminiTextServices {

    @Inject
    ConfigProperties config;

    public String generateText(String model, Content content) {
        try (Client client = new Client.Builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .build()) {

            Log.info("✨ Start using Google AI API with model " + model + " for text generation");
            Log.info("\uD83D\uDCDD \uD83D\uDC49 Prompt: \n \t " + content.text() + "\n");

            var response = client.models.generateContent(
                    model,
                    content,
                    GenerateContentConfig.builder()
                            .temperature(0.7f)
                            .build());

            // Extract text from response parts
            StringBuilder generatedText = new StringBuilder();
            if (response.parts() != null) {
                for (var part : response.parts()) {
                    if (part.text().isPresent()) {
                        generatedText.append(part.text().get());
                    }
                }
            }

            if (generatedText.length() > 0) {
                Log.info("✨ Text generated successfully");
                return generatedText.toString();
            } else {
                Log.warn("⚠️ No text generated in response");
                return "";
            }

        } catch (Exception e) {
            Log.error("❌ Error generating text: " + e.getMessage(), e);
            throw new RuntimeException("Failed to generate text", e);
        }
    }
}
