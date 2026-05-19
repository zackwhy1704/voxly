package app.voxly.model.dto.request;

import app.voxly.model.enums.LearningGoal;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SignUpRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
    @NotBlank
    private String name;
    private String country;
    private LearningGoal goal;
}
