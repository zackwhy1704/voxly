package app.voxly.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChallengeResponse {
    private UUID id;
    private String title;
    private String description;
    private int xpReward;
    private long accentColorHex;
    private LocalDateTime resetsAt;
    private boolean completed;
}
