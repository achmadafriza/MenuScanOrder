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
public class MenuDTO {
    @JsonProperty("category_id")
    private int categoryId;

    @Size(max = 128)
    @NotBlank
    private String name;

    @Size(max = 512)
    private String description;

    @NotBlank
    private String price;

    private String menuPictureUrl;
    private String menuPictureName;

    @JsonProperty("menu_picture")
    private MultipartFile menuPicture;
}
