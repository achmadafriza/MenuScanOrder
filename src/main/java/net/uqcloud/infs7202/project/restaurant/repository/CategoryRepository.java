package net.uqcloud.infs7202.project.restaurant.repository;

import net.uqcloud.infs7202.project.restaurant.repository.model.Category;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByRestaurantAndName(Restaurant restaurant, String name);

    List<Category> findAllByRestaurant(Restaurant restaurant);
}
