package app.voxly.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CompleteSessionRequest {
    @NotNull private int pronunciationScore;
    @NotNull private int fluencyScore;
    @NotNull private int grammarScore;
    @NotNull private int vocabularyScore;
    @NotNull private int durationSeconds;
    private List<String> aiInsights;
    private List<String> fillerWords;
    private List<String> flaggedWords;
    private List<Float> pacingData;
}
