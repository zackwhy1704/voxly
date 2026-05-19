package app.voxly.model.dto.response;

import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.ScenarioCategory;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScenarioResponse {
    private UUID id;
    private String title;
    private String subtitle;
    private DifficultyTier tier;
    private ScenarioCategory category;
    private String description;
    private int durationMinutes;
    private long accentColorHex;
    private int xpReward;
    private boolean isLocked;
    private List<String> skillsTested;
}
