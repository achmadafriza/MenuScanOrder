package net.uqcloud.infs7202.project.auth.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString(of = "name")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 128)
    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_menu_mapping",
            joinColumns = {
                    @JoinColumn(name = "role_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "menu_id", nullable = false)
            }
    )
    private Set<Menu> menus = new HashSet<>();
}
