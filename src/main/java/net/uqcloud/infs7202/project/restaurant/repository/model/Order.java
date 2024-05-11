package net.uqcloud.infs7202.project.restaurant.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "restaurant_order")
@Getter
@Setter
@ToString(of = {"table", "id"})
@NoArgsConstructor
public class Order {
    public enum Status {
        POSTED, RECEIVED, FINISHED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JoinColumn(name = "table_number", nullable = false)
    private RestaurantTable table;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.POSTED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems;

    @CreatedDate
    @Column(name = "ordered_at", nullable = false, updatable = false)
    private LocalDateTime orderedAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Formula("(select sum(i.price * i.quantity) from order_menu_item i where i.order_id = id)")
    @Basic(fetch = FetchType.LAZY)
    private double totalAmount;
}
