package net.uqcloud.infs7202.project.restaurant.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.uqcloud.infs7202.project.exception.DataNotFoundException;
import net.uqcloud.infs7202.project.restaurant.controller.dto.OrderDTO;
import net.uqcloud.infs7202.project.restaurant.repository.MenuItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.OrderItemRepository;
import net.uqcloud.infs7202.project.restaurant.repository.OrderRepository;
import net.uqcloud.infs7202.project.restaurant.repository.RestaurantTableRepository;
import net.uqcloud.infs7202.project.restaurant.repository.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private @NonNull OrderRepository orderRepository;

    private @NonNull RestaurantTableRepository tableRepository;
    private @NonNull MenuItemRepository menuRepository;

    @Transactional
    public Order submitOrder(String uuid, OrderDTO orderDto) {
        RestaurantTable table = tableRepository.findByUuid(uuid)
                .orElseThrow(() -> new DataNotFoundException("Table not found!"));

        Restaurant restaurant = table.getKey().getRestaurant();
        if (!restaurant.isActive()) {
            throw new DataNotFoundException("Restaurant is not found!");
        }

        Order order = new Order();
        order.setTable(table);
        order.setStatus(Order.Status.POSTED);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderDTO.Item item : orderDto.getOrders()) {
            MenuItem menuItem = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new DataNotFoundException("Menu not found!"));

            if (!menuItem.getName().equalsIgnoreCase(item.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data found!");
            }

            if (Double.compare(menuItem.getPrice(), item.getPrice()) != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data found!");
            }

            if (item.getQuantity() > 0) {
                OrderItem orderItem = new OrderItem();
                orderItem.setName(item.getName());
                orderItem.setPrice(item.getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setNotes(item.getNotes());
                orderItem.setOrder(order);

                orderItems.add(orderItem);
            }
        }

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateStatus(Restaurant restaurant, int orderId, String status) {
        Order order = orderRepository.findByTable_Key_RestaurantAndId(restaurant, orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found!"));

        Order.Status orderStatus;
        try {
            orderStatus = Order.Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new DataNotFoundException("Invalid Status!", e);
        }

        order.setStatus(orderStatus);

        return orderRepository.save(order);
    }
}
