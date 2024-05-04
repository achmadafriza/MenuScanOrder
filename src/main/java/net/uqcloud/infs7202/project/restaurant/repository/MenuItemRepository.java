package net.uqcloud.infs7202.project.restaurant.repository;

import net.uqcloud.infs7202.project.restaurant.repository.model.MenuItem;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    Page<MenuItem> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

    @Query("""
        select case when(count(menu) > 0) then true else false end
        from MenuItem menu
        where menu.restaurant.id = ?1 and menu.id = ?2
    """)
    boolean existsByRestaurantAndId(int restaurantId, int id);
}
