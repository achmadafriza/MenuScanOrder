package net.uqcloud.infs7202.project.restaurant.repository;

import jakarta.transaction.Transactional;
import net.uqcloud.infs7202.project.restaurant.repository.model.Order;
import net.uqcloud.infs7202.project.restaurant.repository.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByTable_Key_Restaurant(Restaurant restaurant, Pageable pageable);
    Optional<Order> findByTable_Key_RestaurantAndId(Restaurant restaurant, int id);
}
