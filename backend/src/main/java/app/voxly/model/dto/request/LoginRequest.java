package app.voxly.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
