package net.uqcloud.infs7202.project.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class EditUserDTO {
    @Size(max = 320)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @JsonProperty("confirm_password")
    @NotBlank
    private String confirmPassword;

    @AssertTrue(message = "Passwords are not equal")
    private boolean isValid() {
        return password != null && password.equals(confirmPassword);
    }
}
