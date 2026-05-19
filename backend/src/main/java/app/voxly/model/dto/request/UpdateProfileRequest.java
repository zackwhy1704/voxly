package app.voxly.model.dto.request;

import app.voxly.model.enums.LearningGoal;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private String country;
    private LearningGoal goal;
}
