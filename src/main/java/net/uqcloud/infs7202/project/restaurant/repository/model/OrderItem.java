package net.uqcloud.infs7202.project.restaurant.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_menu_item")
@Getter
@Setter
@ToString(of = {"order", "name"})
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Size(max = 128)
    @NotBlank
    private String name;

    @Positive
    @Column(nullable = false)
    private int price;

    @Positive
    @Column(nullable = false)
    private int quantity;

    @Size(max = 2048)
    private String notes;
}
