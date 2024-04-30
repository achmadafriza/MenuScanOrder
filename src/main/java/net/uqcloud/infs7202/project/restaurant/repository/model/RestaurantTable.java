package net.uqcloud.infs7202.project.restaurant.repository.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;

@Entity
@Table(name = "restaurant_table")
@SQLDelete(sql = "UPDATE restaurant_table SET is_active = false WHERE restaurant_id = ? and  table_number = ?")
@Getter
@Setter
@ToString(of = {"key"})
@NoArgsConstructor
public class RestaurantTable {
    @Data
    @NoArgsConstructor
    @Embeddable
    public static class Key implements Serializable {
        @ManyToOne
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;

        @Column(name = "table_number", nullable = false)
        @Positive
        private int tableNumber;
    }

    @EmbeddedId
    @JsonUnwrapped
    private Key key;

    @Size(max = 36)
    @NotBlank
    private String uuid;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
