package zenika.marketing.domain;

import java.time.LocalDateTime;

public record HistoryEntry(
        String templateName,
        String prompt,
        String imagePath,
        LocalDateTime timestamp) {
}
