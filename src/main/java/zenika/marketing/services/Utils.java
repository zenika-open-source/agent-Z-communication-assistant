package zenika.marketing.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Utils {

    public static String getMimeType(String filePath) {
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

}
