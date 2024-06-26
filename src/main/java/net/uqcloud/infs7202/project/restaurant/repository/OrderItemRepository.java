package net.uqcloud.infs7202.project.restaurant.repository;

import net.uqcloud.infs7202.project.restaurant.repository.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
