package app.voxly.model.dto.response;

import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.LearningGoal;
import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String country;
    private LearningGoal goal;
    private DifficultyTier currentTier;
    private int careerReadinessScore;
    private int streakDays;
    private int totalXp;
    private int weeklyXp;
    private String rank;
    private boolean emailVerified;
}
