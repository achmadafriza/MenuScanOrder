package net.uqcloud.infs7202.project.restaurant.repository;

import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Modifying
    @Query("update Restaurant r set r.isActive = ?2 where r.id = ?1")
    int setRestaurantActive(int id, boolean isActive);
}
