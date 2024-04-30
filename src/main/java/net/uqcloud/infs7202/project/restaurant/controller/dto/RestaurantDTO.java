package net.uqcloud.infs7202.project.restaurant.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class RestaurantDTO {
    @Size(max = 128)
    @NotBlank
    private String restaurantName;

    @Size(max = 512)
    private String description;

    @Positive
    private int tableNumber;

    private String profilePictureUrl;
    private String profilePictureName;

    private String backgroundPictureUrl;
    private String backgroundPictureName;

    @JsonProperty("profile_picture")
    private MultipartFile profilePicture;

    @JsonProperty("background_picture")
    private MultipartFile backgroundPicture;
}
