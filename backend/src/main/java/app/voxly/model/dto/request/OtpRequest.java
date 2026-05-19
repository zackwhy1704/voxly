package app.voxly.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OtpRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 6)
    private String code;
}
