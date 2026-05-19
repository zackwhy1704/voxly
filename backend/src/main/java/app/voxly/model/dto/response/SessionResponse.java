package app.voxly.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SessionResponse {
    private UUID id;
    private UUID userId;
    private UUID scenarioId;
    private String scenarioTitle;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int durationSeconds;
    private int overallScore;
    private int pronunciationScore;
    private int fluencyScore;
    private int grammarScore;
    private int vocabularyScore;
    private int xpEarned;
    private List<String> aiInsights;
}
