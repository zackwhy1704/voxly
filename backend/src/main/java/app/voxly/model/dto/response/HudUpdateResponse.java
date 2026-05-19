package app.voxly.model.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HudUpdateResponse {
    private String pace;
    private int fillerCount;
    private float clarityScore;
    private int elapsedSeconds;
    private String partialTranscript;
}
