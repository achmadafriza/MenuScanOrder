package net.uqcloud.infs7202.project.restaurant.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("menu")
    @Valid
    private List<Item> orders;

    @Data
    @NoArgsConstructor
    public static class Item {
        @JsonProperty("menu_id")
        private int menuId;

        private String name;

        @Positive
        private double price;

        @PositiveOrZero
        private int quantity;

        @Size(max = 2048)
        private String notes;
    }
}
