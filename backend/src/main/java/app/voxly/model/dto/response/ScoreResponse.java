package app.voxly.model.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScoreResponse {
    private int pronunciation;
    private int fluency;
    private int grammar;
    private int vocabulary;
    private int overall;
    private List<String> aiInsights;
    private List<String> flaggedWords;
    private List<String> fillerWords;
    private int xpEarned;
    private boolean isPersonalBest;
}
