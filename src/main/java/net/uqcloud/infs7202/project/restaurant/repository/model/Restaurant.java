package net.uqcloud.infs7202.project.restaurant.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurant")
@SQLDelete(sql = "UPDATE restaurant SET is_active = false WHERE id = ?")
@Getter
@Setter
@ToString(of = "name")
@NoArgsConstructor
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 128)
    @NotBlank
    private String name;

    @Size(max = 512)
    private String description;

    @Size(max = 2048)
    @URL
    @Column(name = "profile_pic")
    private String profilePic;

    @Size(max = 2048)
    @URL
    @Column(name = "background_pic")
    private String backgroundPic;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "restaurant")
    private List<Category> categories;

    @OneToMany
    @JoinColumn(name = "restaurant_id")
    @SQLRestriction("is_active = true")
    private List<RestaurantTable> tables;
}
